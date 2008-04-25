/*
 *     FileID.java
 *     Copyright (C) 2008  Asger Blekinge-Rasmussen
 * 
 *     This library is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public
 *     License as published by the Free Software Foundation; either
 *     version 2.1 of the License, or (at your option) any later version.
 * 
 *     This library is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with this library; if not, write to the Free Software
 *     Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package dk.statsbiblioteket.jpar2.par2packets.headers;

import java.nio.ByteBuffer;
import java.util.Arrays;
import dk.statsbiblioteket.jpar2.byteutils.ByteUtil;

/**
 * Represents the FileID, a packet-internal way to reference Files.
 * @author Asger Blekinge-Rasmussen
 */
public class FileID {

    private byte[] fileID;

    public FileID(byte[] fileID) {
        setFileID(fileID);
    }

    public FileID(ByteBuffer fileID) {
        this.fileID = new byte[16];
        fileID.get(this.fileID);
        fileID.position(fileID.position()-16);
    }
    
    
    public byte[] getBytes() {
        return fileID;
    }

    
    public void setFileID(byte[] fileID) {

        this.fileID = fileID;
    }

    @Override
    public boolean equals(Object id) {
        if (id instanceof FileID && 
                Arrays.equals(this.getBytes(), ((FileID)id).getBytes())){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(fileID);
    }

    @Override
    public String toString() {
        return ByteUtil.ascii(getBytes());
    }
    
    
}
