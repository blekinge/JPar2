package dk.statsbiblioteket.jpar2.reedsolomon;

import dk.statsbiblioteket.jpar2.byteutils.Bytes;
import dk.statsbiblioteket.jpar2.files.DataSlice;
import dk.statsbiblioteket.jpar2.files.Par2File;
import dk.statsbiblioteket.jpar2.files.Par2Slice;
import dk.statsbiblioteket.jpar2.reedsolomon.math.field.Field;
import dk.statsbiblioteket.jpar2.reedsolomon.math.field.GaloisField;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.CondensedDispersalMatrix;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.DispersalMatrix;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.Matrix;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.MatrixException;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.NotRecoverableException;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.Vector;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author abr
 */
public class ReedSolomon {

    private List<Par2Slice> dataSlices;
    private List<Par2Slice> paritySlices;
    private List<Integer> lostSlices;
    private long slicesize;
    private int ins_counter = 0;
    private GaloisField.WordSize size = GaloisField.WordSize.SIXTEEN;
    private GaloisField field = new GaloisField(size);

    public ReedSolomon(long slicesize) {

        this.slicesize = slicesize;
        dataSlices = new ArrayList<Par2Slice>();
        paritySlices = new ArrayList<Par2Slice>();
        lostSlices = new ArrayList<Integer>();
    }

    public boolean addDataSlice(Par2Slice e) {
        return dataSlices.add(e);
    }

    public boolean addParitySlice(Par2Slice e) {
        return paritySlices.add(e);
    }

    public void addLostSlice(Par2Slice e) {
        dataSlices.add(e);
        lostSlices.add(dataSlices.indexOf(e));
    }

    public boolean addDataFile(Par2File e) throws IOException{
        return dataSlices.addAll(Arrays.asList(e.getSlices()));
    }
    
    public boolean addParityFile(Par2File e) throws IOException{
        return paritySlices.addAll(Arrays.asList(e.getSlices()));
    }
    
    public void addLostFile(Par2File e) throws IOException {
        List<Par2Slice> sliceList = Arrays.asList(e.getSlices());
        for (Par2Slice slice: sliceList){
            dataSlices.add(slice);
            lostSlices.add(dataSlices.indexOf(slice));
        }
    }

  

    public void recoverLostSlices() throws MatrixException,
                                            NotRecoverableException, IOException {


        Matrix<Integer, Field<Integer>> disp =
                new DispersalMatrix(
                dataSlices.size() + paritySlices.size(),
                dataSlices.size(),
                field);
        System.out.println(disp);




        int columns = (int) (slicesize / 2);



        List<Boolean> present = new ArrayList<Boolean>();
        for (int i = 0; i < dataSlices.size() + paritySlices.size(); i++) {
            if (lostSlices.contains(i)) {
                present.add(false);
            } else {
                present.add(true);
            }
        }

        CondensedDispersalMatrix c =
                new CondensedDispersalMatrix(dataSlices.size(),
                                             dataSlices.size(), disp, present);

        int[] id = c.getRowMapping();//which rows now correspond to which rows in the uncondensed disp matrix

        Matrix<Integer, Field<Integer>> cond = c.invert();





        Matrix<Integer, Field<Integer>> data = readMatrixFromSlices(dataSlices,
                                                                    columns);

        Matrix<Integer, Field<Integer>> parity =
                readMatrixFromSlices(paritySlices, columns);

        if (lostSlices.size() > paritySlices.size()) {
            return;
        }

        int parityIndex = 0;
        for (int lostindex : lostSlices) {
            data.setRow(lostindex, parity.getRow(parityIndex));
            parityIndex++;
        }

        Matrix<Integer, Field<Integer>> restored =
                Matrix.mult(cond, data, field);

        writeSlicesFromMatrix(restored, present);



    }

    public void writeSlicesFromMatrix(Matrix<Integer, Field<Integer>> data,
                                       List<Boolean> present) throws IOException {

        for (int rowIndex = 0; rowIndex < data.getRows(); rowIndex++) {

            if (present.get(rowIndex)){
                continue; //Slice was not lost, only write out the lost slices
            }
            Vector<Integer> row = data.getRow(rowIndex);
            Par2Slice slice = dataSlices.get(rowIndex);

            slice.reset();
            for (int i = 0; i < row.length(); i++) {
                Integer word = row.get(i);
                
                
                byte[] wordbytes = Bytes.toBytesB(word.shortValue());
                slice.write(wordbytes);
            }
        }
    }

