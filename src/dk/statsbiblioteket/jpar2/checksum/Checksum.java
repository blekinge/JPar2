/*
 *     Checksum.java
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
package dk.statsbiblioteket.jpar2.checksum;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import dk.statsbiblioteket.jpar2.byteutils.ByteUtil;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public abstract class Checksum {

    byte[] checksum;

    public Checksum() {
        //ever used?
        checksum = new byte[32];
    }

    Checksum(byte[] block) {
        checksum = block;
    }

    public Checksum(String algorithm, File file)
            throws FileNotFoundException, IOException {
        MessageDigest digester;
        try {
            digester = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new Error(algorithm + " not available", ex);
        }
        InputStream in = new FileInputStream(file);

        byte[] block = new byte[1024 * 1024];//1 mb

        int len = in.read(block);
        while (len > 0) {
            digester.update(block, 0, len);
            len = in.read(block);
        }
        checksum = digester.digest();
        in.close();
    }

    public byte[] getBytes() {
        return checksum;
    }

    public void setBytes(byte[] checksum) {
        this.checksum = checksum;
    }

    public ByteBuffer getByteBuffer(){
        return ByteBuffer.wrap(checksum);
    }
    
    @Override
    public boolean equals(Object md) {
        if (md instanceof Checksum) {
            return Arrays.equals(checksum, ((Checksum) md).getBytes());
        }
        return false;

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(checksum);
    }

    @Override
    public String toString() {
        return ByteUtil.toHex(checksum).toLowerCase();

    }
}
