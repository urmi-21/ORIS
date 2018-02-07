/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author jnu
 */
public class PredictOric extends Thread {

    char[] sequence = null;
    char[] dnaa = null;
    int[] cdslist = null;
    int[] intergeniclist = null;
    boolean gcflag;
    boolean mkflag;
    boolean ryflag;
    boolean corrflag;
    boolean enflag;
    int depth, mismatch, threshold, ftype; //ftype 0 for glimmer, 1 for .ptt

    public PredictOric(char[] seq, char[] dbox, int mm, int t, String fname, int type, int dos, boolean gc, boolean mk, boolean ry, boolean corr, boolean en) {

        sequence = seq;
        mismatch = mm;
        threshold = t - 1; //minus to check for '<' rather than '<='
        dnaa = dbox;
        gcflag = gc;
        mkflag = mk;
        ryflag = ry;
        corrflag = corr;
        enflag = en;
        depth = dos;
        //System.err.println("DOS="+depth);
        //read appropriate file
        if (type == 0) {
            System.err.println("Reading glimmer");
            cdslist = readglimmeroutput(fname);
        } else if (type == 1) {
            System.err.println("Reading ptt");
            cdslist = readptt(fname);
        }
//        for (int i=0;i<cdslist.length-1;i=i+2){
//            System.out.println(cdslist[i]+"\t"+cdslist[i+1]);
//        }
        intergeniclist = extractbetween(cdslist);
//        for(int i=0;i<intergeniclist.length;i++){
//            System.out.println(intergeniclist[i]);
//        }

    }

    public void run() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        //start cumm gcskew and find max value window
        //System.out.print(sequence.length);
        //define window size 1/100 of total
        //define inc size 1/5 of window
        int windowsize = sequence.length / 100;
        int inc = windowsize / 5;
        //Display results
        final StringBuilder resstr = new StringBuilder();
        resstr.append("Results for automated Oric Prediction\n");
        resstr.append("Window Size= " + String.valueOf(windowsize) + " Increment=" + String.valueOf(inc) + " dnaA box motif searched " + String.valueOf(dnaa) + "/" + String.valueOf(getcomplement(dnaa)) + " with no more than " + mismatch + " mismatches. Oric predicted which contains at least " + (threshold + 1) + " number of dnaA motifs");
        //list to store final oric coordinates
        List<Integer> finalresults_list = new ArrayList<Integer>();
        int[] finalres = null;// to store final results in aray

        /* Result returned by each function is a single dimentional array. Results are stored as Start pos, end pos, 
         number of dnaA matches (S,E,M). To read results increment i by two i.e to read three values at a time.
         */
        if (gcflag == true) {
            int[] gcres;
            gcres = findoricgcskew(windowsize, inc, depth);
//            for (int x : gcres) {
//                finalresults_list.add(x);
//            }

            resstr.append("\n\nResults by GC skew\n");
            if (gcres.length <= 0) {
                //resstr.append("\nNone predicted by GC skew!!! \n");
            } else {

                for (int i = 0; i < gcres.length - 2; i = i + 3) {
                    char[] tempseq = null;
                    int ctr = 0, tctr = 0;

                    //extract seq
                    tempseq = new char[gcres[i + 1] - gcres[i] + 1];
                    for (int j = gcres[i]; j <= gcres[i + 1]; j++) {
                        tempseq[tctr] = sequence[j - 1];
                        tctr++;
                    }

                    resstr.append("\n> location " + gcres[i] + "-" + gcres[i + 1] + " number of dnaA motifs found: " + gcres[i + 2] + " Total AT content: " + df.format(returnatcontent(tempseq) * 100) + " Total GC content: " + df.format((1 - returnatcontent(tempseq)) * 100) + "\n");

                    for (int j = 0; j < tempseq.length; j++) {
                        if (ctr == 70) {
                            ctr = 0;
                            resstr.append("\n");
                        }
                        resstr.append(tempseq[j]);
                        ctr++;
                    }

                }
            }

        }

        if (mkflag == true) {
            int[] mkres;
            mkres = findoricmkskew(windowsize, inc, depth);
//            for (int x : gcres) {
//                finalresults_list.add(x);
//            }

            resstr.append("\n\nResults by MK skew\n");
            if (mkres.length <= 0) {
                //resstr.append("\nNone predicted by GC skew!!! \n");
            } else {
                for (int i = 0; i < mkres.length - 2; i = i + 3) {

                    char[] tempseq = null;
                    int ctr = 0, tctr = 0;
                    tempseq = new char[mkres[i + 1] - mkres[i] + 1];
                    for (int j = mkres[i]; j <= mkres[i + 1]; j++) {
                        tempseq[tctr] = sequence[j - 1];
                        tctr++;
                    }

                    resstr.append("\n> location " + mkres[i] + "-" + mkres[i + 1] + " number of dnaA motifs found: " + mkres[i + 2]);
                    resstr.append(" Total AT content: " + df.format(returnatcontent(tempseq) * 100) + " Total GC content: " + df.format((1 - returnatcontent(tempseq)) * 100) + "\n");
                    for (int j = 0; j < tempseq.length; j++) {
                        if (ctr == 70) {
                            ctr = 0;
                            resstr.append("\n");
                        }
                        resstr.append(tempseq[j]);
                        ctr++;
                    }

                }
            }

        }

        if (ryflag == true) {
            int[] ryres;
            ryres = findoricryskew(windowsize, inc, depth);

            resstr.append("\n\nResults by RY skew\n");
            if (ryres.length <= 0) {
                //resstr.append("\nNone predicted by GC skew!!! \n");
            } else {

                for (int i = 0; i < ryres.length - 2; i = i + 3) {

                    char[] tempseq = null;
                    int ctr = 0, tctr = 0;
                    tempseq = new char[ryres[i + 1] - ryres[i] + 1];
                    for (int j = ryres[i]; j <= ryres[i + 1]; j++) {
                        tempseq[tctr] = sequence[j - 1];
                        tctr++;
                    }

                    resstr.append("\n> location " + ryres[i] + "-" + ryres[i + 1] + " number of dnaA motifs found: " + ryres[i + 2]);
                    resstr.append(" Total AT content: " + df.format(returnatcontent(tempseq) * 100) + " Total GC content: " + df.format((1 - returnatcontent(tempseq)) * 100) + "\n");
                    for (int j = 0; j < tempseq.length; j++) {
                        if (ctr == 70) {
                            ctr = 0;
                            resstr.append("\n");
                        }
                        resstr.append(tempseq[j]);
                        ctr++;
                    }

                }
            }

        }

