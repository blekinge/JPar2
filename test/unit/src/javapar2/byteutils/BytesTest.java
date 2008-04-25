/*
 *     BytesTest.java
 *     Copyright (C) 2008  Asger
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

import junit.framework.TestCase;

/**
 *
 * @author Asger
 */
public class BytesTest extends TestCase {

    public BytesTest(String testName) {
        super(testName);
    }
    byte[] bytes = new byte[8];

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    short s1 = -1;
    short s2 = (short) (1 << 16 - 1);
    short s3 = 0;
    int i1 = -1;
    int i2 = 1 << 32 - 1;
    int i3 = 0;
    long l1 = -1;
    long l2 = 1 << 63 - 1;
    long l3 = 0;

    /**
     * Test of asShortB method, of class Bytes.
     */
    public void testAsShortB() {



        assertEquals(s1, Bytes.asShortB(Bytes.toBytesB(s1, bytes, 0), 0));
        assertEquals(s2, Bytes.asShortB(Bytes.toBytesB(s2, bytes, 0), 0));
        assertEquals(s3, Bytes.asShortB(Bytes.toBytesB(s3, bytes, 0), 0));


    }

    /**
     * Test of asShortL method, of class Bytes.
     */
    public void testAsShortL() {

        assertEquals(s1, Bytes.asShortL(Bytes.toBytesL(s1, bytes, 0), 0));
        assertEquals(s2, Bytes.asShortL(Bytes.toBytesL(s2, bytes, 0), 0));
        assertEquals(s3, Bytes.asShortL(Bytes.toBytesL(s3, bytes, 0), 0));



    }

    /**
     * Test of asIntB method, of class Bytes.
     */
    public void testAsIntB() {

        assertEquals(i1, Bytes.asIntB(Bytes.toBytesB(i1, bytes, 0), 0));
        assertEquals(i2, Bytes.asIntB(Bytes.toBytesB(i2, bytes, 0), 0));
        assertEquals(i3, Bytes.asIntB(Bytes.toBytesB(i3, bytes, 0), 0));



    }

    /**
     * Test of asIntL method, of class Bytes.
     */
    public void testAsIntL() {


        assertEquals(i1, Bytes.asIntL(Bytes.toBytesL(i1, bytes, 0), 0));
        assertEquals(i2, Bytes.asIntL(Bytes.toBytesL(i2, bytes, 0), 0));
        assertEquals(i3, Bytes.asIntL(Bytes.toBytesL(i3, bytes, 0), 0));


    }

    /**
     * Test of asLongB method, of class Bytes.
     */
    public void testAsLongB() {
        assertEquals(l1, Bytes.asLongB(Bytes.toBytesB(l1, bytes, 0), 0));
        assertEquals(l2, Bytes.asLongB(Bytes.toBytesB(l2, bytes, 0), 0));
        assertEquals(l3, Bytes.asLongB(Bytes.toBytesB(l3, bytes, 0), 0));

    }

    /**
     * Test of asLongL method, of class Bytes.
     */
    public void testAsLongL() {
        assertEquals(l1, Bytes.asLongL(Bytes.toBytesL(l1, bytes, 0), 0));
        assertEquals(l2, Bytes.asLongL(Bytes.toBytesL(l2, bytes, 0), 0));
        assertEquals(l3, Bytes.asLongL(Bytes.toBytesL(l3, bytes, 0), 0));

    }
}
