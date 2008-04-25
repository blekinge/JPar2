/*
 *     GaloisField.java
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
package dk.statsbiblioteket.jpar2.math.field;

import java.util.Arrays;

/**
 * Actually GaloisField(2)
 * @author Asger Blekinge-Rasmussen
 */
public class GaloisField extends Field<Integer> {

    /**
     * The width of the words, in bits
     */
    public static enum WordSize {

        /**
         * Two bits per word.
         * Max value in this field is 3. Uses the generator 07 (octal)
         */
        TWO(2, 07),
        /**
         * Four bits per word.
         * Max value in this field is 15. Uses the generator 023 (octal)
         */
        FOUR(4, 023),
        /**
         * Eight bits per word, ie. one byte.
         * Max value in this field is 255. Uses the generator 0435 octal
         */
        EIGHT(8, 0435),
        /**
         * Sixteen bits per word, ie. one short
         * Max value in this field is 65535. Uses the generator 0210013 octal
         */
        SIXTEEN(16, 0210013),
        /**
         * 32 bits per word, ie. one int.
         * Max value in this field is 4294967295. Uses the generator 020000007 octal.
         * This wordsize does not work, as Java does not handle unsigned ints.
         */
        THIRTYTWO(32, 020000007);
        private Integer w;
        private Integer p;

        WordSize(Integer w, Integer p) {
            this.w = w;
            this.p = p;
        }

        /**
         * The width in bits of this field
         */
        Integer getWidth() {
            return w;
        }

        /**
         * the number of elements in this field
         */
        Integer getCount() {
            return 1 << w;
        }

        /**
         * The primitive polynomial of this field
         */
        Integer getPrimPoly() {
            return p;
        }

        /**
         * The highest element in this field
         */
        Integer getHighest() {
            return (1 << w) - 1;
        }
    }
    private Integer highest;
    private Integer width;
    private Integer[] gflog;
    private Integer[] gfilog;

    public GaloisField(WordSize f) {

        ZERO = 0;
        ONE = 1;
        Integer count = f.getCount();
        highest = f.getHighest();
        width = f.getWidth();

        gflog = new Integer[count];
        gfilog = new Integer[count];

        Integer b = 1;
        for (Integer log = 0; log < highest; log++) {
            gflog[b] = log;
            gfilog[log] = b;
            b = b << 1;
            if ((b & count) > 0) {
                b = b ^ f.getPrimPoly();
            }
        }
    }

    public Integer div(Integer a, Integer b) throws ArithmeticException {
        Integer diff_log;
        if (a == 0) {
            return 0;
        }
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        diff_log = gflog[a] - gflog[b];
        if (diff_log < 0) {
            diff_log += highest;
        }
        return gfilog[diff_log];
    }

    public Integer mult(Integer a, Integer b) {
        Integer sum_log;
        if (a == 0 || b == 0) {
            return 0;
        }
        sum_log = gflog[a] + gflog[b];
        if (sum_log >= highest) {
            sum_log -= highest;
        }
        return gfilog[sum_log];
    }

    public Integer pow(Integer a, Integer b) {


        // anything to the zero power is always 1
        if (b == 0) {
            return 1;
        }
        // zero to anypower is always 0
        if (a == 0) {
            return 0;
        }

        int sum_pow = gflog[a] * b;
        //shifting width downwards is the same as subtracting highest
        //The and operation isolates the bottom half of the variable; 
        sum_pow = (sum_pow >> width) + (sum_pow & highest);
        //equivalant to while(sum_pow > highest) sum_pow -= highest;

//        
//        for (Integer i = 0; i < b; i++){
//            sum_pow += gflog[a];
//            if (sum_pow >= highest) {
//                sum_pow -= highest;
//            }
//        }
//        
        return gfilog[sum_pow];

    }

    public Integer add(Integer a, Integer b) {
        return a ^ b;
    }

    public Integer sub(Integer a, Integer b) {
        return a ^ b;
    }

    public void mult(Integer[] region, Integer factor) {
        Integer sum_log;
        Integer flog;

        if (factor == 1) {
            return;
        }
        if (factor == 0) {
            Arrays.fill(region, 0);
            return;
        }

        flog = gflog[factor];

        for (Integer i = 0; i < region.length; i++) {
            sum_log = gflog[region[i]] + flog;
            if (sum_log >= highest) {
                sum_log -= highest;
            }
            region[i] = gfilog[sum_log];
        }

    }

    public Integer numberOfDifferentValuesPossible() {
        return highest;
    }

    @Override
    public Integer invert(Integer b) {

        int diff_log;
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        if (b == 1) {
            return 1;
        }

        diff_log = highest - gflog[b];
        return gfilog[diff_log];
    }
}
