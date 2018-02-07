/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import static genelib.EnumerationScheme.nextVertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author mrbai
 */
public class ComputePvals {

    private char[] seq;
    int k = 1;    //order of markov chain default=1
    char[] alphabets = {'A', 'C', 'G', 'T'};
    float[][] transition_Mat = null;
    int totalStates = 0;
    String[] states;

    public ComputePvals(char[] seq, int o) {
        this.seq = seq;
        k = o;
        totalStates = (int) Math.pow(alphabets.length, k);
        transition_Mat = new float[alphabets.length][totalStates];
        buildTransitionMat();
    }

    //handle non acgt chars
    private void buildTransitionMat() {
        states = new String[totalStates];
        int ind = 0;
        //use this mapping
        //1 A 2 C 3 G 4 T
        EnumerationScheme ob = new EnumerationScheme();
        //System.out.println("Enumerating all lmers");
        List<Integer> result = new ArrayList<Integer>();
        List<Integer> a1 = new ArrayList<Integer>();
        List<Integer> k1 = new ArrayList<Integer>();
        List<Integer> finallist = new ArrayList<Integer>();
        k1.add(1);
        k1.add(2);
        k1.add(3);
        k1.add(4);
        int L = k;
        //enumerate all the states
        for (int i = 0; i < L; i++) {
            finallist.add(k1.get(k1.size() - 1));
        }

        while (true) {
            result = nextVertex(result, L, k1);
            //System.err.println(result);
            if (result.size() == L) {
                //System.out.println(result);
                //add result to states
                states[ind] = maptochar(result);
                ind++;
            }
            if (finallist.equals(result)) {
                break;
            }
        }
        /*
        System.out.println("states::");
        for (int i = 0; i < states.length; i++) {
            System.out.println(states[i]);
        }*/

        //start estimating the matrix
        //init matrix
        for (int t = 0; t < seq.length - k; t++) {
            char curr_state = seq[t];
            int curr_state_index = Arrays.asList(alphabets).indexOf(curr_state);

            if (curr_state == 'A') {
                curr_state_index = 0;
            } else if (curr_state == 'C') {
                curr_state_index = 1;
            } else if (curr_state == 'G') {
                curr_state_index = 2;
            } else if (curr_state == 'T') {
                curr_state_index = 3;
            }

            String next_state = new String(seq, t + 1, k);
            int next_state_index = Arrays.asList(states).indexOf(next_state);

            //System.out.println("curr state:" + curr_state + " ind:" + curr_state_index);
            //System.out.println("next state:" + next_state + " ind:" + next_state_index);
            transition_Mat[curr_state_index][next_state_index] = transition_Mat[curr_state_index][next_state_index] + 1;
        }

        /*
        for (int t = 0; t < alphabets.length; t++) {
            for (int u = 0; u < totalStates; u++) {

                System.out.print(transition_Mat[t][u] + "\t");
            }
            System.out.print("\n");

        }
         */
        //normalize transition mat
        for (int t = 0; t < alphabets.length; t++) {
            float colsum = 0;
            for (int u = 0; u < totalStates; u++) {

                //get colsum for uth row
                colsum += transition_Mat[t][u];

            }

            for (int u = 0; u < totalStates; u++) {

                //normalise matrix
                transition_Mat[t][u] = transition_Mat[t][u] / colsum;
            }

        }
        /*
        System.out.print("Normalised\n");
        for (int t = 0; t < alphabets.length; t++) {
            for (int u = 0; u < totalStates; u++) {

                System.out.print(transition_Mat[t][u] + "\t");
            }
            System.out.print("\n");

        }
         */
    }

