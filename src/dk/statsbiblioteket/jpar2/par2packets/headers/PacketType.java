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
package dk.statsbiblioteket.jpar2.par2packets.headers;

import java.util.HashMap;
import java.util.Map;
import dk.statsbiblioteket.jpar2.par2packets.CreatorPacket;
import dk.statsbiblioteket.jpar2.par2packets.FileDescriptionPacket;
import dk.statsbiblioteket.jpar2.par2packets.MainPacket;
import dk.statsbiblioteket.jpar2.par2packets.SliceChecksumPacket;
import dk.statsbiblioteket.jpar2.par2packets.RecoverySlicePacket;



/**
 * The type of a packet. 
 * @author Asger
 */
public enum PacketType {
    Main(MainPacket.SIGNATURE),
    FileDescription(FileDescriptionPacket.SIGNATURE),
    SliceChecksum(SliceChecksumPacket.SIGNATURE),
    RecoverySlice(RecoverySlicePacket.SIGNATURE),
    Creator(CreatorPacket.SIGNATURE),
    Other(null);

    
    private static Map<PacketSignature, PacketType> packetTypes = 
            new HashMap<PacketSignature, PacketType>();
    
    static {
        packetTypes.put(Main.getSignature(), Main);
        packetTypes.put(FileDescription.getSignature(), FileDescription);
        packetTypes.put(SliceChecksum.getSignature(), SliceChecksum);
        packetTypes.put(RecoverySlice.getSignature(), RecoverySlice);
        packetTypes.put(Creator.getSignature(), Creator);
    }
    
    private PacketSignature packetSignature;
    
    /**
     * Private constructor. Links a PacketType with a Signature. 
     * @param packetSignature The Signature to link to this PacketType
     */
    private PacketType(PacketSignature packetSignature) {
        this.packetSignature = packetSignature;
    }
    
    
    /**
     * Get the signature that corresponds to this PacketType
     * @return the packetSignature
     */
    public PacketSignature getSignature() {
        return packetSignature;
    }
    
    /**
     * Static method to return the types of a packet. Used when to packet
     * is initially parsed on input.
     * @param packetSignature The signature to lookup
     * @return The PacketType from this Enum
     */
    public static PacketType getPacketType(byte[] packetSignature) {
        PacketType packetType = packetTypes.get(
                new PacketSignature(packetSignature));
        if (packetType == null){
            return packetType.Other;
        }
        return packetType;
    }
      
}
