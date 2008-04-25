/*
 *     DispersalMatrixTest.java
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
package javapar2.math.matrix;

import javapar2.math.MathException;
import javapar2.math.field.Field;
import javapar2.math.field.GaloisField;
import junit.framework.TestCase;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class DispersalMatrixTest extends TestCase {

    public DispersalMatrixTest(String testName) {
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

    public void testDispersalFormat() throws MatrixDimensionException, MatrixException  {

        Field<Integer> field = new GaloisField(GaloisField.WordSize.FOUR);
        int rows = 6;
        int cols = 3;
        Integer[] ba = {//Taken from "Note: Correction to the 1997 Tutorial on Reed-Solomon Coding."
            1, 0, 0,
            0, 1, 0,
            0, 0, 1,
            1, 1, 1,
            15, 8, 6,
            14, 9, 6
        };


        Matrix<Integer, Field<Integer>> comp =
                new Matrix<Integer, Field<Integer>>(rows, cols, field, ba);
        Matrix<Integer, Field<Integer>> disp =
                new DispersalMatrix(rows, cols, field);
        assertEquals(comp, disp);
    }
}
