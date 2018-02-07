/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import javax.swing.JOptionPane;

/**
 *
 * @author jnu
 */
public class wholegenomeshannon extends Thread {

    char[] sequence;
    double hx=0,info=0;
    public wholegenomeshannon(char[] seq) {

        sequence = seq;
    }

    public void run() {
        int cA = 0, cG = 0, cC = 0, cT = 0;
        double pa, pg, pc, pt;
        
        
        //count occurances ofA,G,C ant T
        cA=countA(sequence);
        cG=countG(sequence);
        cC=countC(sequence);
        cT=countT(sequence);
        //find pi for all
        pa = ((double) cA / (double) sequence.length);
        pc = ((double) cC / (double) sequence.length);
        pg = ((double) cG / (double) sequence.length);
        pt = ((double) cT / (double) sequence.length);

       
       
        //H(x)=-sum(pilopi)
        pa = pa * (Math.log(pa) / Math.log(2));
        pc = pc * (Math.log(pc) / Math.log(2));
        pg = pg * (Math.log(pg) / Math.log(2));
        pt = pt * (Math.log(pt) / Math.log(2));
        hx=-1 * (pa + pc + pg + pt);
        info=2-hx;
        //System.out.print("\n\nHX is"+hx);
        JOptionPane.showMessageDialog(null, "H(x) is:"+hx+"  Information is:"+info);
    }

    private int countA(char[] seq) {
        int count = 0;
        for (int i = 0; i < seq.length; i++) {
            if (seq[i] == 'A' ) {
                count++;
            }
        }
        return count;
    }

    private int countG(char[] seq) {
        int count = 0;
        for (int i = 0; i < seq.length; i++) {
            if (seq[i] == 'G' ) {
                count++;
            }
        }
        return count;
    }

    private int countC(char[] seq) {
        int count = 0;
        for (int i = 0; i < seq.length; i++) {
            if (seq[i] == 'C' ) {
                count++;
            }
        }
        return count;
    }

    private int countT(char[] seq) {
        int count = 0;
        for (int i = 0; i < seq.length; i++) {
            if (seq[i] == 'T' ) {
                count++;
            }
        }
        return count;
    }
    
    public double returnentropy(){
       this.run();
        return hx;
    }
}
