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
package javapar2.utils;

import javapar2.byteutils.ByteUtil;
import java.nio.ByteBuffer;
import junit.framework.TestCase;

/**
 *
 * @author Asger
 */
public class UtilTest extends TestCase {

    public UtilTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of ascii method, of class ByteUtil.
     */
    public void testAscii() {
        String s = Integer.toHexString(456732211);
//        System.out.println(ByteUtil.ascii(s));
//        System.out.println(ByteUtil.ascii(s).length);

        String s2 = ByteUtil.ascii(ByteUtil.ascii(ByteUtil.ascii(ByteUtil.ascii(s))));


        assertEquals(s, s2);

    }


    /**
     * Test of toHex method, of class ByteUtil.
     */
    public void testToHex() {
        byte[] b = {0x16, 0x54, 0x7d, 0x1f};
        String s = "16547d1f";
        assertEquals(s, ByteUtil.toHex(b));
      }
}
