/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author slim
 */
public class Createseqalignmentfile extends Thread {

    public void run() {
        /*read the config file to read the parameters*/
        BufferedReader br = null;
        String sCurrentLine = null;
        String substr = null;
        int linectr = 0, wordctr = 0;



        File f = new File("samplealignment.txt");

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(f));
            writer.write("First line for info of the alighnent file. From second line begin sequence");
            writer.newLine();

            //random sequence of 10x10
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 13; j++) {
                    if (i % 2 == 0 && j % 2 == 0) {
                        writer.write("A");
                    }else if (i % 2 != 0 && j % 2 == 0) {
                        writer.write("G");
                    }else if (i % 2 != 0 && j % 2 != 0) {
                        writer.write("C");
                    }else if (i % 2 == 0 && j % 2 != 0) {
                        writer.write("T");
                    }
                }
                writer.newLine();
            }

            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Createseqalignmentfile.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
