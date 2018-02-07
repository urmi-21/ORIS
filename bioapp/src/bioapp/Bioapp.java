/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bioapp;

import genelib.Readfile;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Image;
import java.awt.SplashScreen;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author jnu
 */
public class Bioapp  {
//the pictures

    JLabel pn = new JLabel();
    JPanel panel = new JPanel();
    
    static SplashScreen mySplash;                   // instantiated by JVM we use it to get graphics

    Bioapp() {
       
    }

    public static void main(final String args[]) {


        /*Bioapp ob = new Bioapp();
        //display image for some time then display main form
        long start = System.currentTimeMillis();
        //display image for t secs
        int t=3;
        long end = start + t * 1000; // 60 seconds * 1000 ms/sec
        
        while ((System.currentTimeMillis() < end)) {
        ob.setVisible(true);
        }ob.dispose();
         */

        //for Splash screen
        initsplashscreen();     // initialize splash screen drawing parameters
        appInit();              // Show splash image for some time or show a message
        if (mySplash != null) // check if we really had a spash screen
        {
            mySplash.close();   // we're done with it
        }
        // begin the program after splash is over
        //System.out.print("Splash done");

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {


                try {
                    new Form2().callmain();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Bioapp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Bioapp.class.getName()).log(Level.SEVERE, null, ex);
                }





            }
        });
    }

    private static void appInit() {
        //splash screen for 3 secs
        for (int i = 1; i <= 3; i++) {

            try {
                Thread.sleep(1000);             // wait a second
            } catch (InterruptedException ex) {
                break;
            }
        }
    }

    private static void initsplashscreen() {
        // the splash screen object is created by the JVM, if it is displaying a splash image

        mySplash = SplashScreen.getSplashScreen();
        // if there are any problems displaying the splash image
        // the call to getSplashScreen will returned null
        if (mySplash != null) {
            // get the size of the image now being displayed
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;

        }
    }
    // Variables declaration - do not modify
}
