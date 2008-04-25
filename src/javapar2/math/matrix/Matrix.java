/*
 *     Matrix.java
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


import javapar2.math.field.Field;

/**
 * Represents a two dimensional matrix. Needs to know the types of elements, and
 * the field in which operations are made. This could be a Galois field, og 
 * RealField.
 * 
 * Class contains the used matrix operations. Should be replaces with a general
 * matrix package later.
 * @author Asger Blekinge-Rasmussen
 */
public class Matrix<N extends Number, F extends Field<N>> extends BackingMatrix<N> {

    protected final F field;

    /**
     * Create a new Matrix with these dimensions. Allocate the space immediately.
     * @param rows Number of rows in the matrix
     * @param cols Number of colums in the matrix
     * @param field The field that describes how to do aritmethic on the elements.
     */
    public Matrix(int rows, int cols, F field) {
        super(rows,cols);
        set(field.ZERO);
        this.field = field;
    }

    public F getField() {
        return field;
    }

    
    /**
     * Add the value k to the element in i,j
     * @param i the row of the element
     * @param j the column of the element
     * @param k the value to add to it
     */
    public void add(int i, int j, N k) {
        set(i,j,field.add(get(i,j), k));
    }

    /**
     * Subtract the value k from the element in i,j
     * @param i the row of the element
     * @param j the column of the element
     * @param k the value to subtract from it
     */
    public void sub(int i, int j, N k) {
                set(i,j,field.sub(get(i,j), k));

    }

    /**
     * Multiplies the element i,j with k
     * @param i the row of the element
     * @param j the column of the element
     * @param k the value to multiply 
     */
    public void mult(int i, int j, N k) {
                set(i,j,field.mult(get(i,j), k));

    }

    /**
     * Divides the element i,j with k. I.N. (i,j)/k
     * @param i the row of the element
     * @param j the column of the element
     * @param k the value to divide 
     */
    public void div(int i, int j, N k) {
                set(i,j,field.div(get(i,j), k));


    }

    /**
     * Takes the element i,j to the power of k
     * @param i the row of the element
     * @param j the column of the element
     * @param k the power
     */
    public void pow(int i, int j, N k) {
                set(i,j,field.pow(get(i,j), k));


    }

    /**
     * Invert the element in i,j. I.N. 1/(i,j)
     * @param i the row of the element
     * @param j the column of the element
     */
    public void invert(int i, int j) {
                set(i,j,field.invert(get(i,j)));


    }

    /**
     * Protected constructor. Takes the backing array as input
     * @param rows The number of rows of the matrix
     * @param cols the number of columns of the matrix
     * @param field The field to do operations in
     * @param matrix The backing array
     */
    public Matrix(int rows, int cols, F field, N[] matrix) throws MatrixDimensionException {
        super(rows,cols,matrix);
        this.field = field;

    }
    
    /**
     * Private constructor for use with copy.
     * @param rows The number of rows of the matrix
     * @param cols the number of columns of the matrix
     * @param field The field to do operations in
     * @throws javapar2.math.matrix.MatrixDimensionException
     */
    private Matrix(int rows, int cols, F field, Object[] matrix) throws MatrixDimensionException{
        super(rows,cols,matrix);
        this.field = field;
    }
    
    /**
     * Copies this matrix. The copy should be identical, but unrelated to this
     * @return A new identical matrix
     */
    public Matrix<N, F> copy() {
        try {
            return new Matrix<N, F>(getRows(), getCols(), field, copyBacking());
        } catch (MatrixDimensionException ex) {
            throw new Error("We should not ever get here!");
        }
    }

    /**
     * Static method to multiply to matrixes. Not good, will be rewritten
     * @param a the left matrix
     * @param b the right matrix
     * @param field The field in which to do the operations
     * @return the new multiplied matrix
     * @throws javapar2.math.MatrixDimensionException if the rows of a is not equal to the cols of b
     */
    public static <E extends Number, F extends Field<E>> Matrix<E, F> mult(Matrix<E, F> a, Matrix<E, F> b, F field) throws MatrixDimensionException {
        if (a.getCols() != b.getRows()) {
            throw new MatrixDimensionException("Rows does not equal cols");
        }
        

        Matrix<E, F> product = new Matrix<E, F>(a.getRows(), b.getCols(), field);



        product.set(field.ZERO);

        for (int i = 0; i < product.getRows(); i++) {//rows in product
            for (int j = 0; j < product.getCols(); j++) {//cols in product
                E tmp = product.get(i, j);
                for (int k = 0; k < a.getCols(); k++) {//the lines in a and b
                    tmp = field.add(tmp, field.mult(a.get(i, k), b.get(k, j)));
                }
                product.set(i, j, tmp);
            }
        }
        return product;

    }

    /**
     * Construct the identity matrix. Static factory method. The identity will 
     * always be square
     * @param sides The number of elements on either side
     * @param field The field for this matrix
     * @return An identity matrix
     */
    public static <E extends Number, F extends Field<E>> Matrix<E, F> identity(int sides, F field) {
        Matrix<E, F> identity = new Matrix<E, F>(sides, sides, field);
        for (int i = 0; i < sides; i++) {
            identity.set(i, i, field.ONE);
        }
        return identity;
    }

