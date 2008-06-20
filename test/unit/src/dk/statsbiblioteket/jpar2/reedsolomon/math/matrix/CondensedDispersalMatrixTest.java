/*
 *     CondensedDispersalMatrixTest.java
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
package dk.statsbiblioteket.jpar2.reedsolomon.math.matrix;

import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.Matrix;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.MatrixException;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.CondensedDispersalMatrix;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.NotRecoverableException;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.DispersalMatrix;
import dk.statsbiblioteket.jpar2.reedsolomon.math.matrix.MatrixDimensionException;
import java.util.Arrays;
import java.util.List;
import dk.statsbiblioteket.jpar2.reedsolomon.math.field.Field;
import dk.statsbiblioteket.jpar2.reedsolomon.math.field.GaloisField;
import junit.framework.TestCase;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class CondensedDispersalMatrixTest extends TestCase {

    public CondensedDispersalMatrixTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCondense() throws MatrixDimensionException, MatrixException, NotRecoverableException {
        Field<Integer> field = new GaloisField(GaloisField.WordSize.FOUR);
        Integer[][] ba = {//Taken from "Note: Correction to the 1997 Tutorial on Reed-Solomon Coding."
            {1, 1, 1},
            {14, 9, 6},
            {0, 0, 1}
        };
        Matrix<Integer, Field<Integer>> comp =
                new Matrix<Integer, Field<Integer>>(3, 3, field, ba);



        List<Boolean> present = Arrays.asList(new Boolean[]{
            false,
            false,
            true,
            true,
            false,
            true
        });

        int numberRecoveryBlocks = 3;
        Matrix<Integer, Field<Integer>> disp =
                new DispersalMatrix(comp.getRows()+numberRecoveryBlocks, comp.getCols(), field);

        Matrix<Integer, Field<Integer>> cond =
                new CondensedDispersalMatrix(comp.getRows(), comp.getCols(), disp, present);

        assertEquals(comp, cond);




    }
}
