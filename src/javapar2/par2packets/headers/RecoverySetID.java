/*
 *     RecoverySetID.java
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

package javapar2.par2packets.headers;

import java.nio.ByteBuffer;
import java.util.Arrays;
import javapar2.byteutils.ByteUtil;

/**
 * Represents the RecoverySetID, an internal way to group the packets.
 * @author Asger Blekinge-Rasmussen
 */
public class RecoverySetID {

    private byte[] recoverySetID;

    public byte[] getBytes() {
        return recoverySetID;
    }

    public void setID(byte[] recoverySetID) {
       
        this.recoverySetID = recoverySetID;
    }

    public RecoverySetID(byte[] recoverySetID) {
        setID(recoverySetID);
    }

    public RecoverySetID(ByteBuffer recoverySetID) {
        setID(recoverySetID.array());
    }

    public boolean equals(Object id) {
        return (id instanceof RecoverySetID) &&
                Arrays.equals(this.recoverySetID,
                ((RecoverySetID) id).getBytes());
    }

    public int hashCode() {
        return Arrays.hashCode(getBytes());
    }

    public String toString() {
        return ByteUtil.ascii(getBytes());
    }
}