        if (corrflag == true) {
            int[] corrres = findoriccorrelation(windowsize, inc, depth);
//            for (int x : corrres) {
//                finalresults_list.add(x);
//            }

            resstr.append("\n\nResults by Correlation Method\n");

            if (corrres.length <= 0) {
                //resstr.append("\nNone predicted by Correlation Method !!! \n");
            } else {
                for (int i = 0; i < corrres.length - 2; i = i + 3) {
                    char[] tempseq = null;
                    int ctr = 0, tctr = 0;
                    tempseq = new char[corrres[i + 1] - corrres[i] + 1];
                    for (int j = corrres[i]; j <= corrres[i + 1]; j++) {
                        tempseq[tctr] = sequence[j - 1];
                        tctr++;
                    }
                    resstr.append("\n> location " + corrres[i] + "-" + corrres[i + 1] + " number of dnaA motifs found: " + corrres[i + 2]);
                    resstr.append(" Total AT content: " + df.format(returnatcontent(tempseq) * 100) + " Total GC content: " + df.format((1 - returnatcontent(tempseq)) * 100) + "\n");
                    for (int j = 0; j < tempseq.length; j++) {
                        if (ctr == 70) {
                            ctr = 0;
                            resstr.append("\n");
                        }
                        resstr.append(tempseq[j]);
                        ctr++;
                    }

                }
            }

        }
        if (enflag == true) {
            int[] enres = findoricentropy(windowsize, inc, depth);
//            for (int x : enres) {
//                finalresults_list.add(x);
//            }

            resstr.append("\n\nResults by GC Entropy\n");
            if (enres.length <= 0) {
                // resstr.append("\nNone predicted by GC Entropy !!! \n");
            } else {
                for (int i = 0; i < enres.length - 2; i = i + 3) {
                    char[] tempseq = null;
                    int ctr = 0, tctr = 0;
                    tempseq = new char[enres[i + 1] - enres[i] + 1];
                    for (int j = enres[i]; j <= enres[i + 1]; j++) {
                        tempseq[tctr] = sequence[j - 1];
                        tctr++;
                    }
                    resstr.append("\n> location " + enres[i] + "-" + enres[i + 1] + " number of dnaA motifs found: " + enres[i + 2]);
                    resstr.append(" Total AT content: " + df.format(returnatcontent(tempseq) * 100) + " Total GC content: " + df.format((1 - returnatcontent(tempseq)) * 100) + "\n");
                    for (int j = 0; j < tempseq.length; j++) {
                        if (ctr == 70) {
                            ctr = 0;
                            resstr.append("\n");
                        }
                        resstr.append(tempseq[j]);
                        ctr++;
                    }

                }
            }

        }

        finalres = new int[finalresults_list.size()];
        finalres = convertIntegers(finalresults_list);

        //for (int x : finalres) {
        //   System.err.println(x);
        // }
        //////////////////////////////////////////////////////////////////
        //////////////Start Jframe code to display results////////////////
        /////////////////////////////////////////////////////////////////
        System.out.println(resstr.toString());
        //display results ina single frame
        JFrame seqframe = new JFrame("Sequence");
        seqframe.setSize(600, 600);
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
        seq.setText(resstr.toString());
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

                    writer.write(resstr.toString());

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
        //JFrame seqframe = new JFrame("Sequence");
        seqframe.setSize(600, 600);
        // seqframe.setResizable(false);
        seqframe.add(seqpanel);
        seqframe.add(bottompanel, new BorderLayout().SOUTH);
        seqframe.setVisible(true);
        seqframe.setTitle("Oric Prediction Results");
        seqframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public int searchdnabox(char[] seq, char[] dbox) {
        int total = 0;

        Searchseq ob = new Searchseq(seq, dbox, mismatch, 0, 0, 0, null, true);
        ob.run();
        total = ob.total;
        return total;
    }

    private char[] getcomplement(char[] seq) {

        char[] revcseq = new char[seq.length];
        int j = 0;
        //find reverse complement
        for (int i = seq.length - 1; i >= 0; --i) {
            if (seq[i] == 'X') {
                revcseq[j] = 'X';
            } else if (seq[i] == 'A') {
                revcseq[j] = 'T';
            } else if (seq[i] == 'T') {
                revcseq[j] = 'A';
            } else if (seq[i] == 'G') {
                revcseq[j] = 'C';
            } else if (seq[i] == 'C') {
                revcseq[j] = 'G';
            } else if (seq[i] == 'K') {
                revcseq[j] = 'M';
            } else if (seq[i] == 'M') {
                revcseq[j] = 'K';
            } else if (seq[i] == 'R') {
                revcseq[j] = 'Y';
            } else if (seq[i] == 'Y') {
                revcseq[j] = 'R';
            } else if (seq[i] == 'W') {
                revcseq[j] = 'S';
            } else if (seq[i] == 'S') {
                revcseq[j] = 'W';
            }

            j++;
        }

        return revcseq;
    }

    public int[] extractbetween(int[] cdsl) {

        //int[] templ=new int[10];
//        for(int i=0;i<templ.length;i++){
//            templ[i]=(i*4)+5;
//            System.out.println("**"+templ[i]);
//        }
        int[] list = new int[cdsl.length - 2];
        int ctr = 0;
        for (int i = 1; i < cdsl.length - 1; i = i + 2) {

            //System.out.print(cdsl[i]+1+"-"+(cdsl[i+1]-1)+"\n");
            //System.out.print(cdsl[i] + 1 + "-" + (cdsl[i + 1] - 1) + "\n");
            list[ctr] = cdsl[i] + 1;
            ctr++;
            list[ctr] = cdsl[i + 1] - 1;
            ctr++;

        }

        return list;
    }

