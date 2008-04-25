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
package javapar2.par2packets;

import javapar2.par2packets.headers.PacketSignature;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import javapar2.par2packets.InvalidPacketException;
import javapar2.par2packets.headers.FileID;
import javapar2.par2packets.headers.FileID;
import javapar2.par2packets.headers.PacketType;
import javapar2.par2packets.headers.RecoverySetID;
import javapar2.checksum.MD5;
import javapar2.byteutils.ByteUtil;

/**
 * Class representing a main packet. Cannot be instantiated, except through the
 * static castAsMain method.
 * @author Asger
 */
public class MainPacket extends Packet {

    //TODO Clean up the accessors
    private int sliceSize;
    private Set<FileID> nonRecoverySet = new TreeSet<FileID>();//not really used
    private Set<FileID> recoverySet = new TreeSet<FileID>();
    public static final PacketSignature SIGNATURE = new PacketSignature(
            new byte[]{'P', 'A', 'R', ' ', '2', '.', '0', '\0',
        'M', 'a', 'i', 'n', '\0', '\0', '\0', '\0'
    });

    //ACCESSORS METHODS BEGIN
    
    public void addFileId(FileID fileID, boolean recovery){
        if(recovery){
            recoverySet.add(fileID);
        } else{
            nonRecoverySet.add(fileID);
        }
    }
    
    public int getSliceSize() {
        return sliceSize;
    }

    public void setSliceSize(long sliceSize) {
        if (sliceSize > Integer.MAX_VALUE){
            throw new IllegalArgumentException("Slice size must be lesser than 2GB");
        }
        this.sliceSize = (int)sliceSize;
    }

    /**
     * Calculates the recoverySetID for the set that this is the main packet in.
     * It is based on the contents of the main packet, so if these change in any
     * way, it is no longer valid. Ensure that you have finalished this object 
     * before calculating the recoverySetID<br>
     * Does not automatically update the recoverySetID in this packet, you must
     * do that yourself.
     * @return the new RecoverySetID. 
     */
    public RecoverySetID calculateRecoverySetID() {
        ByteBuffer contents = writeContents();
        return new RecoverySetID(MD5.calc(contents).getBytes());
    }

    public Set<FileID> getNonRecoverySet() {
        return new TreeSet<FileID>(nonRecoverySet);
    }

    public Set<FileID> getRecoverySet() {
        return new TreeSet<FileID>(recoverySet);
    }

    //ACCESSOR METHODS END
    //WRITE METHODS BEGIN
    public ByteBuffer writePacket() {

        ByteBuffer contents = writeContents();
        return writePacket(contents);
    }

    /**
     * Encodes the contents of the packet as a ByteBuffer.
     * Used by {@link #writePacket writePacket}
     * @return the content of the packet, but not the generic header
     */
    private ByteBuffer writeContents() {
        ByteBuffer contents = ByteBuffer.allocate(12 + recoverySet.size() * 16 +
                nonRecoverySet.size() * 16);
        contents.order(ByteOrder.LITTLE_ENDIAN);
        contents.putLong(sliceSize);
        contents.putInt(recoverySet.size());


        //TODO hopefully in the right order. Must be tested
        for (FileID fileID : recoverySet) {
            contents.put(fileID.getBytes());
        }
        for (FileID fileID : nonRecoverySet) {
            contents.put(fileID.getBytes());
        }

        contents.rewind();

        return contents;

    }

    //WRITE METHODS END
    //CONSTRUCTORS BEGIN
    /**
     * Constructor to make a new MainPacket
     * @param recoverySetId the SetId that this packet belongs to
     * @param sliceSize The slice size for this set
     * @param recoverySet The set of FileIDs that should be in the set. Can be 
     * empty, but not null.
     * @param nonRecoverySet The set of FileIDs that should be in the 
     * nonrecovery set. Can be empty, but not null.
     */
    public MainPacket(RecoverySetID recoverySetId, int sliceSize,
            Collection<FileID> recoverySet,
            Collection<FileID> nonRecoverySet) {
        super(recoverySetId, PacketType.Main);
        this.sliceSize = sliceSize;
        this.recoverySet.addAll(recoverySet);
        this.nonRecoverySet.addAll(nonRecoverySet);
    }

    /**
     * Constructs a new MainPacket. Automatically generates the recoverySetID
     * @param sliceSize the slice size in bytes to use. 
     */
    public MainPacket(int sliceSize) {
        super(null, PacketType.Main);
        this.sliceSize = sliceSize;
        this.setRecoverySetId(calculateRecoverySetID());

    }

    /**
     * Casts the given packet as a MainPacket
     * @param p the packet to be cast
     * @throws javapar2.Exceptions.InvalidPacketException if the packet cannot
     * be cast
     */
    public MainPacket(UnparsedPacket p) throws InvalidPacketException {
        super(p);

        if (getPacketType() != PacketType.Main) {
            throw new InvalidPacketException("The packet type field marks" +
                    " it as another type");
        }
        ByteBuffer body = p.getContents();
        body.rewind();
        
        setSliceSize(body.getLong());
        int numberOfFiles = body.getInt();

        for (int i = 0; i < numberOfFiles; i++) {
            byte[] ID = new byte[16];
            body.get(ID);
            recoverySet.add(new FileID(ID));
        }

        while (body.hasRemaining()) {
            byte[] ID = new byte[16];
            body.get(ID);
            recoverySet.add(new FileID(ID));
        }
    }
    //CONSTRUCTORS END
}


