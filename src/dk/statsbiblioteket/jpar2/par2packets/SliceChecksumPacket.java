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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import dk.statsbiblioteket.jpar2.checksum.CRC32;
import dk.statsbiblioteket.jpar2.checksum.MD5;
import dk.statsbiblioteket.jpar2.files.DataFile;
import dk.statsbiblioteket.jpar2.par2packets.headers.FileID;
import dk.statsbiblioteket.jpar2.par2packets.headers.PacketSignature;
import dk.statsbiblioteket.jpar2.par2packets.headers.PacketType;

/**
 *
 * @author Asger
 */
public class SliceChecksumPacket extends Packet {

    public static final PacketSignature SIGNATURE = new PacketSignature(
            new byte[]{'P', 'A', 'R', ' ', '2', '.', '0',
        '\0', 'I', 'F', 'S', 'C', '\0', '\0', '\0', '\0'
    });
    private FileID fileID;

    private FileDescriptionPacket fileDescPacket;
    private List<MD5> md5Hashes = new ArrayList<MD5>();
    private List<CRC32> crc32list = new ArrayList<CRC32>();

    
    public FileID getFileID() {
        return fileID;
    }
    
    /**
     * Remove this function later
     * @return
     */
    public List<MD5> getHashes(){
        return new ArrayList<MD5>(md5Hashes);
    }
    
    public List<CRC32> getCRC32s(){
        return new ArrayList<CRC32>(crc32list);
    }

    
    public ByteBuffer writePacket() {
        ByteBuffer content = ByteBuffer.allocate(16+20*md5Hashes.size());
        content.order(ByteOrder.LITTLE_ENDIAN);
        content.put(fileID.getBytes());
        for (int i = 0; i < md5Hashes.size(); i++) {
            content.put(md5Hashes.get(i).getBytes());
            content.put(crc32list.get(i).getBytes());
        }
        return writePacket(content);
    }

    
    //CONSTRUCTORS BEGIN
    
 
    /**
     * Cast the packet as a Input File Slice Checksum packet
     * @param p the packet to cast
     * @throws javapar2.Exceptions.InvalidPacketException if the packet cannot
     * be cast
     */
    public SliceChecksumPacket(UnparsedPacket p) throws InvalidPacketException {
        super(p);

        if (p.getPacketType() != PacketType.SliceChecksum) {
            throw new InvalidPacketException("The packet type field marks it as" +
                    "another type");
        }
       
        byte[] fileID = new byte[16];
        ByteBuffer contents = p.getContents();
        contents.rewind();
        
        contents.get(fileID);
        this.fileID = new FileID(fileID);

        while (contents.hasRemaining()) {
            ByteBuffer md5hash = ByteBuffer.allocate(16);
            ByteBuffer crc32hash = ByteBuffer.allocate(4);
            contents.get(md5hash.array());
            contents.get(crc32hash.array());
            
            MD5 md5 = new MD5();
            md5.setBytes(md5hash.array());
            md5Hashes.add(md5);
            
            CRC32 crc32 = new CRC32();
            crc32.setBytes(crc32hash.array());
            crc32list.add(crc32);
        }

    }
    
    public SliceChecksumPacket(FileDescriptionPacket fileDescriptionPacket,
            DataFile dataFile){
        super(fileDescriptionPacket.getRecoverySetId(),PacketType.SliceChecksum);
        fileDescPacket = fileDescriptionPacket;
        md5Hashes.addAll(dataFile.getHashes());
        fileID = fileDescriptionPacket.getFileID();
        crc32list.addAll(dataFile.getCRC32s());
    }

    //CONSTRUCTORS END
}
