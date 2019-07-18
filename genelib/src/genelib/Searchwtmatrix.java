/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *Class for weight matrix search
 * @author urmi
 */
public class Searchwtmatrix extends Thread {

    char[] seq;
    float[][] wtmatrix;
    int rows;
    int cols;
    float threshold;
    String filename;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("images/icons/orislogo.png"));
    
    /**
     * 
     * @param s input sequence
     * @param mat weight matrix
     * @param row num rows
     * @param col num cols
     * @param tval mismatch allowed
     * @param fname filename to save results
     */
    public Searchwtmatrix(char[] s, float[][] mat, int row, int col, float tval, String fname) {
        seq = s;
        wtmatrix = mat;
        rows = row;
        cols = col;
        threshold = tval;
        filename = fname;
    }

    @Override
    public void run() {

        int winsize = cols;
        int increment = 1;
        int start = 0, totlwin = 0;

        //get size to store all the results from each window
        for (start = 0; start + winsize <= seq.length; start = start + increment) {
            totlwin++;
        }

        //for saving results file
        BufferedWriter writer = null;
        String tmpindex;
        if (true) {
            filename = filename + ".txt";
            File f = new File(filename);
            try {
                writer = new BufferedWriter(new FileWriter(f));
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                writer.write("Weight Matrix search results ");
                writer.newLine();
                writer.write("Number of positions/columns= " + String.valueOf(winsize));
                writer.newLine();
                writer.write("Position number in sequence \t Score ");
                writer.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //for progress bar
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        pBar.setMinimum(0);
        pBar.setMaximum(totlwin);
        pBar.setStringPainted(true);
        frame.add(pBar);
        frame.setSize(400, 80);
        //  frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //set the frame in the middle of screen
        frame.setLocation((width / 2), (height / 2));
        frame.setIconImage(img.getImage());
        frame.setResizable(false);
        frame.setVisible(true);

        double[] wtresults = new double[totlwin];
        double[] xaxis = new double[totlwin];
        int rindex = 0;
        char[] subsequence = new char[winsize];
        float weightsum = 0;
        //to count total results above threshold
        int count=0;

        //start search

        for (start = 0; start + winsize <= seq.length; start = start + increment) {
            for (int i = 0; i < winsize; i++) {
                subsequence[i] = seq[start + i];
            }

            //do search for each subsequence
            weightsum = 0;
            for (int i = 0; i < subsequence.length; i++) {

                if (subsequence[i] == 'A') {
                    weightsum += wtmatrix[0][i];
                } else if (subsequence[i] == 'G') {
                    weightsum += wtmatrix[1][i];
                } else if (subsequence[i] == 'C') {
                    weightsum += wtmatrix[2][i];
                } else if (subsequence[i] == 'T') {
                    weightsum += wtmatrix[3][i];
                }
            }
            xaxis[rindex] = rindex + 1;
            wtresults[rindex++] = weightsum;
            if(weightsum>=threshold){
                count++;
            }


            //update progress bar
            if (rindex % 500 == 0) {
                pBar.setValue(rindex);
            }
           

        }
        frame.dispose();
        //write results to file
        try {
            for (int i = 0; i < wtresults.length; i++) {

                //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                writer.write(String.valueOf(i + 1) + "\t\t\t\t" + String.valueOf(wtresults[i]));
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, filename + "File saved");
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
        }
        //display how many matches above threshold
        float percent=((float)count/(float)wtresults.length)*100;
        JOptionPane.showMessageDialog(null,"Total matches above threshold are "+count+" ("+percent+"%)");
    }
}
