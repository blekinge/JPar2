/*
 *     Slice.java
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;

/**
 * Class representing a slice of the file. Allows for transparent reading of the
 * file, so that the entire file does not need to reside in memory
 * @author Asger Blekinge-Rasmussen
 */
public class Slice {

    private File file;
    private long beginning;
    private int sliceSize;
    private boolean storeSlice;
    private SoftReference<byte[]> slice;

    /**
     * Create a new slice, for this part of the file. No data will be read now.
     * @param file The file to slice
     * @param beginning The offset of this slice in the file
     * @param sliceSize The length of this slice
     */
    public Slice(File file, long beginning, int sliceSize) {
        this.file = file;
        this.beginning = beginning;
        this.sliceSize = sliceSize;
        this.storeSlice = false;
        this.slice = new SoftReference<byte[]>(null);
    }

    /**
     * Reads the slice from the file and returns it
     * @return a byte array representing the contents of the slice
     */
    private byte[] readSliceFromFile() throws FileNotFoundException, IOException {
        if (!(file.isFile() && file.canRead())) {
            throw new FileNotFoundException("The file of this slice does not exist");
        }

        //nothing stored, read it from file
        byte[] sliceArray = new byte[sliceSize];
        FileInputStream in = new FileInputStream(file);
        in.skip(beginning);
        if (in.read(sliceArray) < 0) {
            throw new IOException("The file is contain no data in this location");
        }
        in.close();
        return sliceArray;

    }

    /**
     * Get the byte array with the contents of this slice. Changes will not write
     * through to the file, but this slice will store the same array, so changes
     * will make it confused.
     * The array will be read from the file. It will be stored in cache in this
     * object, to
     * speed up further reads, but will be garbage collected if the memory is
     * needed, and only the slice contain a reference to it. 
     * @return a byte array containing the data from the file.
     * @throws java.io.FileNotFoundException If the file cannot be be found, or
     * is not readable 
     * @throws java.io.IOException if the file is not long enough, or something
     * else went wrong.
     */
    public byte[] getBytes() throws FileNotFoundException, IOException {
        byte[] sliceBytes = slice.get();
        if (sliceBytes == null){
            sliceBytes = readSliceFromFile();
        }
        slice = new SoftReference<byte[]>(sliceBytes);
        return sliceBytes;
    }


    /**
     * Write the stored contents to this part of the file. Will increase the files size if 
     * nessesary to fit the slice.
     * @param bytes 
     * @throws java.io.FileNotFoundException If the file does not exist, or cannot
     * be written to.
     * @throws java.io.IOException If the array is the wrong length or null, or
     * something else went wrong with the write.
     */
    public void writeSlice(byte[] bytes) throws FileNotFoundException, IOException {
        if (bytes != null || bytes.length != sliceSize) {
            throw new RuntimeException("Invalid length of byte array for slice");
        }

        if (!file.isFile() || !file.canWrite()) {
            throw new FileNotFoundException("The file does not exist, or cannot be written");
        }
        FileOutputStream out = new FileOutputStream(file);
        out.getChannel().write(ByteBuffer.wrap(bytes), beginning);
        out.close();
        slice.clear();

    }
}
