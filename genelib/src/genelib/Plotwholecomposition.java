/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.*;

/**
 *
 * @author slim
 */
public class Plotwholecomposition extends Thread {

    ImageIcon img = new ImageIcon("images/icons/orislogo.png");
    final char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int winsize;
    int increament;
    int saveflag;
    String filename;

    public Plotwholecomposition(char[] val, int ws, int inc, int save, String fname) {
        sequence = val;
        winsize = ws;
        increament = inc;

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
                writer.write("Data for the Composition of All Nucleotides");
                writer.newLine();
                writer.write("Winsize = " + String.valueOf(winsize));
                writer.write("\tIncreament  = " + String.valueOf(increament));
                writer.newLine();
                writer.write("Window Number \tA \tC \tG \tT");
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



        double[] compresultsA = new double[totlwin];
        double[] compresultsG = new double[totlwin];
        double[] compresultsC = new double[totlwin];
        double[] compresultsT = new double[totlwin];
        //cgcresults[0] = 0;
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
            compresultsA[rindex] = sob2.returncount(subsequence, 'A');
            compresultsG[rindex] = sob2.returncount(subsequence, 'G');
            compresultsC[rindex] = sob2.returncount(subsequence, 'C');
            compresultsT[rindex] = sob2.returncount(subsequence, 'T');
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
            for (int i = 0; i < compresultsA.length; i++) {
                try {
                    //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                    writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(compresultsA[i]) + " \t" + String.valueOf(compresultsC[i]) + " \t" + String.valueOf(compresultsG[i]) + " \t" + String.valueOf(compresultsT[i]));
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

        //plot the graph
        plotwholecomp ob = new plotwholecomp(sequence, compresultsA, compresultsC, compresultsG, compresultsT, xaxis, winsize, increament);
        ob.plotcomp();


    }
}

class plotwholecomp {

    ImageIcon img = new ImageIcon("JNU_3.jpg");
    char[] seq;
    double[] countA;
    double[] countC;
    double[] countG;
    double[] countT;
    double[] xaxis;
    int winsize;
    int increament;

    public plotwholecomp(char[] val, double[] reA, double[] reC, double[] reG, double[] reT, double[] x, int wsize, int inc) {
        seq = val;
        countA = reA;
        countC = reC;
        countG = reG;
        countT = reT;
        xaxis = x;
        winsize = wsize;
        increament = inc;
    }

    public void plotcomp() {

        // create your PlotPanel (you can use it as a JPanel)
        Plot2DPanel plot = new Plot2DPanel();

        // define the legend position
        plot.addLegend("SOUTH");



        String plotdesc = "Whole Composition Plot with Winsize= " + String.valueOf(winsize) + ", Increament= " + String.valueOf(increament);



        // add a line plot to the PlotPanel
        plot.addLinePlot(plotdesc + "Plot of A", Color.GREEN, xaxis, countA);
        plot.addLinePlot("Plot of C", Color.BLUE, xaxis, countC);
        plot.addLinePlot("Plot of G", Color.ORANGE, xaxis, countG);
        plot.addLinePlot("Plot of T", Color.RED, xaxis, countT);




        // create a new panel for extracting sequence
        JPanel panel1 = new JPanel(new FlowLayout());
        //add componentes to panel1
        JLabel label1 = new JLabel("Enter window numbers to get the sequence");
        JLabel label2 = new JLabel("To");
        final JTextField start = new JTextField(5);
        final JTextField end = new JTextField(5);
        JButton jb = new JButton("Go");


        panel1.add(label1);
        panel1.add(start);
        panel1.add(label2);
        panel1.add(end);
        panel1.add(jb);
        panel1.setBackground(Color.red);
        //define button action to extract the sequence
        jb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                int statusflag = 1;

                if (start.getText().equals("") || end.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Error!! Please Check the values");
                    statusflag = 0;
                } else {
                    final int startingwindow = Integer.parseInt(start.getText());
                    final int endingwindow = Integer.parseInt(end.getText());

                    //check for legal values of windows
                    if (startingwindow <= 0 || endingwindow <= 0 || startingwindow > xaxis.length || endingwindow > xaxis.length || (startingwindow > endingwindow)) {
                        JOptionPane.showMessageDialog(null, "Error!! Please Check the values");
                        statusflag = 0;
                    } else if (statusflag == 1) {

                        int start = startingwindow;;
                        final StringBuilder seqstr = new StringBuilder();

                        //extract sequence and save as string
                        for (int j = 0; j <= endingwindow - startingwindow; j++) {
                            //System.out.println("start="+start);
                            seqstr.append("Window Number" + start + ": ");
                            for (int i = (start - 1) * increament; i < ((start - 1) * increament) + winsize; i++) {

                                //System.out.print(sequence[i]);
                                seqstr.append(seq[i]);

                            }
                            seqstr.append("\n\n");
                            start++;
                        }


                        JPanel seqpanel = new JPanel(new GridLayout(1, 1));
                        JPanel bottompanel = new JPanel(new FlowLayout());
                        //JLabel heading = new JLabel("Extracted Sequence from Window Numbers");
                        JTextArea seq = new JTextArea(100, 100);
                        seq.setColumns(150);
                        seq.setRows(450);
                        seq.setFont(new Font("Monospaced", Font.ROMAN_BASELINE, 14));
                        seq.setLineWrap(true);
                        seq.setWrapStyleWord(true);
                        seq.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                        JScrollPane scrollPane = new JScrollPane(seq);
                        seq.setEditable(false);
                        seq.setText(seqstr.toString());
                        seqpanel.add(scrollPane);



                        //add save button etc to bottom panel
                        JLabel savelabel = new JLabel("Enter file name to Save Data in File");
                        final JTextField filename = new JTextField(10);
                        JButton save = new JButton("Save");

                        save.addActionListener(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent event) {
                                BufferedWriter writer = null;
                                String fname = filename.getText() + ".txt";
                                File f = new File(fname);
                                try {
                                    writer = new BufferedWriter(new FileWriter(f));
                                } catch (IOException ex) {
                                    Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                try {
                                    writer.write("Extracted Sequence from window number " + startingwindow);

                                    writer.write(" till window number " + endingwindow);
                                    writer.write(" For the composition plot");
                                    writer.newLine();
                                    writer.write("Winsize = " + String.valueOf(winsize));
                                    writer.write("\tIncreament  = " + String.valueOf(increament));
                                    writer.newLine();
                                    writer.write(seqstr.toString());
                                    writer.newLine();
                                    writer.write("END");
                                    JOptionPane.showMessageDialog(null, fname + "File saved");
                                    writer.close();
                                } catch (IOException ex) {
                                    Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
                                }



                            }
                        });

                        //add to bottom panel
                        bottompanel.add(savelabel);
                        bottompanel.add(filename);
                        bottompanel.add(save);
                        bottompanel.setBackground(Color.red);


                        //add panels to frame
                        JFrame seqframe = new JFrame("Sequence");
                        seqframe.setSize(600, 600);
                        // seqframe.setResizable(false);
                        seqframe.add(seqpanel);
                        seqframe.add(bottompanel, new BorderLayout().SOUTH);
                        seqframe.setVisible(true);
                        seqframe.setTitle("Extracted Sequence");
                        seqframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                    }
                }
            }
        });


        // put the PlotPanel in a JFrame like a JPanel
        BorderLayout myLayout = new BorderLayout();
        JFrame frame = new JFrame("Whole Composition Plot");
        plot.setAxisLabels("Window Number", "Count");
        //define font for label
        int style =  Font.CENTER_BASELINE;
        int style2 = Font.BOLD;
        Font font = new Font("Garamond", style, 12);
        Font plotfont = new Font("Garamond", style2, 10);
        
        plot.getAxis(1).setLightLabelFont(plotfont);
        plot.getAxis(1).setColor(Color.BLACK);
        plot.getAxis(0).setLightLabelFont(plotfont);
        plot.getAxis(1).setLabelFont(font);
        plot.getAxis(0).setLabelFont(font);
        plot.getAxis(1).setLabelAngle(-Math.PI / 2);
        plot.getAxis(1).setLabelPosition(-0.160, 0.45);
        plot.getAxis(0).setLabelPosition(0.45, -0.10);
        
        

        frame.setSize(600, 600);
        frame.add(plot);
        frame.add(panel1, myLayout.SOUTH);
        frame.setVisible(true);
        frame.setIconImage(img.getImage());
    }
}
