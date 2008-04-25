/*
 *     Field.java
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
package javapar2.math.field;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public abstract class Field<E extends Number> {

    public E ZERO;
    
    public E ONE;
    
    /**
     * Divides a by b
     * @param a a number in this Field
     * @param b a number in this Field
     * @throws ArithmeticException if b==0
     * @return a/b
     */
    public abstract E div(E a, E b) throws ArithmeticException;

    /**
     * Multiplies a by b
     * @param a a number in this Field
     * @param b a number in this Field
     * @return a*b
     */
    public abstract E mult(E a, E b);

    /**
     * Calculates the sum of a and b
     * @param a a number in this Field
     * @param b a number in this Field
     * @return a+b
     */
    public abstract E add(E a, E b);

    /**
     * Calculates the difference between a and b
     * @param a a number in this Field
     * @param b a number in this Field
     * @return a - b
     */
    public abstract E sub(E a, E b);

    /**
     * Calculates a to the power of b
     * @param a a number in this Field
     * @param b a number in this Field
     * @return a**b
     */
    public abstract E pow(E a, E b);

    
    public abstract void mult(E[] region, E factor);
       
    public abstract E invert(E a);
    
    public abstract E numberOfDifferentValuesPossible();

}
