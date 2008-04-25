/*
 *     Bytes.java
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

/**
 * Class of static methods for converting between simple types and byte arrays. 
 * The methods understand both little and big endianness.
 * <p> This class has been inspired, but not copied from, Bits.java by Sun 
 * Microsystems, and contain some of the same functionality, but in a different
 * implementation. 
 * @author Asger Blekinge-Rasmussen
 */
public class Bytes {

    private static int parse(byte b0, byte b1, byte b2, byte b3) {
        return (int) ((((b3 & 0xff) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) << 8) |
                ((b0 & 0xff) << 0)));

    }

    private static short parse(byte b0, byte b1) {
        return (short) ((b1 << 8) | (b0 & 0xff));

    }

    private static long parse(byte b0, byte b1, byte b2, byte b3, byte b4,
            byte b5, byte b6, byte b7) {
        return ((((long) b7 & 0xff) << 56) |
                (((long) b6 & 0xff) << 48) |
                (((long) b5 & 0xff) << 40) |
                (((long) b4 & 0xff) << 32) |
                (((long) b3 & 0xff) << 24) |
                (((long) b2 & 0xff) << 16) |
                (((long) b1 & 0xff) << 8) |
                (((long) b0 & 0xff) << 0));

    }

    /**
     * Parse the first 2 bytes after offset in bytes, as a short, using big endian
     * @param bytes the array that holds the bytes
     * @param offset the offset into the array
     * @return the bytes as a short
     */
    public static short asShortB(byte[] bytes, int offset) {
        return parse(
                bytes[offset + 1],
                bytes[offset + 0]);

    }

    /**
     * Parse the first 2 bytes after offset in bytes, as a short, using little endian
     * @param bytes the array that holds the bytes
     * @param offset the offset into the array
     * @return the bytes as a short
     */
    public static short asShortL(byte[] bytes, int offset) {
        return parse(
                bytes[offset + 0],
                bytes[offset + 1]);


    }

    /**
     * Parse the first 2 bytes after offset in bytes, as a short, according to 
     * bigEndian
     * @param bytes the array that holds the bytes
     * @param offset the offset into the array
     * @param bigEndian the endianness to use
     * @return the bytes as a short
     */
    public static short asShort(byte[] bytes, int offset, boolean bigEndian) {
        if (bigEndian) {
            return asShortB(bytes, offset);
        } else {
            return asShortL(bytes, offset);
        }
    }

    /**
     * Parse the first 4 bytes after offset in bytes, as an int, using big
     * endian
     * @param bytes the array that holds the bytes
     * @param offset the offset into the array
     * @return the bytes as an int
     */
    public static int asIntB(byte[] bytes, int offset) {
        return parse(
                bytes[offset + 3],
                bytes[offset + 2],
                bytes[offset + 1],
                bytes[offset + 0]);

    }

    /**
     * Parse the first 4 bytes after offset in bytes, as an int, using little
     * endian
     * @param bytes the array that holds the bytes
     * @param offset the offset into the array
     * @return the bytes as an int
     */
    public static int asIntL(byte[] bytes, int offset) {
        return parse(
                bytes[offset + 0],
                bytes[offset + 1],
                bytes[offset + 2],
                bytes[offset + 3]);

    }

    /**
     * Parse the first 4 bytes after offset in bytes, as an int, according to 
     * bigEndian
     * <p>It is more efficient to use asIntL or asIntB if you know the endianness
     * at compile time.
     * @param bytes the array that holds the bytes
     * @param offset the offset into the array
     * @param bigEndian the endianness to use
     * @return the bytes as an int
     */
    public static int asInt(byte[] bytes, int offset, boolean bigEndian) {
        if (bigEndian) {
            return asIntB(bytes, offset);
        } else {
            return asIntL(bytes, offset);
        }

    }

    /**
     * Parse the first 8 bytes after offset in bytes, as a long, using big
     * endian
     * @param bytes the array that holds the bytes
     * @param offset the offset into the array
     * @return the bytes as a long
     */
    public static long asLongB(byte[] bytes, int offset) {
        return parse(
                bytes[offset + 7],
                bytes[offset + 6],
                bytes[offset + 5],
                bytes[offset + 4],
                bytes[offset + 3],
                bytes[offset + 2],
                bytes[offset + 1],
                bytes[offset + 0]);


    }

    /**
     * Parse the first 8 bytes after offset in bytes, as a long, using little
     * endian
     * @param bytes the array that holds the bytes
     * @param offset the offset into the array
     * @return the bytes as a long
     */
    public static long asLongL(byte[] bytes, int offset) {
        return parse(
                bytes[offset + 0],
                bytes[offset + 1],
                bytes[offset + 2],
                bytes[offset + 3],
                bytes[offset + 4],
                bytes[offset + 5],
                bytes[offset + 6],
                bytes[offset + 7]);

    }

    /**
     * Parse the first 8 bytes after offset in bytes, as a long, according to 
     * bigEndian
     * <p>It is more efficient to use asLongL or asLongB if you know the endianness
     * at compile time.
     * @param bytes the array that holds the bytes
     * @param offset the offset into the array
     * @param bigEndian the endianness to use
     * @return the bytes as a long
     */
    public static long asLong(byte[] bytes, int offset, boolean bigEndian) {
        if (bigEndian) {
            return asLongB(bytes, offset);
        } else {
            return asLongL(bytes, offset);

        }
    }

    /**
     * Stores the value as 2 bytes from offset and onwards in target, as bit
     * endian. This means the most significant byte is the zeroth element
     * @param value To convert to bytes
     * @param target the array to store the bytes in
     * @param offset the offset into the array
     * @return target
     */
    public static byte[] toBytesB(short value, byte[] target, int offset) {
        target[offset + 0] = byte1(value);
        target[offset + 1] = byte0(value);
        return target;
    }

    /**
     * Stores the value as 2 bytes from offset and onwards in target, as little
     * endian. This means the most significant byte is the last element
     * @param value To convert to bytes
     * @param target the array to store the bytes in
     * @param offset the offset into the array
     * @return target
     */
    public static byte[] toBytesL(short value, byte[] target, int offset) {
        target[offset + 1] = byte1(value);
        target[offset + 0] = byte0(value);
        return target;
    }

    /**
     * Stores the value as 2 bytes from offset and onwards in target, according
     * to bigEndian
     * @param value To convert to bytes
     * @param target the array to store the bytes in
     * @param offset the offset into the array
     * @param bigEndian true to use big endian, false for little endian
     * @return target
     */
    public static byte[] toBytes(short value, byte[] target, int offset,
            boolean bigEndian) {
        if (bigEndian) {
            return toBytesB(value, target, offset);
        } else {
            return toBytesL(value, target, offset);
        }
    }

    /**
     * Stores the value as 4 bytes from offset and onwards in target, according
     * to bigEndian
     * @param value To convert to bytes
     * @param target the array to store the bytes in
     * @param offset the offset into the array
     * @param bigEndian true to use big endian, false for little endian
     * @return target
     */
    public static byte[] toBytes(int value, byte[] target, int offset,
            boolean bigEndian) {
        if (bigEndian) {
            return toBytesB(value, target, offset);
        } else {
            return toBytesL(value, target, offset);
        }
    }

    /**
     * Stores the value as 4 bytes from offset and onwards in target, as big
     * endian. This means the most significant byte is the zeroth element
     * @param value To convert to bytes
     * @param target the array to store the bytes in
     * @param offset the offset into the array
     * @return target
     */
    public static byte[] toBytesB(int value, byte[] target, int offset) {
        target[offset + 0] = byte3(value);
        target[offset + 1] = byte2(value);
        target[offset + 2] = byte1(value);
        target[offset + 3] = byte0(value);
        return target;
    }

    /**
     * Stores the value as 4 bytes from offset and onwards in target, as little
     * endian. This means the most significant byte is the last element
     * @param value To convert to bytes
     * @param target the array to store the bytes in
     * @param offset the offset into the array
     * @return target
     */
    public static byte[] toBytesL(int value, byte[] target, int offset) {
        target[offset + 3] = byte3(value);
        target[offset + 2] = byte2(value);
        target[offset + 1] = byte1(value);
        target[offset + 0] = byte0(value);
        return target;
    }

    /**
     * Stores the value as 8 bytes from offset and onwards in target, as big
     * endian. This means the most significant byte is the zeroth element
     * @param value To convert to bytes
     * @param target the array to store the bytes in
     * @param offset the offset into the array
     * @return target
     */
    public static byte[] toBytesB(long value, byte[] target, int offset) {
        target[offset + 0] = byte7(value);
        target[offset + 1] = byte6(value);
        target[offset + 2] = byte5(value);
        target[offset + 3] = byte4(value);
        target[offset + 4] = byte3(value);
        target[offset + 5] = byte2(value);
        target[offset + 6] = byte1(value);
        target[offset + 7] = byte0(value);
        return target;

    }

    /**
     * Stores the value as 8 bytes from offset and onwards in target, as little
     * endian. This means the most significant byte is the last element
     * @param value To convert to bytes
     * @param target the array to store the bytes in
     * @param offset the offset into the array
     * @return target
     */
    public static byte[] toBytesL(long value, byte[] target, int offset) {
        target[offset + 7] = byte7(value);
        target[offset + 6] = byte6(value);
        target[offset + 5] = byte5(value);
        target[offset + 4] = byte4(value);
        target[offset + 3] = byte3(value);
        target[offset + 2] = byte2(value);
        target[offset + 1] = byte1(value);
        target[offset + 0] = byte0(value);
        return target;

    }

    /**
     * Stores the value as 8 bytes from offset and onwards in target, according
     * to bigEndian
     * @param value To convert to bytes
     * @param target the array to store the bytes in
     * @param offset the offset into the array
     * @param bigEndian true to use big endian, false for little endian
     * @return target
     */
    public static byte[] toBytes(long value, byte[] target, int offset,
            boolean bigEndian) {
        if (bigEndian) {
            return toBytesB(value, target, offset);
        } else {
            return toBytesL(value, target, offset);
        }

    }

    private static byte byte0(long x) {
        return (byte) (x >> 0);
    }

    private static byte byte1(long x) {
        return (byte) (x >> 8);
    }

    private static byte byte2(long x) {
        return (byte) (x >> 16);
    }

    private static byte byte3(long x) {
        return (byte) (x >> 24);
    }

    private static byte byte4(long x) {
        return (byte) (x >> 32);
    }

    private static byte byte5(long x) {
        return (byte) (x >> 40);
    }

    private static byte byte6(long x) {
        return (byte) (x >> 48);
    }

    private static byte byte7(long x) {
        return (byte) (x >> 56);
    }
}
