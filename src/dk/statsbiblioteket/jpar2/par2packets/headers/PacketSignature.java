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

import java.util.Arrays;
import dk.statsbiblioteket.jpar2.byteutils.ByteUtil;


public class PacketSignature {
    
    private byte[] packetSignature;
    
    public PacketSignature(byte[] packetSignature) {
        setSig(packetSignature);
    }
    
    public byte[] getAsBytes() {
        return packetSignature;
    }
    
    private void setSig(byte[] packetSignature) {
        this.packetSignature = packetSignature;
    }
    
    @Override
    public boolean equals(Object sig) {
        if (sig instanceof PacketSignature) {
            return Arrays.equals(this.getAsBytes(),
                    ((PacketSignature) sig).getAsBytes());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(packetSignature);
    }
    
    @Override
    public String toString() {
        return ByteUtil.ascii(getAsBytes());
    }
}
