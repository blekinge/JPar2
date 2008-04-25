/*
 *     VandermondeMatrixTest.java
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
package dk.statsbiblioteket.jpar2.math.matrix;

import dk.statsbiblioteket.jpar2.math.matrix.*;
import dk.statsbiblioteket.jpar2.math.matrix.Matrix;
import dk.statsbiblioteket.jpar2.math.matrix.VandermondeMatrix;
import dk.statsbiblioteket.jpar2.math.matrix.MatrixDimensionException;
import dk.statsbiblioteket.jpar2.math.MathException;
import dk.statsbiblioteket.jpar2.math.field.Field;
import dk.statsbiblioteket.jpar2.math.field.GaloisField;
import junit.framework.TestCase;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class VandermondeMatrixTest extends TestCase {

    public VandermondeMatrixTest(String testName) {
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

    public void testFormat() throws MatrixDimensionException  {

        Integer[] ba = {//Taken from "Correction to the 1997  Tutorial on Reed-Solomon Coding."
            1, 0, 0,
            1, 1, 1,
            1, 2, 4,
            1, 3, 5,
            1, 4, 3,
            1, 5, 2
        };

        int rows = 6;
        int cols = 3;
        Field<Integer> field = new GaloisField(GaloisField.WordSize.FOUR);
        Matrix<Integer, Field<Integer>> vmd = new VandermondeMatrix(rows, cols, field);
        Matrix<Integer, Field<Integer>> comparisson = new Matrix<Integer, Field<Integer>>(rows, cols, field, ba);
        assertEquals(comparisson, vmd);
    }
}