    //function to find and return index of max value
    private int findmax(double[] data) {
        int res = 0;
        double maxval = data[0];
        for (int i = 0; i < data.length; i++) {
            if (data[i] > maxval) {
                maxval = data[i];
                //plus 1 for as in graph values start from 1
                res = i + 1;
            }
        }
        return res;
    }

    //function to fin n max numbers and return their index
    private int[] findnmax(double[] data, int x) {
        int[] res = new int[x];
        int[] indexarray = new int[data.length];
        //init index array
        for (int i = 0; i < indexarray.length; i++) {
            indexarray[i] = i + 1;
        }

        //sort data and index together
        int n = data.length;
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (data[i] > data[k]) {
                    double tempdata = data[i];
                    data[i] = data[k];
                    data[k] = tempdata;

                    int tempindex = indexarray[i];
                    indexarray[i] = indexarray[k];
                    indexarray[k] = tempindex;
                }
            }

        }

        //store last n index in res
        for (int i = 0; i < res.length; i++) {
            res[i] = indexarray[(indexarray.length - 1) - (i * 2)];
        }
        return res;
    }

    //function to fin n min numbers and return their index
    private int[] findnmin(double[] data, int dos) {

        //returns 5*x min nbd
        //checks 5 min values and returns [x-5,x,x+5]
        int x=5*dos;
        int[] res = new int[(5 + 1 + 5)*x];
        
        int[] indexarray = new int[data.length];
        //init index array
        for (int i = 0; i < indexarray.length; i++) {
            indexarray[i] = i + 1;
        }

        //sort data and index together
        int n = data.length;
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (data[i] > data[k]) {
                    double tempdata = data[i];
                    data[i] = data[k];
                    data[k] = tempdata;

                    int tempindex = indexarray[i];
                    indexarray[i] = indexarray[k];
                    indexarray[k] = tempindex;
                }
            }

        }

        //store first n index in res
        int resctr=0;
        for (int i = 0; i < 5; i++) {
            //System.out.println("Now x is" + indexarray[i]);
            for (int j = indexarray[i] - 5; j <= indexarray[i] + 5; j++) {
                if(j<0){
                    j=data.length+j;
                }
                if(j>data.length-1){
                    j=1;
                }
                res[resctr++] = j;
                //System.out.print("\n" + res[i] + "\t");
            }
            //res[i] = indexarray[i];
        }
        
        int []fres= removeduplicateres(res);
        return fres;
    }

    //function to find regions for max gc disparity
    private int[] findputativegc(double[] data, int x) {

        int[] res = new int[(2 * x) + 1];

        List<Integer> list = new ArrayList<Integer>();
        int[] nmax = findnmax(data, 3);
        res = new int[((2 * x) + 1) * 3];
        int ctr = 0;
        int startindex = 0;
        for (int j = 0; j < nmax.length; j++) {
            startindex = nmax[j] - x;
            for (int i = 0; i < res.length; i++) {
                if (startindex > data.length) {
                    startindex = 1;
                }
                //res[ctr++] = startindex;
                list.add(startindex);
                startindex++;
            }
        }

        Set<Integer> hs = new HashSet<>();
        hs.addAll(list);
        list.clear();
        list.addAll(hs);

        res = convertIntegers(list);
        Arrays.sort(res);

        return res;
    }

    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    private int[] removeduplicateres(int[] res) {
        int[] fres = null;
        List<Integer> results = new ArrayList<Integer>();

        //System.err.println("Orignal len:" + res.length);
//        for (int i = 0; i < res.length - 2; i = i + 3) {
//            System.err.println(res[i] + "  " + res[i + 1] + "  " + res[i + 2]);
//        }
        boolean rflag = false;
        for (int i = 0; i < res.length - 2; i = i + 3) {
            rflag = false;
            if (results.isEmpty()) {
                results.add(res[i]);
                results.add(res[i + 1]);
                results.add(res[i + 2]);
            } else {
                //check if it already exists inlist
                for (int j = 0; j < results.size() - 2; j = j + 3) {
                    if (results.get(j) == res[i]) {

                        rflag = true;
                        break;
                    }
                }

                if (rflag == false) {
                    results.add(res[i]);
                    results.add(res[i + 1]);
                    results.add(res[i + 2]);
                }
            }

        }

        fres = convertIntegers(results);
//        System.err.println("Finally len:" + fres.length);
//        for (int i = 0; i < fres.length - 2; i = i + 3) {
//            System.err.println(fres[i] + "  " + fres[i + 1] + "  " + fres[i + 2]);
//        }

        return fres;
    }

    private int[] findoricentropy(int windowsize, int inc, int depth) {

        //call entropy object
        Shentropyo1 ob2 = new Shentropyo1(sequence, windowsize, inc, 0, null, 1);
        ob2.run();
        double[] endata = ob2.entropydata;
        List<Integer> results = new ArrayList<Integer>();
        //Store res in a string first and then in int array

        //char[] putativeseq;
        int[] res = null;
        int[] maxwin = findnmin(endata, 5 * depth);
//        System.out.println("Putative wins: "+maxwin.length);
//        for (int x : maxwin) {
//            System.out.println(x);
//        }

        for (int p = 0; p < maxwin.length; p++) {

            int startpos = inc * (maxwin[p] - 1);
            int endpos = startpos + windowsize;
            //System.out.println("S=" + startpos + " E=" + endpos);

            //extract all intergenic regions and search for DNAAboxes
            // count number of intergenic regions in putative
            int total = 0;
            int[] candidatelist = null;
            //cctr candidate list index counter
            int cctr = 0;
            for (int i = 0; i < intergeniclist.length - 1; i = i + 2) {

                // System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                if (startpos <= intergeniclist[i] && intergeniclist[i] <= endpos) {
                    total++;
                } else if (startpos <= intergeniclist[i + 1] && intergeniclist[i + 1] <= endpos) {
                    total++;
                }

            }
            candidatelist = new int[total * 2];
            //System.out.println("Total=" + "" + total);
            //store coordinates of intergenic regions in candidatelist
            for (int i = 0; i < intergeniclist.length - 1; i = i + 2) {

                if (startpos <= intergeniclist[i] && intergeniclist[i] <= endpos) {
                    // System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                    candidatelist[cctr] = intergeniclist[i];
                    cctr++;
                    candidatelist[cctr] = intergeniclist[i + 1];
                    cctr++;
                } else if (startpos <= intergeniclist[i + 1] && intergeniclist[i + 1] <= endpos) {
                    //System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                    candidatelist[cctr] = intergeniclist[i];
                    cctr++;
                    candidatelist[cctr] = intergeniclist[i + 1];
                    cctr++;
                }
            }

            //sort list
            for (int i = 0; i < candidatelist.length - 1; i = i + 2) {

                if (candidatelist[i] > candidatelist[i + 1]) {
                    int temp = candidatelist[i];
                    candidatelist[i] = candidatelist[i + 1];
                    candidatelist[i + 1] = temp;
                }

            }

            //for each coordinate in candidatelist, search dnabox motifs and store results
            char[] tempseq = null;
            int seq_strt, seq_end, tcount;
            int[] searchres = new int[candidatelist.length / 2];
            int sctr = 0;
            for (int i = 0; i < candidatelist.length - 1; i = i + 2) {

                seq_strt = candidatelist[i];
                seq_end = candidatelist[i + 1];
                int len = seq_end - seq_strt + 1;
                //System.out.print(seq_strt + "-" + seq_end+"\t");
                tempseq = new char[len];
           // System.out.println("Length= " + tempseq.length);

                //extract temp seq
                for (int j = 0; j < tempseq.length; j++) {
                    tempseq[j] = sequence[seq_strt + j];
                }
                tcount = searchdnabox(tempseq, dnaa) + searchdnabox(tempseq, getcomplement(dnaa));
                searchres[sctr++] = tcount;
                //System.out.print(tcount+"\n");
            }

            int finalindex = 0;
            //int threshhold = 2;
            int oriflag = 0;
            for (int i = 0; i < searchres.length; i++) {
                //System.out.println(searchres[i]);
                if (searchres[i] > threshold) {
                    //maxmatches=searchres[i];
                    finalindex = i;
                    oriflag = 1;
                    //System.out.println("maxwin=" + maxwin[p] + " OriC Coordinates found by Entropy:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                    if (results.isEmpty()) {
                        //System.out.println("111maxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                        results.add(candidatelist[(2 * finalindex)]);
                        results.add(candidatelist[(2 * finalindex) + 1]);
                        results.add(searchres[i]);
                    }
                    if (!results.isEmpty()) {
                        if (results.get(results.size() - 3) != candidatelist[(2 * finalindex)]) {
                            //System.out.println("hhhmaxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                            results.add(candidatelist[(2 * finalindex)]);
                            results.add(candidatelist[(2 * finalindex) + 1]);
                            results.add(searchres[i]);
                        }
                    }
                }

            }
            if (oriflag == 0) {
                //System.out.println("No Origin could be found by Entropy");
            }
        }

        res = new int[results.size()];
        res = convertIntegers(results);

        //remove duplicate
        res = removeduplicateres(res);
        return res;
    }

    private int[] findoricryskew(int windowsize, int inc, int d) {
        int[] res = null;
        //start cumm GC
        CummRYskew ob2 = new CummRYskew(sequence, windowsize, inc, 0, null);
        ob2.run();
        //list to store res
        List<Integer> results = new ArrayList<Integer>();

        //extract putative seq
        //find windows with min values
        int[] minwin = findputativegc(ob2.cgcdata, d * 7);
//        System.out.println("Putative wins:");
//        for (int x : maxwin) {
//            System.err.println(x + " ");
//        }
        for (int p = 0; p < minwin.length; p++) {

            int startpos = inc * (minwin[p] - 1);
            int endpos = startpos + windowsize;
            //System.out.println("S=" + startpos + " E=" + endpos);

            //extract all intergenic regions and search for DNAAboxes
            // count number of intergenic regions in putative
            int total = 0;
            int[] candidatelist = null;
            //cctr candidate list index counter
            int cctr = 0;
            for (int i = 0; i < intergeniclist.length - 1; i = i + 2) {

                // System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                if (startpos <= intergeniclist[i] && intergeniclist[i] <= endpos) {
                    total++;
                } else if (startpos <= intergeniclist[i + 1] && intergeniclist[i + 1] <= endpos) {
                    total++;
                }

            }
            candidatelist = new int[total * 2];
            //System.out.println("Total=" + "" + total);
            //store coordinates of intergenic regions in candidatelist
            for (int i = 0; i < intergeniclist.length - 1; i = i + 2) {

                if (startpos <= intergeniclist[i] && intergeniclist[i] <= endpos) {
                    // System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                    candidatelist[cctr] = intergeniclist[i];
                    cctr++;
                    candidatelist[cctr] = intergeniclist[i + 1];
                    cctr++;
                } else if (startpos <= intergeniclist[i + 1] && intergeniclist[i + 1] <= endpos) {
                    //System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                    candidatelist[cctr] = intergeniclist[i];
                    cctr++;
                    candidatelist[cctr] = intergeniclist[i + 1];
                    cctr++;
                }
            }

            //sort list
            for (int i = 0; i < candidatelist.length - 1; i = i + 2) {

                if (candidatelist[i] > candidatelist[i + 1]) {
                    int temp = candidatelist[i];
                    candidatelist[i] = candidatelist[i + 1];
                    candidatelist[i + 1] = temp;
                }

            }

            //for each coordinate in candidatelist, search dnabox motifs and store results
            char[] tempseq = null;
            int seq_strt, seq_end, tcount;
            int[] searchres = new int[candidatelist.length / 2];
            int sctr = 0;
            for (int i = 0; i < candidatelist.length - 1; i = i + 2) {

                seq_strt = candidatelist[i];
                seq_end = candidatelist[i + 1];
                int len = seq_end - seq_strt + 1;
                //System.out.print(seq_strt + "-" + seq_end+"\t");
                tempseq = new char[len];
           // System.out.println("Length= " + tempseq.length);

                //extract temp seq
                for (int j = 0; j < tempseq.length; j++) {
                    tempseq[j] = sequence[seq_strt + j];
                }
                tcount = searchdnabox(tempseq, dnaa) + searchdnabox(tempseq, getcomplement(dnaa));
                searchres[sctr++] = tcount;
                //System.out.print(tcount+"\n");
            }

            int finalindex = 0;
            //int threshhold = 2;
            int oriflag = 0;
            for (int i = 0; i < searchres.length; i++) {
                //System.out.println(searchres[i]);
                if (searchres[i] > threshold) {
                    //maxmatches=searchres[i];
                    finalindex = i;
                    oriflag = 1;
                    //System.out.println("maxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                    if (results.isEmpty()) {
                        //System.out.println("111maxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                        results.add(candidatelist[(2 * finalindex)]);
                        results.add(candidatelist[(2 * finalindex) + 1]);
                        results.add(searchres[i]);
                    }
                    if (!results.isEmpty()) {
                        if (results.get(results.size() - 3) != candidatelist[(2 * finalindex)]) {
                            //System.out.println("hhhmaxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                            results.add(candidatelist[(2 * finalindex)]);
                            results.add(candidatelist[(2 * finalindex) + 1]);
                            results.add(searchres[i]);
                        }
                    }

                }

            }
            if (oriflag == 0) {
                //System.out.println("No Origin could be found by Cumulative GC skew");
            }
        }

        //System.out.println(resstr.toString());
        res = new int[results.size()];
        res = convertIntegers(results);

