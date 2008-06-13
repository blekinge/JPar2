package dk.statsbiblioteket.jpar2.files;

import java.io.IOException;

/**
 *
 * @author abr
 */
public class Par2Slice {
    
    private Par2File par2file;
    
    private long offset;
    
    private long length;
    
    private long position;

    public Par2Slice(Par2File par2file, long offset, long length) {
        this.par2file = par2file;
        this.offset = offset;
        this.length = length;
        this.position = 0;
    }

    
    /**
     * Return the position, relative to the start of this slice.
     * @return The position in bytes, as a long
     */
    public long getPosition() {
        return position;
    }

    /**
     * Set the position, relative to the start of this slice.
     * @param position the position in bytes, as a long
     */
    public void setPosition(long position) {
        this.position = position;
    }

    /**
     * Get the length of this slice, in bytes.
     * @return the length as a long in bytes
     */
    public long getLength() {
        return length;
    }
    
    
    /**
     * Resets the position to 0;
     */
    public void reset(){
        position = 0;
    }
    
    
    
    /**
     * Fill the given array with bytes read from the file, starting at the
     * current position.
     * @param data The data array to fill with bytes
     * @throws java.io.IOException If something goes wrong with the read
     */
    public void read(byte[] data) throws IOException{
        //sanity checks about length...
        par2file.read(data, offset+position);
        position += data.length;
    }
    
    
    /**
     * Write the contents of the given array to the file, starting at the current
     * position.
     * @param data the bytes to write
     * @throws java.io.IOException If something goes wrong with the write.
     */
    public void write(byte[] data) throws IOException{
        //sanity checks about length
        par2file.write(data, offset+position);
        position += data.length;
    }
    

}
