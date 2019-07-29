/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
//import sun.org.mozilla.javascript.internal.ScriptRuntime;

/**
 * Class to read input genome file in fasta format and compute sequence
 * statistics
 *
 * @author urmi
 */
public class Readfile {

    static private char[] sequence;
    String fileinfo = null;
    String filedata;
    int numlines;

    /**
     * function to read a new file
     *
     * @param filepath absolute path of the fasta file
     * @return
     */
    public String newread(String filepath) {

        numlines = 0;
        BufferedReader br = null;
        BufferedReader cr = null;
        /*stringBuilderdisp for storing data with new lines to display and stringBuilder
         seq for ignoring newlines and getting only sequence chars*/
        StringBuilder stringBuilderdisp = new StringBuilder();
        StringBuilder stringBuilderseq = new StringBuilder();
        String ls = System.getProperty("line.separator");

        int f = 0;
        //read file
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(filepath));

            while ((sCurrentLine = br.readLine()) != null) {

                //check multi fasta file
                if (f == 1 && sCurrentLine.contains(">")) {
                    JOptionPane.showMessageDialog(null, "Multiple fasta headers detected in file. Please use a single-sequence fasta file", "Error", JOptionPane.ERROR_MESSAGE);
                    return null;
                }

                //use contains instead of startswith if there aren't newlines
                if (f == 0 && sCurrentLine.contains(">")) {
                    fileinfo = sCurrentLine;
                    f = 1;
                } else {
                    stringBuilderdisp.append(sCurrentLine);
                    numlines++;
                    if (!(sCurrentLine.isEmpty())) {
                        stringBuilderdisp.append(ls);
                    }

                    stringBuilderseq.append(sCurrentLine);

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

        System.out.println("Total lines=" + numlines);
        String seqstr = stringBuilderseq.toString();

        sequence = new char[seqstr.length()];
        //extra charflag to indicate that sequence contains charecter other than A,G,C,T
        boolean extracharflag = false, checkindex = false;

        for (int i = 0; i < sequence.length; i++) {
            sequence[i] = seqstr.charAt(i);
            sequence[i] = Character.toUpperCase(sequence[i]);
            if (extracharflag == false) {
                if ((sequence[i] != 'A') && (sequence[i] != 'T') && (sequence[i] != 'G') && (sequence[i] != 'C')) {//||sequence[i]!='C'||sequence[i]!='G'||sequence[i]!='T'){
                    extracharflag = true;
                    System.out.print("** " + sequence[i]);
                }
            }
        }

        if (extracharflag) {
            JOptionPane.showMessageDialog(null, "Sequence Contains Characters other than A G C T");
        }

        int index = 0, flag = 0;

        JOptionPane.showMessageDialog(null, "Read Successful");

        //return the sequence with newline properties to display
        return stringBuilderdisp.toString();

    }

    /**
     * return the DNA sequence data as char array
     *
     * @return DNA sequence
     */
    public char[] returnseq() {
        return sequence;
    }

    /**
     * rotate the circular genomic DNA by given nucleotides.
     *
     * @param index number of nucleotides to rotate
     * @param direction clockwise ot anti-clockwise
     */
    public void rotateseq(int index, int direction) {

        char[] result = new char[sequence.length];
        //backward or Anticlockwise rotation
        if (direction == 0) {
            System.arraycopy(sequence, index, result, 0, sequence.length - index);
            System.arraycopy(sequence, 0, result, sequence.length - index, index);
            System.arraycopy(result, 0, sequence, 0, sequence.length);

        } //Fwd or clockwise rotation
        else {
            System.arraycopy(sequence, sequence.length - index, result, 0, index);
            System.arraycopy(sequence, 0, result, index, sequence.length - index);
            System.arraycopy(result, 0, sequence, 0, sequence.length);

        }

        JOptionPane.showMessageDialog(
                null, "Sequence has been rotated by: " + String.valueOf(index));
    }

    /**
     * returns the length of genome
     *
     * @return
     */
    public String returnglength() {
        //System.out.println("length=" + sequence.length);
        if(sequence==null){
            return "0";
        }
        return String.valueOf(sequence.length);
    }

    /**
     * return percent of A nucleotides
     *
     * @return
     */
    public String returnpercentA() {
        
        float ctrA = 0;
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] == 'A') {
                ctrA = ctrA + 1;
            }
        }
        float percentA = (ctrA * 100) / sequence.length;
        return String.valueOf(percentA);
    }

    /**
     * return percent of G nucleotides
     *
     * @return
     */
    public String returnpercentG() {
        float ctrG = 0;
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] == 'G') {
                ctrG++;
            }
        }
        float percentG = (ctrG * 100) / sequence.length;
        return String.valueOf(percentG);
    }

    /**
     * return percent of C nucleotides
     *
     * @return
     */
    public String returnpercentC() {
        float ctrC = 0;
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] == 'C') {
                ctrC++;
            }
        }
        float percentC = (ctrC * 100) / sequence.length;
        return String.valueOf(percentC);
    }

    /**
     * return percent of T nucleotides
     *
     * @return
     */
    public String returnpercentT() {
        float ctrT = 0;
        for (int i = 0; i < sequence.length; i++) {
            if (sequence[i] == 'T') {
                ctrT++;
            }
        }
        float percentT = (ctrT * 100) / sequence.length;
        return String.valueOf(percentT);
    }

    /**
     * return genome GI from fasta header
     *
     * @return
     */
    public String returngi() {

        if (fileinfo == null) {
            return "No data Availavle";
        } else {
            char[] gichar = fileinfo.toCharArray();
            StringBuilder gi = new StringBuilder();

            int pipe1flag, pipe2flag;
            pipe1flag = pipe2flag = 0;
            for (int i = 0; i < gichar.length; i++) {

                if (pipe1flag == 1 && pipe2flag == 0) {
                    //   System.out.print(gichar[i]);
                    gi.append(gichar[i]);
                } else if (pipe2flag == 1) {
                    // System.out.print("breakin");
                    break;
                }

                if (gichar[i] == '|' && pipe1flag == 1) {
                    pipe2flag = 1;
                }

                if (gichar[i] == '|' && pipe2flag == 0) {
                    pipe1flag = 1;
                }

            }
            if (gi.length() - 1 >= 0) {
                return gi.substring(0, (gi.length()) - 1);
            } else {
                return "No data available";
            }

        }

    }

    /**
     * return genome accession from fasta header
     *
     * @return
     */
    public String returnaccession() {

        if (fileinfo == null) {
            return "No data Availavle";
        } else {
            char[] gichar = fileinfo.toCharArray();
            StringBuilder acc = new StringBuilder();

            int pipe1flag, pipe2flag, ctr;
            pipe1flag = pipe2flag = ctr = 0;
            for (int i = 0; i < gichar.length; i++) {

                //skip first two pipes
                if (gichar[i] == '|') {
                    ctr++;
                }
                if (ctr <= 2) {
                    continue;
                } else if (ctr > 2) {
                    if (pipe1flag == 1 && pipe2flag == 0) {
                        //     System.out.print(gichar[i]);
                        acc.append(gichar[i]);
                    } else if (pipe2flag == 1) {
                        //     System.out.print("breakin");
                        break;
                    }

                    if (gichar[i] == '|' && pipe1flag == 1) {
                        pipe2flag = 1;
                    }

                    if (gichar[i] == '|' && pipe2flag == 0) {
                        pipe1flag = 1;
                    }

                }

            }

            if (acc.length() - 1 >= 0) {
                return acc.substring(0, (acc.length()) - 1);
            } else {
                return "No data available";
            }

        }

    }

    /**
     * return genome info from fasta header
     *
     * @return
     */
    public String returninfo() {

        if (fileinfo == null) {
            return "No data Availavle";
        } else {
            char[] gichar = fileinfo.toCharArray();
            StringBuilder info = new StringBuilder();

            int ctr;
            ctr = 0;
            for (int i = 0; i < gichar.length; i++) {

                //skip first 4 pipes
                if (gichar[i] == '|') {
                    ctr++;
                }
                if (ctr < 4) {
                    continue;
                } else if (ctr >= 4) {
                    info.append(gichar[i]);
                }
            }
            if (info.length() - 1 >= 0) {
                return info.substring(1);
            } else {
                return fileinfo.substring(1);
            }
        }
    }
}
