

package dk.statsbiblioteket.jpar2.reedsolomon;

import dk.statsbiblioteket.jpar2.files.Par2File;
import dk.statsbiblioteket.jpar2.files.Par2Slice;
import junit.framework.TestCase;

import java.util.Arrays;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Random;


/**
 *
 * @author abr
 */
public class ReedSolomonTest extends TestCase {
    
    long sliceSize = 4*100;
    
    int afile_length = (int) (sliceSize * 10 + (186%sliceSize));
    int bfile_length = (int) (sliceSize * 25+(435%sliceSize));
    
    int parityfile_length = (int) (sliceSize * 26);
    
    public ReedSolomonTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {

        super.setUp();
        
        Random rnd = new Random();
        
        byte[] abuf = new byte[afile_length];
        rnd.nextBytes(abuf);
        
        byte[] bbuf = new byte[bfile_length];
        rnd.nextBytes(bbuf);
        
        File testFileA = new File("testfile1.txt");
        testFileA.createNewFile();
        testFileA.deleteOnExit();
        OutputStream outA = new FileOutputStream(testFileA);
        outA.write(abuf);
        outA.close();

        File testFileB = new File("testfile2.txt");
        testFileB.createNewFile();
        testFileB.deleteOnExit();
        OutputStream outB = new FileOutputStream(testFileB);
        outB.write(bbuf);
        outB.close();

        
        
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of addDataStream method, of class ReedSolomon.
     */
    public void testAddDataStream() {
    }

    /**
     * Test of generateParity method, of class ReedSolomon.
     */
    public void testGenerateParity() throws Exception{
        

        ReedSolomon rd = new ReedSolomon(sliceSize);
        
        Par2File afile = new Par2File(new RandomAccessFile("testfile1.txt", "r"), sliceSize);
        Par2Slice[] aslices = afile.getSlices();
        for (Par2Slice slice: aslices){
            rd.addDataSlice(slice);
        }

        Par2File bfile =
                new Par2File(new RandomAccessFile("testfile2.txt", "r"),
                             sliceSize);
        Par2Slice[] bslices = bfile.getSlices();
        for (Par2Slice slice: bslices){
            rd.addDataSlice(slice);
        }

        File testFileB = new File("testoutfile1.txt");
        testFileB.createNewFile();
        testFileB.deleteOnExit();
        Par2File parfile = new Par2File(new RandomAccessFile(testFileB, "rw"), sliceSize);
        rd.addParitySlice(new Par2Slice(parfile, 0, sliceSize));
        rd.addParitySlice(new Par2Slice(parfile, sliceSize, sliceSize));
        
        
        rd.generateParitySlices();
                
        
        byte[] data = new byte[(int)sliceSize*2];
        parfile.read(data, 0);
//        for (int i=0;i<data.length;i++){
//            System.out.println(data[i]);
//        }

    }

    /**
     * Test of testRecovery method, of class ReedSolomon.
     */
    public void testTestRecovery() throws Exception {
        ReedSolomon rd = new ReedSolomon(sliceSize);
        
        File testdatafile1 = new File("testfile1.txt");

        File testdatafile2 = new File("testfile2.txt");

        File testparityfile1 = new File("TestParityFile1.txt");
        testparityfile1.createNewFile();
        testparityfile1.deleteOnExit();

        File testrestoredfile1 = new File("TestrestoredFile1.txt");
        testrestoredfile1.createNewFile();
        testrestoredfile1.deleteOnExit();
        

        
        
        Par2File afile = new Par2File(new RandomAccessFile(testdatafile1, "r"), sliceSize);
        rd.addDataFile(afile);

        Par2File bfile =
                new Par2File(new RandomAccessFile(testdatafile2, "r"),
                             sliceSize);
        rd.addDataFile(bfile);

        Par2File parfile = new Par2File(new RandomAccessFile(testparityfile1, "rw"), sliceSize);
        parfile.setLength(parityfile_length);
        rd.addParityFile(parfile);
        
        
        rd.generateParitySlices();
        
        //The parity information is now stored in testparityfile1
        
        
        rd = new ReedSolomon(sliceSize);
        
        
        afile = new Par2File(new RandomAccessFile(testdatafile1, "r"), sliceSize);
        rd.addDataFile(afile);
        

        
        Par2File bfile_recovered =
                new Par2File(new RandomAccessFile(testrestoredfile1, "rw"),
                             sliceSize);
        bfile_recovered.setLength(bfile_length);
        rd.addLostFile(bfile_recovered);
        

        parfile = new Par2File(new RandomAccessFile(testparityfile1, "r"), sliceSize);
        rd.addParityFile(parfile);
        
        rd.recoverLostSlices();
        
        

        byte[] data = new byte[bfile_length];
        bfile.read(data, 0);
        byte[] data_recovered = new byte[bfile_length];
        bfile_recovered.read(data_recovered, 0);
 
        assertTrue(Arrays.equals(data, data_recovered));
        
        
        
        
        
    }

}
