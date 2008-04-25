/*
 *     ByteUtils.java
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
package javapar2.byteutils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Static utility methods that are used all over
 * @author Asger Blekinge-Rasmussen
 */
public class ByteUtil {

    private static final byte MAGIC_INTEGER_4 = 4;
    private static final byte MAGIC_INTEGER_OxOF = 0x0f;

    /**
     * Convert an ascii string to a byte array. Note, no hexstring here.
     */
    public static byte[] ascii(String s) {
        try {
            return s.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException("ASCII not known!!!", ex);
        }
    }

    /**
     * Convert a byte array to an ascii string. Note, no hexstring here.
     */
    public static String ascii(byte[] s) {
        Charset ascii = Charset.forName("US-ASCII");
        return new String(s, ascii);
    }
    
    public static byte[] array(ByteBuffer b){
        int position = b.position();
        byte[] a = new byte[b.remaining()];
        b.get(a);
        b.position(position);
        return a;
    }


    /**
     * Converts a byte array to a string of hexadecimal characters.
     * @param ba the bytearray to be converted
     * @return ba converted to a hexstring
     */
    public static String toHex(final byte[] ba) {
        char[] hexdigit = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a',
            'b', 'c',
            'd', 'e', 'f'
        };

        StringBuffer sb = new StringBuffer("");
        int ba_len = ba.length;

        for (int i = 0; i < ba_len; i++) {
            sb.append(hexdigit[(ba[i] >> MAGIC_INTEGER_4) & MAGIC_INTEGER_OxOF]);
            sb.append(hexdigit[ba[i] & MAGIC_INTEGER_OxOF]);
        }
        return sb.toString();
    }

    /**
     * Creates a new ByteBuffer of length source.remaining(), and copies source
     * into it. Source position will not be changed.
     * @param source The ByteBuffer to copy from
     * @return A copy of the remaining bytes in source
     */
    public static ByteBuffer copy(ByteBuffer source) {
        ByteBuffer target = ByteBuffer.wrap(new byte[source.remaining()]);
        int position = source.position();
        target.put(source);
        source.position(position);
        return target;
    }
}