//        for (int x : res) {
//            System.err.println(x);
//        }
//        System.err.println("THE END");
        //remove duplicate
        res = removeduplicateres(res);
        return res;
    }

    private int[] findoricgcskew(int windowsize, int inc, int d) {
        int[] res = null;
        //start cumm GC
        CummGCskew ob2 = new CummGCskew(sequence, windowsize, inc, 0, null);
        ob2.run();
        //list to store res
        List<Integer> results = new ArrayList<Integer>();
        //System.out.println("max val at" + ob2.maxwin);
        //char[] putativeseq = null;
        //extract putative seq
        //int maxwin = ob2.maxwin;
        //int[] maxwin = findnmax(ob2.cgcdata, 10);
        int[] maxwin = findputativegc(ob2.cgcdata, d * 7);
//        System.out.println("Putative wins:");
//        for (int x : maxwin) {
//            System.err.println(x + " ");
//        }
        for (int p = 0; p < maxwin.length; p++) {

            int startpos = inc * (maxwin[p] - 1);
            int endpos = startpos + windowsize;
            //System.out.println("S=" + startpos + " E=" + endpos);

            //extract all intergenic regions and search for DNAAboxes
            // count number of intergenic regions in putative
            int total = 0;
            int[] candidatelist = null;
            //cctr candidate list index counter
            int cctr = 0;
            for (int i = 0; i < intergeniclist.length - 1; i = i + 2) {

                // System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                if (startpos <= intergeniclist[i] && intergeniclist[i] <= endpos) {
                    total++;
                } else if (startpos <= intergeniclist[i + 1] && intergeniclist[i + 1] <= endpos) {
                    total++;
                }

            }
            candidatelist = new int[total * 2];
            //System.out.println("Total=" + "" + total);
            //store coordinates of intergenic regions in candidatelist
            for (int i = 0; i < intergeniclist.length - 1; i = i + 2) {

                if (startpos <= intergeniclist[i] && intergeniclist[i] <= endpos) {
                    // System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                    candidatelist[cctr] = intergeniclist[i];
                    cctr++;
                    candidatelist[cctr] = intergeniclist[i + 1];
                    cctr++;
                } else if (startpos <= intergeniclist[i + 1] && intergeniclist[i + 1] <= endpos) {
                    //System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                    candidatelist[cctr] = intergeniclist[i];
                    cctr++;
                    candidatelist[cctr] = intergeniclist[i + 1];
                    cctr++;
                }
            }

            //sort list
            for (int i = 0; i < candidatelist.length - 1; i = i + 2) {

                if (candidatelist[i] > candidatelist[i + 1]) {
                    int temp = candidatelist[i];
                    candidatelist[i] = candidatelist[i + 1];
                    candidatelist[i + 1] = temp;
                }

            }

            //for each coordinate in candidatelist, search dnabox motifs and store results
            char[] tempseq = null;
            int seq_strt, seq_end, tcount;
            int[] searchres = new int[candidatelist.length / 2];
            int sctr = 0;
            for (int i = 0; i < candidatelist.length - 1; i = i + 2) {

                seq_strt = candidatelist[i];
                seq_end = candidatelist[i + 1];
                int len = seq_end - seq_strt + 1;
                //System.out.print(seq_strt + "-" + seq_end+"\t");
                tempseq = new char[len];
           // System.out.println("Length= " + tempseq.length);

                //extract temp seq
                for (int j = 0; j < tempseq.length; j++) {
                    tempseq[j] = sequence[seq_strt + j];
                }
                tcount = searchdnabox(tempseq, dnaa) + searchdnabox(tempseq, getcomplement(dnaa));
                searchres[sctr++] = tcount;
                //System.out.print(tcount+"\n");
            }

            int finalindex = 0;
            //int threshhold = 2;
            int oriflag = 0;
            for (int i = 0; i < searchres.length; i++) {
                //System.out.println(searchres[i]+"--"+threshold);
                if (searchres[i] > threshold) {
                    //maxmatches=searchres[i];
                    finalindex = i;
                    oriflag = 1;
                    //System.out.println("maxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                    if (results.isEmpty()) {
                        //System.out.println("111maxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                        results.add(candidatelist[(2 * finalindex)]);
                        results.add(candidatelist[(2 * finalindex) + 1]);
                        results.add(searchres[i]);
                        //System.out.println("init " + searchres[i] + "--" + threshold + " " + candidatelist[(2 * finalindex)]);
                    }
                    if (!results.isEmpty()) {
                        if (results.get(results.size() - 3) != candidatelist[(2 * finalindex)]) {
                            //System.out.println("hhhmaxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                            results.add(candidatelist[(2 * finalindex)]);
                            results.add(candidatelist[(2 * finalindex) + 1]);
                            results.add(searchres[i]);
                            //System.out.println("** " + searchres[i] + "--" + threshold + " " + candidatelist[(2 * finalindex)]);
                        }
                    }

                }

            }
            if (oriflag == 0) {
                //System.out.println("No Origin could be found by Cumulative GC skew");
            }
        }

        //System.out.println(resstr.toString());
        res = new int[results.size()];
        res = convertIntegers(results);

