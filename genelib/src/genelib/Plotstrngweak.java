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
public class Plotstrngweak extends Thread {

    ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("images/icons/orislogo.png"));
    final char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int winsize;
    int increament;
    int saveflag;
    int plots;
    int plotw;
    String filename;

    public Plotstrngweak(char[] val, int ws, int inc, int s, int w, int save, String fname) {
        sequence = val;
        winsize = ws;
        increament = inc;
        plots=s;
        plotw=w;

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
                if (plots == 1 && plotw == 0) {
                    writer.write("Data for the Composition of Strong");
                    writer.newLine();
                    writer.write("Winsize = " + String.valueOf(winsize));
                    writer.write("\tIncreament  = " + String.valueOf(increament));
                    writer.newLine();
                    writer.write("Window Number \t Amino( A or T )");
                    writer.newLine();
                } else if (plots == 0 && plotw == 1) {
                    writer.write("Data for the Composition of Weak");
                    writer.newLine();
                    writer.write("Winsize = " + String.valueOf(winsize));
                    writer.write("\tIncreament  = " + String.valueOf(increament));
                    writer.newLine();
                    writer.write("Window Number \t Keto( G or C )");
                    writer.newLine();
                } else if (plots == 1 && plotw == 1) {
                    writer.write("Data for the Composition of Strong and Weak");
                    writer.newLine();
                    writer.write("Winsize = " + String.valueOf(winsize));
                    writer.write("\tIncreament  = " + String.valueOf(increament));
                    writer.newLine();
                    writer.write("Window Number \t Strong( A or T ) \t Weak( G or C )");
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



        double[] compresultss = null;
        double[] compresultsw = null;

        if (plots == 1) {
            compresultss= new double[totlwin];
        }

        if (plotw == 1) {
            compresultsw = new double[totlwin];
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
            if (plots == 1) {
                compresultss[rindex] = sob2.returncount(subsequence, 'A') + sob2.returncount(subsequence, 'T');
            }
            if (plotw == 1) {
                compresultsw[rindex] = sob2.returncount(subsequence, 'G') + sob2.returncount(subsequence, 'C');
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
            if (plots == 1 && plotw == 0) {
                for (int i = 0; i < compresultsw.length; i++) {
                    try {

                        //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                        writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(compresultsw[i]));
                        writer.newLine();

                    } catch (IOException ex) {
                        Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
           else if (plots == 0 && plotw == 1) {
                for (int i = 0; i < compresultsw.length; i++) {
                    try {

                        
                        writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(compresultsw[i]));
                        writer.newLine();

                    } catch (IOException ex) {
                        Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            if (plots == 1 && plotw == 1) {
                for (int i = 0; i < compresultss.length; i++) {
                    try {

                        //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                        writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(compresultss[i])+"\t "+String.valueOf(compresultsw[i]));
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
        Plotcomp ob2=new Plotcomp(sequence,compresultss,compresultsw, xaxis, winsize, increament, 0,0,0,0,plots,plotw);
        ob2.plotcompositions();



    }
}

