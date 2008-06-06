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

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    
        
    }
    public void reset(){
        position = 0;
    }
    
    
    
    public void read(byte[] data) throws IOException{
        //sanity checks about length...
        par2file.read(data, offset+position);
        position += data.length;
    }
    
    public void write(byte[] data) throws IOException{
        //sanity checks about length
        par2file.write(data, offset+position);
        position += data.length;
    }
    

}
