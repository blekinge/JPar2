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

import javapar2.checksum.MD5;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import junit.framework.TestCase;

/**
 *
 * @author Asger
 */
public class MD5Test extends TestCase {
    
    File dataDir = new File("test/unit/src/data/");
    
    File file = new File(dataDir,"ytcracker - in my time.mp3");
    String hash = "2130714986afd265ebdb05889cfcc344";
    
    public MD5Test(String testName) {
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
    
    public void testMD5file() throws FileNotFoundException, IOException{
        //System.out.println(file.getAbsolutePath());
        MD5 md5 = new MD5(file);
        assertEquals(md5.toString(), hash);
    }

}