    /**
     * Swap two rows in this matrix.
     * @param from one row
     * @param to the other row
     */
    public void swapRow(int from, int to) {
        for (int k = 0; k < getCols(); k++) {
            N tmp = get(from, k);//switch
            set(from, k, get(to, k));
            set(to, k, tmp);
        }
    }

    /**
     * Multipy an entire row with the factor
     * @param row the row to multiply
     * @param factor the factor to multiply on
     */
    public void multRow(int row, N factor) {
        for (int j = 0; j < getCols(); j++) {
            mult(row, j, factor);
        }
    }
    
        /**
     * Multipy an entire column with the factor
     * @param col the column to multiply
     * @param factor the factor to multiply on
     */
    public void multCol(int col, N factor) {
        for (int j = 0; j < getRows(); j++) {
            mult(j, col, factor);
        }
    }


    /**
     * Inverts this matrix. The matrix must be square. This matrix is not changed in the process.
     * @return The inverted matrix.
     * @throws javapar2.math.NonInvertibleException if the matrix is non-invertible
     * @throws javapar2.math.MatrixDimensionException If the matrix is non-squre 
     */
    public Matrix<N, F> invert() throws NonInvertibleException, MatrixDimensionException {



        int rows = getRows();
        int cols = getCols();
        if (rows != cols) {
            throw new MatrixDimensionException("Matrix to inverse must be square!!");
        }


        //We use two matrices. All operations will be done identically on both
        //The set of operations that will turn the copy into the identity matrix
        //will turn the identity matrix into the inverse of copy
        Matrix<N, F> inv = identity(rows, field);
        Matrix<N, F> copy = copy();

        N tmp;

        /* pic(inv, copy, rows, "Start"); */

        /* First -- convert into upper triangular */
        for (int i = 0; i < rows; i++) {


            /* Swap rows if we have a zero i,i element.  If we can't swap, then the 
            matrix was not invertible */
            if (copy.get(i, i) == field.ZERO) {
                int j;
                for (j = i + 1; j < rows; j++) {
                    if (copy.get(j, i) != field.ZERO) {
                        break;//increment until we find a non-zero element
                    }
                }
                if (j == rows) {//if we made it all the way through, not finding any
                    throw new NonInvertibleException("gf_invert_matrix: Matrix not invertible!!\n");
                }
                //j is now the found row. i is the current row
                copy.swapRow(i, j);
                inv.swapRow(i, j);
            }

            /* Multiply the row by 1/element(i,i) */
            tmp = copy.get(i, i);
            if (tmp != field.ONE) {//normalise
                N inverse = field.invert(tmp);
                copy.multRow(i, inverse);
                inv.multRow(i, inverse);
            /* pic(inv, copy, rows, "Divided through"); */
            }

            /* Now for each j>i, add A_ji*Ai to Aj */
            //k = cols * i+i;
            
            //For all rows (j) below i, substract the row i from them times the value in the i'th column
            //This will set their value in the i'th column to zero
            for (int j = i + 1; j < rows; j++) {
                //k += cols;
                tmp = copy.get(j, i);
                if (tmp != field.ZERO) {
                    if (tmp == field.ONE) {//optimization, my arse
                        for (int x = 0; x < cols; x++) {
                            copy.sub(j, x, copy.get(i, x));
                            inv.sub(j, x, inv.get(i, x));
                        }
                    } else {
                        for (int x = 0; x < cols; x++) {
                            copy.sub(j, x, field.mult(tmp, copy.get(i, x)));
                            inv.sub(j, x, field.mult(tmp, inv.get(i, x)));
                        }
                    }
                }
            }
        /* pic(inv, copy, rows, "Eliminated rows"); */
        }

        // Now the matrix is upper triangular.  Start with the downmost row 
         // and substitute it into the upper rows 

        for (int i = rows - 1; i >= 0; i--) {//the downmost row
            for (int j = 0; j < i; j++) { //all the colums to the left of diagonal
                //      rs2 = j*cols;
                tmp = copy.get(j, i);
                if (tmp != field.ZERO) {
                    copy.set(j, i, field.ZERO); //set them to zero if they are not already

                    for (int k = 0; k < cols; k++) {//now we use j to represent all the rows above the diagonal

                        inv.add(j, k, field.mult(tmp, inv.get(i, k)));//decipher why we do this
                    }
                }
            }
        /* pic(inv, copy, rows, "One Column"); */
        }

        return inv;


    }
    
    @Override
    public String toString(){
        
        String m = "\n";
        for (int i = 0;i<getRows();i++){
            m= m + "[";
            for (int j = 0; j<getCols(); j++){
                m = m + " " + String.format(get(i,j).toString(),"%5f");
            }
            m = m + "]\n";
        }
        return m;
    }
}
    



