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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javapar2.par2packets.InvalidPacketException;
import javapar2.par2packets.headers.PacketSignature;
import javapar2.par2packets.headers.PacketType;
import javapar2.par2packets.headers.RecoverySetID;
import javapar2.checksum.MD5;
import javapar2.byteutils.ByteUtil;

/**
 * A packet before the type has been determined. Contains the parsed header and
 * an unparsed body.<br>
 * The other subclasses of Packet have constructors that take an UnparsedPacket.
 * @author Asger
 */
public class UnparsedPacket extends Packet {

    private ByteBuffer contents;

    /**
     * Reads in a packet from a ByteBuffer and returns it as an object.
     * @param packetBuffer the buffer to read the packet from. Starts from
     * current position. The position is updated to the end of the packet, which
     * might no be the end of the packetbuffer. Limit is preserved, while mark
     * is lost. Capacity is of course preserved.
     * @throws javapar2.Exceptions.InvalidPacketException if the packet cannot
     * be parsed
     */
    public UnparsedPacket(ByteBuffer packetBuffer) throws InvalidPacketException {

        super(null, null);//hack

        packetBuffer.order(ByteOrder.LITTLE_ENDIAN);
        byte[] magic = new byte[8];
        int beginning = packetBuffer.position();//the beginning index of the packet
        int limit  = packetBuffer.limit(); //the old limit of the buffer
        int end; //the end index of the packet
        
        packetBuffer.get(magic);

        PacketSignature magicWord = new PacketSignature(magic);

        if (!Packet.MAGIC.equals(magicWord)) {
            throw new InvalidPacketException("Invalid packet header," +
                    " wrong magic word '" + magicWord + "' and '"
                    + Packet.MAGIC + "'");
        }

        long length = packetBuffer.getLong();//read 8
        
        end = beginning + (int)length;//the end index of the packet
        
        packetBuffer.limit(end); //limit the buffer to this packet

        byte[] md5 = new byte[16];
        packetBuffer.get(md5);
        ByteBuffer packetChecksum = ByteBuffer.wrap(md5);

        //mark for the checksumming
        packetBuffer.mark();

        byte[] set_id = new byte[16];
        packetBuffer.get(set_id);

        setRecoverySetId(new RecoverySetID(set_id));

        byte[] type = new byte[16];
        packetBuffer.get(type);
        setPacketType(PacketType.getPacketType(type));

        if (length > Integer.MAX_VALUE) {
            throw new InvalidPacketException("Packet length longer than 2GB");
        }

        byte[] contents = new byte[(int) length - 16 * 4];
        packetBuffer.get(contents);
        this.contents = ByteBuffer.wrap(contents);
        this.contents.order(ByteOrder.LITTLE_ENDIAN);


        //checksum
        
        packetBuffer.reset();
        ByteBuffer checksum = MD5.calc(packetBuffer).getByteBuffer();
        if (!checksum.equals(packetChecksum)) {
            throw new InvalidPacketException("Packet checksum does not match");
        }
        packetBuffer.position(end);//go to the start of next packet
        packetBuffer.limit(limit); //and restore the old limit
        

    }

    protected ByteBuffer getContents() {
        return contents;
    }

    public ByteBuffer writePacket() {
       return super.writePacket(contents);
 
    }
}
