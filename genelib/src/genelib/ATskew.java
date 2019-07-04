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
 *This class computes the ATskew method
 * @author slim
 */
public class ATskew extends Thread {

    
    final char[] sequence;
    int winsize;
    int increament;
    int saveflag;
    String filename;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();

    /**
     * Constructor for ATskew class
     * @param val the input DNA sequence
     * @param ws Window size
     * @param inc Increment size
     * @param save Save flag to save the results
     * @param fname Filename to save the results
     */
    public ATskew(char[] val, int ws, int inc, int save, String fname) {
        sequence = val;
        winsize = ws;
        increament = inc;
        if (save == 1) {
            if(fname==null){
                JOptionPane.showMessageDialog(null, "No filename given");
                return;
            }
            filename = fname;
            saveflag = 1;
        }
    }

    public void run() {

        if (sequence == null) {
            JOptionPane.showMessageDialog(null, "Fatal Error No file Read");
            return;
        }
        int start = 0, totlwin = 0;

        //compute total number of windows
        for (start = 0; start + winsize <= sequence.length; start = start + increament) {
            totlwin++;
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
        frame.setResizable(false);
        frame.setVisible(true);

        //if savefile is required 
        BufferedWriter writer = null;
        String tmpindex;
        if (saveflag == 1) {
            filename = filename + ".txt";
            File f = new File(filename);
            try {
                writer = new BufferedWriter(new FileWriter(f));
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                writer.write("Data for the AT-skew plot ");
                writer.newLine();
                writer.write("Winsize = " + String.valueOf(winsize));
                writer.write("\tIncreament  = " + String.valueOf(increament));
                writer.newLine();
                writer.write("Window Number\t (A - T) / (A + T) ");
                writer.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }


        }

        double[] atresults = new double[totlwin]; //to store main results
        double[] xaxis = new double[totlwin]; //to store x axis
        int rindex = 0; //index variable for atresults and xaxis arrays
        char[] subsequence = new char[winsize];

        //create object of class FUNC which contains method to calculate ATskew
        Func sob2 = new Func();
        
        //for each window
        for (start = 0; start + winsize <= sequence.length; start = start + increament) {
            for (int i = 0; i < winsize; i++) {
                subsequence[i] = sequence[start + i];
            }
            xaxis[rindex] = rindex + 1;
            atresults[rindex++] = sob2.ATskew(subsequence);
           //update progress bar
            if(rindex%5000==0){
            pBar.setValue(rindex);
           
            }
        }
        frame.dispose(); //dispose progress bar

        //save file
        if (saveflag == 1) {
            for (int i = 0; i < atresults.length; i++) {
                try {
                    //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                    writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(atresults[i]));
                    writer.newLine();
                } catch (IOException ex) {
                    Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        ///after writing
        if (saveflag == 1) {
            try {
                JOptionPane.showMessageDialog(null, filename + "File saved");
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //create plot object
        Plot newplot = new Plot();
        newplot.doplot(sequence,xaxis, atresults, "ATskew result", "Window Number", "(A - T) / (A + T)", winsize, increament);



    }
}
