/*
 *     Par2Set.java
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

package javapar2.sets;


import javapar2.files.DataFile;
import javapar2.files.RecoveryData;
import java.io.FileNotFoundException;
import java.io.IOException;
import javapar2.*;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java representation of at set of files. Contain all the information from the
 * packets, in a more asseciable format. Feed this class to StorageSet to write
 * to disk og to RecoverySet to perform recovery functions. 
 * @author Asger Blekinge-Rasmussen
 */
public class Par2Set {
    private Logger logger = LoggerFactory.getLogger("javapar2.sets.Par2Set");
    
    private int sliceSize;
    private String client;
    
    private List<DataFile> datafiles = new ArrayList<DataFile>();
    private List<RecoveryData> parity = new ArrayList<RecoveryData>();

    public Par2Set(int sliceSize){
        this.sliceSize = sliceSize;
    }
    
    public void addDataFile(DataFile datafile){
        if (datafile!= null){
            datafiles.add(datafile);
        }
    }
        
    public void addDataFiles(List<DataFile> datafiles){
        for (DataFile df: datafiles){
            addDataFile(df);
        }
    }
    
    public void addParityBlock(RecoveryData block){
        if (block != null){
            parity.add(block);
        }
    }
    
    public void addParityBlocks(List<RecoveryData> blocks){
        for (RecoveryData rd: blocks){
            addParityBlock(rd);
        }
    }
   
   
    
    public List<DataFile> getDataFiles(){
        return new ArrayList<DataFile>(datafiles);
    }
    
    public List<RecoveryData> getParityBlocks(){
        return new ArrayList<RecoveryData>(parity);
    }
    
    public int getSliceSize(){
        return sliceSize;
    }
    
    public void setSliceSize(int sliceSize) 
            throws FileNotFoundException, IOException{
        this.sliceSize = sliceSize;
        sliceSizeChanged();
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getClient() {
        return client;
    }
    
    
    /**
     * Update the StoredSet when the sliceSize changed
     */
    private void sliceSizeChanged() throws FileNotFoundException, IOException{
        parity.clear();
        for (DataFile df: datafiles){
            df.setSliceSize(sliceSize);
        }
    }
}
