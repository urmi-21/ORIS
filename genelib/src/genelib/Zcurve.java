/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.math.plot.Plot2DPanel;
import org.math.plot.Plot3DPanel;

/**
 *Class to implement the z-curve method
 * @author urmi
 */
public class Zcurve extends Thread {

    final char[] sequence;
    int d2d, d3d,saveflag,winsize;
    String filename;
   ImageIcon img = new ImageIcon("images/icons/orislogo.png");
   
   /**
    * 
    * @param val DNA sequence
    * @param wsize window size
    * @param threed flag from 3d plot
    * @param twod flag for 2d plot
    * @param save save flag
    * @param fname filename to save 
    */
    public Zcurve(char[] val, int wsize, int threed, int twod,int save,String fname) {
        sequence = val;
        d3d = threed;
        d2d = twod;
        saveflag=save;
        filename=fname;
        winsize=wsize;
    }

    @Override
    public void run() {

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
                writer.write("Data for the Z-curve plot ");
                writer.newLine();
                writer.write("Index\t x-component\ty-component\tz-component");
                writer.newLine();
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        int a, c, g, t;
        int size = 0;
        a = g = c = t = 0;
        int R, Y, M, K, W, S;

        //loop to calculate size
        for (int i = 0; i < sequence.length; i++) {

            if (i % winsize == 0) {
                size++;
            }

        }
        double[] x = new double[size];
        double[] y = new double[size];
        double[] z = new double[size];
        double[] xaxis = new double[size];
        int index = 0;

        for (int i = 0; i < sequence.length; i++) {

            //increament a,g,c or t
            if (sequence[i] == 'A') {
                a++;
            } else if (sequence[i] == 'G') {
                g++;
            } else if (sequence[i] == 'C') {
                c++;
            } else if (sequence[i] == 'T') {
                t++;
            }

            R = a + g;
            Y = c + t;
            M = a + c;
            K = g + t;
            W = a + t;
            S = c + g;

            if (i % winsize == 0) {
                //give values for xn,yn and zn
                x[index] = R - Y;
                y[index] = M - K;
                z[index] = W - S;
                xaxis[index] = index + 1;
                index++;

                /*if (i % 500000 == 0) {
                    System.out.println(i);
                }*/
            }
        }

        if(saveflag==1){
            for(int i=0;i<x.length;i++){
                try {
                    writer.write(String.valueOf(xaxis[i])+"\t\t"+String.valueOf(x[i])+"\t\t"+String.valueOf(y[i])+"\t\t"+String.valueOf(z[i]));
                    writer.newLine();
                } catch (IOException ex) {
                    Logger.getLogger(Zcurve.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            try {
                //after writing
                JOptionPane.showMessageDialog(null,"File "+filename+" saved");
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(Zcurve.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        //plot the curve
        if (d2d == 1) {
            draw2d(xaxis, x, y);
        }
        if (d3d == 1) {
            draw3d(x, y,z);
        }

    }

    /**
     * Draw x and y components on 2d plane
     * @param axis
     * @param x
     * @param y 
     */
    private void draw2d(double[] axis, double[] x, double[] y) {
        // create your PlotPanel (you can use it as a JPanel) with a legend at SOUTH
        Plot2DPanel plot = new Plot2DPanel("SOUTH");
        //add label to plot
        plot.setAxisLabels("Position","Z-curve Component");
        plot.getAxis(1).setLabelAngle(-Math.PI / 2);
        plot.getAxis(1).setLabelPosition(-0.15, 0.45);
        plot.getAxis(0).setLabelPosition(0.45, -0.10);
        // add plot to the PlotPanel
        plot.addLinePlot("X Component", axis, x);
        plot.addLinePlot("Y component", axis, y);
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
        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("X-Y Plot");
        frame.setIconImage(img.getImage());
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);

    }

    /**
     * function to draw 3d z-curve
     * @param x
     * @param y
     * @param z 
     */
    private void draw3d(double[] x, double[] y, double[] z) {
        // create your PlotPanel (you can use it as a JPanel) with a legend at SOUTH
        Plot3DPanel plot = new Plot3DPanel("SOUTH");

        // add plot to the PlotPanel
        plot.addLinePlot("Z-curve", x, y, z);

        // put the PlotPanel in a JFrame like a JPanel
        JFrame frame = new JFrame("Z curve 3d Plot");
        frame.setIconImage(img.getImage());
        frame.setSize(600, 600);
        frame.setContentPane(plot);
        frame.setVisible(true);

    }
}
