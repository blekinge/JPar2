

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


/**
 *
 * @author abr
 */
public class ReedSolomonTest extends TestCase {
    
    long sliceSize = 16;
    
    public ReedSolomonTest(String testName) {
        super(testName);
    }            

    @Override
    protected void setUp() throws Exception {

        super.setUp();
        
        byte[] abuf = new byte[(int)sliceSize*2+3];
        Arrays.fill(abuf, (byte)64);

        byte[] bbuf = new byte[(int)sliceSize*2];
        Arrays.fill(bbuf, (byte)68);
        
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
        for (int i=0;i<data.length;i++){
            System.out.println(data[i]);
        }

    }

    /**
     * Test of testRecovery method, of class ReedSolomon.
     */
    public void testTestRecovery() throws Exception {
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
        
        
        rd = new ReedSolomon(sliceSize);
        afile = new Par2File(new RandomAccessFile("testfile1.txt", "r"), sliceSize);
        aslices = afile.getSlices();
        for (Par2Slice slice: aslices){
            rd.addDataSlice(slice);
        }
        
        Par2File bfile_recovered =
                new Par2File(new RandomAccessFile("testfile2_recovered.txt", "rw"),
                             sliceSize);
        Par2Slice[] bslices_recovered = bfile.getSlices();
        for (Par2Slice slice: bslices_recovered){
            rd.addLostSlice(slice);
        }

        
        parfile = new Par2File(new RandomAccessFile(testFileB, "rw"), sliceSize);
        Par2Slice[] parslices = parfile.getSlices();
        for (Par2Slice slice: parslices){
            rd.addParitySlice(slice);
        }
        
        rd.recoverLostSlices();
        

        
        byte[] data = new byte[(int)sliceSize*2];
        bfile.read(data, 0);
        for (int i=0;i<data.length;i++){
            System.out.println(data[i]);
        }
        
        
        data = new byte[(int)sliceSize*2];
        bfile_recovered.read(data, 0);
        for (int i=0;i<data.length;i++){
            System.out.println(data[i]);
        }

        
        
        
        
    }

}
