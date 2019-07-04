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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *Class to compute MKskew
 * @author urmi
 */
public class MKskew extends Thread {

    final char[] sequence;
    int winsize;
    int increament;
    int saveflag;
    String filename;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();

    /**
     * 
     * @param val DNA sequence
     * @param ws window size
     * @param inc increment size
     * @param save save flag
     * @param fname filename to save results
     */
    public MKskew(char[] val, int ws, int inc, int save, String fname) {
        sequence = val;
        winsize = ws;
        increament = inc;
        if (save == 1) {
            filename = fname;
            saveflag = 1;
        }
    }

    public void run() {

       
        int start = 0,totlwin = 0;

        //get size to store all the results from each window
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
                writer.write("Data for the MK-skew plot ");
                writer.newLine();
                writer.write("Winsize = "+String.valueOf(winsize));
                writer.write("\tIncreament  = "+String.valueOf(increament));
                writer.newLine();
                writer.write("Window Number\t (M - K) / (M + K) ");
                writer.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        double[] gcresults = new double[totlwin];
        double[] xaxis = new double[totlwin];
        int rindex = 0;
        char[] subsequence = new char[winsize];
        //create object of class FUNC
        Func sob2 = new Func();

        for (start = 0; start + winsize <= sequence.length; start = start +increament) {
            for (int i = 0; i < winsize; i++) {
                subsequence[i] = sequence[start + i];
            }
            xaxis[rindex] = rindex + 1;
            gcresults[rindex++] = sob2.MKskew(subsequence);
            //update progress bar
            if(rindex%5000==0){
            pBar.setValue(rindex);
           // pBar.update(pBar.getGraphics());
            }
        }
        frame.dispose();
        
        if(saveflag==1){
         for (int i = 0; i < gcresults.length; i++) {
             try {
                 //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                      writer.write(String.valueOf(i+1)+"\t"+String.valueOf(gcresults[i]));
                      writer.newLine();
             } catch (IOException ex) {
                 Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
             }
        }
        }
        
        //after writing
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
        newplot.doplot(sequence,xaxis, gcresults, "MKskew results", "Window Number", "(M - K) / (M + K)",winsize,increament);

    }
}
