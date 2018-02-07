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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author slim
 */
public class Correlationmultithreaded extends Thread {

    ImageIcon img = new ImageIcon("images/icons/orislogo.png");
    private int flag = 0;
    final char[] sequence;
    final JFrame frame = new JFrame("Progress");
    private JProgressBar pBar = new JProgressBar();
    int kmax;
    int kflag;
    int winsize;
    int increament;
    int saveflag;
    String filename;
    char priority;
   static public char corrcoef;
   //array to return data
   public double[]corrdata;
   

    public Correlationmultithreaded(char[] val, int wsize, int inc, int kval, int flagval, int save, String fname, char pri,char coeff) {

        sequence = val;
        winsize = wsize;
        increament = inc;
        kmax = kval;
        //kflag is 1 only when correlation of a particular k is selected
        kflag = flagval;
        saveflag = save;
        filename = fname;
        priority = pri;
        corrcoef=coeff;
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
                writer.write("Data for the Correlation plot ");
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


        //Use CPUs based on priority
        if (priority == 'M' && cpus > 1) {
            System.out.println("Priority is medium");
            cpus = cpus / 2;
        } else if (priority == 'L') {
            System.out.println("Priority is LOw");
            cpus = 1;
        }


        //create executer service object
        ExecutorService pool = Executors.newFixedThreadPool(cpus);
        //arrays to store the results
        double[] corrresults = new double[totlwin];
        double[] xaxis = new double[totlwin];
        int rindex = 0;
        char[] subsequence = new char[winsize];
        //List to store callables
        List<Corrcaller> tasks = new ArrayList<Corrcaller>();
        //List to store the results
        List<Future<Double>> results = null;

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
                    for (int i = 0; i < winsize; i++) {
                        subsequence[i] = sequence[start + i];

                    }

                    start = start + increament;

                    tasks.add(new Corrcaller(subsequence, kmax, kflag));
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
                //pBar.update(pBar.getGraphics());
               // frame.repaint();
            }
            // System.out.printf("\n%f", corrresults[rindex - 1]);

        }
        //shutdown executer obj
        pool.shutdown();
        //dispose progress bar
        frame.dispose();
        //calculate time to be siplayed on terminal only!!!
       /* long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("total time by multi thread" + totalTime);*/

        //if save file is required
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

        //after writing file close it
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
        if (kflag == 1) {
            newplot.doplot(sequence,xaxis, corrresults, "Corelation by window Results for only k= " + String.valueOf(kmax), "Window Number", "Correlation(Cg)", winsize, increament);
        } else if (kflag == 0) {
            newplot.doplot(sequence,xaxis, corrresults, "Corelation by window Results for k till " + String.valueOf(kmax), "Window Number", "Correlation(Cg)", winsize, increament);
        } else {
            return;
        }
        
        corrdata=corrresults;


    }
}

class Corrcaller implements Callable<Double> {

    int kvalue, kflag;
    char[] seq;
    char coef;
    Corrcaller(char[] s, int k, int flag) {
        seq = s;
        kvalue = k;
        kflag = flag;
        this.coef=coef;
    }

    @Override
    public Double call() throws Exception {
        Corrfunc2 ob = new Corrfunc2(kvalue, kflag);
        return ob.corG(this.seq);
    }
}

//class to computer correlation for a sequence
class Corrfunc2 {

    float[] cg;
    int start;
    int kvalue;
    
    public Corrfunc2(int kmax, int kflag) {

        if (kflag == 0) {
            start = 1;
            kvalue = kmax;
        } else {
            start = kmax;
            kvalue = kmax;
        }
        

    }

    public double corG(char[] orgseq) {
        char[] cS = orgseq;
        cg = new float[cS.length];
        //iS for integer sequence
        int[] iS = new int[cS.length];
        //create numeric sequence from char sequence
        for (int i = 0; i < cS.length; i++) {
            if (cS[i] == Correlationmultithreaded.corrcoef) {
                iS[i] = 1;
            } else {
                iS[i] = -1;
            }
        }

        double sum = 0;
        //do correlation 
        for (int k = start; k <= kvalue; k++) {
            for (int j = 0; j < iS.length - k; j++) {
                sum += (iS[j]) * (iS[j + k]);
            }

            cg[k - 1] = (float) (sum / (iS.length - k));
            //System.out.printf("%f\n",cg[k-1]);
            sum = 0;

        }
        sum = 0;
        //sum all the correlation values to get Cg
        for (int i = 0; i < cg.length; i++) {
            //sum += cg[i] / (cg.length - 1);
            //correction for normalisation 22/7/14
            sum += cg[i] / (kvalue);
            //System.out.printf("k=%d sum=%f\n", i + 1, cg[i]);
        }

        //return Cg
        return sum;

    }
}
