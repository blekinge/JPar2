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
package javapar2.math;

//#include   "par2cmdline.h"
import java.util.Iterator;
import java.util.List;

/**
 *
 * 
 * The ReedSolomon object is used to calculate and store the matrix
 * used during recovery block creation or data block reconstruction.
 * @author Asger
 */
public class ReedSolomon {
//
//// During initialisation, one RSOutputRow object is created for each
//// recovery block that either needs to be created or is available for
//// use.
//    private static class RSOutputRow {
//
//        private boolean present;
//        private int exponent;
//
//        RSOutputRow(boolean present, int exponent) {
//            this.present = present;
//            this.exponent = exponent;
//        }
//    }
//    //Stuff for galois8
//    int inputcount;        // Total number of input blocks
//    int datapresent;       // Number of input blocks that are present 
//    int datamissing;       // Number of input blocks that are missing
//    int[] datapresentindex; // The index numbers of the data blocks that are present
//    int[] datamissingindex; // The index numbers of the data blocks that are missing
//    int[] database;// The "base" value to use for each input block //CONSTANTS or BYTE VALUES
//    int outputcount;       // Total number of output blocks
//    int parpresent;        // Number of output blocks that are present
//    int parmissing; // Number of output blocks that are missing
//    int[] parpresentindex;  // The index numbers of the output blocks that are present
//    int[] parmissingindex;  // The index numbers of the output blocks that are missing
//    List<RSOutputRow> outputrows; // Details of the output blocks
//    int[] leftmatrix;    // The main matrix //GALOIS
//
//    // When the matrices are initialised: values of the form base ^ exponent are
//    // stored (where the base values are obtained from database[] and the exponent
//    // values are obtained from outputrows[]).
//    public ReedSolomon() {
//    }
//
//    private int gcd(int a, int b) {
//        if (a != 0 &&
//                b != 0) {
//            while (a != 0 && b != 0) {
//                if (a > b) {
//                    a = a % b;
//                } else {
//                    b = b % a;
//                }
//            }
//            return a + b;
//        } else {
//            return 0;
//        }
//    }
//
//    /** Record whether the recovery block with the specified
//    exponent values is present or missing.*/
//    public boolean setOutput(boolean present, int exponent) {
//        // Store the exponent and whether or not the recovery block is present or missing
//        outputrows.add(new RSOutputRow(present, exponent));
//
//        outputcount++;
//
//        // Update the counts.
//        if (present) {
//            parpresent++;
//        } else {
//            parmissing++;
//        }
//
//        return true;
//    }
//
//    /** Record whether the recovery blocks with the specified
//    range of exponent values are present or missing.*/
//    boolean setOutput(boolean present, int lowexponent, int highexponent) {
//        for (int exponent = lowexponent; exponent <= highexponent; exponent++) {
//            if (!setOutput(present, exponent)) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    public boolean setInput(List<Boolean> present) {
//        inputcount = present.size();
//
//        datapresentindex = new int[inputcount];
//        datamissingindex = new int[inputcount];
//        database = new int[inputcount]; //G.ValueType
//
//
//        int base = 1;
//
//        for (int index = 0; index < inputcount; index++) {
//            // Record the index of the file in the datapresentindex array 
//            // or the datamissingindex array
//            if (present.get(index)) {
//                datapresentindex[datapresent++] = index;
//            } else {
//                datamissingindex[datamissing++] = index;
//            }
//
//            database[index] = base++;
//        }
//
//        return true;
//    }
//
//    public boolean setInput(int count) {
//        inputcount = count;
//
//        datapresentindex = new int[inputcount];
//        datamissingindex = new int[inputcount];
//        database = new int[inputcount]; //G.ValueType
//
//
//        int base = 1;
//
//        for (int index = 0; index < count; index++) { // Record that the file is present
//            datapresentindex[datapresent++] = index;
//            database[index] = base++;
//        }
//
//        return true;
//    }
//
//    // Construct the Vandermonde matrix and solve it if necessary
//    public boolean compute() {
//        int outcount = datamissing + parmissing;
//        int incount = datapresent + datamissing;
//
//        if (datamissing > parpresent) {
//            System.err.println("Not enough recovery blocks.");
//            return false;
//        } else if (outcount == 0) {
//            System.err.println("No output blocks.");
//            return false;
//        }
//
//
////   Layout of RS Matrix:
////
////                                       parpresent
////                     datapresent       datamissing         datamissing       parmissing
////               /                     |             \ /                     |           \
////   parpresent  |           (ppi[row])|             | |           (ppi[row])|           |
////   datamissing |          ^          |      I      | |          ^          |     0     |
////               |(dpi[col])           |             | |(dmi[col])           |           |
////               +---------------------+-------------+ +---------------------+-----------+
////               |           (pmi[row])|             | |           (pmi[row])|           |
////   parmissing  |          ^          |      0      | |          ^          |     I     |
////               |(dpi[col])           |             | |(dmi[col])           |           |
////               \                     |             / \                     |           /
//
//
//        // Allocate the left hand matrix
//        leftmatrix = new int[outcount * incount];//incount is the row size. outcount is the column size
//
//
//        // Allocate the right hand matrix only if we are recovering
//        int[] rightmatrix = null;
//        if (datamissing > 0) {
//            rightmatrix = new int[outcount * outcount];//square, outcount in both directions
//
//        }
//
//        // Fill in the two matrices:
//        Iterator<RSOutputRow> outputrow = outputrows.iterator();
//
//        // One row for each present recovery block that will be used for a missing data block
//        for (int row = 0; row < datamissing; row++) {
//            
//            // Get the exponent of the next present recovery block
//            RSOutputRow currentrow = outputrow.next();
//            while (!currentrow.present) {
//                currentrow = outputrow.next();
//            }
//            int exponent = currentrow.exponent;
//
//            // One column for each present data block
//            for (int col = 0; col < datapresent; col++) {
//                leftmatrix[row * incount + col] = G(database[datapresentindex[col]]).
//                        pow(exponent);
//            }
//            // One column for each each present recovery block that will be used for a missing data block
//            for (int col = 0; col < datamissing; col++) {
//                leftmatrix[row * incount + col + datapresent] = (row == col) ? 1
//                        : 0;
//            }
//
//            if (datamissing > 0) {
//                // One column for each missing data block
//                for (int col = 0; col < datamissing; col++) {
//                    rightmatrix[row * outcount + col] = G(database[datamissingindex[col]]).
//                            pow(exponent);
//                }
//                // One column for each missing recovery block
//                for (int col = 0; col < parmissing; col++) {
//                    rightmatrix[row * outcount + col + datamissing] = 0;
//                }
//            }
//
//        /*
//        outputrow++;
//         */
//        }
//
//        // One row for each recovery block being computed
//        outputrow = outputrows.iterator();
//        for (int row = 0; row < parmissing; row++) {
//            /*if (noiselevel > NoiseLevel.Quiet)    {
//            int progress = (row+datamissing) * 1000 / (datamissing+parmissing);
//            System.out.println("Constructing: " + progress/10 + '.' + progress%10 + "%\r");
//            }*/
//
//            RSOutputRow currentrow = outputrow.next();
//            // Get the exponent of the next missing recovery block
//            while (currentrow.present) {
//                currentrow = outputrow.next();
//            }
//            int exponent = currentrow.exponent;
//
//
//            // One column for each present data block
//            for (int col = 0; col < datapresent; col++) {
//                leftmatrix[(row + datamissing) * incount + col] = G(database[datapresentindex[col]]).
//                        pow(exponent);
//            }
//            // One column for each each present recovery block that will be used for a missing data block
//            for (int col = 0; col < datamissing; col++) {
//                leftmatrix[(row + datamissing) * incount + col + datapresent] =
//                        0;
//            }
//
//            if (datamissing > 0) {
//                // One column for each missing data block
//                for (int col = 0; col < datamissing; col++) {
//                    rightmatrix[(row + datamissing) * outcount + col] = G(database[datamissingindex[col]]).
//                            pow(exponent);
//                }
//                // One column for each missing recovery block
//                for (int col = 0; col < parmissing; col++) {
//                    rightmatrix[(row + datamissing) * outcount + col +
//                            datamissing] = (row == col) ? 1 : 0;
//                }
//            }
//        /*
//        outputrow++;
//         */
//        }
//
//        /*
//        if (noiselevel > CommandLine.NoiseLevel.Quiet)
//        System.out.println("Constructing: done.");
//         */
//
//
//        // Solve the matrices only if recovering data
//        if (datamissing > 0) {
//            // Perform Gaussian Elimination and then delete the right matrix (which 
//            // will no longer be required).
//            boolean success = GaussElim(outcount, incount, leftmatrix,
//                                        rightmatrix, datamissing);
//            //delete [] rightmatrix;
//            return success;
//        }
//
//        return true;
//    }
////    
////    
////    
////    
////boolean Process(size_t size, u32 inputindex, const void *inputbuffer, u32 outputindex, void *outputbuffer){
////  // Look up the appropriate element in the RS matrix
////
////  Galois16 factor = leftmatrix[outputindex * (datapresent + datamissing) + inputindex];
////  // Do nothing if the factor happens to be 0
////  if (factor == 0)
////    return eSuccess;
////
////#ifdef LONGMULTIPLY
////  // The 8-bit long multiplication tables
////  Galois16 *table = glmt->tables;
////
////  // Split the factor into Low and High bytes
////  unsigned int fl = (factor >> 0) & 0xff;
////  unsigned int fh = (factor >> 8) & 0xff;
////
////  // Get the four separate multiplication tables
////  Galois16 *LL = &table[(0*256 + fl) * 256 + 0]; // factor.low  * source.low
////  Galois16 *LH = &table[(1*256 + fl) * 256 + 0]; // factor.low  * source.high
////  Galois16 *HL = &table[(1*256 + 0) * 256 + fh]; // factor.high * source.low
////  Galois16 *HH = &table[(2*256 + fh) * 256 + 0]; // factor.high * source.high
////
////  // Combine the four multiplication tables into two
////  unsigned int L[256];
////  unsigned int H[256];
////
////#if __BYTE_ORDER == __LITTLE_ENDIAN
////  unsigned int *pL = &L[0];
////  unsigned int *pH = &H[0];
////#else
////  unsigned int *pL = &H[0];
////  unsigned int *pH = &L[0];
////#endif
////
////        
////  for (unsigned int i=0; i<256; i++)
////  {
////#if __BYTE_ORDER == __LITTLE_ENDIAN
////    *pL = *LL + *HL;
////#else
////    unsigned int temp = *LL + *HL;
////    *pL = (temp >> 8) & 0xff | (temp << 8) & 0xff00;
////#endif
////
////    pL++;
////    LL++;
////    HL+=256;
////
////#if __BYTE_ORDER == __LITTLE_ENDIAN
////    *pH = *LH + *HH;
////#else
////    temp = *LH + *HH;
////    *pH = (temp >> 8) & 0xff | (temp << 8) & 0xff00;
////#endif
////
////    pH++;
////    LH++;
////    HH++;
////  }
////
////  // Treat the buffers as arrays of 32-bit unsigned ints.
////  u32 *src = (u32 *)inputbuffer;
////  u32 *end = (u32 *)&((u8*)inputbuffer)[size];
////  u32 *dst = (u32 *)outputbuffer;
////  
////  // Process the data
////  while (src < end)
////  {
////    u32 s = *src++;
////
////    // Use the two lookup tables computed earlier
//////#if __BYTE_ORDER == __LITTLE_ENDIAN
////    u32 d = *dst ^ (L[(s >> 0) & 0xff]      )
////                 ^ (H[(s >> 8) & 0xff]      )
////                 ^ (L[(s >> 16)& 0xff] << 16)
////                 ^ (H[(s >> 24)& 0xff] << 16);
////    *dst++ = d;
//////#else
//////    *dst++ ^= (L[(s >> 8) & 0xff]      )
//////           ^  (H[(s >> 0) & 0xff]      )
//////           ^  (L[(s >> 24)& 0xff] << 16)
//////           ^  (H[(s >> 16)& 0xff] << 16);
//////#endif
////  }
////#else
////  // Treat the buffers as arrays of 16-bit Galois values.
////
////  // BUG: This only works for __LITTLE_ENDIAN
////  Galois16 *src = (Galois16 *)inputbuffer;
////  Galois16 *end = (Galois16 *)&((u8*)inputbuffer)[size];
////  Galois16 *dst = (Galois16 *)outputbuffer;
////
////  // Process the data
////  while (src < end)
////  {
////    *dst++ += *src++ * factor;
////  }
////#endif
////
////  return eSuccess;
////}
////
////    
////    
//
////
////// Use Gaussian Elimination to solve the matrices
////public boolean GaussElim(int rows,  int leftcols, 
////        int[] leftmatrix, int[] rightmatrix,  int datamissing){
////
////    //rightmatrix[rows][rows]
////    //leftmatric[rows][leftcols]
////
////  // Because the matrices being operated on are Vandermonde matrices
////  // they are guaranteed not to be singular.
////
////  // Additionally, because Galois arithmetic is being used, all calulations
////  // involve exact values with no loss of precision. It is therefore
////  // not necessary to carry out any row or column swapping.
////
////  // Solve one row at a time
////
////  int progress = 0;
////
////  // For each row in the matrix
////  for ( int row=0; row<datamissing; row++)  {
////    // NB Row and column swapping to find a non zero pivot value or to find the largest value
////    // is not necessary due to the nature of the arithmetic and construction of the RS matrix.
////
////    // Get the pivot value.
////    int pivotvalue = rightmatrix[row * rows + row];
////    assert(pivotvalue != 0);
////    if (pivotvalue == 0)    {
////      System.err.println("RS computation error.");
////      return false;
////    }
////
////    // If the pivot value is not 1, then the whole row has to be scaled
////    if (pivotvalue != 1)    {
////      for ( int col=0; col<leftcols; col++)      {
////        if (leftmatrix[row * leftcols + col] != 0)        {
////          leftmatrix[row * leftcols + col] /= pivotvalue;//GALOIS
////        }
////      }
////      rightmatrix[row * rows + row] = 1;
////      for ( int col=row+1; col<rows; col++)      {
////        if (rightmatrix[row * rows + col] != 0)        {
////          rightmatrix[row * rows + col] /= pivotvalue;//GALOIS
////        }
////      }
////    }
////
////    // For every other row in the matrix
////    for ( int row2=0; row2<rows; row2++)    {
////      /*if (noiselevel > CommandLine.NoiseLevel.Quiet)      {
////        int newprogress = (row*rows+row2) * 1000 / (datamissing*rows);
////        if (progress != newprogress)        {
////          progress = newprogress;
////          cout << "Solving: " << progress/10 << '.' << progress%10 << "%\r" << flush;
////        }
////      }*/
////
////      if (row != row2)      {
////        // Get the scaling factor for this row.
////        int scalevalue = rightmatrix[row2 * rows + row];
////
////        if (scalevalue == 1)        {
////          // If the scaling factor happens to be 1, just subtract rows
////          for ( int col=0; col<leftcols; col++)          {
////            if (leftmatrix[row * leftcols + col] != 0)            {
////              leftmatrix[row2 * leftcols + col] -= leftmatrix[row * leftcols + col];//GALOIS
////            }
////          }
////
////          for ( int col=row; col<rows; col++)          {
////            if (rightmatrix[row * rows + col] != 0)            {
////              rightmatrix[row2 * rows + col] -= rightmatrix[row * rows + col];//GALOIS
////            }
////          }
////        }
////        else if (scalevalue != 0)        {
////          // If the scaling factor is not 0, then compute accordingly.
////          for ( int col=0; col<leftcols; col++)          {
////            if (leftmatrix[row * leftcols + col] != 0)            {
////              leftmatrix[row2 * leftcols + col] -= leftmatrix[row * leftcols + col] * scalevalue; //GALOIS
////            }
////          }
////
////          for ( int col=row; col<rows; col++)          {
////            if (rightmatrix[row * rows + col] != 0)            {
////              rightmatrix[row2 * rows + col] -= rightmatrix[row * rows + col] * scalevalue; //GALOIS
////            }
////          }
////        }
////      }
////    }
////  }
////
////  return true;
////
////}
////    
}
