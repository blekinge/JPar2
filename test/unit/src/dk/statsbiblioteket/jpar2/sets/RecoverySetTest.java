/*
 *    RecoverySetTest.java
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

package dk.statsbiblioteket.jpar2.sets;

import dk.statsbiblioteket.jpar2.sets.RecoverySet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import dk.statsbiblioteket.jpar2.reedsolomon.math.field.GaloisField;
import dk.statsbiblioteket.jpar2.files.DataFile;
import dk.statsbiblioteket.jpar2.checksum.MD5;
import junit.framework.TestCase;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class RecoverySetTest extends TestCase {
    
    public RecoverySetTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InputStream in = new FileInputStream(file);
        
        OutputStream out1 = new FileOutputStream(test1);
        
        byte[] contents = new byte[(int)file.length()];
        in.read(contents);
        in.close();
        out1.write(contents);
        out1.flush();
        out1.close();

        testfile1 = new DataFile(file, sliceSize);

                
        
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        test1.delete();

        
    }

    int sliceSize = 100;
    File dataDir = new File("test/unit/src/data/");
    File file = new File(dataDir, "testaaa.txt");

    File test1 = new File(dataDir, "testfile1.txt");


    
    DataFile testfile1;

//    
//    public void testRegenerateEntireFile() throws Exception {
//        Par2Set p1 = new Par2Set(sliceSize);
//        
//        testfile1 = new DataFile(test1,sliceSize);
//
//        p1.addDataFile(testfile1);
//        
//        RecoverySet r = new RecoverySet(p1);
//        r.generateParityBlocks(6);
//        p1 = r.write();
//        
//        assertEquals(testfile1.verify(),0);
//        MD5 hash = testfile1.getHash();
//        testfile1.getFile().delete();
//        
//        assertEquals(testfile1.verify(), 6);
//        
//        RecoverySet r2 = new RecoverySet(p1);
//                
//        r2.recover();
//        testfile1.writeEntireSliceList();
//        testfile1.recalculate();
//        
//        assertEquals(hash,testfile1.getHash());
//        
//        
//        
//    }
//    
    /**
     * Test of generateParityBlocks method, of class RecoverySet.
     */
    public void testGenerateConstants() throws Exception {

        int blocks = 11;
        GaloisField gf = new GaloisField(GaloisField.WordSize.SIXTEEN);
        
        
        int[] constants = RecoverySet.generateConstants(blocks, gf);
        
        int[] result = new int[]{2,4,16,128,256,2048,8192,16384,4107,32856,17132};
        
        assertTrue(Arrays.equals(constants, result));

    }


}

