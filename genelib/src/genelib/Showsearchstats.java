/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import org.math.plot.Plot2DPanel;

/**
 *
 * @author slim
 */
public class Showsearchstats extends Thread {

    char[] orignalseq;
    int[] posarray;
    int totallen;
    int windowsize;
    int increment;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    ImageIcon img = new ImageIcon("images/icons/orislogo.png");
    

    //List p is binary containing 1 or 0 where i is a match and 0 is mismatch in the orignal sequencea
    public Showsearchstats(char[] seq, List p, int tsize, int wsize, int inc) {
        orignalseq = seq;
        windowsize = wsize;
        increment = inc;
        totallen = tsize;
        posarray = new int[tsize];
        int val;
        int lindex = 0;
        for (int i = 0; i < posarray.length && i < p.size(); i++) {
            String s = p.get(i).toString();
            val = Integer.parseInt(s);

            posarray[i] = val;
            //lindex++;
        }


    }

    @Override
    public void run() {

        int v = 1;
        int sum = 0;

        int totlwin = 0;
        int start = 0;
        for (start = 0; start + windowsize <= totallen; start = start + increment) {
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
        frame.setIconImage(img.getImage());
        frame.setResizable(false);
        frame.setVisible(true);


        int[] subsequence = new int[windowsize];
        double[] cgcresults = new double[totlwin];
        //cgcresults[0] = 0;
        double[] xaxis = new double[totlwin];
        int rindex = 0;

        //start calculation
        for (start = 0; start + windowsize <= posarray.length; start = start + increment) {
            for (int i = 0; i < windowsize; i++) {
                subsequence[i] = posarray[start + i];
            }

            xaxis[rindex] = rindex + 1;
            cgcresults[rindex++] = returncount(subsequence);

            if (rindex % 5000 == 0) {
                pBar.setValue(rindex);

            }

        }
        frame.dispose();

        //create plot object
        Plot newplot = new Plot();
        newplot.doplot(orignalseq, xaxis, cgcresults, "Search statics", "Window Number", "Number of Matches", windowsize, increment);


    }

    //arr contains 0 or 1 whre 1 is match and 0 is mismatch
    //simply adding in an interval will give count in that interval or window.
    int returncount(int arr[]) {
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            count += arr[i];
        }
        return count;
    }
}
