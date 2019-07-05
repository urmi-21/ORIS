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
 *Class for Jenson-Shannon divergence
 * @author urmi
 */
public class jensonshannon extends Thread {

    ImageIcon img = new ImageIcon("images/icons/orislogo.png");
    private int flag = 0;
    char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int increament;
    int saveflag;
    String filename;

     /**
     * 
     * @param seq DNA sequence
     * @param inc increment size
     * @param save save flag
     * @param fname filename to save results
     */
    public jensonshannon(char[] seq, int inc, int save, String fname) {
        sequence = seq;
        increament = inc;
        filename = fname;
        saveflag = save;
    }

    public void run() {

        int start = 0, totlval = 0, len = sequence.length, partition = 0;
        double wholeentropy = 0;

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
                writer.write("Data for the jensonshannon Entropy plot ");
                writer.newLine();
                writer.write("\tIncreament  = " + String.valueOf(increament));
                writer.newLine();
                writer.write("Window Number\t Entropy H(x) ");
                writer.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //get size to store all the results from each window
        for (start = 1; start < sequence.length; start = start + increament) {
            totlval++;
        }
        System.out.println("Total vals=" + totlval);

        //for progress bar
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        pBar.setMinimum(0);
        pBar.setMaximum(totlval);
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

        int countA, countC, countG, countT, countK, countM;
        double pa1, pc1, pg1, pt1, pk1, pm1, entropy1;
        double pa2, pc2, pg2, pt2, pk2, pm2, entropy2;
        int subseqpos = 0;
        Compositioncount sob2 = new Compositioncount();
        double[] enresults = new double[totlval];
        double[] xaxis = new double[totlval];
        int rindex = 0;

        //calculate whole genome entropy
        // wholegenomeshannon ob = new wholegenomeshannon(sequence);
        //wholeentropy = ob.returnentropy();
        countA = sob2.returncount(sequence, 'A');
        countC = sob2.returncount(sequence, 'C');
        countG = sob2.returncount(sequence, 'G');
        countT = sob2.returncount(sequence, 'T');
        countM = countA + countC;
        countK = countG + countT;
        System.out.println("count K="+countK);
        System.out.println("count M="+countM);
        System.out.println("Total="+(countM+countK)+"diff="+(sequence.length-(countM+countK)));
        pk1 = ((double) countK / (double) sequence.length);
        pk1 = pk1 * (Math.log(pk1) / Math.log(2));
        pm1 = ((double) countM / (double) sequence.length);
        pm1 = pm1 * (Math.log(pm1) / Math.log(2));
        wholeentropy=-1 * (pm1 + pk1);
        System.out.println("entropyfull=" + wholeentropy);
        //start calculation

        while (partition < sequence.length) {
            subseqpos = 0;
           
            char[] subsequence1 = new char[partition + 1];
            char[] subsequence2 = new char[(sequence.length) - partition];
         //   System.out.println("len part 1=" + subsequence1.length);
          //  System.out.println("len part 2=" + subsequence2.length);

            //calculate for 1st partition
            for (int i = 0; i <= partition; i++) {
                subsequence1[i] = sequence[i];
            }
            countA = sob2.returncount(subsequence1, 'A');
            countC = sob2.returncount(subsequence1, 'C');
            countG = sob2.returncount(subsequence1, 'G');
            countT = sob2.returncount(subsequence1, 'T');
            countM = countA + countC;
            countK = countG + countT;
            //System.out.println("ca=" + countA);
            //System.out.println("cg=" + countG);
            //System.out.println("cc=" + countC);
            //System.out.println("ct=" + countT);
            // calculate pilogpi
            pk1 = ((double) countK / (double) subsequence1.length);
            pk1 = pk1 * (Math.log(pk1) / Math.log(2));
            pm1 = ((double) countM / (double) subsequence1.length);
            pm1 = pm1 * (Math.log(pm1) / Math.log(2));

            pa1 = ((double) countA / (double) subsequence1.length);
            pa1 = pa1 * (Math.log(pa1) / Math.log(2));
            pa1 = ((double) countA / (double) subsequence1.length);
            pa1 = pa1 * (Math.log(pa1) / Math.log(2));
            pc1 = ((double) countC / (double) subsequence1.length);
            pc1 = pc1 * (Math.log(pc1) / Math.log(2));
            pg1 = ((double) countG / (double) subsequence1.length);
            pg1 = pg1 * (Math.log(pg1) / Math.log(2));
            pt1 = ((double) countT / (double) subsequence1.length);
            pt1 = pt1 * (Math.log(pt1) / Math.log(2));
            //entropy1 = -1 * (pa1 + pc1 + pg1 + pt1);
            entropy1 = -1 * (pm1 + pk1);


            entropy1 = ((double) (partition + 1) / (double) sequence.length) * entropy1;
            if (Double.isNaN(entropy1)) {
                entropy1 = 0;
            }
         //   System.out.println("entropy1=" + entropy1);

            //calculate for 2nd partition
            for (int i = partition; i <= sequence.length - 1; i++) {
                subsequence2[subseqpos++] = sequence[i];
            }
            countA = sob2.returncount(subsequence2, 'A');
            countC = sob2.returncount(subsequence2, 'C');
            countG = sob2.returncount(subsequence2, 'G');
            countT = sob2.returncount(subsequence2, 'T');
            countM = countA + countC;
            countK = countG + countT;
            // calculate pilogpi
            pa2 = ((double) countA / (double) subsequence2.length);
            pa2 = pa2 * (Math.log(pa2) / Math.log(2));
            pc2 = ((double) countC / (double) subsequence2.length);
            pc2 = pc2 * (Math.log(pc2) / Math.log(2));
            pg2 = ((double) countG / (double) subsequence2.length);
            pg2 = pg2 * (Math.log(pg2) / Math.log(2));
            pt2 = ((double) countT / (double) subsequence2.length);
            pt2 = pt2 * (Math.log(pt2) / Math.log(2));
            pk2 = ((double) countK / (double) subsequence2.length);
            pk2 = pk2 * (Math.log(pk2) / Math.log(2));
            pm2 = ((double) countM / (double) subsequence2.length);
            pm2 = pm2 * (Math.log(pm2) / Math.log(2));

            //entropy2 = -1 * (pa2 + pc2 + pg2 + pt2);
            entropy2 = -1 * (pm2 + pk2);

            entropy2 = (((double) sequence.length - (partition + 1)) / (double) sequence.length) * entropy2;
            if (entropy2 == Double.NaN) {
                entropy2 = 0;
            }
           // System.out.println("entropy2=" + entropy2);

            xaxis[rindex] = rindex + 1;
            enresults[rindex] = (2 * sequence.length) * (wholeentropy- entropy1 - entropy2);
            if (Double.isNaN(enresults[rindex])) {
                enresults[rindex] = 0;
            }
            rindex++;
           
            if (rindex % 100 == 0) {
                //update progress bar
                pBar.setValue(rindex);
               // pBar.update(pBar.getGraphics());
            }
            partition = partition + increament;
            subsequence1 = null;
            subsequence2 = null;
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
        newplot.doplot(sequence, xaxis, enresults, "Entropy results", "Window Number", "Entropy H(x)", partition, increament);
    }
}
