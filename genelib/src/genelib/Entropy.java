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
 *Class to compute Shannon's entropy measure
 * @author urmi
 */
public class Entropy extends Thread {

    ImageIcon img = new ImageIcon("JNU_3.jpg");
    private int flag = 0;
    final char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int winsize;
    int increament;
    int saveflag;
    String filename;
    
    /**
     * 
     * @param val DNA sequence
     * @param ws window size
     * @param inc increment size
     * @param save save flag
     * @param fname filename to save results
     */
    public Entropy(char[] seq, int wsize, int inc, int save, String fname) {
        sequence = seq;
        winsize = wsize;
        increament = inc;
        filename = fname;
        saveflag = save;
    }

    @Override
    public void run() {

        int start = 0, totlwin = 0;

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
                writer.write("Data for the Entropy plot ");
                writer.newLine();
                writer.write("Winsize = " + String.valueOf(winsize));
                writer.write("\tIncreament  = " + String.valueOf(increament));
                writer.newLine();
                writer.write("Window Number\t Entropy H(x) ");
                writer.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


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
        frame.setIconImage(img.getImage());
        //set the frame in the middle of screen
        frame.setLocation((width / 2), (height / 2));
        frame.setResizable(false);
        frame.setVisible(true);

        int countA, countC, countG, countT;
        double pa, pc, pg, pt;
        Compositioncount sob2 = new Compositioncount();
        double[] enresults = new double[totlwin];
        double[] xaxis = new double[totlwin];
        int rindex = 0;
        char[] subsequence = new char[winsize];

        //start windowing method
        for (start = 0; start + winsize <= sequence.length; start = start + increament) {
            for (int i = 0; i < winsize; i++) {
                subsequence[i] = sequence[start + i];
            }
            countA = sob2.returncount(subsequence, 'A');
            countC = sob2.returncount(subsequence, 'C');
            countG = sob2.returncount(subsequence, 'G');
            countT = sob2.returncount(subsequence, 'T');


            // calculate Entropy: -sum{pi*log(pi)}
            pa = ((double) countA / (double) subsequence.length);

            pa = pa * (Math.log(pa) / Math.log(2));

            pc = ((double) countC / (double) subsequence.length);
            pc = pc * (Math.log(pc) / Math.log(2));
            pg = ((double) countG / (double) subsequence.length);
            pg = pg * (Math.log(pg) / Math.log(2));
            pt = ((double) countT / (double) subsequence.length);
            pt = pt * (Math.log(pt) / Math.log(2));

            xaxis[rindex] = rindex + 1;
            enresults[rindex] = -1 * (pa + pc + pg + pt);
            rindex++;

            if (rindex % 100 == 0) {
                //update progress bar
                pBar.setValue(rindex);
               // pBar.update(pBar.getGraphics());
            }
        }

        frame.dispose();
        if (saveflag == 1) {
            for (int i = 0; i < enresults.length; i++) {
                try {
                    //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                    writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(enresults[i]));
                    writer.newLine();
                } catch (IOException ex) {
                    Logger.getLogger(Entropy.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        //after writing
        if (saveflag == 1) {
            try {
                JOptionPane.showMessageDialog(null, filename + "File saved");
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Entropy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //create plot object
        Plot newplot = new Plot();
        newplot.doplot(sequence,xaxis, enresults, "Entropy results", "Window Number", "Entropy H(x)", winsize, increament);
    }
}
