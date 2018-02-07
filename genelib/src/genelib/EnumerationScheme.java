/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

//package cs567;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class EnumerationScheme {

	/**
	 * Given a vertex a, return the next vertex of a preorder enumeration
	 * 
	 * Each element of the enumeration (i.e. vertex) is represented
	 * as a java.util.List of Integers. The root of the enumeration tree
	 * is represented as the empty list (a List of size 0), and each prefix node
	 * i.e. internal node has a size equal to the length of the prefix, where a.size()-1
	 * is the right-most value of the prefix. The left most index is 0.
	 * 
	 * If a is the last element in the enumeration, then the first element is returned.
	 * 
	 * @param a The vertex from which the next vertex is to be found
	 * @param L The total possible number of digits each vertex can have
	 * @param k A List of size L, where k.get(i) is the total possible number of values the ith digit can have
	 * @return the next vertex
	 */
	public static List<Integer> nextVertex(List<Integer> a, int L, List<Integer> k){
		List<Integer> result=new ArrayList<Integer>();
            
                int i=a.size();
                //return next left child
                if (i<L){
                    result=a;
                    //Collections.copy(result, a);
                    result.add(k.get(0));
                }
                else{
                    //choose approprite number to enumerate
                    int lastval=a.get(a.size()-1);
                    if(lastval>=k.get(k.size()-1)){
                        //go back to last root node
                        //System.out.println("case T");
                        //System.out.println(a);
                        for(int j=a.size()-1;j>=0;j--){
                            if(a.get(j)==lastval){
                                a.remove(j);
                                //System.out.println("removed");
                                //System.out.println(a);
                            }
                            else if(a.get(j)<lastval){
                                a.set(j, a.get(j)+1);
                                break;
                            }
                        }
                        //System.out.println(a);
                        result= a;
                        return result;
                    }
                    result= a;
                    //Collections.copy(result, a);
                    result.set(a.size()-1, lastval+1);
                }
            
            return result; //TODO
	}
	
        public static void main(String[] args) {
        
        EnumerationScheme ob=new EnumerationScheme();
        System.out.println("Enumerating all lmers");
        List<Integer> result = new ArrayList<Integer>();
        List<Integer> a1 = new ArrayList<Integer>();
        List<Integer> k = new ArrayList<Integer>();
        List<Integer> finallist = new ArrayList<Integer>();
        k.add(1);
        k.add(2);
        k.add(3);
        k.add(4);
        int L = 4;
        for (int i = 0; i < L; i++) {
            finallist.add(k.get(k.size() - 1));
        }

       while (true) {
            result = nextVertex(result, L, k);
            //System.err.println(result);
            if (result.size() == L) {
                System.out.println(result);
            }
            if (finallist.equals(result)) {
                break;
            }
        }
       
       
    }
}