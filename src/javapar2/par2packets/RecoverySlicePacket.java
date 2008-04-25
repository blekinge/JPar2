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
import javapar2.par2packets.InvalidPacketException;
import javapar2.files.RecoveryData;
import javapar2.par2packets.headers.PacketSignature;
import javapar2.par2packets.headers.PacketType;
import javapar2.par2packets.headers.RecoverySetID;


/**
 *
 * @author Asger
 */
public class RecoverySlicePacket extends Packet {

    public static final PacketSignature SIGNATURE = new PacketSignature(
            new byte[]{'P', 'A', 'R', ' ', '2', '.', '0', '\0',
        'R', 'e', 'c', 'v', 'S', 'l', 'i', 'c'
    });
    private RecoveryData recoveryData;
    
    public RecoverySlicePacket(UnparsedPacket p) throws InvalidPacketException {

        super(p);

        if (p.getPacketType() != PacketType.RecoverySlice) {
            throw new InvalidPacketException("The packet type field marks it as" +
                    "another type");
        }
        ByteBuffer body = p.getContents();
        
        long exponent = body.getInt();//TODO Unsigned

        byte[] data = new byte[body.remaining()];
        body.get(data);
        
        recoveryData = new RecoveryData(exponent, data);
    }

    public RecoverySlicePacket(RecoverySetID recoverySetID, RecoveryData parity) {
        super(recoverySetID, PacketType.RecoverySlice);
        recoveryData = parity;
    }

    public ByteBuffer writePacket() {
        //if more elaborate, remember the byte order... LITTLE ENDIAN
        return writePacket(recoveryData.writeContents());

    }

    public RecoveryData getRecoveryData() {
        return recoveryData;
    }
    
    
}
