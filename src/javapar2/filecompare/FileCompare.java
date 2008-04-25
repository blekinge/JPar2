/*
 *     FileCompare.java
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


package javapar2.filecompare;

import java.io.File;
import java.io.IOException;

import java.util.List;
import javapar2.sets.Par2Set;
import javapar2.files.DataFile;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 *
 * @author Asger Blekinge-Rasmussen
 */
public class FileCompare {
    
    /**
     * Main method. 
     * @param args
     */
    public static void main(String[] args) {
        
        Options options = new Options();
        options.addOption("s", "slices", true,
                          "The number of slices to use in the comparison");

        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(options,args);
            args = cmd.getArgs();
            if (!cmd.hasOption("s") || args.length != 2){
                System.exit(2);
            }
            int slices = Integer.parseInt(cmd.getOptionValue("s").trim());

            File f1 = new File(args[0]);
            File f2 = new File(args[1]);
            if (f1.length() == f2.length()){
                int sliceSize = (int)(f1.length() / slices);//rounding here...

                DataFile df1 = new DataFile(f1, sliceSize);
                DataFile df2 = new DataFile(f2, sliceSize);
                
                List<Integer> defectIndexes = df1.compareWithIndex(df2);
                

                for (int index: defectIndexes){
                    System.out.println("index " + index + ", from " 
                            + index*sliceSize + " to "
                            + (index+1)*sliceSize + " is defect");
                }
                if (defectIndexes.size() == 0){
                    System.out.println("Files are identical");
                }
                
                
            }else {
                System.out.println("Files differ in length, cannot help you");
            }
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }


}