    //    
//    
//        public void testRecovery() throws MatrixDimensionException, MatrixException,
//            NotRecoverableException {
//        //First, we make the data vector
//
//        Field<Integer> field = new GaloisField(GaloisField.WordSize.EIGHT);
//
//        Integer[] matrix = new Integer[]{
//            11,
//            2,
//            3,
//            44,
//            5,
//            6
//        };
//
//
//
//
//        Matrix<Integer, Field<Integer>> data =
//                new Matrix<Integer, Field<Integer>>(matrix.length, 1, field, matrix);
//
//        int recoveryBlocks = 6;
//        
//        //+3 is the number of recovery blocks to make
//        Matrix<Integer, Field<Integer>> disp = new DispersalMatrix(data.getRows() + recoveryBlocks, data.getRows(), field);
//
//        Matrix<Integer, Field<Integer>> parity = Matrix.mult(disp, data, field);
//
//
//
//        List<Boolean> present = Arrays.asList(new Boolean[]{
//            
//            false,//datablocks
//            false,
//            false,
//            true,
//            false,
//            false,
//            
//            true,//recoveryblocks
//            true,
//            true,
//            true,
//            true,
//            true
//        });
//        
//        if (present.size() != disp.getRows()){
//            fail("The present list must account for all the rows in the dispersal matrix");
//        }
//
//        CondensedDispersalMatrix c = new CondensedDispersalMatrix(data.getRows(), data.getRows(), disp, present);
//        int[] id = c.getRowMapping();
//        
//        Matrix<Integer, Field<Integer>> cond = c.invert();
//
//
//
//        
//
//        Integer[] matrix2 = new Integer[data.getRows()];
//        
//        for (int i=0; i<matrix2.length; i++)        {
//            matrix2[i] = parity.get(0,id[i]);
//        }
//        
//        if (matrix2.length != data.getRows()){
//            fail("The restored matrix must be of the same size as the original matris");
//        }
//
//        Matrix<Integer, Field<Integer>> toRestore = new Matrix<Integer, Field<Integer>>(data.getRows(), data.getCols(), field, matrix2);
//
//
//        Matrix<Integer, Field<Integer>> restored = Matrix.mult(cond, toRestore, field);
//
////        System.out.println(cond);
////
////        System.out.println(toRestore);
////
////        System.out.println(restored);
//
//        assertEquals(restored, data);
//
//
//    }
//        
    /**
     * Recalculates all the parity slices. Discards the contents of all parity
     * slices, and recalculates it based on the data slices.
     * @throws dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.MatrixException
     * @throws java.io.IOException
     */
    public void generateParitySlices() throws MatrixException, IOException {
        //Assume now that the ins is all the datastreams from the content


        //TODO: if lostSlices contain something, problem;


        Matrix<Integer, Field<Integer>> disp =
                new DispersalMatrix(
                dataSlices.size() + paritySlices.size(),
                dataSlices.size(),
                field);
        //System.out.println(disp);

        int columns = (int) (slicesize / 2);
        int rows = paritySlices.size();
        
        Matrix<Integer, Field<Integer>> data =
                readMatrixFromSlices(dataSlices, columns);

        //System.out.println(data);
        Matrix<Integer, Field<Integer>> parity = Matrix.mult(disp, data,
                                                             field);

        //System.out.println(parity);

        int ndataslices = dataSlices.size();
        for (int i = 0; i < rows; i++) {//row number
            
            Vector<Integer> row = parity.getRow(i+ndataslices);
            
            Par2Slice paritySlice = paritySlices.get(i);
            
            for (int j = 0; j < columns; j++) {//column number

                //now write out the contents again

                byte[] blah = Bytes.toBytesB(row.get(j).shortValue());

                paritySlice.write(blah);


            }
        }

    }

    /**
     * Creates the datamatrix. A data matrix is a NxM matrix, where N (rows) is 
     * the number of data slices, and M is the number of 2-byte words in a slice.
     * @return
     */
    private Matrix<Integer, Field<Integer>> readMatrixFromSlices(
            List<Par2Slice> slices,
            int colums) throws IOException {
        Matrix<Integer, Field<Integer>> matrix =
                new Matrix<Integer, Field<Integer>>(
                slices.size(),
                colums,
                field);


        for (int j = 0; j < slices.size(); j++) {
            slices.get(j).reset();
            for (int i = 0; i < colums; i++) {
                byte[] word = new byte[2];//TODO: hardcoded
                slices.get(j).read(word);
                int wordshort = unsign(Bytes.asShortB(word));
                matrix.set(j, i, wordshort);
            }
        }
        return matrix;

    }
    
    private int unsign(short signed){
        if (signed <0){
            int b = (1 << 16) + signed;
            return b;
        }else{
            return signed;
        }
    }
}
