/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package genelib;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TreeSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
//import sun.rmi.log.LogOutputStream;

/**
 *
 * @author slim
 */
public class Showlogo extends Thread {

    final float[][] logodata;
    final int seqlength;
    int totalmatches;
    int exactmatches;
    String searchseq;
    String mismatches;
    ImageIcon img = new ImageIcon("images/icons/orislogo.png");
    public Showlogo(float[][] mat, int l,int total,int exact,String str,String tolerance) {
        logodata = mat;
        seqlength = l;
        totalmatches=total;
        exactmatches=exact;
        searchseq=str;
        mismatches=tolerance;
    }

    public void run() {

        JFrame frame = new JFrame("Logo");
        /* JMenuBar menuBar = new JMenuBar();
         JMenu fileMenu = new JMenu("File");
         JMenu editMenu = new JMenu("Edit");
         JMenu viewMenu = new JMenu("View");
         menuBar.add(fileMenu);
         menuBar.add(editMenu);
         menuBar.add(viewMenu);
         */
        
        frame.setIconImage(img.getImage());
        
        loadimage ob = new loadimage(logodata, seqlength);
        frame.add(ob);



        JComboBox formats = new JComboBox(ob.getFormats());
        JButton savebutton = new JButton("Save");
        formats.setActionCommand("Formats");
        formats.addActionListener(ob);
        savebutton.setActionCommand("save");
        savebutton.addActionListener(ob);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Save Image"));
        panel.add(savebutton);
        frame.add("South", panel);
        
        JPanel labelpanel = new JPanel();
        JLabel searchstr=new JLabel("Searched for the string \""+searchseq+"\""+" With exactly "+mismatches+" mismatches");
        JLabel totalm=new JLabel("\nTotal Matches= "+String.valueOf(totalmatches));
        JLabel exactm=new JLabel("\nExact Matches= "+String.valueOf(exactmatches));
        //labelpanel.setLayout();
        labelpanel.add(searchstr);
        labelpanel.add(totalm);
        labelpanel.add(exactm);
        frame.add("North",labelpanel);

        frame.setVisible(true);
        frame.pack();
        frame.setSize(80 * (seqlength + 1), 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


    }
}

class loadimage extends Component implements ActionListener {

    BufferedImage img1;
    BufferedImage img2;
    BufferedImage img3;
    BufferedImage img4;
    BufferedImage result;
    float[][] data;
    int seqlen;

    public loadimage(float[][] a, int len) {

        data = a;
        seqlen = len;
        try {
            img1 = ImageIO.read(new File("images/logo/An.png"));
            img2 = ImageIO.read(new File("images/logo/Cn.png"));
            img3 = ImageIO.read(new File("images/logo/Gn.png"));
            img4 = ImageIO.read(new File("images/logo/Tn.png"));
            result = new BufferedImage(seqlen * 80, 315, BufferedImage.TYPE_INT_ARGB);
        } catch (IOException e) {
        }

    }

    public void paint(Graphics g) {

        //ordering index
        int a, b, c, d;
        a = b = c = d = -1;
        float max = -1;
        int xpos = 0;
        //Font f=new Font(Font.SANS_SERIF,Font.CENTER_BASELINE , 80);


        Graphics g1 = result.getGraphics();


        BufferedImage[] images = new BufferedImage[4];
        images[0] = img1;
        images[1] = img2;
        images[2] = img3;
        images[3] = img4;

        for (int i = 0; i < seqlen; i++) {

            //find greatest
            for (int j = 0; j < 4; j++) {
                if (data[j][i] > max) {
                    max = data[j][i];
                    a = j;
                }
            }

            //           System.out.println("Highest=" + a);
            max = -1;
            //find second greatest
            for (int j = 0; j < 4; j++) {
                if (data[j][i] > max && j != a) {
                    max = data[j][i];
                    b = j;
                }
            }
            //         System.out.println("2nd Highest=" + b);
            max = -1;
            //find third greatest
            for (int j = 0; j < 4; j++) {
                if (data[j][i] > max && j != a && j != b) {
                    max = data[j][i];
                    c = j;
                }
            }
            //       System.out.println("3rdHighest=" + c);
            d = 6 - (a + b + c);
            //     System.out.println("4th Highest=" + d);
            int sizea, sizeb, sizec, sized;
            sizea = sizeb = sizec = sized = 0;

            if (a != -1) {
                //Draw logo for 3rd pos
                sizea = (int) (data[a][i] * 300);
                g1.drawImage(images[a], xpos, 5, 80, sizea, Color.WHITE, null);
                g.drawImage(images[a], xpos, 5, 80, sizea, Color.WHITE, null);

            }
            if (b != -1) {
                //Draw logo for 3rd pos
                sizeb = (int) (data[b][i] * 300);
                g1.drawImage(images[b], xpos, 5 + sizea, 80, sizeb, Color.WHITE, null);
                g.drawImage(images[b], xpos, 5 + sizea, 80, sizeb, Color.WHITE, null);
            }
            if (c != -1) {
                //Draw logo for 2nd pos
                sizec = (int) (data[c][i] * 300);
                g1.drawImage(images[c], xpos, 5 + sizea + sizeb, 80, sizec, Color.WHITE, null);
                g.drawImage(images[c], xpos, 5 + sizea + sizeb, 80, sizec, Color.WHITE, null);

            }
            if (d != -1) {
                //Draw logo for 1st pos
                sized = (int) (data[d][i] * 300);

                g1.drawImage(images[d], xpos, 5 + sizea + sizeb + sizec, 80, sized, Color.WHITE, null);
                g.drawImage(images[d], xpos, 5 + sizea + sizeb + sizec, 80, sized, Color.WHITE, null);

            }

            //draw the position
            String pos = Integer.toString(i + 1);
            pos = "POS " + pos;
            char[] posdata = pos.toCharArray();
            g1.drawChars(posdata, 0, posdata.length, xpos + 10, 15 + sizea + sizeb + sizec + sized);
            g.drawChars(posdata, 0, posdata.length, xpos + 10, 15 + sizea + sizeb + sizec + sized);





            //   System.out.println("Update y");
            xpos = xpos + 80;

        }


       // System.out.println("DONE");
    }

    /* Return the formats sorted alphabetically and in lower case */
    public String[] getFormats() {
        String[] formats = ImageIO.getWriterFormatNames();
        TreeSet<String> formatSet = new TreeSet<String>();
        for (String s : formats) {
            formatSet.add(s.toLowerCase());
        }
        return formatSet.toArray(new String[0]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // JComboBox cb = (JComboBox)e.getSource();




        /* Save the filtered image in the selected format.
         * The selected item will be the name of the format to use
         */
        String format = "png";
        /* Use the format name to initialise the file suffix.
         * Format names typically correspond to suffixes
         */
        File saveFile = new File("savedimage." + format);
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(saveFile);
        int rval = chooser.showSaveDialog(null);
        if (rval == JFileChooser.APPROVE_OPTION) {
            saveFile = chooser.getSelectedFile();
            /* Write the filtered image in the selected format,
             * to the file chosen by the user.
             */
            try {
                ImageIO.write(result, format, saveFile);
            } catch (IOException ex) {
            }
        }

    }
;
}
