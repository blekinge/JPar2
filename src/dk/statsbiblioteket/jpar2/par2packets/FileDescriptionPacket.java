/*
JavaPar2, a library for the par2 (par2.net) specification
Copyright (C) 2007  Asger Blekinge-Rasmussen
This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.
This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.
You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package dk.statsbiblioteket.jpar2.par2packets;

import dk.statsbiblioteket.jpar2.par2packets.headers.PacketSignature;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import dk.statsbiblioteket.jpar2.files.DataFile;
import dk.statsbiblioteket.jpar2.par2packets.InvalidPacketException;
import dk.statsbiblioteket.jpar2.par2packets.headers.FileID;
import dk.statsbiblioteket.jpar2.par2packets.headers.PacketType;
import dk.statsbiblioteket.jpar2.par2packets.headers.RecoverySetID;
import dk.statsbiblioteket.jpar2.checksum.MD5;
import dk.statsbiblioteket.jpar2.byteutils.ByteUtil;

/**
 * Class representing an file description packet. A file description packet 
 * contains the file ID and the filename. It thereby provides the link between
 * the internal representation of a file, and the external file.
 * <br>
 * It also contains the hash of the file, in order for the file to be verified.
 * This protects the recovery set against a odd file being named as one of the
 * files in the set.
 * 
 * @author Asger Blekinge-Rasmussen
 */
public class FileDescriptionPacket extends Packet {

    /**
     * All packets of this type must have this string in the type field.
     */
    public static final PacketSignature SIGNATURE = new PacketSignature(
            new byte[]{'P', 'A', 'R', ' ', '2', '.', '0', '\0',
        'F', 'i', 'l', 'e', 'D', 'e', 's', 'c'
    });
    private FileID fileID;
    private MD5 md5Hash;
    private MD5 md5Hash16k;
    private long lengthOfFile;
    private String nameOfFile;
    private File file;
    private File parentFile;

    public long getLengthOfFile() {
        return lengthOfFile;
    }

    /**
     * Private utility method that decodes the filename.
     * The filename in the packet uses '/' as path separator, but this is only
     * valid java in unix. Replaces the '/' with File.separatorChar and returns
     * the string
     * @param nameOfFile the filename to decode
     * @return the java filename
     */
    private String decodeFileName(String nameOfFile) {
        return nameOfFile.replace('/', File.separatorChar);
    }

    /**
     * Private utility method that encodes the filename.
     * Inverse of decodeFileName.
     * @param nameOfFile the filename to encode
     * @return The packet-encoded filename
     */
    private String encodeFileName(String nameOfFile) {
        return nameOfFile.replace(File.separatorChar, '/');
    }

    //ACCESSORS METHODS BEGIN
    /**
     * Gets a copy of the internal FileID for the file referenced 
     * by this packet.
     * @return The FileID
     */
    public FileID getFileID() {
        return fileID;
    }

    /**
     * Gets the File that this packet references, as a jave File object
     * @return the Java File that this packet references
     */
    public File getFile() {
        if (file != null) {
            return file;
        } else {
            return new File(parentFile, decodeFileName(nameOfFile));
        }
    }

    public MD5 getMd5Hash() {
        return md5Hash;
    }

    public MD5 getMd5Hash16k() {
        return md5Hash16k;
    }

    //ACCESSOR METHODS END
    //WRITE METHODS BEGIN
    /**
     * Encodes the packet as a ByteBuffer. Used when the packet is to be 
     * written out to a file.
     * @return the packet, ready to be written to disk.
     */
    public ByteBuffer writePacket() {

        byte[] filename = ByteUtil.ascii(encodeFileName(nameOfFile));
        ByteBuffer contents = ByteBuffer.allocate(16 * 3 + 8 + filename.length);
        contents.order(ByteOrder.LITTLE_ENDIAN);
        contents.put(fileID.getBytes());//16
        contents.put(md5Hash.getBytes());//16
        contents.put(md5Hash16k.getBytes());//16
        contents.putLong(lengthOfFile);//8
        contents.put(filename);

        return writePacket(contents);

    }

