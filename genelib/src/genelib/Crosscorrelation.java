
package genelib;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *Class to compute cross-correlation measure
 * @author slim
 */
public class Crosscorrelation extends Thread {
     ImageIcon img = new ImageIcon("images/icons/orislogo.png");
    private int flag = 0;
    final char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int kmax;
    char nuc1;
    char nuc2;
    int kflag;
    int winsize;
    int increament;
    int saveflag;
    String filename;

    /**
     * 
     * @param val DNA sequence
     * @param wsize window size
     * @param inc increment size
     * @param n1 first nucleotide
     * @param n2 second nucleotide
     * @param kval k value to compute correlation
     * @param flagval flag indicating multiple kvalues from 1...k
     * @param save save file flag
     * @param fname filename to save results
     */
    public Crosscorrelation(char[] val, int wsize, int inc,char n1,char n2, int kval, int flagval, int save, String fname) {

        sequence = val;
        winsize = wsize;
        increament = inc;
        kmax = kval;
        kflag = flagval;
        saveflag = save;
        filename = fname;
        nuc1=n1;
        nuc2=n2;
    }

    public void run() {

        long startTime = System.currentTimeMillis();
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
                writer.write("Data for the Cross-Correlation plot ");
                writer.newLine();
                writer.write("Winsize = " + String.valueOf(winsize));
                writer.write("\tIncreament  = " + String.valueOf(increament));
                if (kflag == 1) {
                    writer.write("\t for a particular kvalue  = " + String.valueOf(kmax));
                } else if (kflag == 0) {
                    writer.write("\t for kvalue till  = " + String.valueOf(kmax));
                }
                writer.newLine();
                writer.write("Window Number\t C(g)");
                writer.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
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
        frame.setIconImage(img.getImage());
        //set the frame in the middle of screen
        frame.setLocation((width / 2), (height / 2));
        frame.setResizable(false);
        frame.setVisible(true);
        //get the number of cores
        int cpus = Runtime.getRuntime().availableProcessors();
        //cpus-=1;
        System.out.println("cpus="+cpus);
        
        ExecutorService pool = Executors.newFixedThreadPool(cpus);
        double[] corrresults = new double[totlwin];
        double[] xaxis = new double[totlwin];
        int rindex = 0;
        char[] subsequence = new char[winsize];
        //char[] subsequence2 = new char[winsize];
        //create object of class Func



        List<Crosscorrcaller> tasks = new ArrayList<Crosscorrcaller>();
        List<Future<Double>> results = null;
        System.out.println("totl win=" + totlwin);
        int breakflag = 0;
        //do correlation by sliding-window
        for (start = 0; start + winsize <= sequence.length;) {
            subsequence = new char[winsize];
            //create threads equal to number of core and assign a subseq to each to calculate correlation
            for (int x = 0; x < cpus; x++) {

                if (totlwin - rindex <= x) {
                    breakflag = 1;
                }
                if (breakflag == 1) {
                    break;
                } else {
                    //create subsequence equal to winsize
                    for (int i = 0; i < winsize; i++) {
                        subsequence[i] = sequence[start + i];
                        // System.out.printf("\ni=%d", i);
                    }

                    start = start + increament;

                    tasks.add(new Crosscorrcaller(subsequence,nuc1,nuc2, kmax, kflag));
                    subsequence = new char[winsize];

                }
            }

            try {
                //execute the tasks concurrently
                results = pool.invokeAll(tasks);
            } catch (InterruptedException ex) {
                Logger.getLogger(Correlationmultithreaded.class.getName()).log(Level.SEVERE, null, ex);
            }


            //store results
            for (Future<Double> result : results) {
                if (result.isCancelled()) {
                    System.err.println("ERROR!!");
                } else {
                    try {
                        try {
                            //System.out.println("result: + index " + result.get() + "\t" + rindex);
                            xaxis[rindex] = rindex + 1;
                            corrresults[rindex++] = result.get();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Correlationmultithreaded.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (ExecutionException e) {
                        System.out.println(e.getCause());
                    }
                }
            }
            tasks.clear();
            results.clear();
            //System.out.printf("\nSusseq length=%d", subsequence.length);

            if (rindex % 10 == 0) {
                //update progress bar
                pBar.setValue(rindex);
               // pBar.update(pBar.getGraphics());
            }
            // System.out.printf("\n%f", corrresults[rindex - 1]);

        }

        pool.shutdown();
        //dispose progress bar
        frame.dispose();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("total time by multi thread" + totalTime);

        if (saveflag == 1) {
            for (int i = 0; i < corrresults.length; i++) {
                try {

                    writer.write(String.valueOf(i + 1) + "\t" + String.valueOf(corrresults[i]));
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
        if(kflag==1){
        newplot.doplot(sequence,xaxis, corrresults, "Cross-Corelation by window Results for only k= "+String.valueOf(kmax), "Window Number", "Correlation(Cg)", winsize, increament);
        }
        else if(kflag==0){
        newplot.doplot(sequence,xaxis, corrresults, "Cross-Corelation by window Results for k till "+String.valueOf(kmax), "Window Number", "Correlation(Cg)", winsize, increament);
        }
        else return;


    }
}

//class to call function
class Crosscorrcaller implements Callable<Double> {

    int kvalue, kflag;
    char[] seq;
    char n1,n2;

    Crosscorrcaller(char[] s,char c1,char c2, int k, int flag) {
        seq = s;
        kvalue = k;
        kflag = flag;
        n1=c1;
        n2=c2;

    }

    @Override
    public Double call() throws Exception {
        Crosscorrfunc ob = new Crosscorrfunc(kvalue, kflag);
        return ob.corG(this.seq,n1,n2);
    }
}

class Crosscorrfunc {

    float[] cg;
    int start;
    int kvalue;

    public Crosscorrfunc(int kmax, int kflag) {

        if (kflag == 0) {
            start = 1;
            kvalue = kmax;
        } else {
            start = kmax ;
            kvalue = kmax;
        }

    }
    /**
     * Function to convert char array(DNA) to int array
     * @param orgseq input sequence
     * @param c1 first nucleotide
     * @param c2 second nucleotide
     * @return 
     */
    public double corG(char[] orgseq,char c1, char c2) {
        char[] cS = orgseq;
        cg = new float[cS.length];
        int[] iSeq1 = new int[cS.length];
        int[] iSeq2 = new int[cS.length];
        for (int i = 0; i < cS.length; i++) {
            if (cS[i] == c1) {
                iSeq1[i] = 1;
            } else {
                iSeq1[i] = -1;
            }
        }
        
        for (int i = 0; i < cS.length; i++) {
            if (cS[i] == c2) {
                iSeq2[i] = 1;
            } else {
                iSeq2[i] = -1;
            }
        }
        //for (int i = 0; i < iS.length; i++) {
        //System.out.printf("%d",iS[i]);
        //}
        double sum = 0;
               
        for (int k = start; k <= kvalue; k++) {
            for (int j = 0; j < iSeq1.length - k; j++) {
                sum += (iSeq1[j]) * (iSeq2[j + k]);
            }

            cg[k - 1] = (float) (sum / (iSeq1.length - k));
            //System.out.printf("%f\n",cg[k-1]);
            sum = 0;

        }
        sum = 0;
        for (int i = 0; i < cg.length; i++) {
            //sum += cg[i] / (cg.length - 1);
            //correction for normalisation 22/7/14
            sum += cg[i] / (kvalue);
            //System.out.printf("k=%d sum=%f\n", i + 1, cg[i]);
        }

        //return answer
        return sum;

    }
}
