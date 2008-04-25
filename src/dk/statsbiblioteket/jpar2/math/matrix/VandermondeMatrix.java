/*
 *     VandeMondeMatrix.java
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

import dk.statsbiblioteket.jpar2.math.field.Field;



/**
 * The vandermonde matrix is a special kind of matrix. See http://en.wikipedia.org/wiki/Vandermonde_matrix
 * @author Asger Blekinge-Rasmussen
 */
public class VandermondeMatrix extends Matrix<Integer,Field<Integer>> {
    
    
     /* This returns the rows*cols vandermonde matrix.  N+M must be
    less than 2^w -1.  Row 0 is in elements 0 to cols-1.  Row one is 
    in elements cols to 2cols-1.  Etc.
     * int *gf_make_vandermonde(int rows, int cols): This allocates and returns
     * a rows by cols Vandermonde matrix. You do not need to call this 
     * explicitly to perform Reed-Solomon coding, but in case you want to see
     * a Vandermonde matrix, you can use this. Rows and cols must be less than
     * 256 for GF(2^8) and 65536 for GF(2^16).
     * The matrix returned is a rows*cols array. You may access 
     * element (i,j) at matrix element i*cols+j.
     */
    

    
    public VandermondeMatrix(int rows, int cols, Field<Integer> field) throws MatrixDimensionException  {

        super(rows, cols, field);
        
        int i, j, k;

 
        int highest = field.numberOfDifferentValuesPossible();

        if (rows >= highest || cols >= highest) {
            throw new MatrixDimensionException(String.format("Error: VanderMondeMatrix: %d + %d >= %d\n",
                    rows, cols, highest));
        }

        //matrix = new Integer[rows*cols];

        for (i = 0; i < rows; i++) {
            k = 1;
            for (j = 0; j < cols; j++) {
                set(i,j,k);
                k = field.mult(k, i);
                //matrix[i * cols + j] = k;
                //k = gf.mult(k, i);
            }
        }

    }


}
