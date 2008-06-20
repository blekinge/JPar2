/*
 *     BackingMatrix.java
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

import java.util.Arrays;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public abstract class BackingMatrix<N extends Number> {
    
    private int rows;
    private int cols;
    
    private Object[][] matrix;
    
    
    
    private Object[][] matrix_transposed;
    


    
    /**
     * Create a new Matrix with these dimensions. Allocate the space immediately.
     * @param rows Number of rows in the matrix
     * @param cols Number of colums in the matrix
     * @param field The field that describes how to do aritmethic on the elements.
     */
    @SuppressWarnings("unchecked")
    protected  BackingMatrix(int rows, int cols) {
        //matrixlist = new ArrayList<E>(rows*cols);
        matrix = new Object[rows][cols];//hack that produces warning, in order to make array
        matrix_transposed = new Object[cols][rows];
        this.cols = cols;
        this.rows = rows;
    }
    
    
    private Object[][] transpose(Object[][] matrix){
        int rows = matrix.length;
        int cols = matrix[0].length;
        
        Object[][] transposed = new Object[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }
    
    /**
     * Takes the backing array as input. Performs some sanity checks on it
     * @param rows The number of rows of the matrix
     * @param cols the number of columns of the matrix
     * @param field The field to do operations in
     * @param matrix The backing array
     * @throws MatrixDimensionException if the backing array have the wrong size
     */
    protected BackingMatrix(int rows, int cols, Object[][] matrix) throws MatrixDimensionException {
        if (matrix.length != rows){
            throw new MatrixDimensionException("The backing array does not match the specified dimensions");
        }
        if (matrix[0].length != cols){
            throw new MatrixDimensionException("The backing array does not match the specified dimensions");
        }
        this.matrix = matrix;
        this.matrix_transposed = transpose(matrix);
        this.cols = cols;
        this.rows = rows;

    }

    protected Object[][] backingArray(){
        return matrix;
    }
    
    /**
     * Copies this matrix. The copy should be identical, but unrelated to this
     * @return A new identical matrix
     */
    protected Object[][] copyBacking() {
        
        Object[][] array = new Object[rows][cols];
        for (int i=0;i<rows;i++){
            for (int j=0;j<cols;j++){
                array[i][j] = get(i,j);
            }
        }
        return array;
    }
    
    


        /**
     * Set this element to k. There are no boundschecking.
     * @param i the row of the element
     * @param j the colum of the element
     * @param k The new value to assign to it
     */
    public void set(int i, int j, N k) {
        //matrixlist.set(i*cols+j, k);
        matrix[i][j] = k;
        matrix_transposed[j][i] = k;
    }

    /**
     * Get this element. There are no boundschecking
     * @param i the row of the element
     * @param j the column of the element
     * @return The element in this location
     */
        @SuppressWarnings("unchecked")
    public N get(int i, int j) {
        //return matrixlist.get(i*cols+j);
        return (N) matrix[i][j];
    }

    /**
     * Get the number of columns of the matrix
     * @return the number of columns
     */
    public int getCols() {
        return cols;
    }
    
    @SuppressWarnings("unchecked")
    public Vector<N> getRow(int i){
        return new Vector<N>(matrix[i]);
    }
    
    @SuppressWarnings("unchecked")
    public Vector<N> getCol(int j){
        return new Vector<N>(matrix_transposed[j]);
    }

    /**
     * Get the number of rows of the matrix
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Set every element in the matrix to k
     * @param k The value to assign to every element
     */
    public void set(N k) {
        for (Object[] ar : matrix){
            Arrays.fill(ar, k);
        }
        for (Object[] ar : matrix_transposed) {
            Arrays.fill(ar, k);
        }

        
    }
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o){
        if (!getClass().isAssignableFrom(o.getClass())){
            return false;
        }
        BackingMatrix<N> that = (BackingMatrix<N>) o;
        
        return Arrays.deepEquals(
                this.backingArray(), that.backingArray());
        
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.matrix != null ? this.matrix.hashCode() : 0);
        hash = 37 * hash + this.rows;
        hash = 37 * hash + this.cols;
        return hash;
    }
    
}
