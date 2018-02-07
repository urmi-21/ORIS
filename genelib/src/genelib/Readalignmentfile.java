/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author slim
 */
public class Readalignmentfile {

    String filepath;
    char[][] seqmatrix = null;
    int rows = 0;
    int cols = 0;

    public Readalignmentfile(String file) {
        filepath = file;
    }

    public void read() {
        int x;
        char ch;
        int numlines = 0;
        BufferedReader br = null;
        BufferedReader cr = null;
        /*stringBuilderdisp for storing data with new lines to display and stringBuilder
        seq for ignoring newlines and getting only sequence chars*/

        StringBuilder stringBuilderseq = new StringBuilder();
        String ls = System.getProperty("line.separator");

        int f = 0;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader(filepath));

            while ((sCurrentLine = br.readLine()) != null) {
                if (f == 0) {

                    f = 1;
                } else {
                    stringBuilderseq.append(sCurrentLine);
                    numlines++;
                    if (!(sCurrentLine.isEmpty())) {
                        stringBuilderseq.append(ls);
                    }

                }

            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR File not found");
            e.printStackTrace();

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ERROR File not found");
                //ex.printStackTrace();

            }
        }

        
        
        //find num of columns

        for (int i = 0; i < stringBuilderseq.length(); i++) {
            if (stringBuilderseq.charAt(i) == '\n') {

                break;
            } else {
                cols++;

            }
        }


        
        //find num or rows

        for (int i = 0; i < stringBuilderseq.length(); i++) {
            if (stringBuilderseq.charAt(i) == '\n') {
                rows++;
            }
        }
       // System.out.println("rows=" + rows);
       

        //remove /n from seq
        String newseqstring = stringBuilderseq.toString();
        while (newseqstring.contains("\n")) {
            newseqstring = newseqstring.replaceFirst("\n", "");
        }
       
        //create sequence matrix
        seqmatrix = new char[rows][cols];
        int k = 0;
        int r = 0;
        int index = 0;
        while (r < rows) {
            k = 0;
            while (k < cols) {
                seqmatrix[r][k] = newseqstring.charAt((cols * r) + k);
                k++;
            }
            r++;
        }


        


    }

    public float[][] returnmatrix() {

        float[][] wtmatrix = new float[4][cols-1];
        int countA = 0;
        int countG = 0;
        int countC = 0;
        int countT = 0;

       /* //print sematrix
        System.out.println("***seq matrix:\n");

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j <rows ; j++) {

                System.out.printf("%c", seqmatrix[j][i]);
            }
            System.out.println();
        }*/
        
        //find count for each column
        int index=0;
        for (int i = 0; i < cols-1; i++) {
            countA = countC = countG = countT = 0;
            for (int j = 0; j < rows; j++) {
                
                if (seqmatrix[j][i] == 'A') {
                    countA++;
                } else if (seqmatrix[j][i] == 'G') {
                    countG++;
                } else if (seqmatrix[j][i] == 'C') {
                    countC++;
                } else if (seqmatrix[j][i] == 'T') {
                    countT++;
                }
                
            }
            //after counts put in wt matrix
           
            wtmatrix[0][i]=(float) countA/rows;
            wtmatrix[1][i]=(float)countG/rows;
            wtmatrix[2][i]=(float)countC/rows;
            wtmatrix[3][i]=(float)countT/rows;
        }

        return wtmatrix;
    }
    
    public int returncols(){
        return cols;
    }

    public static void main(String[] args) {
        new Readalignmentfile("samplealignment.txt").read();
    }
}
