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
package dk.statsbiblioteket.jpar2.sets;

import java.util.List;
import java.util.Set;



import dk.statsbiblioteket.jpar2.files.DataFile;
import dk.statsbiblioteket.jpar2.files.RecoveryData;
import dk.statsbiblioteket.jpar2.math.field.GaloisField;

/**
 * The class used to perform recovery operations on a Par2Set
 * @author Asger
 */
public class RecoverySet {

    private List<DataFile> datafiles;
    private List<RecoveryData> parityblocks;
    private Set<Integer> usedExponents;
    private List<byte[]> sliceList;
    //private List<Integer> indexForDefectSlices;
    private int[] constants;
    private int sliceSize;
    //RECOVERY BLOCKS
    
    
    private GaloisField gf16 = new GaloisField(GaloisField.WordSize.SIXTEEN);

//    /*
//    public void computeRSmatrix(){
//        inputblocks.resize(sourceblockcount);   // The DataBlocks that will read from disk
//        copyblocks.resize(availableblockcount); // Those DataBlocks which need to be copied
//        outputblocks.resize(missingblockcount); // Those DataBlocks that will re recalculated
//
//        vector<DataBlock*>::iterator inputblock  = inputblocks.begin();
//        vector<DataBlock*>::iterator copyblock   = copyblocks.begin();
//        vector<DataBlock*>::iterator outputblock = outputblocks.begin();
//
//        // Build an array listing which source data blocks are present and which are missing
//        vector<bool> present;
//        present.resize(sourceblockcount);
//
//        vector<DataBlock>::iterator sourceblock  = sourceblocks.begin();
//        vector<DataBlock>::iterator targetblock  = targetblocks.begin();
//        vector<bool>::iterator              pres = present.begin();
//
//        // Iterate through all source blocks for all files
//        while (sourceblock != sourceblocks.end())
//        {
//            // Was this block found
//            if (sourceblock->IsSet())
//            {
//        //      // Open the file the block was found in.
//        //      if (!sourceblock->Open())
//        //        return false;
//
//            // Record that the block was found
//            *pres = true;
//
//            // Add the block to the list of those which will be read 
//            // as input (and which might also need to be copied).
//            *inputblock = &*sourceblock;
//            *copyblock = &*targetblock;
//
//            ++inputblock;
//            ++copyblock;
//            }
//            else
//            {
//            // Record that the block was missing
//            *pres = false;
//                
//            // Add the block to the list of those to be written
//            *outputblock = &*targetblock;
//            ++outputblock;
//            }
//
//            ++sourceblock;
//            ++targetblock;
//            ++pres;
//        }
//
//        // Set the number of source blocks and which of them are present
//        if (!rs.SetInput(present))
//            return false;
//
//        // Start iterating through the available recovery packets
//        map<u32,RecoveryPacket*>::iterator rp = recoverypacketmap.begin();
//
//        // Continue to fill the remaining list of data blocks to be read
//        while (inputblock != inputblocks.end())
//        {
//            // Get the next available recovery packet
//            u32 exponent = rp->first;
//            RecoveryPacket* recoverypacket = rp->second;
//
//            // Get the DataBlock from the recovery packet
//            DataBlock *recoveryblock = recoverypacket->GetDataBlock();
//
//        //    // Make sure the file is open
//        //    if (!recoveryblock->Open())
//        //      return false;
//
//            // Add the recovery block to the list of blocks that will be read
//            *inputblock = recoveryblock;
//
//            // Record that the corresponding exponent value is the next one
//            // to use in the RS matrix
//            if (!rs.SetOutput(true, (u16)exponent))
//                return false;
//
//            ++inputblock;
//            ++rp;
//        }
//
//        // If we need to, compute and solve the RS matrix
//        if (missingblockcount == 0)
//            return true;
//  
//        bool success = rs.Compute(noiselevel);
//
//        return success;
//        
//    }
//    
//    
//    
//// Read source data, process it through the RS matrix and write it to disk.
//boolean ProcessData(long blockoffset, int blocklength)
//{
//  long totalwritten = 0;
//
//  // Clear the output buffer
//  memset(outputbuffer, 0, (size_t)chunksize * missingblockcount);
//
//  vector<DataBlock*>::iterator inputblock = inputblocks.begin();
//  vector<DataBlock*>::iterator copyblock  = copyblocks.begin();
//  u32                          inputindex = 0;
//
//  DiskFile *lastopenfile = NULL;
//
//  // Are there any blocks which need to be reconstructed
//  if (missingblockcount > 0)
//  {
//    // For each input block
//    while (inputblock != inputblocks.end())       
//    {
//      // Are we reading from a new file?
//      if (lastopenfile != (*inputblock)->GetDiskFile())
//      {
//        // Close the last file
//        if (lastopenfile != NULL)
//        {
//          lastopenfile->Close();
//        }
//
//        // Open the new file
//        lastopenfile = (*inputblock)->GetDiskFile();
//        if (!lastopenfile->Open())
//        {
//          return false;
//        }
//      }
//
//      // Read data from the current input block
//      if (!(*inputblock)->ReadData(blockoffset, blocklength, inputbuffer))
//        return false;
//
//      // Have we reached the last source data block
//      if (copyblock != copyblocks.end())
//      {
//        // Does this block need to be copied to the target file
//        if ((*copyblock)->IsSet())
//        {
//          size_t wrote;
//
//          // Write the block back to disk in the new target file
//          if (!(*copyblock)->WriteData(blockoffset, blocklength, inputbuffer, wrote))
//            return false;
//
//          totalwritten += wrote;
//        }
//        ++copyblock;
//      }
//
//      // For each output block
//      for (u32 outputindex=0; outputindex<missingblockcount; outputindex++)
//      {
//        // Select the appropriate part of the output buffer
//        void *outbuf = &((u8*)outputbuffer)[chunksize * outputindex];
//
//        // Process the data
//        rs.Process(blocklength, inputindex, inputbuffer, outputindex, outbuf);
//
//        if (noiselevel > CommandLine::nlQuiet)
//        {
//          // Update a progress indicator
//          u32 oldfraction = (u32)(1000 * progress / totaldata);
//          progress += blocklength;
//          u32 newfraction = (u32)(1000 * progress / totaldata);
//
//          if (oldfraction != newfraction)
//          {
//            cout << "Repairing: " << newfraction/10 << '.' << newfraction%10 << "%\r" << flush;
//          }
//        }
//      }
//
//      ++inputblock;
//      ++inputindex;
//    }
//  }
//  else
//  {
//    // Reconstruction is not required, we are just copying blocks between files
//
//    // For each block that might need to be copied
//    while (copyblock != copyblocks.end())
//    {
//      // Does this block need to be copied
//      if ((*copyblock)->IsSet())
//      {
//        // Are we reading from a new file?
//        if (lastopenfile != (*inputblock)->GetDiskFile())
//        {
//          // Close the last file
//          if (lastopenfile != NULL)
//          {
//            lastopenfile->Close();
//          }
//
//          // Open the new file
//          lastopenfile = (*inputblock)->GetDiskFile();
//          if (!lastopenfile->Open())
//          {
//            return false;
//          }
//        }
//
//        // Read data from the current input block
//        if (!(*inputblock)->ReadData(blockoffset, blocklength, inputbuffer))
//          return false;
//
//        size_t wrote;
//        if (!(*copyblock)->WriteData(blockoffset, blocklength, inputbuffer, wrote))
//          return false;
//        totalwritten += wrote;
//      }
//
//      ++copyblock;
//      ++inputblock;
//    }
//  }
//
//  // Close the last file
//  if (lastopenfile != NULL)
//  {
//    lastopenfile->Close();
//  }
//return true;
//}
//
//*/
//
//
//    /**
//     * Generates a number of blocks of parity data. Each block contains enough
//     * to recover from the loss of one slice of data. Will distribute the data
//     * evenly between the recovery files in the recovery set.
//     * @param blocks 
//     * @throws java.io.FileNotFoundException
//     * @throws java.io.IOException 
//     */
//    //TODO generateParityData method
//    public void generateParityBlocks(int blocks)
//            throws FileNotFoundException, IOException {
//
//        //The parity blocks are seperated by their exponents
//
//        //how many blocks can we actually have? 32768
//        if (blocks + usedExponents.size() > 1 << 15) {
//        //error here
//        }
//        
//        if (blocks > constants.length){
//            blocks = constants.length;
//        }
//
//
//        //The combined list of slices
//
//        //reuse previous constants?
//        if (constants == null || constants.length < sliceList.size()) {
//            constants = generateConstants(sliceList.size(), gf16);
//        }
//
//        int ex = 0;
//        int used = 0;
//        while (used < blocks) {
//            if (!usedExponents.contains(ex)) {
//                byte[] parityBlock = new byte[sliceSize];
//
//                for (int bytes = 0; bytes < sliceSize; bytes += 2) {
//                    int c = 0;
//
//                    for (int i = 0; i < sliceList.size(); i++) {
//                        int d = unsign(Bits.getShortB(sliceList.get(i), bytes));
//                        d = gf16.mult(d,gf16.pow(constants[i], ex));
//                        c = gf16.add(c, d);
//                    }
//                    Bytes.toBytesL((short)c,parityBlock, bytes);
//
//
//                }
//
//
//                parityblocks.add(new RecoveryData(ex, parityBlock));
//                usedExponents.add(ex);
//                used++;
//            }
//            ex++;
//        }
//
//
//    /* for each parityblock
//     * choose ex
//     * for each 2 byte pair in the parityBlock
//     *   c = 0;
//     *   for each slice number i
//     *     d = take next 2 bytes
//     *     d = d * gf16.pow(constants[i],ex)
//     *     c = gf16.add(c,d)
//    endfor 
//     *   write c
//     * endfor
//     * endfor
//     * */
//
//
//    }
//
//    public void clearParityBlocks() {
//        parityblocks.clear();
//        usedExponents.clear();
//    }
//
//    //TODO recover method
//    public void recover() {
//        //first, figure out the number of slices to recover
//        //we have a matrix, numberOfSlices wide and numberOfSlices+numberOfParityBlocks high. The top numberOfSlices rows are the 
//        /* identity matrix, and the bottom numberOfParityBlocks rows are the IDM. 
//         * We have numberOfSlices slices in total, and numberOfParityBlocks parity blocks
//         * Each row of the identity matrix coresponds to a
//         * particular recovery slice
//         * We need the matrix square for recovery.
//         * We achive this by deleting rows of the identity
//         * matrix FOR SLICES WE DO NOT NEED TO RECOVER, until it is
//         * only numberOfSlices high. Ie. we delete numberOfParityBlocks rows.
//         * Having done this, we can now solve the equation
//         * AD = E, where A is the numberOfSlices,numberOfSlices matrix, D is the numberOfSlices slices, all that we
//         * seek to recover, and E is the vector (length numberOfSlices) having the numberOfParityBlocks parity blocks in
//         * the end, and the slices we seek to recover in front.
//         * 
//         * */
//
//
//
//        //if we need to recover a lot of slices, reduce numberOfParityBlocks. But numberOfParityBlocks must be larger
//        //than the number of slices to recover. 
//        //we want to use as much of parityblocks as possible. So the vector should
//        /*[identity matrix, so that this is numberOfSlices high
//         *[p_1
//         *[
//         *[p_m
//         */
//        // There must be an entry in the identity matrix for each slice to
//        // keep. Removes the entrys corresponding to defect slices.
//        // If the number of intact slices + numberOfParityBlocks > numberOfSlices, remove additional intact
//        // slices, until the condition is not true.
//        // Remember the index of the defect slices. While the recovery will
//        // recalculate ALL the slices, only the defect slices need to be restored.
//
//
//        int numberOfSlices = sliceList.size();
//        int intactSlicesUsed = numberOfSlices - parityblocks.size();
//        List<Integer> indexForDefectSlices = defectSlices();
//
//        //it must be of this length.
//        List<Integer> slicesToUse = slicesToUse(intactSlicesUsed, indexForDefectSlices);
//
//        int[][] A = setUpA(slicesToUse, indexForDefectSlices);
//
//        for (int i = 0; i < sliceSize; i += 2) {//increment two bytes
//            int[] E = setUpE(slicesToUse, indexForDefectSlices, i);
//            int[] D = null;//GaussianEliminator.lsolve(A, E, gf16);
//            //So D now contains the new results for the defect blocks and the used blocks
//            for (int defectSlice : indexForDefectSlices) {
//                byte[] b = sliceList.get(defectSlice);
//                
//                Bits.putShortB(b, i, (short) D[defectSlice]);
//            }
//        }
//        
//        
//    }
//
//    private int[] setUpE(List<Integer> slicesToUse,
//            List<Integer> indexForDefectSlices, int position) {
//
//        int numberOfSlices = sliceList.size();
//        int numberOfParityBlocks = parityblocks.size();
//        int numberOfDefectSlices = indexForDefectSlices.size();
//
//        if (numberOfDefectSlices > numberOfParityBlocks) {
//        //problem
//        }
//
//        int[] E = new int[numberOfSlices];
//
//        int row = 0;
//        for (int index : slicesToUse) {
//            E[row] = unsign(Bits.getShortL(sliceList.get(index), position));
//            row++;
//        }
//
//        for (int i = row; i < numberOfSlices; i++) {
//            RecoveryData par = parityblocks.get(i - row);
//            E[i] = unsign(Bits.getShortL(par.getRecoveryData(), position));
//
//        }
//
//        return E;
//
//    }
//
//    /**
//     * Construct the A matrix. The matrix is constructed from an identity matrix on 
//     * top of the information dispersal matrix. Rows are removed until the matrix is
//     * square. All the removed rows correspond to slices that will be recovered. Only
//     * rows from the identity matrix are removed. 
//     */
//    private int[][] setUpA(List<Integer> slicesToUse,
//            List<Integer> indexForDefectSlices) {
//        int numberOfSlices = sliceList.size();
//        int numberOfParityBlocks = parityblocks.size();
//        int numberOfDefectSlices = indexForDefectSlices.size();
//
//        if (numberOfDefectSlices > numberOfParityBlocks) {
//        //problem
//        }
//
//        int[][] A = new int[numberOfSlices][numberOfSlices];
//
//        //Fill the remnants of the identity matrix in
//        int row = 0;
//        for (int index : slicesToUse) {
//            A[row][index] = 1;
//            row++;
//        }
//
//        for (int i = row; i < numberOfSlices; i++) {
//            RecoveryData par = parityblocks.get(i - row);
//            for (int j = 0; j < numberOfSlices; j++) {
//                A[i][j] = gf16.pow(constants[j], par.getExponent());
//            }
//        }
//        return A;
//    }
//
//    public boolean isRecoveryPossible() throws IOException {
//        if (parityblocks.size() >= getNumberOfSlicesToRecover()) {
//            return true;
//        }
//        return false;
//    }
//
//    private int getNumberOfSlicesToRecover() throws IOException {
//        int n = 0;
//        for (DataFile df : datafiles) {
//            n += df.verify();
//        }
//        return n;
//    }
//
//    //LOADING
//    public RecoverySet(Par2Set set) {
//        //TODO: null checks?
//        this.datafiles = set.getDataFiles();
//        this.parityblocks = set.getParityBlocks();
//        this.sliceSize = set.getSliceSize();
//        usedExponents = new TreeSet<Integer>();
//        for (RecoveryData rd : parityblocks) {
//            usedExponents.add(rd.getExponent());
//        }
//        sliceList = getSliceList();
//        constants = generateConstants(sliceList.size(), gf16);
//
//
//
//    }
//
//    /**
//     * Gives the indexes in the sliceList that refers to slices that did
//     * not have the correct hash
//     * @return
//     */
//    private List<Integer> defectSlices() {
//        List<Integer> defectSlices = new ArrayList<Integer>();
//
//        int sliceoffset = 0;
//        try {
//            for (DataFile df : datafiles) {
//                List<Integer> defects = df.verifyWithIndex();
//                for (int defect : defects) {
//                    defectSlices.add(defect + sliceoffset);
//                }
//                sliceoffset += df.getNumberOfSlices();
//            }
//        } catch (IOException e) {
//        //TODO something here
//
//        }
//        return defectSlices;
//
//    }
//
//    /**
//     * Returns a list of slice indexes. The list will have length k. It will 
//     * consist of indexes for slices that are not defect.
//     */
//    private List<Integer> slicesToUse(int k, List<Integer> indexForDefectSlices) {
//
//        List<Integer> slicesToUse = new ArrayList<Integer>();
//
//        int slicenum = 0;
//        while (slicesToUse.size() < k) {
//            if (!indexForDefectSlices.contains(slicenum)) {
//                slicesToUse.add(slicenum);
//            }
//            slicenum++;
//        }
//        return slicesToUse;
//    }
//
//    /**
//     * Gives the combined list of all the slices, in the correct order.
//     * @return
//     */
//    private List<byte[]> getSliceList() {
//        //The combined list of slices
//        List<byte[]> slices = new ArrayList<byte[]>();
//
//        /* UPDATE
//        for (DataFile df : datafiles) {
//            List<byte[]> slicelist = df.getSliceList();
//            slices.addAll(slicelist);
//        }*/
//        return slices;
//
//    }
//
    /**
     * Each slice is represented by a constant. This method gives you
     * these constants. 
     * @param size
     * @param gf
     * @return
     */
    static int[] generateConstants(int size, GaloisField gf) {
        int[] constants = new int[size];

        int found = 0;
        int n = 0;

        while (found < size) {
            if (n % 3 != 0 && n % 5 != 0 && n % 17 != 0 && n % 257 != 0) {
                constants[found] = gf.pow(2, n);
                found++;
            }
            n++;
        }
        return constants;

    }

//    public Par2Set write() {
//        Par2Set p = new Par2Set(sliceSize);
//        p.addDataFiles(datafiles);
//        p.addParityBlocks(parityblocks);
//        return p;
//    }
//
//    private int unsign(short a) {
//        return a < 0 ? a + (1 << 16) : a;
//    }
    
}
