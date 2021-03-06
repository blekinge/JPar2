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
package dk.statsbiblioteket.jpar2.sets;

import dk.statsbiblioteket.jpar2.sets.StorageSet;
import dk.statsbiblioteket.jpar2.sets.Par2Set;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import dk.statsbiblioteket.jpar2.files.DataFile;
import junit.framework.TestCase;

/**
 *
 * @author Asger
 */
public class StorageSetTest extends TestCase {

    File dataDir = new File("test/unit/src/data/");
    File file = new File(dataDir, "lgpl-2.1.txt");
    String hash = "fbc093901857fcd118f065f900982c24";
    int sliceSize = 1024 * 1024;
    Par2Set p = new Par2Set(sliceSize);

    public StorageSetTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        p.addDataFile(new DataFile(file, sliceSize));

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of readFromFiles method, of class StorageSet.
     */
    public void testReadItselfFromFiles() throws Exception {
        List<ByteBuffer> bs = new StorageSet(p).writeData();
        File f = new File(dataDir, "tempfile.par2");
        f.deleteOnExit();
        f.createNewFile();
        OutputStream out = new FileOutputStream(f);
        for (ByteBuffer b : bs) {
            out.write(b.array());
        }
        out.flush();
        out.close();
        //set now in files

        Par2Set p2 = StorageSet.readFromFiles(Arrays.asList(f), null);

        List<ByteBuffer> bs2 = new StorageSet(p2).writeData();

        for (int i = 0; i < bs2.size(); i++) {
            if (!bs.get(i).equals(bs2.get(i))) {
                fail("Set changed after being written to disk");
            }
        }

    }

    /**
     * Test of readFromFiles method, of class StorageSet.
     */
    public void testReadValidatedSetFromFiles() throws Exception {

        File f = new File(dataDir, "defined.par2");


        Par2Set p2 = StorageSet.readFromFiles(Arrays.asList(f), dataDir);
        List<DataFile> datafiles = p2.getDataFiles();
        int sliceSize = p2.getSliceSize();
        assertEquals(sliceSize, 1024 );
        //System.out.println("SliceSize: " + sliceSize + " bytes");

        assertEquals(datafiles.size(), 1);
        //System.out.println("Number of datafiles in set: " + datafiles.size());

        for (DataFile df : datafiles) {
            assertEquals(df.getFile().exists(), true);
            //System.out.println("Exists: " + df.exists());

            assertEquals(file.getPath(), df.getFile().getPath().trim());
            //System.out.println("File: " + df.getPath().trim());

            assertEquals(df.getNumberOfSlices(), 26);
            //System.out.println("Number of slices: " + df.getNumberOfSlices());

            assertEquals(df.verify(), 0);
            //System.out.println("Number of slices to recover: " + df.verify());
        }
        //System.out.println("Number of parity blocks: " + p2.getParityBlocks().size());
        assertEquals(p2.getParityBlocks().size(), 0);

        assertEquals(p2.getClient(), "Created by par2cmdline version 0.4. ");
        ///System.out.println("Client used to write par2 file: " + p2.getClient());


    }
}
