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

package javapar2.files;


/* TODO Sliding checksummer
 * // This source file defines the FileCheckSummer object which is used
// when scanning a data file to find blocks of undamaged data.
//
// The object uses a "window" into the data file and slides that window
// along the file computing the CRC of the data in that window as it
// goes. If the computed CRC matches the value for a block of data
// from a target data file, then the MD5 Hash value is also computed
// and compared with the value for that block of data. When a match
// has been confirmed, the object jumps forward to where the next
// block of data is expected to start. Whilst the file is being scanned
// the object also computes the MD5 Hash of the whole file and of
// the first 16k of the file for later tests.
 * 
 * 
 * */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javapar2.checksum.CRC32;
import javapar2.checksum.MD5;

/**
 * The internal representation of file in the recoverySet. This class contain
 * all the information stored about a file. Storage mechanisms should read these
 * files and store them in some format.
 * @author Asger
 */
public class DataFile  {

    //TODO ADD hash16k to this
    private MD5 hash16k;
    private MD5 hash;
    private int sliceSize;
    private long length;
    private List<MD5> hashes = new ArrayList<MD5>();
    private List<CRC32> crc32s = new ArrayList<CRC32>();
    private File file;
    List<DataSlice> sliceList; 
   

    /**
     * Get the recorded md5 hash of the file
     * @return a 16 byte ByteBuffer with the hash.
     */
    public MD5 getHash() {
        return hash;
    }

    public MD5 getHash16k() {
        return hash16k;
    }

    /**
     * Get a list of hashes, one for each slice of the file. Enables you to
     * pinpoint exactly which parts of the file that should be recovered.
     * @return A list of 16 byte md5 hashes.
     */
    public List<MD5> getHashes() {
        return new ArrayList<MD5>(hashes);
    }

    public List<CRC32> getCRC32s() {
        return new ArrayList<CRC32>(crc32s);
    }

    /**
     * Get the number of slices in this fie
     * @return The number of slices, as an int
     */
    public int getNumberOfSlices() {
        return hashes.size();
    }

    /**
     * Get the size of the fileslices
     * @return the size of the file slices in bytes, as a long
     */
    public int getSliceSize() {
        return sliceSize;
    }

    /**
     * Get the length of the file
     * @return the length of the file in bytes, as a long
     */
    public long getLength() {
        return length;
    }

    public List<CRC32> getCrc32s() {
        return new ArrayList<CRC32>(crc32s);
    }
    
    public File getFile() {
        return file;
    }
    
    
    public List<DataSlice> getSliceList(){
        return sliceList;
    }
    
    private List<DataSlice> makeSliceList()
            throws FileNotFoundException, IOException {

        List<DataSlice> slices = new ArrayList<DataSlice>();
        for (long i = 0; i < length; i += sliceSize) {
            slices.add(new DataSlice(file,i,sliceSize));
        }
        return slices;
    }
    
    /**
     * TODO
     * @throws java.io.IOException
     */
   public void writeEntireSliceList() throws IOException{
        file.delete();
        file.createNewFile();
        OutputStream out = new FileOutputStream(file);
        /*
        int s = 0;
        for (Slice slice: sliceList){
            int len = (int)Math.min(sliceSize,length-s*sliceSize);
            out.write(slice,0,len);
        }*/
        out.close();
    }

  
    /**
     * The index into the slicelist of the defect slices.
     * @return
     * @throws java.io.IOException
     */
    public List<Integer> verifyWithIndex()throws IOException {
        List<Integer> defectSlices = new ArrayList<Integer>();
        try {

            /* Reading the entire file twice, not good. Ditch this
            MD5 hashOfFile = new MD5(file);
            if (hashOfFile.equals(hash)) {
                //entire file matches, so all slices must match to. 
                return defectSlices;
            }*/
            
            int sliceNumber = 0;

            for (DataSlice slice : sliceList) {
                MD5 hashOfSlice = slice.getMD5();
                if (!hashOfSlice.equals(hashes.get(sliceNumber))) {
                    //the hash doesnt match
                    defectSlices.add(sliceNumber);
                }
                sliceNumber++;
            }
            return defectSlices;
        } catch (FileNotFoundException f) {
            for (int i = 0; i < hashes.size(); i++) {
                defectSlices.add(i);
            }
            return defectSlices;
        }


    }
    
    
    /** Utility method, not guaranteed.
     * 
     */
    public List<Integer> compareWithIndex(DataFile df) throws IOException {
        List<Integer> diffs = new ArrayList<Integer>();
        
        if (df.length != this.length){
            throw new IOException("Files are not same length!");
        }
        
        if (df.getHash().equals(hash)){
            return diffs;
        }
        
        
        List<MD5> dfhashes = df.getHashes();
        for (int i=0;i<hashes.size();i++){
            if (!dfhashes.get(i).equals(hashes.get(i))){
                diffs.add(i);
            }
            
        }

        return diffs;
    }