    //WRITE METHODS END
    public FileDescriptionPacket(UnparsedPacket p)
            throws InvalidPacketException {
        this(p, null);
    }

    //CONSTRUCTORS BEGIN
    /**
     * Cast the packet p into a file description packet
     * @param p the packet to cast
     * @throws javapar2.Exceptions.InvalidPacketException if the packet cannot
     * be cast
     */
    public FileDescriptionPacket(UnparsedPacket p, File parentFile)
            throws InvalidPacketException {
        super(p);
        if (getPacketType() != PacketType.FileDescription) {
            throw new InvalidPacketException("The packet type field marks it as" +
                    "another type");
        }
        this.parentFile = parentFile;
        ByteBuffer contents = p.getContents();
        contents.rewind();

        byte[] fileID = new byte[16];
        contents.get(fileID);

        this.fileID = new FileID(fileID);

        byte[] md5Hash = new byte[16];
        contents.get(md5Hash);
        this.md5Hash = new MD5();
        this.md5Hash.setBytes(md5Hash);

        byte[] md5Hash16k = new byte[16];
        contents.get(md5Hash16k);
        this.md5Hash16k = new MD5();
        this.md5Hash16k.setBytes(md5Hash16k);

        lengthOfFile = contents.getLong();

        byte[] filename = new byte[contents.remaining()];
        contents.get(filename);
        this.nameOfFile = decodeFileName(ByteUtil.ascii(filename));

    }

    /**
     * Creates a new FileDescription packet. Takes a File as input, and generates
     * the hashes from it. Sets the filename to the relative path of this file.
     * @param recoverySetID The recoverySetID that this packet belongs to
     * @param file The file to generate this packet about
     * @throws java.io.FileNotFoundException If the file does not exist
     * @throws java.io.IOException If the file cannot be read
     */
    public FileDescriptionPacket(RecoverySetID recoverySetID, File file) throws
            FileNotFoundException, IOException {
        super(recoverySetID, PacketType.FileDescription);

        if (file.exists()) {
            try {
                lengthOfFile = file.length();
                //read the file into a bytebuffer
//                MappedByteBuffer fileMap = new FileInputStream(file).getChannel().
//                        map(FileChannel.MapMode.READ_ONLY, 0, lengthOfFile);

                //Hash of the entire file
                md5Hash = new MD5(file);

                //fileMap.rewind();
                //Hash of the first 16k of the file

                byte[] first16k = new byte[16 * 1024];
                //so short files are padded with zeroes
                InputStream in = new FileInputStream(file);
                int len = in.read(first16k);

                md5Hash16k = MD5.calc(Arrays.copyOf(first16k, len));

                nameOfFile = file.getPath();//TODO relative path?

                fileID = calculateFileID();
                this.file = file;


            } catch (IOException ex) {
                throw new IOException("The file could not be opened for reading",
                        ex);
            }

        } else {
            throw new FileNotFoundException("The specified file does not exist");
        }
    }

    public FileDescriptionPacket(RecoverySetID recoverySetID, DataFile dataFile) {
        super(recoverySetID, PacketType.FileDescription);
        md5Hash = dataFile.getHash();
        lengthOfFile = dataFile.getLength();
        file = dataFile.getFile();
        nameOfFile = file.getPath();
        md5Hash16k = dataFile.getHash16k();
        fileID = calculateFileID();
    }

    private FileID calculateFileID() {
        //Calculate the FileID
        byte[] asciiname = ByteUtil.ascii(encodeFileName(nameOfFile));
        ByteBuffer temp = ByteBuffer.allocate(16 + 8 + asciiname.length);
        temp.put(md5Hash16k.getBytes());
        temp.putLong(lengthOfFile);
        temp.put(asciiname);

        return new FileID(MD5.calc(temp).getBytes());
    }
}
