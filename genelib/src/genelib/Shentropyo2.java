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
 *calculate order 2 entropy
 * @author urmi
 */
public class Shentropyo2 extends Thread {

    ImageIcon img = new ImageIcon("images/icons/orislogo.png");
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
     * @param seq DNA sequence
     * @param wsize window size
     * @param inc increment size
     * @param save save flag
     * @param fname filename to save results
     */
    public Shentropyo2(char[] seq, int wsize, int inc, int save, String fname) {
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
                writer.write("Data for the order two Shannon's Entropy plot ");
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

        int countAA, countAC, countAG, countAT, countCA, countCC, countCG, countCT, countGA, countGC, countGG, countGT, countTA, countTC, countTG, countTT;
        double paa, pac, pag, pat, pca, pcc, pcg, pct, pga, pgc, pgg, pgt, pta, ptc, ptg, ptt;
        Compositioncount sob2 = new Compositioncount();
        double[] enresults = new double[totlwin];
        double[] xaxis = new double[totlwin];
        int rindex = 0;
        char[] subsequence = new char[winsize];

        for (start = 0; start + winsize <= sequence.length; start = start + increament) {
            for (int i = 0; i < winsize; i++) {
                subsequence[i] = sequence[start + i];
            }
            countAA = sob2.returncount(subsequence, "AA");
            countAC = sob2.returncount(subsequence, "AC");
            countAG = sob2.returncount(subsequence, "AG");
            countAT = sob2.returncount(subsequence, "AT");
            countCA = sob2.returncount(subsequence, "CA");
            countCC = sob2.returncount(subsequence, "CC");
            countCG = sob2.returncount(subsequence, "CG");
            countCT = sob2.returncount(subsequence, "CT");
            countGA = sob2.returncount(subsequence, "GA");
            countGC = sob2.returncount(subsequence, "GC");
            countGG = sob2.returncount(subsequence, "GG");
            countGT = sob2.returncount(subsequence, "GT");
            countTA = sob2.returncount(subsequence, "TA");
            countTC = sob2.returncount(subsequence, "TC");
            countTG = sob2.returncount(subsequence, "TG");
            countTT = sob2.returncount(subsequence, "TT");


            // calculate pilogpi
            paa = ((double) countAA / (double) subsequence.length);
            paa = paa * (Math.log(paa) / Math.log(2));
            pac = ((double) countAC / (double) subsequence.length);
            pac = pac * (Math.log(pac) / Math.log(2));
            pag = ((double) countAG / (double) subsequence.length);
            pag = pag * (Math.log(pag) / Math.log(2));
            pat = ((double) countAT / (double) subsequence.length);
            pat = pat * (Math.log(pat) / Math.log(2));
            pca = ((double) countCA / (double) subsequence.length);
            pca = pca * (Math.log(pca) / Math.log(2));
            pcc = ((double) countCC / (double) subsequence.length);
            pcc = pcc * (Math.log(pcc) / Math.log(2));
            pcg = ((double) countCG / (double) subsequence.length);
            pcg = pcg * (Math.log(pcg) / Math.log(2));
            pct = ((double) countCT / (double) subsequence.length);
            pct = pct * (Math.log(pct) / Math.log(2));
            pga = ((double) countGA / (double) subsequence.length);
            pga = pga * (Math.log(pga) / Math.log(2));
            pgc = ((double) countGC / (double) subsequence.length);
            pgc = pgc * (Math.log(pgc) / Math.log(2));
            pgg = ((double) countGG / (double) subsequence.length);
            pgg = pgg * (Math.log(pgg) / Math.log(2));
            pgt = ((double) countGT / (double) subsequence.length);
            pgt = pgt * (Math.log(pgt) / Math.log(2));
            pta = ((double) countTA / (double) subsequence.length);
            pta = pta * (Math.log(pta) / Math.log(2));
            ptc = ((double) countTC / (double) subsequence.length);
            ptc = ptc * (Math.log(ptc) / Math.log(2));
            ptg = ((double) countTG / (double) subsequence.length);
            ptg = ptg * (Math.log(ptg) / Math.log(2));
            ptt = ((double) countTT / (double) subsequence.length);
            ptt = ptt * (Math.log(ptt) / Math.log(2));

            xaxis[rindex] = rindex + 1;
            enresults[rindex] = -1 * (paa + pac + pag + pat+pca + pcc + pcg + pct+pga + pgc + pgg + pgt+pta + ptc + ptg + ptt);
            rindex++;

            if (rindex % 100 == 0) {
                //update progress bar
                pBar.setValue(rindex);
            //    pBar.update(pBar.getGraphics());
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
        newplot.doplot(sequence,xaxis, enresults, "Entropy results", "Window Number", "Entropy H2(x)", winsize, increament);
    }
}
