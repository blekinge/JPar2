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
package dk.statsbiblioteket.jpar2.checksum;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import dk.statsbiblioteket.jpar2.byteutils.ByteUtil;

/**
 *
 * @author Asger
 */
public class MD5 extends Checksum {

    public MD5(){
        super();
    }
    
    public MD5(byte[] block){
        super(block);
    }
    
    public MD5(File file) 
            throws FileNotFoundException, IOException{
        super("MD5",file);
    }
    
    
    
    
   /**
    * Inefficient, large data copied.
    * @param block
    * @return
    * @throws java.lang.Error
    */
    public static MD5 calc(ByteBuffer block) throws Error {
        return calc(ByteUtil.array(block));

    }
    
    /**
     * Calculates the md5 hash of the bytes in block, and returns an
     * array of 16 bytes.
     * @param block the bytes to hash
     * @return The hash of block
     * @throws Error if md5 is not available
     */
    public static MD5 calc(byte[] block) throws Error {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new Error("md5 not available", ex);
        }

        algorithm.reset();
        algorithm.update(block);
        byte[] hash = algorithm.digest();
        
        return new MD5(hash);
    }
   
    
}