//        for (int x : res) {
//            System.err.println(x);
//        }
//        System.err.println("THE END");
        //remove duplicates
        res = removeduplicateres(res);
//        for (int x : res) {
//            System.err.println(x);
//        }
//        System.err.println("THE END");
        return res;
    }

    private int[] findoricmkskew(int windowsize, int inc, int d) {
        int[] res = null;
        //start cumm GC
        CummMKskew ob2 = new CummMKskew(sequence, windowsize, inc, 0, null);
        ob2.run();
        //list to store res
        List<Integer> results = new ArrayList<Integer>();
        //System.out.println("max val at" + ob2.maxwin);

        //extract putative seq
        //int maxwin = ob2.maxwin;
        //int[] maxwin = findnmax(ob2.cgcdata, 10);
        int[] maxwin = findputativegc(ob2.cgcdata, d * 7);
//        System.out.println("Putative wins:");
//        for (int x : maxwin) {
//            System.err.println(x + " ");
//        }

        for (int p = 0; p < maxwin.length; p++) {

            int startpos = inc * (maxwin[p] - 1);
            int endpos = startpos + windowsize;
            //System.out.println("S=" + startpos + " E=" + endpos);

            //extract all intergenic regions and search for DNAAboxes
            // count number of intergenic regions in putative
            int total = 0;
            int[] candidatelist = null;
            //cctr candidate list index counter
            int cctr = 0;
            for (int i = 0; i < intergeniclist.length - 1; i = i + 2) {

                // System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                if (startpos <= intergeniclist[i] && intergeniclist[i] <= endpos) {
                    total++;
                } else if (startpos <= intergeniclist[i + 1] && intergeniclist[i + 1] <= endpos) {
                    total++;
                }

            }
            candidatelist = new int[total * 2];
            //System.out.println("Total=" + "" + total);
            //store coordinates of intergenic regions in candidatelist
            for (int i = 0; i < intergeniclist.length - 1; i = i + 2) {

                if (startpos <= intergeniclist[i] && intergeniclist[i] <= endpos) {
                    // System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                    candidatelist[cctr] = intergeniclist[i];
                    cctr++;
                    candidatelist[cctr] = intergeniclist[i + 1];
                    cctr++;
                } else if (startpos <= intergeniclist[i + 1] && intergeniclist[i + 1] <= endpos) {
                    //System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                    candidatelist[cctr] = intergeniclist[i];
                    cctr++;
                    candidatelist[cctr] = intergeniclist[i + 1];
                    cctr++;
                }
            }

            //sort list
            for (int i = 0; i < candidatelist.length - 1; i = i + 2) {

                if (candidatelist[i] > candidatelist[i + 1]) {
                    int temp = candidatelist[i];
                    candidatelist[i] = candidatelist[i + 1];
                    candidatelist[i + 1] = temp;
                }

            }

            //for each coordinate in candidatelist, search dnabox motifs and store results
            char[] tempseq = null;
            int seq_strt, seq_end, tcount;
            int[] searchres = new int[candidatelist.length / 2];
            int sctr = 0;
            for (int i = 0; i < candidatelist.length - 1; i = i + 2) {

                seq_strt = candidatelist[i];
                seq_end = candidatelist[i + 1];
                int len = seq_end - seq_strt + 1;
                //System.out.print(seq_strt + "-" + seq_end+"\t");
                tempseq = new char[len];
           // System.out.println("Length= " + tempseq.length);

                //extract temp seq
                for (int j = 0; j < tempseq.length; j++) {
                    tempseq[j] = sequence[seq_strt + j];
                }
                tcount = searchdnabox(tempseq, dnaa) + searchdnabox(tempseq, getcomplement(dnaa));
                searchres[sctr++] = tcount;
                //System.out.print(tcount+"\n");
            }

            int finalindex = 0;
            //int threshhold = 2;
            int oriflag = 0;
            for (int i = 0; i < searchres.length; i++) {
                //System.out.println(searchres[i]);
                if (searchres[i] > threshold) {
                    //maxmatches=searchres[i];
                    finalindex = i;
                    oriflag = 1;
                    //System.out.println("maxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                    if (results.isEmpty()) {
                        //System.out.println("111maxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                        results.add(candidatelist[(2 * finalindex)]);
                        results.add(candidatelist[(2 * finalindex) + 1]);
                        results.add(searchres[i]);
                    }
                    if (!results.isEmpty()) {
                        if (results.get(results.size() - 3) != candidatelist[(2 * finalindex)]) {
                            //System.out.println("hhhmaxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                            results.add(candidatelist[(2 * finalindex)]);
                            results.add(candidatelist[(2 * finalindex) + 1]);
                            results.add(searchres[i]);
                        }
                    }

                }

            }
            if (oriflag == 0) {
                //System.out.println("No Origin could be found by Cumulative GC skew");
            }
        }

        //System.out.println(resstr.toString());
        res = new int[results.size()];
        res = convertIntegers(results);

