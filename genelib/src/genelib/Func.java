/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *This class contains the autocorrelation and skew measures
 * @author urmi
 */
public class Func {

    private float[] cg;
    static int wno = 1;
    static int ctr = 1;
    int start;
    int kvalue;

    /*store all the results in ca ct cg cc resp*/
    //corelation with G
    //cS copied seq, iS corresponding integer seq
    /**
     * @param orgseq input DNA sequence
     * @return autocorrelation value
     */
    public float corG(char[] orgseq) {
        char[] cS = orgseq;
        cg = new float[cS.length];
        int[] iS = new int[cS.length];
        for (int i = 0; i < cS.length; i++) {
            if (cS[i] == 'G') {
                iS[i] = 1;
            } else {
                iS[i] = -1;
            }
        }
        //for (int i = 0; i < iS.length; i++) {
        //System.out.printf("%d",iS[i]);
        //}
        float sum = 0;
        int index = 0;

        for (int k = 1; k <= iS.length - 1; k++) {
            for (int j = 0; j < iS.length - k; j++) {
                sum += (iS[j]) * (iS[j + k]);
            }

            cg[k - 1] = sum / (iS.length - k);
            //System.out.printf("%f\n",cg[k-1]);
            sum = 0;

        }
        sum = 0;
        for (int i = 0; i < cg.length; i++) {
            sum += cg[i] / (cg.length - 1);
            //System.out.printf("k=%d sum=%f\n", i + 1, cg[i]);
        }
        //System.out.printf("%d\t%f\n",wno,sum);
        //wno++;
        return sum;

    }

    /**
     * @param orgseq input DNA sequence
     * @return GCskew value
     */
    public float GCskew(char[] orgseq) {

        int gcount = 0, ccount = 0;

        for (int i = 0; i < orgseq.length; i++) {
            if (orgseq[i] == 'G') {
                gcount++;
            } else if (orgseq[i] == 'C') {
                ccount++;
            }

        }
        float res = (float) (ccount - gcount) / (ccount + gcount);
        //System.out.printf("%d\t%f\n", ctr++, res);
        return res;
    }

    /**
     * @param Orgseq input DNA sequence
     * @return ATskew value
     */
    public float ATskew(char[] Orgseq) {

        int acount = 0, tcount = 0;

        for (int i = 0; i < Orgseq.length; i++) {
            if (Orgseq[i] == 'A') {
                acount++;
            } else if (Orgseq[i] == 'T') {
                tcount++;
            }

        }
        float res = (float) (acount - tcount) / (acount + tcount);
        //System.out.printf("%d\t%f\n", ctr++, res);
        return res;
    }

    /**
     * @param Orgseq input DNA sequence
     * @return MKskew value
     */
    public float MKskew(char[] Orgseq) {

        int acount = 0, tcount = 0, ccount = 0, gcount = 0,mcount=0,kcount=0;

        for (int i = 0; i < Orgseq.length; i++) {
            if (Orgseq[i] == 'A') {
                acount++;
            } else if (Orgseq[i] == 'T') {
                tcount++;
            } else if (Orgseq[i] == 'G') {
                gcount++;
            } else if (Orgseq[i] == 'C') {
                ccount++;
            }

        }
        
        mcount=acount+ccount;
        kcount=gcount+tcount;
        float res = (float) (mcount - kcount) / (mcount + kcount);
        //System.out.printf("%d\t%f\n", ctr++, res);
        return res;
    }
    
    /**
     * @param Orgseq input DNA sequence
     * @return RYskew value
     */
    public float RYskew(char[] Orgseq) {

        int acount = 0, tcount = 0, ccount = 0, gcount = 0,rcount=0,ycount=0;

        for (int i = 0; i < Orgseq.length; i++) {
            if (Orgseq[i] == 'A') {
                acount++;
            } else if (Orgseq[i] == 'T') {
                tcount++;
            } else if (Orgseq[i] == 'G') {
                gcount++;
            } else if (Orgseq[i] == 'C') {
                ccount++;
            }

        }
        
        rcount=acount+gcount;
        ycount=ccount+tcount;
        //float res = (float) (rcount - ycount) / (rcount + ycount);
        float res = (float) (ycount - rcount) / (rcount + ycount);
        //System.out.printf("%d\t%f\n", ctr++, res);
        return res;
    }
    
}
