package dk.statsbiblioteket.jpar2.reedsolomon;

import dk.statsbiblioteket.jpar2.reedsolomon.math.field.Field;
import dk.statsbiblioteket.jpar2.reedsolomon.math.field.GaloisField;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.DispersalMatrix;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.Matrix;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author abr
 */
public class ReedSolomon {
    
    
    private InputStream[] ins;
    private int slicesize;
    
    public ReedSolomon(int slicesize){
        
        
        
    }
    
    
    public void addDataStream(InputStream in){
        
    }
    
    
    
    public void generateParity(OutputStream[] outs){
        //Assume now that the ins is all the datastreams from the content
        
        
        Field<Integer> field = new GaloisField(GaloisField.WordSize.EIGHT);
                
        Matrix<Integer, Field<Integer>> disp = new DispersalMatrix(ins.length + outs.length, ins.length, field);
        
        int read = 0;
        while (read<slicesize){
            Integer[] data = new Integer[ins.length];
            for (int i=0;i<ins.length;i++){
                byte[] word = new byte[GaloisField.WordSize.EIGHT.]
                ins[i].r
            }
            
        }
        
        
        
        
    }
    
    
    
    
        public void testRecovery() throws MatrixDimensionException, MatrixException,
            NotRecoverableException {
        //First, we make the data vector

        Field<Integer> field = new GaloisField(GaloisField.WordSize.EIGHT);

        Integer[] matrix = new Integer[]{
            11,
            2,
            3,
            44,
            5,
            6
        };




        Matrix<Integer, Field<Integer>> data =
                new Matrix<Integer, Field<Integer>>(matrix.length, 1, field, matrix);

        int recoveryBlocks = 6;
        
        //+3 is the number of recovery blocks to make
        Matrix<Integer, Field<Integer>> disp = new DispersalMatrix(data.getRows() + recoveryBlocks, data.getRows(), field);

        Matrix<Integer, Field<Integer>> parity = Matrix.mult(disp, data, field);



        List<Boolean> present = Arrays.asList(new Boolean[]{
            
            false,//datablocks
            false,
            false,
            true,
            false,
            false,
            
            true,//recoveryblocks
            true,
            true,
            true,
            true,
            true
        });
        
        if (present.size() != disp.getRows()){
            fail("The present list must account for all the rows in the dispersal matrix");
        }

        CondensedDispersalMatrix c = new CondensedDispersalMatrix(data.getRows(), data.getRows(), disp, present);
        int[] id = c.getRowMapping();
        
        Matrix<Integer, Field<Integer>> cond = c.invert();



        

        Integer[] matrix2 = new Integer[data.getRows()];
        
        for (int i=0; i<matrix2.length; i++)        {
            matrix2[i] = parity.get(0,id[i]);
        }
        
        if (matrix2.length != data.getRows()){
            fail("The restored matrix must be of the same size as the original matris");
        }

        Matrix<Integer, Field<Integer>> toRestore = new Matrix<Integer, Field<Integer>>(data.getRows(), data.getCols(), field, matrix2);


        Matrix<Integer, Field<Integer>> restored = Matrix.mult(cond, toRestore, field);

//        System.out.println(cond);
//
//        System.out.println(toRestore);
//
//        System.out.println(restored);

        assertEquals(restored, data);


    }
        
        
        
        
        

}
