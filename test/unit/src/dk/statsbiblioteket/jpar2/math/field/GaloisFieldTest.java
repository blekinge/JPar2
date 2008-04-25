/*
 *     GaloisFieldTest.java
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

import dk.statsbiblioteket.jpar2.math.field.GaloisField;
import junit.framework.TestCase;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class GaloisFieldTest extends TestCase {

    GaloisField field;

    public GaloisFieldTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        field = new GaloisField(GaloisField.WordSize.FOUR);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of div method, of class GaloisField.
     */
    public void testDiv() {
        assertEquals(field.div(13, 10).intValue(), 3);
        assertEquals(field.div(3, 7).intValue(), 10);


    }

    /**
     * Test of mult method, of class GaloisField.
     */
    public void testMult() {
        assertEquals(field.mult(13, 10).intValue(), 11);
        assertEquals(field.mult(3, 7).intValue(), 9);

    }

    /**
     * Test of pow method, of class GaloisField.
     */
    public void testPow() {
        assertEquals(field.pow(4, 3).intValue(), 12);
        assertEquals(field.pow(3, 2).intValue(), 5);
        assertEquals(field.pow(9, 3).intValue(), 15);


    }

    /**
     * Test of add method, of class GaloisField.
     */
    public void testAdd() {
        assertEquals(field.add(11, 7).intValue(), 12);


    }

    /**
     * Test of sub method, of class GaloisField.
     */
    public void testSub() {
        assertEquals(field.sub(11, 7).intValue(), 12);


    }
    
    public void testInvert() {
        assertEquals(field.div(1,7),field.invert(7));
        assertEquals(field.div(1,1),field.invert(1));
        assertEquals(field.div(1,15),field.invert(15));
    }
}