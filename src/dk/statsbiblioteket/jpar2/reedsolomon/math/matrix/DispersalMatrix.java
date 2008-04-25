/*
 *     DispersalMatrix.java
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



/**
 * <p>The reed solomon information dispersal matrix. 
 * 
 * <p>This is the matrix B in the paper <i>Note: Correction to the 1997 
 * Tutorial on Reed-Solomon Coding.</i>
 * <p>Rows and cols must be less than
 * 256 for GF(2^8) and 65536 for GF(2^16).
 *
 * @author Asger Blekinge-Rasmussen
 *         
 */
public class DispersalMatrix extends VandermondeMatrix {

    /**
     * Find a row below row_num that does not have a zero element in the row_num
     * column
     * @param rows The rows of this matrix
     * @param cols The columns of this matrix
     * @param row_num The row number to begin the search from
     * @return The row number of the row to swap
     */
    private int find_swap_row(int rows, int cols, int row_num) {
        int j;

        for (j = row_num; j < rows; j++) {
            if (get(j, row_num) != field.ZERO) {
                //if (matrix[j * cols + row_num] != 0) {
                return j;
            }
        }
        return -1;
    }

    /**
     * Make a new information dispersal matrix
     * @param rows number of rows
     * @param cols number of columns
     * @param field the field of operations
     * @throws javapar2.math.matrix.MatrixException If the matrix cannot be made TODO
     */
    public DispersalMatrix(int rows, int cols, Field<Integer> field) throws MatrixException {
        //int[] vdm;
        super(rows, cols, field);
        
        


        for (int currentRow = 0;
                currentRow < cols && currentRow < rows;
                currentRow++) {
            //find a row to swap, or this if its okay
            int swapRow = find_swap_row(rows, cols, currentRow);
            if (swapRow == -1) {
                throw new MatrixException(
                        String.format("Error: make_dispersal_matrix.  " +
                        "Can't find swap row %d\n", currentRow));
            }

            if (swapRow != currentRow) {//swap, if another row was found
                swapRow(swapRow, currentRow);
            }

            if (get(currentRow, currentRow) != field.ONE) {
                //if the diagonal element is not one, normalise
                Integer inv = field.invert(get(currentRow, currentRow));
                multCol(currentRow, inv);

            }

            for (int currentCol = 0; currentCol < cols; currentCol++) {
                Integer value = get(currentRow, currentCol);
                if (currentCol != currentRow && value != field.ZERO) {
                    //if elements not on the diagonal is non-zero

                    for (int l = 0; l < rows; l++) {
                        Integer tmp = field.mult(value, get(l, currentRow));
                        
                        tmp = field.sub(get(l, currentCol), tmp);
                        
                        set(l, currentCol, tmp);
                    }
                }
            }
        }
    }
}
