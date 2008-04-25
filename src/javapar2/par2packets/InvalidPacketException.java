/*
JavaPar2, a library for the par2 (par2.net) specification
Copyright (C) 2007  Asger Blekinge-Rasmussen

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package javapar2.par2packets;

/**
 * Exception thrown when attemption to parse an invalid packet
 * @author Asger
 */
public class InvalidPacketException extends Exception {
    
    /**
     * Constructor
     * @param s The message to embed in the exception
     * @param t The cause of the exception
     */
    public InvalidPacketException(String s, Throwable t){
        super(s,t);
    }

    /**
     * Constructor
     * @param s The message to embed in the exception
     */
    public InvalidPacketException(String s){
        super(s);
    }

}
