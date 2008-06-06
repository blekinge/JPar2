package dk.statsbiblioteket.jpar2.reedsolomon;

import dk.statsbiblioteket.jpar2.byteutils.Bytes;
import dk.statsbiblioteket.jpar2.files.DataSlice;
import dk.statsbiblioteket.jpar2.files.Par2Slice;
import dk.statsbiblioteket.jpar2.reedsolomon.math.field.Field;
import dk.statsbiblioteket.jpar2.reedsolomon.math.field.GaloisField;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.CondensedDispersalMatrix;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.DispersalMatrix;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.Matrix;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.MatrixException;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.NotRecoverableException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.ArrayList;
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
    
    
    
    private Matrix<Integer,Field<Integer>> createDataMatrix(){
        

        Matrix<Integer, Field<Integer>> data =
                new Matrix<Integer, Field<Integer>>(
                dataSlices.size(),
                columns,
                field);

        for (int i = 0; i < data.getRows(); i++) {
            for (int j = 0; j < data.getCols(); j++) {
                if (lostSlices.contains(i)) {
                    

                } else {
                    byte[] h = new byte[2];
                    dataSlices.get(i).read(h);
                    short h2 = Bytes.asShortB(h);
                    data.set(i, j, new Integer(h2));


                }
            }
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



        for (int j = 0; j < slices.size(); j++) {
            slices.get(j).reset();
            for (int i = 0; i < colums; i++) {
                byte[] word = new byte[2];//TODO: hardcoded
                slices.get(j).read(word);
                matrix.set(j, i, new Integer(Bytes.asShortB(word)));
            }
        }
        return matrix;


        System.out.println(data);

        Matrix<Integer, Field<Integer>> parity = Matrix.mult(disp, data,
                                                             field);

        System.out.println(parity);


        Integer[] matrix2 = new Integer[dataSlices.size()];

        for (int i = 0; i < matrix2.length; i++) {
            matrix2[i] = parity.get(0, id[i]);
        }


        Matrix<Integer, Field<Integer>> toRestore =
                new Matrix<Integer, Field<Integer>>(data.getRows(),
                                                    data.getCols(), field,
                                                    matrix2);


        Matrix<Integer, Field<Integer>> restored = Matrix.mult(cond, toRestore,
                                                               field);

        System.out.println(cond);

        System.out.println(toRestore);

        System.out.println(restored);

//        assertEquals(restored, data);



    }

    public void generateParitySlices() throws MatrixException, IOException {
        //Assume now that the ins is all the datastreams from the content


        //TODO: if lostSlices contain something, problem;


        Matrix<Integer, Field<Integer>> disp =
                new DispersalMatrix(
                dataSlices.size() + paritySlices.size(),
                dataSlices.size(),
                field);
        System.out.println(disp);

        int columns = (int) (slicesize / 2);

        Matrix<Integer, Field<Integer>> data =
                readMatrixFromSlices(dataSlices, columns);

        System.out.println(data);
        Matrix<Integer, Field<Integer>> parity = Matrix.mult(disp, data,
                                                             field);

        System.out.println(parity);



        for (int i = 0; i < paritySlices.size(); i++) {//row number

            for (int j = 0; j < columns; j++) {//column number

                //now write out the contents again

                Integer part = parity.get(i + dataSlices.size(), j);

                byte[] blah = Bytes.toBytesB(part.shortValue());

                paritySlices.get(i).write(blah);


            }
        }

    }

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
                matrix.set(j, i, new Integer(Bytes.asShortB(word)));
            }
        }
        return matrix;

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
}
