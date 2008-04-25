/*
 *     SliceChecksumPacketTest.java
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

package dk.statsbiblioteket.jpar2.par2packets;

import dk.statsbiblioteket.jpar2.par2packets.MainPacket;
import dk.statsbiblioteket.jpar2.par2packets.UnparsedPacket;
import dk.statsbiblioteket.jpar2.par2packets.FileDescriptionPacket;
import dk.statsbiblioteket.jpar2.par2packets.SliceChecksumPacket;
import java.io.File;
import java.nio.ByteBuffer;
import dk.statsbiblioteket.jpar2.files.DataFile;
import dk.statsbiblioteket.jpar2.par2packets.headers.RecoverySetID;
import junit.framework.TestCase;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class SliceChecksumPacketTest extends TestCase {

    public SliceChecksumPacketTest(String testName) {
        super(testName);
    }
    File dataDir = new File("test/unit/src/data/");
    File file = new File(dataDir, "lgpl-2.1.txt");
    String hash = "2130714986afd265ebdb05889cfcc344";
    RecoverySetID id;

    int sliceSize = 1024*1024*2;
    FileDescriptionPacket m1;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        MainPacket m = new MainPacket(sliceSize);
        id = m.calculateRecoverySetID();//problem here, but never mind
         m1 = new FileDescriptionPacket(id, file);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReadMySelf() throws Exception {
        SliceChecksumPacket s1 =
                new SliceChecksumPacket(m1, new DataFile(file, sliceSize));
        ByteBuffer d1 = s1.writePacket();
        SliceChecksumPacket s2 =
                new SliceChecksumPacket(new UnparsedPacket(d1));
        d1.rewind();
        ByteBuffer d2 = s2.writePacket();
        assertEquals(d2, d1);
    }
}
