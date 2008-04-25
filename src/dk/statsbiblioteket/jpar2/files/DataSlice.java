/*
 *     DataSlice.java
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

package dk.statsbiblioteket.jpar2.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import dk.statsbiblioteket.jpar2.checksum.CRC32;
import dk.statsbiblioteket.jpar2.checksum.MD5;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class DataSlice extends Slice{

    
    public DataSlice(File file, long beginning, int sliceSize){
        super(file, beginning, sliceSize);
    }
    /**
     * Compute the MD5 checksum of this slice. Read in the slice, and stores if
     * if setStoreSlice is true.
     * @return The MD5 checksum object
     * @throws java.io.FileNotFoundException If the file does not exist, or is
     * not readable
     * @throws java.io.IOException If something went wrong in the write.
     */
    public MD5 getMD5() throws FileNotFoundException, IOException {
        return MD5.calc(getBytes());
    }

    /**
     * Compute the CRC32 checksum of this slice. Read in the slice, and stores it
     * if setStoreSlice is true.
     * @return The CRC32 checksum object
     * @throws java.io.FileNotFoundException If the file does not exist, or is
     * not readable
     * @throws java.io.IOException If something went wrong in the write.
     */
    public CRC32 getCRC32() throws FileNotFoundException, IOException {
        return CRC32.calc(getBytes());
    }

}
