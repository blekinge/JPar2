/*
 *     CreatorPacketTest.java
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

import java.io.File;
import java.nio.ByteBuffer;
import javapar2.par2packets.InvalidPacketException;
import javapar2.par2packets.headers.RecoverySetID;
import junit.framework.TestCase;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class CreatorPacketTest extends TestCase {

    public CreatorPacketTest(String testName) {
        super(testName);
    }
    File dataDir = new File("test/unit/src/data/");
    File file = new File(dataDir, "ytcracker - in my time.mp3");
    String hash = "2130714986afd265ebdb05889cfcc344";
    RecoverySetID id;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MainPacket m = new MainPacket(1024 * 2);
        id = m.calculateRecoverySetID();//problem here, but never mind
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReadMySelf() throws InvalidPacketException {
        CreatorPacket m1 = new CreatorPacket(id,"TestClient");
        ByteBuffer d1 = m1.writePacket();
        CreatorPacket m2 = new CreatorPacket(new UnparsedPacket(d1));
        d1.rewind();
        ByteBuffer d2 = m2.writePacket();
        assertEquals(d2, d1);

    }
}
