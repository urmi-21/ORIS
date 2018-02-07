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
public class Plotpyrpur extends Thread {

    ImageIcon img = new ImageIcon("images/icons/orislogo.png");
    final char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int winsize;
    int increament;
    int saveflag;
    int plotpyr;
    int plotpur;
    String filename;

    public Plotpyrpur(char[] val, int ws, int inc, int pyr, int pur, int save, String fname) {
        sequence = val;
        winsize = ws;
        increament = inc;
        plotpyr=pyr;
        plotpur=pur;

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
                if (plotpur == 1 && plotpyr == 0) {
                    writer.write("Data for the Composition of Pyridine");
                    writer.newLine();
                    writer.write("Winsize = " + String.valueOf(winsize));
                    writer.write("\tIncreament  = " + String.valueOf(increament));
                    writer.newLine();
                    writer.write("Window Number \t Pyridine( A or G )");
                    writer.newLine();
                } else if (plotpur == 0 && plotpyr == 1) {
                    writer.write("Data for the Composition of Pyridine");
                    writer.newLine();
                    writer.write("Winsize = " + String.valueOf(winsize));
                    writer.write("\tIncreament  = " + String.valueOf(increament));
                    writer.newLine();
                    writer.write("Window Number \t Purine( C or T )");
                    writer.newLine();
                } else if (plotpur == 1 && plotpyr == 1) {
                    writer.write("Data for the Composition of Pyridine and Purine");
                    writer.newLine();
                    writer.write("Winsize = " + String.valueOf(winsize));
                    writer.write("\tIncreament  = " + String.valueOf(increament));
                    writer.newLine();
                    writer.write("Window Number \t Pyridine( A or G ) \t Purine( C or T )");
                    writer.newLine();
                }
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



        double[] compresultsPyr = null;
        double[] compresultsPur = null;

        if (plotpyr == 1) {
            compresultsPyr = new double[totlwin];
        }

        if (plotpur == 1) {
            compresultsPur = new double[totlwin];
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
            if (plotpur == 1) {
                compresultsPur[rindex] = sob2.returncount(subsequence, 'A') + sob2.returncount(subsequence, 'G');
            }
            if (plotpyr == 1) {
                compresultsPyr[rindex] = sob2.returncount(subsequence, 'C') + sob2.returncount(subsequence, 'T');
            }
            rindex++;


            if (rindex % 100 == 0) {
                //update progress bar
                pBar.setValue(rindex);
                pBar.update(pBar.getGraphics());
            }
        }
        frame.dispose();
        //for (int i = 0; i < cgcresults.length; i++) {
        //   System.out.printf("\n%d\t%f", i + 1, cgcresults[i]);
        //}
        if (saveflag == 1) {
            if (plotpyr == 1 && plotpur == 0) {
                for (int i = 0; i < compresultsPyr.length; i++) {
                    try {

                        //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                        writer.write(String.valueOf(i + 1) + "\t\t" + String.valueOf(compresultsPyr[i]));
                        writer.newLine();

                    } catch (IOException ex) {
                        Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
           else if (plotpyr == 0 && plotpur == 1) {
                for (int i = 0; i < compresultsPyr.length; i++) {
                    try {

                        //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                        writer.write(String.valueOf(i + 1) + "\t\t" + String.valueOf(compresultsPur[i]));
                        writer.newLine();

                    } catch (IOException ex) {
                        Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (plotpyr == 1 && plotpur == 1) {
                for (int i = 0; i < compresultsPyr.length; i++) {
                    try {

                        //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                        writer.write(String.valueOf(i + 1) + "\t\t" + String.valueOf(compresultsPyr[i])+"\t\t "+String.valueOf(compresultsPur[i]));
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
        Plotcomp ob2=new Plotcomp(sequence,compresultsPyr,compresultsPur, xaxis, winsize, increament, plotpyr,plotpur,0,0,0,0);
        ob2.plotcompositions();



    }
}
