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
import javapar2.checksum.CRC32;
import java.io.File;
import junit.framework.TestCase;

/**
 *
 * @author Asger
 */
public class CRC32Test extends TestCase {
    
    File dataDir = new File("test/unit/src/data/");
    
    File file = new File(dataDir,"ytcracker - in my time.mp3");
    String checksum = "CE65E39F";
    
    
    public CRC32Test(String testName) {
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
     * Test of calc method, of class CRC32.
     */
    public void testCalc() throws Exception {
        CRC32 c = new CRC32(file);

        String hash = ByteUtil.toHex(c.getBytes());
        
        if (!hash.equalsIgnoreCase(checksum)){
            fail("Checksums does not match");
        }
        
        
        
    }
    
    

}
