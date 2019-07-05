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
 *Class to implement the DNA bending analysis
 * @author urmi
 */
public class Trimermethod extends Thread {

    char[] sequence;
    int windowsize;
    int inc;
    int oristart;
    int oriend;
    int saveflag;
    String filename;
    //trimer data
    static String[][] datavalues = {{"AAT", "-0.280"}, {"ATT", "-0.280"}, {"AAA", "-0.274"}, {"TTT", "-0.274"}, {"CCA", "-0.246"}, {"TGG", "-0.246"}, {"AAC", "-0.205"}, {"GTT", "-0.205"}, {"ACT", "-0.183"}, {"AGT", "-0.183"}, {"CCG", "-0.136"}, {"CGG", "-0.136"}, {"ATC", "-0.110"}, {"GAT", "-0.110"}, {"AAG", "-0.081"}, {"CTT", "-0.081"}, {"CGC", "-0.077"}, {"GCG", "-0.077"}, {"AGG", "-0.057"}, {"CCT", "-0.057"}, {"GAA", "-0.037"}, {"TTC", "-0.037"}, {"ACG", "-0.033"}, {"CGT", "-0.033"}, {"ACC", "-0.032"}, {"GGT", "-0.032"}, {"GAC", "-0.013"}, {"GTC", "-0.013"}, {"CCC", "0.012"}, {"GGG", "0.012"}, {"ACA", "-0.006"}, {"TGT", "-0.006"}, {"CGA", "-0.003"}, {"TCG", "-0.003"}, {"GGA", "0.013"}, {"TCC", "0.013"}, {"CAA", "0.015"}, {"TTG", "0.015"}, {"AGC", "0.017"}, {"GCT", "0.017"}, {"GTA", "0.025"}, {"TAC", "0.025"}, {"AGA", "0.027"}, {"TCT", "0.027"}, {"CTC", "0.031"}, {"GAG", "0.031"}, {"CAC", "0.040"}, {"GTG", "0.040"}, {"TAA", "0.068"}, {"TTA", "0.068"}, {"GCA", "0.076"}, {"TGC", "0.076"}, {"CTA", "0.090"}, {"TAG", "0.090"}, {"GCC", "0.107"}, {"GGC", "0.107"}, {"ATG", "0.134"}, {"CAT", "0.134"}, {"CAG", "0.175"}, {"CTG", "0.175"}, {"ATA", "0.182"}, {"TAT", "0.182"}, {"TCA", "0.194"}, {"TGA", "0.194"}};
    //for progress bar
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    ImageIcon img = new ImageIcon("images/icons/orislogo.png");

    /**
     * 
     * @param seq DNA sequence
     * @param winsize window size
     * @param increment increment size
     * @param strt start position
     * @param end end position
     * @param save save flag
     * @param fname filename to save
     */
    public Trimermethod(char[] seq, int winsize,int increment, int strt, int end, int save, String fname) {

        int k = 0;
        windowsize = winsize;
        inc=increment;
        sequence = new char[end - strt + 1];
       //copy the origin sequence for which we want to do bending analysis in to array "sequence"
        //map the positions starting from 1 to indexes starting from 0
        //hence start=start-1
        for (int i = strt - 1; i < end; i++) {

            sequence[k] = seq[i];
            k++;
        }

        oristart = strt;
        oriend = end;

        if (save == 1) {
            filename = fname;
            saveflag = 1;
        }
    }

    public void run() {
       int numtrimers = 0;
        int totlwin = 0, start = 0;
        char[] subsequence = new char[windowsize];

        //calculate total windows to be made
        for (start = 0; start + windowsize <=sequence.length; start = start + inc) {
            totlwin++;
        }

        double[] results = new double[totlwin];
        double[] xaxis = new double[totlwin];
        char[] trimer = new char[3];
        StringBuilder trimerstr = new StringBuilder(3);
       
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
                writer.write("Data for the Trimer method plot ");
                writer.newLine();
                writer.write("Origin start = " + String.valueOf(oristart));
                writer.write("\tOrigin end  = " + String.valueOf(oriend));

                writer.newLine();
                writer.write("Window Num\t CummulativeTrimervalue");
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
        frame.setIconImage(img.getImage());
        //set the frame in the middle of screen
        frame.setLocation((width / 2), (height / 2));
        frame.setResizable(false);
        frame.setVisible(true);


        double sum = 0;
        int k = 0;
        int increment=inc;

        //calculate all trimers for each window and add them to get the value for that window
        for (start = 0; start + windowsize <= sequence.length; start = start + increment) {
            
            //take a subsequence equal to windowsize then calculate trimers
            for (int i = 0; i < windowsize; i++) {
                subsequence[i] = sequence[start + i];

            }
            //calculate the number of trimers
            numtrimers = subsequence.length - 2;

            sum = 0;
            for (int i = 0; i < numtrimers; i++) {
                //find the trimer
                trimerstr.append(subsequence[i]);
                trimerstr.append(subsequence[i + 1]);
                trimerstr.append(subsequence[i + 2]);

          //      System.out.println("trimer=" + trimerstr.toString());
                //get the value of the trimer
                for (int j = 0; j < 64; j++) {
                    if (datavalues[j][0].equals(trimerstr.toString())) {

                        sum += Double.parseDouble(datavalues[j][1]);
                        // System.out.println("sum== " +sum);
                        break;
                    }

                }
                trimerstr.delete(0, 3);

            }

            xaxis[k] = k + 1;
            results[k++] = sum;

            //update progress bar
            if (k % 10000 == 0) {

                pBar.setValue(k);
                //      pBar.update(pBar.getGraphics());
            }

        }
        //dispose progress bar
        frame.dispose();
        if (saveflag == 1) {
            for (int i = 0; i < results.length; i++) {
                try {
                    //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                    writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(results[i]));
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
        newplot.doplot(sequence, xaxis, results, "Bending results", "Window number", "Bendability", windowsize, 1);

    }
}
