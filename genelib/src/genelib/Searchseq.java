/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.Position;

/**
 *
 * @author jnu
 */
public class Searchseq extends Thread {

    ImageIcon img = new ImageIcon("images/icons/orislogo.png");
    final char[] sequence;
    char[] subseq;
    int tolerance;
    int printlogo;
    int showstat;
    int excatmatches;
    JFrame frame;
    private JProgressBar pBar = new JProgressBar();
    JLabel found = new JLabel();
    List<Integer> positions = new ArrayList();
    //matchpositions is for creating array contating position to be highleighed in main form through search
    List<Integer> matchpositions = new ArrayList();
    //position array is used by the main form to highlight search results!!!
    public int[] posarray;
    int saveflag = 0;
    String savefile;
    boolean suppressmessage = false;
    public int total = 0;

    //to compute pval urmi 25/12/17
    ComputePvals obj = null;
    //lists to store pvals and motifs
    List<String> motifs;
    List<Double> pvals;

    public Searchseq(char[] val, char[] tosearch, int tval, int lstatus, int sstatus, int save, String filename, boolean supressmessage) {
        sequence = val;
        suppressmessage = supressmessage;
        subseq = tosearch;
        tolerance = tval;
        //show logo if printlogo is 1
        printlogo = lstatus;
        //show stat if shostat is 1
        showstat = sstatus;
        //save file if file name..
        if (save == 1) {
            savefile = filename;
            saveflag = 1;
            //init object to find pvals
            //ComputePvals(sequence, order)
            obj = new ComputePvals(sequence, 2);
            motifs = new ArrayList();
            pvals = new ArrayList();
        }

    }

