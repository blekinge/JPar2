package dk.statsbiblioteket.jpar2.files;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author abr
 */
public class Par2File {

    private RandomAccessFile raf;
    private long sliceSize;
    private long header_offset;

    public Par2File(RandomAccessFile raf, long sliceSize) {
        this.raf = raf;
        this.sliceSize = sliceSize;
        this.header_offset = 0;
    }

    public Par2File(RandomAccessFile raf, long sliceSize, long header_offset) {
        this.raf = raf;
        this.sliceSize = sliceSize;
        this.header_offset = header_offset;
    }

    public void setLength(long newLength) throws IOException {
        raf.setLength(newLength);
    }

    public void write(byte[] data, long offset) throws IOException {
        raf.seek(header_offset+offset);
        raf.write(data);
    }

    public void read(byte[] data, long offset) throws IOException {
        raf.seek(header_offset+offset);
        raf.read(data);

    }
    
    public Par2Slice[] getSlices() throws IOException{
        long length = raf.length();
        
        int nslices = (int) Math.ceil((double)length/sliceSize);
        Par2Slice[] slices = new Par2Slice[nslices];
        
        for (int i=0;i<nslices-1;i++){
            slices[i] = new Par2Slice(this, i*sliceSize,sliceSize);
        }
        long offset =  (nslices-1)*sliceSize;
        slices[nslices-1] = new Par2Slice(this,offset,length-offset);
        
        
        return  slices;
        
        
        
    }
    
}
