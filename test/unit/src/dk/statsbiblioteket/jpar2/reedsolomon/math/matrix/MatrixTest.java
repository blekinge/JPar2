/*
 *     MatrixTest.java
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

import dk.statsbiblioteket.jpar2.reedsolomon.math.field.Field;
import dk.statsbiblioteket.jpar2.reedsolomon.math.field.GaloisField;
import junit.framework.TestCase;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class MatrixTest extends TestCase {
    
    public MatrixTest(String testName) {
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




    /**
     * Test of invert method, of class Matrix.
     */
    public void testInvertIdentity() throws Exception {
        Field<Integer> gf = new GaloisField(GaloisField.WordSize.FOUR);
        Matrix<Integer, Field<Integer>> test = Matrix.identity(5, gf);
        Matrix<Integer, Field<Integer>> invertedTest = test.invert();
        assertEquals(test, invertedTest);
    }


    
    public void testInvertAndMultGalois() throws NonInvertibleException, MatrixDimensionException{
        int side = 3;
        Integer[] ba = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        Field<Integer> gf = new GaloisField(GaloisField.WordSize.FOUR);
        
        Matrix<Integer,Field<Integer>> orig = new Matrix<Integer, Field<Integer>>(side, side, gf, ba);
        Matrix<Integer,Field<Integer>> inverted = orig.invert();
        Matrix<Integer,Field<Integer>> identity = Matrix.identity(side, gf);
        assertEquals(identity,Matrix.mult(orig, inverted, gf));
        System.out.println("Original");
        System.out.println(orig);
        System.out.println("Inverted");
        System.out.println(inverted);
        
        
        
        
    }
    
    public void testToString(){
        Matrix<Integer,GaloisField> test = Matrix.identity(5, new GaloisField(GaloisField.WordSize.FOUR));
        //System.out.println(test);
    }

}
