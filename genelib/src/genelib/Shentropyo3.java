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
 *
 * @author jnu
 */
public class Shentropyo3 extends Thread {

    ImageIcon img = new ImageIcon("images/icons/orislogo.png");
    private int flag = 0;
    final char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int winsize;
    int increament;
    int saveflag;
    String filename;

    public Shentropyo3(char[] seq, int wsize, int inc, int save, String fname) {
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
                writer.write("Data for the order third Shannon's Entropy plot ");
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

        String[] trimers = new String[64];

        //make all possible 3 lenght combos
        trimers[0]="AAA";
        trimers[1]="AAG";
        trimers[2]="AAC";
        trimers[3]="AAT";
        trimers[4]="AGA";
        trimers[5]="AGC";
        trimers[6]="AGG";
        trimers[7]="AGT";
        trimers[8]="ATA";
        trimers[9]="ATG";
        trimers[10]="ATT";
        trimers[11]="ATC";
        trimers[12]="ACA";
        trimers[13]="ACG";
        trimers[14]="ACT";
        trimers[15]="ACC";

        trimers[16]="GAA";
        trimers[17]="GAG";
        trimers[18]="GAC";
        trimers[19]="GAT";
        trimers[20]="GGA";
        trimers[21]="GGC";
        trimers[22]="GGG";
        trimers[23]="GGT";
        trimers[24]="GTA";
        trimers[25]="GTG";
        trimers[26]="GTT";
        trimers[27]="GTC";
        trimers[28]="GCA";
        trimers[29]="GCG";
        trimers[30]="GCT";
        trimers[31]="GCC";

        trimers[32]="CAA";
        trimers[33]="CAG";
        trimers[34]="CAC";
        trimers[35]="CAT";
        trimers[36]="CGA";
        trimers[37]="CGC";
        trimers[38]="CGG";
        trimers[39]="CGT";
        trimers[40]="CTA";
        trimers[41]="CTG";
        trimers[42]="CTT";
        trimers[43]="CTC";
        trimers[44]="CCA";
        trimers[45]="CCG";
        trimers[46]="CCT";
        trimers[47]="CCC";

        trimers[48]="TAA";
        trimers[49]="TAG";
        trimers[50]="TAC";
        trimers[51]="TAT";
        trimers[52]="TGA";
        trimers[53]="TGC";
        trimers[54]="TGG";
        trimers[55]="TGT";
        trimers[56]="TTA";
        trimers[57]="TTG";
        trimers[58]="TTT";
        trimers[59]="TTC";
        trimers[60]="TCA";
        trimers[61]="TCG";
        trimers[62]="TCT";
        trimers[63]="TCC";


        int[] countsAA = new int[64];
        double[] prob = new double[64];
        Compositioncount sob2 = new Compositioncount();
        double[] enresults = new double[totlwin];
        double[] xaxis = new double[totlwin];
        int rindex = 0;
        char[] subsequence = new char[winsize];

        for (start = 0; start + winsize <= sequence.length; start = start + increament) {
            for (int i = 0; i < winsize; i++) {
                subsequence[i] = sequence[start + i];
            }

            for (int index = 0; index < 64; index++) {
                countsAA[index] = sob2.returncount(subsequence, trimers[index]);
            }

            // calculate pilogpi
            for (int index = 0; index < 64; index++) {
                prob[index] = ((double) countsAA[index] / (double) subsequence.length);
                prob[index] = prob[index] * (Math.log(prob[index]) / Math.log(2));
            }


            xaxis[rindex] = rindex + 1;
            double sum = 0;
            for (int index = 0; index < 64; index++) {
                sum = sum + prob[index];
            }
            enresults[rindex] = -1 * sum;
            rindex++;

            if (rindex % 100 == 0) {
                //update progress bar
                pBar.setValue(rindex);
                //   pBar.update(pBar.getGraphics());
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
        newplot.doplot(sequence, xaxis, enresults, "Entropy results", "Window Number", "Entropy H3(x)", winsize, increament);
    }
}
