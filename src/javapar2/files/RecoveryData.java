/*
 *     RecoveryData.java
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

package javapar2.files;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class RecoveryData {
    
    
    
    private long exponent;//actually, an uint
    private byte[] recoveryData;

    /**
     * hack to int...
     * @return
     */
    public int getExponent() {
        return (int)exponent;
    }

    private void setExponent(long exponent) {
        this.exponent = exponent;
    }

    public byte[] getRecoveryData() {
        return recoveryData;
    }

    private void setRecoveryData(byte[] recoveryData) {
        this.recoveryData = recoveryData;
    }

    public RecoveryData(long exponent, byte[] recoveryData){
        setExponent(exponent);
        setRecoveryData(recoveryData);
    }
    
    public ByteBuffer writeContents(){
        ByteBuffer content = ByteBuffer.allocate(4+recoveryData.length);
        content.order(ByteOrder.LITTLE_ENDIAN);
        content.putInt((int)exponent);
        content.put(recoveryData);
        return content;

    }
    

}
