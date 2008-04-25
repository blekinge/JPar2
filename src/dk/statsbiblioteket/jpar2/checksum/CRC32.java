/*
 *     CRC32.java
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

import dk.statsbiblioteket.jpar2.byteutils.ByteUtil;
import dk.statsbiblioteket.jpar2.byteutils.Bytes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class CRC32 extends Checksum {

    public CRC32() {
        super();
    }

    public CRC32(byte[] block) {
        super(block);
    }

    public CRC32(File file)
            throws FileNotFoundException, IOException {

        java.util.zip.CRC32 digester = new java.util.zip.CRC32();

        InputStream in = new FileInputStream(file);

        digester.reset();
        byte[] block = new byte[1024 * 1024];//1 mb

        int len = in.read(block);
        while (len > 0) {
            digester.update(block, 0, len);
            len = in.read(block);
        }
        //Todo: Convert a long to a byte array;

        byte[] hash = new byte[4];
        Bytes.toBytesB((int) digester.getValue(), hash, 0);

        in.close();

        this.setBytes(hash);
    }

    public static CRC32 calc(ByteBuffer block) throws Error {
        return calc(ByteUtil.array(block));
    }

    /**
     * Calculates the CRC32 hash of the bytes in block, and returns an
     * array of 16 bytes.
     * @param block the bytes to hash
     * @return The hash of block
     * @throws Error if CRC32 is not available
     */
    public static CRC32 calc(byte[] block) throws Error {
        java.util.zip.CRC32 digester = new java.util.zip.CRC32();

        digester.reset();
        digester.update(block);


        byte[] hash = new byte[4];
        Bytes.toBytesB((int) digester.getValue(), hash, 0);





        return new CRC32(hash);
    }
}