//        for (int x : res) {
//            System.err.println(x);
//        }
//        System.err.println("THE END");
        //remove duplicate
        res = removeduplicateres(res);
        return res;
    }

    private int[] findoriccorrelation(int windowsize, int inc, int d) {

        Correlationmultithreaded cob = new Correlationmultithreaded(sequence, windowsize, inc, windowsize / 10, 0, 0, null, 'H', 'G');
        cob.run();
        double[] corrdata = cob.corrdata;
        List<Integer> results = new ArrayList<Integer>();
        int[] res = null;
        int[] putativewindows = null;
        //find putative window using sharp transition in correlation data
        putativewindows = findpeaks(corrdata, 2.5);
        if (putativewindows == null) {
            System.err.println("No transitions found in correlation");
            return null;
        } else {
            //print putative windows
//            for (int i = 0; i < putativewindows.length; i++) {
//                System.out.println(putativewindows[i]);
//            }
        }

        //for each putative window do
        for (int p = 0; p < putativewindows.length; p++) {

            //start with putative
            //char[] putativeseq = null;
            int winnum = putativewindows[p];
            //System.err.println("Current putative window:" + winnum);

            int startpos = inc * (winnum - 1);
            int endpos = startpos + windowsize;
            //System.out.println("S=" + startpos + " E=" + endpos);

            //extract all intergenic regions and search for DNAAboxes
            // count number of intergenic regions in putative
            int total = 0;
            int[] candidatelist = null;
            int cctr = 0;
            for (int i = 0; i < intergeniclist.length - 1; i = i + 2) {

                // System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                if (startpos <= intergeniclist[i] && intergeniclist[i] <= endpos) {
                    total++;
                } else if (startpos <= intergeniclist[i + 1] && intergeniclist[i + 1] <= endpos) {
                    total++;
                }

            }
            candidatelist = new int[total * 2];
            //System.out.println("Total=" + "" + total);
            //store coordinates in candilist
            for (int i = 0; i < intergeniclist.length - 1; i = i + 2) {

                if (startpos <= intergeniclist[i] && intergeniclist[i] <= endpos) {
                    //System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                    candidatelist[cctr] = intergeniclist[i];
                    cctr++;
                    candidatelist[cctr] = intergeniclist[i + 1];
                    cctr++;
                } else if (startpos <= intergeniclist[i + 1] && intergeniclist[i + 1] <= endpos) {
                    System.out.print(intergeniclist[i] + "-" + intergeniclist[i + 1] + "\n");
                    candidatelist[cctr] = intergeniclist[i];
                    cctr++;
                    candidatelist[cctr] = intergeniclist[i + 1];
                    cctr++;
                }
            }

            //sort list
            for (int i = 0; i < candidatelist.length - 1; i = i + 2) {

                if (candidatelist[i] > candidatelist[i + 1]) {
                    int temp = candidatelist[i];
                    candidatelist[i] = candidatelist[i + 1];
                    candidatelist[i + 1] = temp;
                }

            }

            //for each coordinate in candidatelist, search dnabox motifs and store results
            char[] tempseq = null;
            int seq_strt, seq_end, tcount;
            int[] searchres = new int[candidatelist.length / 2];
            int sctr = 0;
            for (int i = 0; i < candidatelist.length - 1; i = i + 2) {

                seq_strt = candidatelist[i];
                seq_end = candidatelist[i + 1];
                int len = seq_end - seq_strt + 1;
                //System.out.print(seq_strt + "-" + seq_end+"\t");
                tempseq = new char[len];
           // System.out.println("Length= " + tempseq.length);

                //extract temp seq
                for (int j = 0; j < tempseq.length; j++) {
                    tempseq[j] = sequence[seq_strt + j];
                }
                tcount = searchdnabox(tempseq, dnaa) + searchdnabox(tempseq, getcomplement(dnaa));
                searchres[sctr++] = tcount;
                //System.out.print(tcount+"\n");
            }

            int finalindex = 0;
            //int threshhold = 2;
            int oriflag = 0;
            for (int i = 0; i < searchres.length; i++) {
                //System.out.println(searchres[i]);
                if (searchres[i] > threshold) {
                    //maxmatches=searchres[i];
                    finalindex = i;
                    oriflag = 1;
                    //System.out.println("Oric found by correlation; putative window num:" + winnum + "OriC Coordinates:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes" + searchres[i]);
                    if (results.isEmpty()) {
                        //System.out.println("111maxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                        results.add(candidatelist[(2 * finalindex)]);
                        results.add(candidatelist[(2 * finalindex) + 1]);
                        results.add(searchres[i]);
                    }
                    if (!results.isEmpty()) {
                        if (results.get(results.size() - 3) != candidatelist[(2 * finalindex)]) {
                            //System.out.println("hhhmaxwin=" + maxwin[p] + " OriC Coordinates found by Cumulative GC skew:" + candidatelist[(2 * finalindex)] + "-" + candidatelist[(2 * finalindex) + 1] + "dnaA boxes: " + searchres[i]);
                            results.add(candidatelist[(2 * finalindex)]);
                            results.add(candidatelist[(2 * finalindex) + 1]);
                            results.add(searchres[i]);
                        }
                    }
                }

            }
            if (oriflag == 0) {
                //System.out.println("No Origin could be found by Correlation Method");
            }

        }
        res = new int[results.size()];
        res = convertIntegers(results);
        //remove duplicate
        res = removeduplicateres(res);
        return res;

    }

    //find if there are sharp transitions then return array contating the position ie window number
    int[] findpeaks(double[] data, double delta) {
        int[] pos = null;
        int posctr = 0;
        double[] derivative = new double[data.length - 1];
        double sum = 0, mean = 0, mean_sq = 0, stddev = 0;

        //find derivative of data
        for (int i = 0; i < derivative.length; i++) {
            derivative[i] = data[i] - data[i + 1];
        }
        //find mean and stddev of derivative array
        for (int i = 0; i < derivative.length; i++) {
            sum += derivative[i];
        }
        mean = sum / derivative.length;
        sum = 0;
        for (int i = 0; i < derivative.length; i++) {
            sum += (derivative[i] * derivative[i]);
        }
        mean_sq = sum / derivative.length;
        stddev = Math.sqrt(mean_sq - (mean * mean));

        //subtract mean from derivative array
        for (int i = 0; i < derivative.length; i++) {
            derivative[i] = derivative[i] - mean;
        }

        for (int i = 0; i < derivative.length; i++) {
            if (derivative[i] > delta * stddev || derivative[i] < -delta * stddev) {
                posctr++;
            }
        }

        //add positions to pos array
        if (posctr > 0 && posctr < 0.15 * data.length) {
            pos = new int[posctr];
            posctr = 0;
            for (int i = 0; i < derivative.length; i++) {
                if (derivative[i] > delta * stddev || derivative[i] < -delta * stddev) {
                    pos[posctr++] = i;
                }
            }

            // System.out.println("Total elemrnts in pos=" + pos.length);
            return pos;
        } else {
            System.out.print("No oric found by correlation");
            return null;
        }

    }

    private int[] readglimmeroutput(String filepath) {
        int[] list = null;
        String fileinfo = null;
        String filedata;
        int numlines = 0;
        int f = 0;
        BufferedReader br = null;
        BufferedReader cr = null;
        /*stringBuilderdisp for storing data with new lines to display and stringBuilder
         seq for ignoring newlines and getting only sequence chars*/
        StringBuilder stringBuilderdisp = new StringBuilder();
        StringBuilder stringBuilderseq = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader(filepath));
            while ((sCurrentLine = br.readLine()) != null) {
                if (f == 0 && sCurrentLine.contains(">")) {
                    fileinfo = sCurrentLine;
                    f = 1;
                } else {
                    // System.out.println("line: "+sCurrentLine);
                    numlines++;
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR File not found");
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ERROR File not found");
                //ex.printStackTrace();
                return null;
            }
        }

        //System.out.println("Total lines=" + numlines);
        list = new int[numlines * 2];
        int listctr = 0;
        f = 0;
        //read coordinates
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filepath));
            while ((sCurrentLine = br.readLine()) != null) {
                if (f == 0 && sCurrentLine.contains(">")) {
                    fileinfo = sCurrentLine;
                    f = 1;
                    // System.out.println("****line: "+sCurrentLine);
                } else {

                    // System.out.println("line: "+sCurrentLine);
                    int ctr = 0;
                    for (String eachSplit : sCurrentLine.split(" ")) {

                        if (ctr >= 2) {
                            break;
                        }

                        if (!eachSplit.isEmpty()) {

                            if (!eachSplit.contains("orf")) {
                                // System.out.print("**" + eachSplit);
                                try {
                                    list[listctr++] = Integer.parseInt(eachSplit);
                                } catch (NumberFormatException e) {
                                    JOptionPane.showMessageDialog(null, "ERROR in annotation file");
                                    e.printStackTrace();
                                    return null;

                                }
                                ctr++;
                            }
                        }

                    }

                }

            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR File not found");
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ERROR File not found");
                //ex.printStackTrace();
                return null;

            }
        }

        //System.out.println(seqstr);
        //sort list pairs
        for (int i = 0; i < list.length - 1; i = i + 2) {

            if (list[i] > list[i + 1]) {
                int temp = list[i];
                list[i] = list[i + 1];
                list[i + 1] = temp;
            }

        }
