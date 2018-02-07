/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.math.plot.Plot2DPanel;

/**
 *
 * @author jnu
 * class to plot pyr/pur or M/K or S/W
 */
public class Plotcomp {

    ImageIcon img = new ImageIcon("images/icons/orislogo.png");
    char[] sequence;
    double[] countpyr;
    double[] countpur;
    double[] xaxis;
    int fpyr;
    int fpur;
    int fam, fke, fstrong, fweak;
    int winsize;
    int increament;

    public Plotcomp(char[] seq, double[] repyr, double[] repur, double[] x, int wsize, int inc, int pyr, int pur, int am, int ke, int strng, int weak) {
        sequence = seq;
        countpyr = repyr;
        countpur = repur;
        xaxis = x;
        winsize = wsize;
        increament = inc;
        fpyr = pyr;
        fpur = pur;
        fam = am;
        fke = ke;
        fstrong = strng;
        fweak = weak;

    }

    public void plotcompositions() {

        // create your PlotPanel (you can use it as a JPanel)
        Plot2DPanel plot = new Plot2DPanel();

        // define the legend position
        plot.addLegend("SOUTH");



        String plotdesc = "Composition Plot with Winsize= " + String.valueOf(winsize) + ", Increament= " + String.valueOf(increament);



        // add a line plot to the PlotPanel
        if (fpyr == 1) {
            plot.addLinePlot(plotdesc + "Plot of Pyridines", Color.BLUE, xaxis, countpyr);
        }
        if (fpur == 1) {
            plot.addLinePlot("Plot of Purines", Color.RED, xaxis, countpur);
        }
        if (fam == 1) {
            plot.addLinePlot(plotdesc + "Plot of Amino", Color.BLUE, xaxis, countpyr);
        }
        if (fke == 1) {
            plot.addLinePlot(plotdesc + "Plot of Keto", Color.RED, xaxis, countpur);
        }
        if (fstrong == 1) {
            plot.addLinePlot(plotdesc + "Plot of Strong", Color.BLUE, xaxis, countpyr);
        }
        if (fweak == 1) {
            plot.addLinePlot(plotdesc + "Plot of Weak", Color.RED, xaxis, countpur);
        }

        // create a new panel for extracting sequence
        JPanel panel1 = new JPanel(new FlowLayout());
        //add componentes to panel1
        JLabel label1 = new JLabel("Enter window numbers to get the sequence");
        JLabel label2 = new JLabel("To");
        final JTextField start = new JTextField(5);
        final JTextField end = new JTextField(5);
        JButton jb = new JButton("Go");


        panel1.add(label1);
        panel1.add(start);
        panel1.add(label2);
        panel1.add(end);
        panel1.add(jb);
        panel1.setBackground(Color.red);
        //define button action to extract the sequence
        jb.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                int statusflag = 1;

                if (start.getText().equals("")||end.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Error!! Please Check the values");
                    statusflag = 0;
                }else{
                final int startingwindow = Integer.parseInt(start.getText());
                final int endingwindow = Integer.parseInt(end.getText());
                
                //check for legal values of windows
                if (startingwindow <= 0 || endingwindow <= 0 || startingwindow > xaxis.length || endingwindow > xaxis.length || (startingwindow > endingwindow)) {
                    JOptionPane.showMessageDialog(null, "Error!! Please Check the values");
                    statusflag = 0;
                } else if(statusflag==1) {

                    int start = startingwindow;;
                    final StringBuilder seqstr = new StringBuilder();

                    //extract sequence and save as string
                    for (int j = 0; j <= endingwindow - startingwindow; j++) {
                        //System.out.println("start="+start);
                        seqstr.append("Window Number" + start + ": ");
                        for (int i = (start - 1) * increament; i < ((start - 1) * increament) + winsize; i++) {

                            //System.out.print(sequence[i]);
                            seqstr.append(sequence[i]);

                        }
                        seqstr.append("\n\n");
                        start++;
                    }


                    JPanel seqpanel = new JPanel(new GridLayout(1, 1));
                    JPanel bottompanel = new JPanel(new FlowLayout());
                    //JLabel heading = new JLabel("Extracted Sequence from Window Numbers");
                    JTextArea seq = new JTextArea(100, 100);
                    seq.setColumns(150);
                    seq.setRows(450);
                    seq.setFont(new Font("Monospaced", Font.ROMAN_BASELINE, 14));
                    seq.setLineWrap(true);
                    seq.setWrapStyleWord(true);
                    seq.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
                    JScrollPane scrollPane = new JScrollPane(seq);
                    seq.setEditable(false);
                    seq.setText(seqstr.toString());
                    seqpanel.add(scrollPane);



                    //add save button etc to bottom panel
                    JLabel savelabel = new JLabel("Enter file name to Save Data in File");
                    final JTextField filename = new JTextField(10);
                    JButton save = new JButton("Save");

                    save.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent event) {
                            BufferedWriter writer = null;
                            String fname = filename.getText() + ".txt";
                            File f = new File(fname);
                            try {
                                writer = new BufferedWriter(new FileWriter(f));
                            } catch (IOException ex) {
                                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                writer.write("Extracted Sequence from window number " + startingwindow);

                                writer.write(" till window number " + endingwindow);
                                writer.write(" For the composition plot");
                                writer.newLine();
                                writer.write("Winsize = " + String.valueOf(winsize));
                                writer.write("\tIncreament  = " + String.valueOf(increament));
                                writer.newLine();
                                writer.write(seqstr.toString());
                                writer.newLine();
                                writer.write("END");
                                JOptionPane.showMessageDialog(null, fname + "File saved");
                                writer.close();
                            } catch (IOException ex) {
                                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
                            }



                        }
                    });

                    //add to bottom panel
                    bottompanel.add(savelabel);
                    bottompanel.add(filename);
                    bottompanel.add(save);
                    bottompanel.setBackground(Color.red);


                    //add panels to frame
                    JFrame seqframe = new JFrame("Sequence");
                    seqframe.setSize(600, 600);
                    // seqframe.setResizable(false);
                    seqframe.add(seqpanel);
                    seqframe.add(bottompanel, new BorderLayout().SOUTH);
                    seqframe.setVisible(true);
                    seqframe.setTitle("Extracted Sequence");
                    seqframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                }}
            }
        });



        // put the PlotPanel in a JFrame like a JPanel
        BorderLayout myLayout = new BorderLayout();
        JFrame frame = new JFrame("Whole Composition Plot");
        plot.setAxisLabels("Window Number", "Count");
        
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
        
        plot.getAxis(1).setLabelAngle(-Math.PI / 2);
        plot.getAxis(1).setLabelPosition(-0.15, 0.45);
        plot.getAxis(0).setLabelPosition(0.45, -0.10);
        frame.setLayout(myLayout);
        frame.setSize(600, 600);
        frame.add(plot);
        frame.add(panel1, myLayout.SOUTH);
        frame.setVisible(true);
        frame.setIconImage(img.getImage());
    }
}
