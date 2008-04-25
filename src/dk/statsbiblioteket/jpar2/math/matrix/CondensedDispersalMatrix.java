/*
 *     CondensedDispersalMatrix.java
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
import java.util.Arrays;
import java.util.List;


/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class CondensedDispersalMatrix extends Matrix<Integer, Field<Integer>> {

    int[] id;
    
    public CondensedDispersalMatrix(int rows, int cols, Matrix<Integer, Field<Integer>> disp, 
            List<Boolean> existingRows) throws NotRecoverableException{
        super(rows,cols,disp.getField());
        
        id = new int[cols];
        Arrays.fill(id, -1);
        
        for (int i = 0; i < this.getRows(); i++) {//ie. the cols,cols matrix made with the supercall
            if (existingRows.get(i)) {

                id[i] = i;
                //int tmp = cols * i;
                for (int j = 0; j < this.getCols(); j++) {
                    this.set(i,j,disp.get(i, j));
                }
            }
        }
    
        /* Next, put coding rows in */
        int k = 0;
        for (int i = this.getRows(); i < disp.getRows(); i++) {//the coding rows below the identity rows in disp
            if (existingRows.get(i)) {
            
                while (k < cols && id[k] != -1) {//k should now have the index of the first row found above
                    k++;
                }
                
                if (k == cols) {//if it did not find any more, we are dont
                    return;
                }
                
                id[k] = i;
                
                for (int j = 0; j < cols; j++) {
                    this.set(k,j,disp.get(i, j));

                }
            }
        }
        
        /* If we're here, there are no more coding rows -- check to see that the
        condensed dispersal matrix is full -- otherwise, it's not -- return an
        error */

        while (k < cols && id[k] != -1) {
            k++;
        }
        if (k == cols) {
            return;
        }
        throw new NotRecoverableException("There are more missing blocks than there are recoveryblocks");


        
        
        
    }
    
    @Override
    public String toString(){
        String s = super.toString();
        
        s = s + "\n";
        for (int i=0;i<id.length;i++){
            s = s + "row " + i + " is " + id[i] +"\n";
        }
        
        return s;
        
    }
    
    public int[] getRowMapping(){
        return id;
    }

}
