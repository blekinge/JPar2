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
import javapar2.byteutils.ByteUtil;

/**
 * Packet containing information about the client utility that made the recovery
 * set. 
 * @author Asger
 */
public class CreatorPacket extends Packet {

    public static final PacketSignature SIGNATURE =
            new PacketSignature(
            new byte[]{'P', 'A', 'R', ' ', '2', '.', '0',
                       '\0', 'C', 'r', 'e', 'a', 't', 'o', 'r', '\0'
    });
    private String client;

    //ACCESSORS METHODS BEGIN
    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    //ACCESSOR METHODS END

    //WRITE METHODS BEGIN
    public ByteBuffer writePacket() {
        ByteBuffer contents =
                ByteBuffer.wrap(ByteUtil.ascii(client));
        contents.order(ByteOrder.LITTLE_ENDIAN);
        return writePacket(contents);

    }
    //WRITE METHODS END
    //CONSTRUCTORS BEGIN
    /**
     * Casts the UnparsedPacket p into a CreatorPacket
     * @param p The UnparsedPacket to Cast
     * @throws InvalidPacketException if the packet cannot be cast
     */
    public CreatorPacket(UnparsedPacket p) throws InvalidPacketException {
        super(p);
        if (p.getPacketType() != PacketType.Creator) {
            throw new InvalidPacketException("The packet type field marks it as" +
                                             "another type");
        }
        ByteBuffer body = p.getContents();
        body.rewind();
        if (!body.hasArray()) {
            throw new InvalidPacketException("The body of packet '" + p +
                                             "' is not asssesible");
        }
        client = ByteUtil.ascii(body.array());
    }

    /**
     * Create a new CreatorPacket, with the given Client. 
     * @param client The string identifyin the client used to modify this set
     */
    public CreatorPacket(RecoverySetID recoverySetID, String client) {
        super(recoverySetID, PacketType.Creator);
        this.client = client;
    }
    //CONSTRUCTORS END
}
