/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.awt.Color;
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
import org.math.plot.Plot2DPanel;

/**
 *
 * @author slim
 */
public class Plotaminoketo extends Thread {

    ImageIcon img = new ImageIcon("JNU_3.jpg");
    final char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int winsize;
    int increament;
    int saveflag;
    int plotam;
    int plotke;
    String filename;

    public Plotaminoketo(char[] val, int ws, int inc, int am, int ke, int save, String fname) {
        sequence = val;
        winsize = ws;
        increament = inc;
        plotam=am;
        plotke=ke;

        if (save == 1) {
            filename = fname;
            saveflag = 1;
        }
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
                if (plotam == 1 && plotke == 0) {
                    writer.write("Data for the Composition of Aminos");
                    writer.newLine();
                    writer.write("Winsize = " + String.valueOf(winsize));
                    writer.write("\tIncreament  = " + String.valueOf(increament));
                    writer.newLine();
                    writer.write("Window Number \t Amino( A or C )");
                    writer.newLine();
                } else if (plotam == 0 && plotke == 1) {
                    writer.write("Data for the Composition of Keto");
                    writer.newLine();
                    writer.write("Winsize = " + String.valueOf(winsize));
                    writer.write("\tIncreament  = " + String.valueOf(increament));
                    writer.newLine();
                    writer.write("Window Number \t Keto( G or T )");
                    writer.newLine();
                } else if (plotam == 1 && plotke == 1) {
                    writer.write("Data for the Composition of Amino and Keto");
                    writer.newLine();
                    writer.write("Winsize = " + String.valueOf(winsize));
                    writer.write("\tIncreament  = " + String.valueOf(increament));
                    writer.newLine();
                    writer.write("Window Number \t Amino( A or C ) \t Keto( G or T )");
                    writer.newLine();
                }
            } catch (IOException ex) {
                Logger.getLogger(Plotaminoketo.class.getName()).log(Level.SEVERE, null, ex);
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



        double[] compresultsam = null;
        double[] compresultske = null;

        if (plotam == 1) {
            compresultsam = new double[totlwin];
        }

        if (plotke == 1) {
            compresultske = new double[totlwin];
        }


        double[] xaxis = new double[totlwin];
        int rindex = 0;
        char[] subsequence = new char[winsize];
        //create object of class FUNC
        Compositioncount sob2 = new Compositioncount();

        for (start = 0; start + winsize <= sequence.length; start = start + increament) {
            for (int i = 0; i < winsize; i++) {
                subsequence[i] = sequence[start + i];
            }

            xaxis[rindex] = rindex + 1;
            if (plotam == 1) {
                compresultsam[rindex] = sob2.returncount(subsequence, 'A') + sob2.returncount(subsequence, 'C');
            }
            if (plotke == 1) {
                compresultske[rindex] = sob2.returncount(subsequence, 'G') + sob2.returncount(subsequence, 'T');
            }
            rindex++;


            if (rindex % 100 == 0) {
                //update progress bar
                pBar.setValue(rindex);
           //     pBar.update(pBar.getGraphics());
            }
        }
        frame.dispose();
        //for (int i = 0; i < cgcresults.length; i++) {
        //   System.out.printf("\n%d\t%f", i + 1, cgcresults[i]);
        //}
        if (saveflag == 1) {
            if (plotam == 1 && plotke == 0) {
                for (int i = 0; i < compresultsam.length; i++) {
                    try {

                        //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                        writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(compresultsam[i]));
                        writer.newLine();

                    } catch (IOException ex) {
                        Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
           else if (plotam == 0 && plotke == 1) {
                for (int i = 0; i < compresultske.length; i++) {
                    try {

                        
                        writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(compresultske[i]));
                        writer.newLine();

                    } catch (IOException ex) {
                        Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (plotam == 1 && plotke == 1) {
                for (int i = 0; i < compresultsam.length; i++) {
                    try {

                        //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                        writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(compresultsam[i])+"\t "+String.valueOf(compresultske[i]));
                        writer.newLine();

                    } catch (IOException ex) {
                        Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

        //plot the graph
        Plotcomp ob2=new Plotcomp(sequence,compresultsam,compresultske, xaxis, winsize, increament, 0,0,plotam,plotke,0,0);
        ob2.plotcompositions();



    }
}

