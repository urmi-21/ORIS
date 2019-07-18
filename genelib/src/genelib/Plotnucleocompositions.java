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
 * classto plot nucleotide composition
 */
public class Plotnucleocompositions extends Thread {
    
    ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("images/icons/orislogo.png"));
    final char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int winsize;
    int increament;
    int saveflag;
    char toplot;
    String filename;

    public Plotnucleocompositions(char[] val,char ch, int ws, int inc, int save, String fname) {
        sequence = val;
        winsize = ws;
        increament = inc;
        toplot=ch;
        if (save == 1) {
            filename = fname;
            saveflag = 1;
        }
    }
    
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
                writer.write("Data for the Composition of "+String.valueOf(toplot));
                writer.newLine();
                writer.write("Winsize = " + String.valueOf(winsize));
                writer.write("\tIncreament  = " + String.valueOf(increament));
                writer.newLine();
                writer.write("Window Number\t Count "+String.valueOf(toplot));
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



        double[] compresults = new double[totlwin];
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
                compresults[rindex] =sob2.returncount(subsequence,toplot);
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
            for (int i = 0; i < compresults.length; i++) {
                try {
                    //   System.out.printf("\n%d\t%f", i + 1, atresults[i]);
                    writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(compresults[i]));
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
        newplot.doplot(sequence,xaxis, compresults, "Composition of "+String.valueOf(toplot), "Window Number", "Count", winsize, increament);

    }
}


class Compositioncount{
    
char tosearch;
int ctr=0;
    public Compositioncount() {
        
    }
    
    public int returncount(char[] orgseq,char tofind){
        
        ctr=0;
        tofind=Character.toUpperCase(tofind);
        //System.out.println("to search is" + tofind);
        
        for(int i=0;i<orgseq.length;i++){
            
            if(orgseq[i]==tofind){
                ctr++;
            }
        }
       // System.out.println("ctr= " + ctr);
        return ctr;
    }

public int returncount(char[] orgseq,String tofind){
        
        ctr=0;
        char[] subseq=new char[tofind.length()];
        int k=0;
        int flag=0;
        int ctr=0;
        
        
        
        for(int i=0;i<tofind.length();i++){
            subseq[i]=(Character)tofind.charAt(i);
            
        }
        //System.out.println("to search is" + tofind);
        
        for(int i=0;i<orgseq.length-subseq.length;i++){
            k=0;
            for(int j=i;k<subseq.length;k++,j++){
                if(orgseq[j]==subseq[k]){
                    flag=1;
                }
                else{
                    flag=0;
                    break;
                }
            }
            if(flag==1){
                ctr++;
            }
        }
       // System.out.println("ctr= " + ctr);
        return ctr;
    }

}