//        for(int i=0;i<list.length-1;i=i+2){
//            System.err.println(list[i]+"\t"+list[i+1]);
//        }
        //sort list in ascending
        int n = list.length;
        int k;
        for (int m = n; m >= 0; m = m - 2) {
            for (int i = 0; i < n - 2; i = i + 2) {
                k = i + 2;
                if (list[i] > list[k]) {
                    int tempdata = list[i];
                    list[i] = list[k];
                    list[k] = tempdata;
                    int tempdata2 = list[i + 1];
                    list[i + 1] = list[k + 1];
                    list[k + 1] = tempdata2;

                }
            }

        }

//        for(int i=0;i<list.length-1;i=i+2){
//            System.err.println(list[i]+"***\t"+list[i+1]);
//        }
        return list;
    }

    private int[] readptt(String filepath) {
        int[] list = null;
        String fileinfo = null;
        String filedata;
        int numlines = 0;
        int f = 0;
        BufferedReader br = null;
        BufferedReader cr = null;
        /*stringBuilderdisp for storing data with new lines to display and stringBuilder
         seq for ignoring newlines and getting only sequence chars*/
        StringBuilder stringBuilderdisp = new StringBuilder();
        StringBuilder stringBuilderseq = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader(filepath));
            while ((sCurrentLine = br.readLine()) != null) {
                if (f == 0 || sCurrentLine.contains(">")) {
                    fileinfo = sCurrentLine;
                    f = 1;
                } else if (f == 1) {
                    f = 2;
                } else if (f == 2) {
                    f = 3;
                } else {
                    // System.out.println("line: "+sCurrentLine);
                    numlines++;
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR File not found");
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ERROR File not found");
                //ex.printStackTrace();
                return null;
            }
        }

        //System.out.println("Total lines=" + numlines);
        list = new int[numlines * 2];
        int listctr = 0;
        f = 0;
        //read coordinates
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filepath));
            while ((sCurrentLine = br.readLine()) != null) {
                if (f == 0 || sCurrentLine.contains(">")) {
                    fileinfo = sCurrentLine;
                    f = 1;
                    // System.out.println("****line: "+sCurrentLine);
                } else if (f == 1) {
                    f = 2;
                } else if (f == 2) {
                    f = 3;
                } else {
                    String temp = sCurrentLine.split("\t")[0];
                    //System.err.println(temp);

                    //System.err.println(temp.split("\\.\\.")[0]);
                    list[listctr] = Integer.parseInt(temp.split("\\.\\.")[0]);
                    listctr++;
                    list[listctr] = Integer.parseInt(temp.split("\\.\\.")[1]);
                    listctr++;

                }

            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "ERROR File not found");
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "ERROR File not found");
                //ex.printStackTrace();
                return null;

            }
        }

        //System.out.println(seqstr);
        //sort list pairs
        for (int i = 0; i < list.length - 1; i = i + 2) {

            if (list[i] > list[i + 1]) {
                int temp = list[i];
                list[i] = list[i + 1];
                list[i + 1] = temp;
            }

        }
//        for(int i=0;i<list.length-1;i=i+2){
//            System.err.println(list[i]+"\t"+list[i+1]);
//        }
        //sort list in ascending
        int n = list.length;
        int k;
        for (int m = n; m >= 0; m = m - 2) {
            for (int i = 0; i < n - 2; i = i + 2) {
                k = i + 2;
                if (list[i] > list[k]) {
                    int tempdata = list[i];
                    list[i] = list[k];
                    list[k] = tempdata;
                    int tempdata2 = list[i + 1];
                    list[i + 1] = list[k + 1];
                    list[k + 1] = tempdata2;

                }
            }

        }

//        for (int i = 0; i < list.length - 1; i = i + 2) {
//            System.err.println(list[i] + "***\t" + list[i + 1]);
//        }
        return list;
    }

    private double returnatcontent(char[] seq) {
        double at = 0;

        for (int i = 0; i < seq.length; i++) {
            if (seq[i] == 'A' || seq[i] == 'T') {
                at++;
            }
        }

        return (at / seq.length);

    }
}
