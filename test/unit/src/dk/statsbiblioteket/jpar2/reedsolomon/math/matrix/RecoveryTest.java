/*
 *     RecoveryTest.java
 *     Copyright (C) 2008  Asger
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
package dk.statsbiblioteket.jpar2.reedsolomon.math.matrix;

import dk.statsbiblioteket.jpar2.reedsolomon.math.field.Field;
import dk.statsbiblioteket.jpar2.reedsolomon.math.field.GaloisField;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author Asger
 */
public class RecoveryTest extends TestCase {

    public RecoveryTest(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testRecovery() throws MatrixDimensionException, MatrixException,
            NotRecoverableException {
        //First, we make the data vector

        Field<Integer> field = new GaloisField(GaloisField.WordSize.EIGHT);

        Integer[][] matrix = new Integer[][]{
            {11},
            {2},
            {3},
            {44},
            {5},
            {6}
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



        

        Integer[][] matrix2 = new Integer[data.getRows()][1];
        
        for (int i=0; i<matrix2.length; i++)        {
            matrix2[i][0] = parity.get(id[i],0);
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