    @Override
    public void run() {

        int i, j, k, flag = 0, pos = 0;
        int mismatch = 0;
        excatmatches = 0;

        if (sequence == null) {
            JOptionPane.showMessageDialog(null, "Fatal Error No file Read");
            return;
        }

        if (suppressmessage == false) {
            //for progress bar
            frame = new JFrame("Progress");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int width = (int) screenSize.getWidth();
            int height = (int) screenSize.getHeight();
            pBar.setMinimum(0);
            pBar.setMaximum(sequence.length);
            pBar.setStringPainted(true);
            found.setText(Integer.toString(total));
//        found.setHorizontalAlignment(JLabel.LEFT);
            //      found.setVerticalAlignment(JLabel.BOTTOM);
            Container content = frame.getContentPane();
            // content.setBackground(Color.white);
            content.setLayout(new GridLayout());
            content.add(new JLabel("Pattern Matched :"));
            content.add(found);
            pBar.setAlignmentY(JProgressBar.BOTTOM_ALIGNMENT);
            //pBar.setVerticalAlignment(JLabel.BOTTOM);
            content.add(pBar);
            frame.setIconImage(img.getImage());
            frame.setSize(400, 100);
            //frame.pack();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            //set the frame in the middle of screen
            frame.setLocation((width / 2), (height / 2));
            frame.setResizable(false);
            frame.setVisible(true);
            frame.repaint();
        }
        //for logo
        int tpos = subseq.length;

        //if save file is required
        BufferedWriter writer = null;
        String tmpindex;
        if (saveflag == 1) {
            savefile = savefile + ".txt";
            File f = new File(savefile);
            try {
                writer = new BufferedWriter(new FileWriter(f));
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                writer.write("Search for the String ");
                for (i = 0; i < subseq.length; i++) {
                    writer.write(subseq[i]);
                }
                writer.write("\tTolerance value = ");
                writer.write(String.valueOf(tolerance));
                writer.newLine();
                writer.write("StartingPosition\tMotif\tP-value ");
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        //mat is the matrix used in logo
        float[][] mat = new float[4][tpos];

        //START Search
        for (i = 0; i < sequence.length - subseq.length + 1; ++i) {
            //System.out.println(i);
            //flag = 1;
            k = 0;
            mismatch = 0;
            for (j = i; k < subseq.length; j++) {

                if ((subseq[k] == 'X') || (sequence[j] == subseq[k]) || (subseq[k] == 'Y' && (sequence[j] == 'C' || sequence[j] == 'T')) || (subseq[k] == 'R' && (sequence[j] == 'A' || sequence[j] == 'G')) || (subseq[k] == 'W' && (sequence[j] == 'A' || sequence[j] == 'T')) || (subseq[k] == 'S' && (sequence[j] == 'C' || sequence[j] == 'G')) || (subseq[k] == 'K' && (sequence[j] == 'T' || sequence[j] == 'G')) || (subseq[k] == 'M' && (sequence[j] == 'C' || sequence[j] == 'A'))) {
                    flag = 1;
                    k++;
                } else {
                    mismatch++;
                    k++;

                }
                //if mismathes excedes tolerance it is not a match
                if (mismatch > tolerance) {
                    flag = 0;
                    break;
                }

            }

            if (flag == 1) {
                try {
                    //System.out.println("Found at pos" + (i + 1));
                    if (saveflag == 1) {
                        writer.newLine();
                        //writer.write("Found at pos ");
                        tmpindex = String.valueOf(i + 1);
                        writer.write(tmpindex);
                        writer.write("\t");

                    }
                    //add 1 to positins list where 1 means a match
                    positions.add(1);
                    //matchpositions adds index of match; used in main form to highlight search results
                    matchpositions.add(i + 1);
                    int matindex = 0;
                    //urmi 25/12/17
                    String thisMotif = "";
                    for (int index = i; index < i + subseq.length; index++) {
                        //    System.out.printf("%c", sequence[index]);
                        if (saveflag == 1) {
                            //writer.write(sequence[index]);
                            thisMotif = thisMotif + sequence[index];
                        }
                        if (sequence[index] == 'A') {

                            mat[0][matindex]++;
                        }
                        if (sequence[index] == 'C') {

                            mat[1][matindex]++;
                        }
                        if (sequence[index] == 'G') {

                            mat[2][matindex]++;
                        }
                        if (sequence[index] == 'T') {

                            mat[3][matindex]++;
                        }
                        matindex++;
                    }

                    if (saveflag == 1) {
                        //write motif to file
                        writer.write(thisMotif + "\t");
                        //add motif to list and compute p val
                        //if already present find pval
                        double thisPval = 0;
                        if (motifs.indexOf(thisMotif) >= 0) {
                            //System.out.println("motif found");
                            //find p val
                            int index = 0;
                            index = motifs.indexOf(thisMotif);
                            thisPval = pvals.get(index);
                        } else {
                            //System.out.println("finding pval for motif"+thisMotif);
                            motifs.add(thisMotif);
                            //thisPval=obj.returnpval(sequence.length, thisMotif.toCharArray(), tolerance, obj.returnNumMatches(sequence, thisMotif.toCharArray(), tolerance));
                            thisPval = obj.returnTheorypval(thisMotif.toCharArray());
                            pvals.add(thisPval);
                        }

                        System.out.println(thisMotif + "\t" + thisPval);
                        writer.write(String.valueOf(thisPval));

                    }

                    //find number of exact matches
                    if (mismatch == 0) {
                        excatmatches++;
                    }
                    k = 0;
                    mismatch = 0;
                    total++;
                } catch (IOException ex) {
                    Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                //add 0 to positins list where 0 means a mismatch
                positions.add(0);

            }

            if (suppressmessage == false) {

                if (i % 55000 == 0) {
                    //update progress bar
                    pBar.setValue(i);
                    //pBar.update(pBar.getGraphics());
                    found.setText(Integer.toString(total));
                    // frame.repaint();
                }
            }

        }
        //Finnaly update progress bar
        if (suppressmessage == false) {
            pBar.setValue(i);
            pBar.update(pBar.getGraphics());
            found.setText(Integer.toString(total));
            frame.dispose();
            pBar.removeAll();
        }
        //write to file total matches
        if (saveflag == 1) {
            try {
                writer.newLine();
                writer.write("Total Matches= " + total + ", Exact Matches= " + excatmatches);
                writer.close();
                //JOptionPane.showMessageDialog(null, "File saved");
                JOptionPane.showMessageDialog(null, savefile + "File saved");
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //System.out.printf("Found %d times at", total);
        //System.out.println(positions);
        //normalize the matrix matrix
        //System.out.println("Normalized Matrix");
        for (i = 0; i < 4; i++) {
            for (j = 0; j < subseq.length; j++) {
                mat[i][j] = mat[i][j] / total;
                // System.out.printf("\t%f", mat[i][j]);
            }

        }

        if (total == 0) {
            if (suppressmessage == false) {
                JOptionPane.showMessageDialog(null, "No Matches");
            }
        }

        if (printlogo == 1 && total != 0) {
            StringBuilder str = new StringBuilder();
            for (int x = 0; x < subseq.length; x++) {
                str.append(subseq[x]);
            }
            Showlogo ob = new Showlogo(mat, subseq.length, total, excatmatches, str.toString(), String.valueOf(tolerance));
            ob.start();
        }

        //to show search stats
        if (showstat == 1 && total != 0) {

            JFrame frame1 = new JFrame("Enter parameter to view the search results");
            frame1.setSize(500, 150);
            //define a panel
            JPanel panel1 = new JPanel(new GridLayout(2, 2));
            //define panel1's component
            JLabel labelwinsize = new JLabel("Enter Window Size");
            final JTextField txtfieldwinsize = new JTextField(7);
            JLabel labelinc = new JLabel("Enter Increment");
            final JTextField txtfieldinc = new JTextField(7);
            //add components to panel1
            panel1.add(labelwinsize);
            panel1.add(txtfieldwinsize);
            panel1.add(labelinc);
            panel1.add(txtfieldinc);
            //define button panel
            JPanel buttonpanel = new JPanel(new FlowLayout());
            //define button
            JButton buttongo = new JButton("Go");
            buttonpanel.add(buttongo);
            //add panels to frame
            frame1.add(panel1);
            frame1.add(buttonpanel, new BorderLayout().SOUTH);
            frame1.setVisible(true);

            buttongo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    final int tempws, tempinc, chkflag;
                    try {
                        tempws = Integer.parseInt(txtfieldwinsize.getText());
                        tempinc = Integer.parseInt(txtfieldinc.getText());
                        Showsearchstats ob1 = new Showsearchstats(sequence, positions, sequence.length, tempws, tempinc);
                        ob1.start();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Please check the values");
                    }

                }
            });

        }

        if (showstat == 0 && printlogo == 0 && total != 0) {

            listtoarray();
        }

//        System.out.println("Excat=" + excatmatches);
    }

    //creates a postion array of matches for hilighting results in the main form
    void listtoarray() {

        posarray = new int[matchpositions.size()];
        for (int i = 0; i < matchpositions.size(); i++) {
            String s = matchpositions.get(i).toString();
            posarray[i] = Integer.parseInt(s);
        }

    }

    public int returnTotal() {
        return total;
    }
}