    /**
     * 
     * @return the number of slices that must be recovered.
     * @throws java.io.IOException if the file cannot be read
     */
    public int verify() throws IOException {
        return verifyWithIndex().size();
    }

    /**
     * Recalculates all the hashes in the DataFile, and updates the datafile
     * to the current state of the underlying file object.
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public void recalculate() throws FileNotFoundException, IOException {
       
        length = file.length();

        InputStream in = new FileInputStream(file);
        int length16k = (int) Math.min(16 * 1024, length);
        byte[] bytes16k = new byte[length16k];
        in.read(bytes16k);
        in.close();
        this.hash16k = MD5.calc(bytes16k);

        setSliceSize(sliceSize);
        
    }
    
      /**
     * Set the new Slice size of the file, and recalculate the checksums for
     * each slice.
     * @param sliceSize The new sliceSize
     * @throws java.io.FileNotFoundException If the file cannot be found
     * @throws java.io.IOException If the file cannot be read, or the sliceSize
     * is larger than Integer.MAX_VALUE
     */
    public void setSliceSize(int sliceSize)
            throws FileNotFoundException, IOException {
        if (sliceSize > Integer.MAX_VALUE) {
            throw new IOException("Cannot use such large slices " + sliceSize +
                    ", go below " + Integer.MAX_VALUE);
        }
        
        //TODO: Hide this in md5
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            throw new Error("md5 not available", ex);
        }

        
        this.sliceSize = sliceSize;
        hashes.clear();
        crc32s.clear();
        
        sliceList = makeSliceList();
        for (DataSlice slice: sliceList){
            
            algorithm.update(slice.getBytes());
            hashes.add(slice.getMD5());
            crc32s.add(slice.getCRC32());
            
        }
        hash = new MD5(algorithm.digest());

    }
    
    /**
     * Creates the file that this object refers to, and sets the length. If the 
     * file exist, delete it and make a new empty file of the correct length. 
     * The file will be filled with undefined data.
     * @throws java.io.IOException if the file cannot be written
     */
    public void createFile() throws IOException{
        if (file.exists()) {
            file.delete();

        }
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        out.getChannel().position(length - 1);
        out.write(0);
        out.close();
    }


    /**
     * Create a new DataFile from a file on the system.
     * @param file The Java File object. Encodes the filename
     * @param sliceSize the size of each slice
     * @throws java.io.FileNotFoundException If the file is not found
     * @throws java.io.IOException If the file cannot be read
     */
    public DataFile(File file, int sliceSize)
            throws FileNotFoundException, IOException {

        this.file = file;
        this.sliceSize = sliceSize;
        recalculate();
    }

    /**
     * Construct a new DataFile from stored information. Please note, you cannot
     * expect this file to exist in the system.
     * @param file
     * @param sliceSize
     * @param hash16k 
     * @param hash
     * @param length
     * @param hashes
     * @param crc32s 
     */
    public DataFile(File file, int sliceSize, MD5 hash16k, MD5 hash,
            long length, List<MD5> hashes, List<CRC32> crc32s) {
        this.file = file;
        this.length = length;
        this.sliceSize = sliceSize;
        this.hash16k = hash16k;
        this.hash = hash;

        this.hashes.clear();
        this.hashes.addAll(hashes);
        this.crc32s.clear();
        this.crc32s.addAll(crc32s);
        sliceList = new ArrayList<DataSlice>();
        for (int i=0;i<hashes.size();i++){
            sliceList.add(new DataSlice(file, i*sliceSize, sliceSize));
        }
    }
}

