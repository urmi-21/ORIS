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
 * @author slim
 */
public class Correlationwholegenome extends Thread {

    ImageIcon img = new ImageIcon("JNU_3.jpg");
    private int flag = 0;
    final char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int kmax;
    int kflag;
    int winsize;
    int increament;
    int saveflag;
    int modval;
    String filename;

    public Correlationwholegenome(char[] val, int wsize, int inc, int kval,int kmultiple, int save, String fname) {

        sequence = val;
        winsize = wsize;
        increament = inc;
        kmax = kval;
        kflag = 0;
        saveflag = save;
        filename = fname;
        modval=kmultiple;
    }

    @Override
    public void run() {
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
                writer.write("Data for the whole Correlation plot ");
                writer.newLine();
                writer.write("Winsize = " + String.valueOf(winsize));
                writer.write("\tIncreament  = " + String.valueOf(increament));
                if (kflag == 1) {
                    writer.write("\t for a particular kvalue  = " + String.valueOf(kmax));
                } else if (kflag == 0) {
                    writer.write("\t for kvalue till  = " + String.valueOf(kmax));
                }
                writer.newLine();
                writer.write("Window Number\t C(g)");
                writer.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        int start = 0, totlwin = 0;

        

        //for progress bar
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        pBar.setMinimum(0);
        pBar.setMaximum(kmax);
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
        
        
        double[] corrresults = new double[(int)kmax/modval];
        double[] xaxis = new double[(int)kmax/modval];
        System.out.println("sizeof x="+xaxis.length);
        int rindex = 0;
        char[] subsequence = new char[winsize];
        //create object of class Func

        System.out.println("totl win=" + totlwin);
        //do correlation for whole genome

        System.out.printf("\ndoing whole genome for k=%d", kmax);
        rindex = 0;
        for (int i = 0; i < kmax; i++) {

            if((i+1)%modval==0){
            Correlationk sob2 = new Correlationk(i + 1, kflag);
            xaxis[rindex] = i + 1;
            corrresults[rindex++] = sob2.dock(sequence);
            sob2 = null;
            }
            if (rindex % 10 == 0) {
                //update progress bar
                pBar.setValue(rindex);
                //pBar.update(pBar.getGraphics());
            }
        }


        //dispose progress bar
        frame.dispose();
        if (saveflag == 1) {
            for (int i = 0; i < corrresults.length; i++) {
                try {

                    writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(corrresults[i]));
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
        newplot.doplot(sequence,xaxis, corrresults, "Corelation Results for whole genome with k as multiple of "+ modval, "k", "Correlationk(Cg)", winsize, increament);



    }
}


class Correlationk{
    
    float[] cg;
    int start;
    int kvalue;

    public Correlationk(int kmax, int kflag) {

        if (kflag == 0) {
            start = 1;
            kvalue = kmax;
        } else {
            start = kmax ;
            kvalue = kmax;
        }

    }
    
    public double dock(char[] seq){
        double ck = 0;
        char[] cS = seq;
        
        int[] iS = new int[cS.length];
        for (int i = 0; i < cS.length; i++) {
            if (cS[i] == 'G') {
                iS[i] = 1;
            } else {
                iS[i] = -1;
            }
        }
        
        double sum = 0;
        

        
        for (int k = kvalue; k <= kvalue; k++) {
            for (int j = 0; j < iS.length - k; j++) {
                sum += (iS[j]) * (iS[j + k]);
            }

            ck = (float) (sum / (iS.length - k));
            

        }
        return ck;
    }
}