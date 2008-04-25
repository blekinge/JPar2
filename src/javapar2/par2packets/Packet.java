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
import javapar2.par2packets.headers.PacketType;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javapar2.par2packets.InvalidPacketException;
import javapar2.par2packets.headers.RecoverySetID;
import javapar2.checksum.MD5;
import javapar2.byteutils.ByteUtil;

/**
 * The generic class representing a par2 packet
 * @author Asger
 */
public abstract class Packet {


    //The only things we need to know. Rest will be calculated when needed.
    private RecoverySetID recoverySetId; 
    private PacketType packetType;
    

    /**
     * The signature that all packets must have in the header.
     */
    public static final PacketSignature MAGIC = new PacketSignature(
            new byte[]{'P', 'A', 'R', '2', '\0', 'P', 'K', 'T'});


    /**
     * Constructor, that copies the packet in p, except for the contents. Used by
     * the static cast* methods below
     * @param p the packet to cast
     */
    protected Packet(Packet p) {
        this(p.getRecoverySetId(), p.getPacketType());
    }

      
    /**
     * Create a new packet. Used by the constructors in the subclasses. 
     * @param recoverySetId The recovery set ID that the packet belongs to
     * @param packetType The type of the packet, see the subclasses
     */
    protected Packet(RecoverySetID recoverySetId, PacketType packetType){
        this.packetType = packetType;
        this.recoverySetId = recoverySetId;
    }

   
    /**
     * Return the packet as a ByteBuffer. Only meant to be called from the sub
     * classes, to wrap the correct header on their bodies
     * @param contents the contents of the packet
     * @return A ByteBuffer with the header and the contents.
     */
    protected ByteBuffer writePacket(ByteBuffer contents){
                
        ByteBuffer packet = ByteBuffer.wrap(new byte[16*4+contents.capacity()]);
        packet.order(ByteOrder.LITTLE_ENDIAN);
        
        packet.put(MAGIC.getAsBytes());//8
        packet.putLong(packet.capacity());//8
        
        //Hash problems
        packet.mark();
        packet.position(packet.position()+16);//jump over the checksum field
        
        packet.put(recoverySetId.getBytes());
        
        //ugly
        packet.put(packetType.getSignature().getAsBytes());
        
        contents.rewind();
        packet.put(contents);
        
        packet.reset();//to the checksum filed
        packet.position(packet.position()+16); //jump over
        
        ByteBuffer hash = ByteBuffer.wrap(MD5.calc(packet).getBytes()); //generate checksum
        packet.reset(); //to the checksum field
        packet.put(hash);

        packet.rewind();
        return packet;
        
    }
    /**
     * Encodes the packet as a ByteBuffer.
     * Used when the packet is to be written out to a file.
     * @return the packet, ready to be written to disk.
     */
    public abstract ByteBuffer writePacket();

    public RecoverySetID getRecoverySetId() {
        return recoverySetId;
    }

    public void setRecoverySetId(RecoverySetID recoverySetId) {
        this.recoverySetId = recoverySetId;
    }

    public PacketType getPacketType() {
        return packetType;
    }

    public void setPacketType(PacketType packetType) {
        this.packetType = packetType;
    }
    
}
