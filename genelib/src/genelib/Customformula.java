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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 *Class to compute skew using custom formula entered by the user
 * @author urmi
 */
public class Customformula {

    final char[] sequence;
    String formula;
    int winsize;
    int increament;
    int saveflag;
    String filename;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();

    /**
     * 
     * @param form A string representing the formula
     * @param val DNA sequence
     * @param ws window size
     * @param inc increment size
     * @param save save flag
     * @param fname filename to save results
     */
    public Customformula(String form, char[] val, int ws, int inc, int save, String fname) {
        formula = form;
        sequence = val;
        winsize = ws;
        increament = inc;
        if (save == 1) {
            filename = fname;
            saveflag = 1;
        }
    }

    public void run() {

        if (sequence == null) {
            JOptionPane.showMessageDialog(null, "Fatal Error No file Read");
            return;
        }
        int start = 0, totlwin = 0;

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
        //set the frame in the middle of screen
        frame.setLocation((width / 2), (height / 2));
        frame.setResizable(false);
        frame.setVisible(true);


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
                writer.write("Data for the User-defined plot ");
                writer.newLine();
                writer.write("Winsize = " + String.valueOf(winsize));
                writer.write("\tIncreament  = " + String.valueOf(increament));
                writer.newLine();
                writer.write("Window Number\t "+formula);
                writer.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }


        }

        double[] results = new double[totlwin];
        double[] xaxis = new double[totlwin];
        int rindex = 0;
        char[] subsequence = new char[winsize];


        for (start = 0; start + winsize <= sequence.length; start = start + increament) {
            for (int i = 0; i < winsize; i++) {
                subsequence[i] = sequence[start + i];
            }
            xaxis[rindex] = rindex + 1;
            results[rindex++] = computeformula(subsequence);
            //update progress bar
            if (rindex % 5000 == 0) {
                pBar.setValue(rindex);
                pBar.update(pBar.getGraphics());
            }
        }
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

        ///after writing
        if (saveflag == 1) {
            try {
                JOptionPane.showMessageDialog(null, filename + "File saved");
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(ATskew.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


//        frame.dispose();

        //create plot object
        Plot newplot = new Plot();
        newplot.doplot(sequence, xaxis, results, "Results for "+formula, "Window Number", formula, winsize, increament);



    }

    //function to compute the formula
    /**
     * 
     * @param seq input DNA seq
     * @return  computed skew value
     */
    private double computeformula(char[] seq) {

        //make a a copy of the expression
        String copiedformula = formula;

        //System.out.println("starting formula is :  " + copiedformula);
        Object result = null;
        int countA = 0;
        int countG = 0;
        int countC = 0;
        int countT = 0;

        for (int i = 0; i < seq.length; i++) {
            if (seq[i] == 'A') {
                countA++;
            } else if (seq[i] == 'G') {
                countG++;
            } else if (seq[i] == 'C') {
                countC++;
            } else if (seq[i] == 'T') {
                countT++;
            }
        }

        //substitue all values of A in the formula

        while (copiedformula.contains("A")) {
            copiedformula = copiedformula.replaceFirst("A", String.valueOf(countA));
        }

        //substitue all values of T in the formula
        while (copiedformula.contains("T")) {
            copiedformula = copiedformula.replaceFirst("T", String.valueOf(countT));
        }
        //substitue all values of G in the formula
        while (copiedformula.contains("G")) {
            copiedformula = copiedformula.replaceFirst("G", String.valueOf(countG));
        }
        //substitue all values of C in the formula
        while (copiedformula.contains("C")) {
            copiedformula = copiedformula.replaceFirst("C", String.valueOf(countC));
        }

        //substitue all values of R i.e A and G in the formula
        while (copiedformula.contains("R")) {
            copiedformula = copiedformula.replaceFirst("R", String.valueOf(countA+countG));
        }
        //substitue all values of Y i.e C and T in the formula
        while (copiedformula.contains("Y")) {
            copiedformula = copiedformula.replaceFirst("Y", String.valueOf(countC+countT));
        }
        //substitue all values of M i.e A and C in the formula
        while (copiedformula.contains("M")) {
            copiedformula = copiedformula.replaceFirst("M", String.valueOf(countA+countC));
        }
        //substitue all values of K i.e G and T in the formula
        while (copiedformula.contains("K")) {
            copiedformula = copiedformula.replaceFirst("K", String.valueOf(countG+countT));
        }
         //substitue all values of X i.e A,G,C and T in the formula
        while (copiedformula.contains("X")) {
            copiedformula = copiedformula.replaceFirst("X", String.valueOf(countA+countC+countG+countT));
        }
        //System.out.println("now formula is :  " + copiedformula);

        // Get JavaScript engine
        ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
        try {
            // Evaluate the expression
            result = engine.eval(copiedformula);

           // System.out.println(copiedformula + " = " + result);
        } catch (ScriptException e) {
            // Something went wrong
            e.printStackTrace();
        }

        return Double.valueOf(result.toString());
    }
}