    public String maptochar(List<Integer> l) {
        String res = "";
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i) == 1) {
                res += 'A';
            } else if (l.get(i) == 2) {
                res += 'C';
            } else if (l.get(i) == 3) {
                res += 'G';
            } else if (l.get(i) == 4) {
                res += 'T';
            }

        }
        return res;
    }

    public double returnTheorypval(char[] tosearch) {
        double pval = 1;
        int ctr = 0;
        int sind = 0;
        int curr_state_index = 0;
        int next_state_index = 0;
        char curr_state;
        String nextstate = "";
        while (ctr < tosearch.length - k) {
            nextstate = "";
            curr_state = tosearch[ctr];
            if (curr_state == 'A') {
                curr_state_index = 0;
            } else if (curr_state == 'C') {
                curr_state_index = 1;
            } else if (curr_state == 'G') {
                curr_state_index = 2;
            } else if (curr_state == 'T') {
                curr_state_index = 3;
            }

            // System.out.println("currs:"+curr_state);
            //get transition prob
            for (int j = 1; j <= k; j++) {
                nextstate += tosearch[ctr + j];
            }

            // System.out.println("nxtst:"+nextstate);
            //get col index in transition mat
            for (int i = 0; i < states.length; i++) {
                if (states[i].equals(nextstate)) {
                    sind = i;
                    break;
                }
            }
            //System.out.println("nxtst ind:"+sind);
            pval = pval * transition_Mat[curr_state_index][sind];
            //System.out.println("nxtst prob:"+transition_Mat[curr_state_index][sind]);
            ctr = ctr + k;

        }
        return pval;
    }

    public double returnpval(int w, char[] tosearch, int mismatches, int null_matches) {
        //w is the window length i.e. simulate sequences of length w
        //tosearch is the motif to search
        //null_matches is num of matches under null model i.e. genome
        //mismatches are num of mismatches allowed
        double pval = 0;
        int N = 100; //number of sequences to simulate
        int statistic = 0;
        for (int i = 0; i < N; i++) {
            char[] thisseq = simulateseq(w);

            int this_matches = 0;
            //find num matches in simulates seq
            this_matches = returnNumMatches(thisseq, tosearch, mismatches);
            // System.out.println("now match: "+this_matches);
            if (this_matches >= null_matches) {
                statistic += 1;
            }
        }
        //System.out.println("stat "+statistic);
        pval = statistic / (double) N;
        return pval;
    }

    public int returnNumMatches(char[] thisseq, char[] tosearch, int tval) {
        int res = 0;
        Searchseq ob = new Searchseq(thisseq, tosearch, tval, 0, 0, 0, null, true);
        ob.run();
        res = ob.returnTotal();
        return res;
    }

    //function to simulate seq based on the transisiton matrix
    public char[] simulateseq(int l) {
        char[] resultseq = new char[l];
        Random generator = new Random();
        //init state is equalylikely
        resultseq[0] = alphabets[generator.nextInt(alphabets.length)];
        //simulate rest of seq according to transitionMat
        int ctr = 0;
        int curr_state_index = 0;
        int next_state_index = 0;
        String next_nucs = "";

        char curr_state;
        while (ctr < l - 1) {
            curr_state = resultseq[ctr];
            if (curr_state == 'A') {
                curr_state_index = 0;
            } else if (curr_state == 'C') {
                curr_state_index = 1;
            } else if (curr_state == 'G') {
                curr_state_index = 2;
            } else if (curr_state == 'T') {
                curr_state_index = 3;
            }

            try {
                //find next k nucleotides
                next_state_index = getNextindex(curr_state_index);
                //System.out.println("next index is:" + next_state_index);
                next_nucs = states[next_state_index];
                //System.out.println("nextnuc is:" + next_nucs + next_nucs.length());
            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.println("curr index is:" + curr_state_index);
                System.out.println("next index is:" + next_state_index);
                System.out.println("nextnuc is:" + next_nucs + next_nucs.length());
                //System.exit(0);
            }
            int s = 0;
            while (s < next_nucs.length()) {

                ctr++;
                //break if seq len exceeds
                if (ctr > resultseq.length - 1) {
                    return resultseq;
                }
                resultseq[ctr] = next_nucs.charAt(s);
                s++;

            }

        }

        return resultseq;
    }

    private int getNextindex(int rowind) {
        int ind = 0;
        double rn = Math.random();

        double cumuProb = 0.0;
        for (int c = 0; c < totalStates; c++) {
            cumuProb += transition_Mat[rowind][c];
            if (rn <= cumuProb) {
                return c;
            }
        }
        //System.out.println("faiing..."+rn+" "+cumuProb);
        return totalStates - 1;
    }

    /*Driver function to check for above function*/
    public static void main(String[] args) {
        System.out.println("in main");
        //char[] seq = {'A', 'A', 'G', 'T', 'T', 'C', 'C', 'C', 'G', 'G', 'A'};
        char[] seq = {'A', 'A', 'A', 'A', 'A', 'A', 'T', 'C', 'G', 'G', 'A'};
        char[] m = {'A', 'A', 'T', 'G', 'C'};
        ComputePvals obj;
        obj = new ComputePvals(seq, 3);

        char[] res = obj.simulateseq(40);

        for (int i = 0; i < res.length; i++) {
            System.out.print(res[i]);
        }
        System.out.print("\n");

        System.out.print("total matches\n");

        System.out.println(obj.returnNumMatches(seq, m, 0));

        System.out.print("get pvals\n");
        System.out.println(obj.returnpval(100, m, 0, obj.returnNumMatches(seq, m, 0)));

        System.out.println(obj.returnTheorypval(m));

    }

}
