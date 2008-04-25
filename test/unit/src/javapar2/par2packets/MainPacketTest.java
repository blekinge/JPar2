/*
 *     MainPacketTest.java
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

package javapar2.par2packets;

import java.nio.ByteBuffer;
import junit.framework.TestCase;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class MainPacketTest extends TestCase {
    
    public MainPacketTest(String testName) {
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

    public void testReadMySelf() throws InvalidPacketException{
        MainPacket m1 = new MainPacket(1024*1024*20);
        ByteBuffer d1 = m1.writePacket();
        MainPacket m2 = new MainPacket(new UnparsedPacket(d1));
        d1.rewind();
        ByteBuffer d2 = m2.writePacket();
        assertEquals(d2, d1);
        
    }
    
   
}
