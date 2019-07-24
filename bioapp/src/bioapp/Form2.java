/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Form2.java
 *
 * Created on Mar 24, 2013, 11:55:01 PM
 */
package bioapp;

//import genelib.Correlation;
import genelib.CummGCskew;
import genelib.Readfile;
import genelib.Searchseq;
import genelib.wholegenomeshannon;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.*;

/**
 *
 * @author urmi
 * This is the main display form for ORIS.
 */
public class Form2 extends javax.swing.JFrame {

   public ImageIcon img = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/images/icons/orislogo.png")));
   
    int filereadflag;
    SimpleAttributeSet set;
    String filedata;
    static public int sequencelength = 0;
    /*below are parameters for the default setting and configuration*/
    static public String DEFAULT_WIN_SIZE, DEFAULT_INCREAMENT;
    static public char PRIORITY;
    static public String DEFAULT_OPEN_DIR;
    static public String DEFAULT_SAVE_DIR;
    
    /*public BufferedImage getImageStream(String path){
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException ex) {
            Logger.getLogger(Form2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public URL getImageResource(String path){
        return getClass().getClassLoader().getResource(path);
       
    }*/

    /**
     * Creates new form Form2
     */
    public Form2() throws FileNotFoundException, IOException {

        /*read the config file to read the parameters*/
        BufferedReader br = null;
        String sCurrentLine = null;
        String substr = null;
        int linectr = 0, wordctr = 0;

        String path = getjarPath();
        String fileSeparator = System.getProperty("file.separator");
        String newDir = path + fileSeparator;

        //parse settings.cfg file
        //for windows
        File f = new File(path + "/settings.cfg");
        if (f.exists()) {
            br = new BufferedReader(new FileReader(f));
            try {
                while ((sCurrentLine = br.readLine()) != null) {
                    //linectr to identify line number and then read value
                    if (linectr == 0) {
                        for (int i = 0; i < sCurrentLine.length(); i++) {
                            if (sCurrentLine.charAt(i) == '\t') {
                                wordctr = i + 1;
                            }
                        }
                        substr = sCurrentLine.substring(wordctr);
                        DEFAULT_WIN_SIZE = substr;
                        System.out.print("winsize=" + DEFAULT_WIN_SIZE);
                        linectr++;
                    } else if (linectr == 1) {
                        for (int i = 0; i < sCurrentLine.length(); i++) {
                            if (sCurrentLine.charAt(i) == '\t') {
                                wordctr = i + 1;
                            }
                        }
                        substr = sCurrentLine.substring(wordctr);
                        DEFAULT_INCREAMENT = substr;
                        System.out.println("inc=" + DEFAULT_INCREAMENT);
                        linectr++;
                    } else if (linectr == 2) {
                        for (int i = 0; i < sCurrentLine.length(); i++) {
                            if (sCurrentLine.charAt(i) == '\t') {
                                wordctr = i + 1;
                            }
                        }
                        substr = sCurrentLine.substring(wordctr);
                        PRIORITY = substr.charAt(0);
                        System.out.print("pri= " + PRIORITY);
                        linectr++;
                    } else if (linectr == 3) {
                        for (int i = 0; i < sCurrentLine.length(); i++) {
                            if (sCurrentLine.charAt(i) == '\t') {
                                wordctr = i + 1;
                            }
                        }
                        substr = sCurrentLine.substring(wordctr);
                        DEFAULT_SAVE_DIR = substr;
                        System.out.print("SAVE dir= " + DEFAULT_SAVE_DIR);
                        linectr++;
                    } else {
                        for (int i = 0; i < sCurrentLine.length(); i++) {
                            if (sCurrentLine.charAt(i) == '\t') {
                                wordctr = i + 1;
                            }
                        }
                        substr = sCurrentLine.substring(wordctr);
                        DEFAULT_OPEN_DIR = substr;
                        System.out.print(" *dir=" + DEFAULT_OPEN_DIR);

                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(Form2.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            BufferedWriter writer = null;
            writer = new BufferedWriter(new FileWriter(f));
            writer.write("DEAFULT_WIN_SIZE\t50000");
            DEFAULT_WIN_SIZE = String.valueOf(50000);
            writer.newLine();
            writer.write("DEAFULT_INCREAMENT\t10000");
            DEFAULT_INCREAMENT = String.valueOf(10000);
            writer.newLine();
            writer.write("PRIORITY\tH");
            PRIORITY = 'H';
            writer.newLine();
            writer.write("DEFAULT_SAVE_DIR\t");
            DEFAULT_SAVE_DIR = "";
            writer.newLine();
            writer.write("DEFAULT_OPEN_DIR\t");
            DEFAULT_OPEN_DIR = "";
            writer.close();
        }

        //initialize components
        initComponents();

        //set form attributes
        //this.setAlwaysOnTop(true);
        this.setResizable(false);
        this.setTitle("ORIS");
        filereadflag = 0;
        // jTextPanegenome.setEditable(false);
        // jTextPanegenome.setBackground(Color.red);
        this.pack();

        jTextFieldrotation.setText("0000000");
        jTextFieldrotation.setEditable(false);
        jTextFieldrotation.setBackground(Color.orange);
        jTextFieldopened.setEditable(false);
        jTextFieldopened.setBackground(Color.lightGray);
        jTextFieldaccession.setEditable(false);
        jTextFieldaccession.setBackground(Color.lightGray);
        jTextFieldgi.setEditable(false);
        jTextFieldgi.setBackground(Color.lightGray);
        jTextFieldinfo.setEditable(false);
        jTextFieldinfo.setBackground(Color.lightGray);
        jTextFieldglength.setEditable(false);
        jTextFieldglength.setBackground(Color.MAGENTA);
        jTextFieldpercentA.setEditable(false);
        jTextFieldpercentA.setBackground(Color.GREEN);
        jTextFieldpercentT.setEditable(false);
        jTextFieldpercentT.setBackground(Color.red);
        jTextFieldpercentG.setEditable(false);
        jTextFieldpercentG.setBackground(Color.yellow);
        jTextFieldpercentC.setEditable(false);
        jTextFieldpercentC.setBackground(Color.CYAN);

        jTextAreagseq.setEditable(false);
        jTextAreagseq.setBackground(Color.lightGray);
        jTextAreagseq.setColumns(10);
        jTextAreagseq.setFont(new Font("Monospaced", Font.ROMAN_BASELINE, 14));
        jTextAreagseq.setLineWrap(true);
        jTextAreagseq.setWrapStyleWord(true);
        jTextAreagseq.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

        //initialize toolbar and its buttons, icons etc.
        jToolBar1.setRollover(true);
        jToolBar1.setFloatable(false);
        //jButtonopen.setIcon(new ImageIcon("images/icons/folder.png", "open"));
        jButtonopen.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/icons/folder.png"), "open"));
                
        jButtondownload.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/icons/download.png"), "download"));
        jButtondownload.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/icons/download.png"), "download"));
        jButtonfind.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/icons/search.png"), "search"));
        jButtonrotateanti.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/icons/rotateanti.png"), "rotate left"));
        jButtonrotateclock.setIcon(new ImageIcon(getClass().getClassLoader().getResource("images/icons/rotateclock.png"), "rotate right"));
        this.setIconImage(img.getImage());
        jLabelgenomecomposition.setForeground(Color.red);
        jLabelgenome.setForeground(Color.BLUE);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jToolBar1 = new javax.swing.JToolBar();
        jButtonopen = new javax.swing.JButton();
        jButtondownload = new javax.swing.JButton();
        jButtonfind = new javax.swing.JButton();
        jTextFieldfind = new javax.swing.JTextField();
        jButtonrotateanti = new javax.swing.JButton();
        jTextFieldrotation = new javax.swing.JTextField();
        jButtonrotateclock = new javax.swing.JButton();
        jTextFieldopened = new javax.swing.JTextField();
        jLabelopened = new javax.swing.JLabel();
        jLabelgi = new javax.swing.JLabel();
        jTextFieldgi = new javax.swing.JTextField();
        jLabelaccession = new javax.swing.JLabel();
        jTextFieldaccession = new javax.swing.JTextField();
        jLabelinfo = new javax.swing.JLabel();
        jTextFieldinfo = new javax.swing.JTextField();
        jTextFieldglength = new javax.swing.JTextField();
        jLabelglength = new javax.swing.JLabel();
        jTextFieldpercentA = new javax.swing.JTextField();
        jTextFieldpercentC = new javax.swing.JTextField();
        jTextFieldpercentG = new javax.swing.JTextField();
        jTextFieldpercentT = new javax.swing.JTextField();
        jLabelgenome = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelgenomecomposition = new javax.swing.JLabel();
        jButtoncompA = new javax.swing.JButton();
        jButtoncompT = new javax.swing.JButton();
        jButtoncompC = new javax.swing.JButton();
        jButtoncompG = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreagseq = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuopen = new javax.swing.JMenuItem();
        jMenuItemdownload = new javax.swing.JMenuItem();
        jMenuexit = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItempredict = new javax.swing.JMenuItem();
        jMenuskew = new javax.swing.JMenu();
        jMenucummulative = new javax.swing.JMenu();
        jMenuItemCgc = new javax.swing.JMenuItem();
        jMenuItemCat = new javax.swing.JMenuItem();
        jMenuItemCmk = new javax.swing.JMenuItem();
        jMenuItemCry = new javax.swing.JMenuItem();
        jMenuItemgc = new javax.swing.JMenuItem();
        jMenuItemat = new javax.swing.JMenuItem();
        jMenuItemmk = new javax.swing.JMenuItem();
        RYskew = new javax.swing.JMenuItem();
        jMenucorr = new javax.swing.JMenu();
        jMenuItemcorr = new javax.swing.JMenuItem();
        jMenuItemcorrwhole = new javax.swing.JMenuItem();
        jMenuItemcrosscorrelation = new javax.swing.JMenuItem();
        jMenuItemzcurve = new javax.swing.JMenuItem();
        jMenuItemuserdefined = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItemsearch = new javax.swing.JMenuItem();
        jMenuItemdnabox = new javax.swing.JMenuItem();
        jMenuItemyeastacs = new javax.swing.JMenuItem();
        jMenuItemwtmatrix = new javax.swing.JMenuItem();
        jMenuItemextractseq = new javax.swing.JMenuItem();
        jMenuProfile = new javax.swing.JMenu();
        jMenuItembending = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenuItemwholegenomeentropy = new javax.swing.JMenuItem();
        jMenuItemsheno1 = new javax.swing.JMenuItem();
        jMenuItemgcentropy = new javax.swing.JMenuItem();
        jMenuItematentropy = new javax.swing.JMenuItem();
        jMenuItemrenyien = new javax.swing.JMenuItem();
        jMenuItemengx = new javax.swing.JMenuItem();
        jMenuItemjensonshannon = new javax.swing.JMenuItem();
        jMenuview = new javax.swing.JMenu();
        jMenucomposition = new javax.swing.JMenu();
        jMenuItemcompall = new javax.swing.JMenuItem();
        jMenuItemcompA = new javax.swing.JMenuItem();
        jMenuItemcompC = new javax.swing.JMenuItem();
        jMenuItemcompG = new javax.swing.JMenuItem();
        jMenuItemcompT = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItemcompopyr = new javax.swing.JMenuItem();
        jMenuItemcompopur = new javax.swing.JMenuItem();
        jMenuItemcompopp = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenuItemcompoamino = new javax.swing.JMenuItem();
        jMenuItemcompoketo = new javax.swing.JMenuItem();
        jMenuItemcompoak = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItemcompostrong = new javax.swing.JMenuItem();
        jMenuItemcompoweak = new javax.swing.JMenuItem();
        jMenuItemcomposw = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemsettings = new javax.swing.JMenuItem();
        jMenuItemabout = new javax.swing.JMenuItem();
        jMenuItemguide = new javax.swing.JMenuItem();
        jMenuItemjmath = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);

        jButtonopen.setText("Open    ");
        jButtonopen.setFocusable(false);
        jButtonopen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonopen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonopen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonopenActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonopen);

        jButtondownload.setText("Download   ");
        jButtondownload.setFocusable(false);
        jButtondownload.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtondownload.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtondownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtondownloadActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtondownload);

        jButtonfind.setText("Find     ");
        jButtonfind.setFocusable(false);
        jButtonfind.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonfind.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonfind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonfindActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonfind);

        jTextFieldfind.setColumns(5);
        jToolBar1.add(jTextFieldfind);

        jButtonrotateanti.setText("Rotate");
        jButtonrotateanti.setFocusable(false);
        jButtonrotateanti.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonrotateanti.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonrotateanti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonrotateantiActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonrotateanti);

        jTextFieldrotation.setText("00000000");
        jToolBar1.add(jTextFieldrotation);

        jButtonrotateclock.setText("Rotate");
        jButtonrotateclock.setFocusable(false);
        jButtonrotateclock.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonrotateclock.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonrotateclock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonrotateclockActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonrotateclock);

        jTextFieldopened.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldopenedActionPerformed(evt);
            }
        });

        jLabelopened.setText("File Opened");

        jLabelgi.setText("gi:");

        jLabelaccession.setText("accession:");

        jLabelinfo.setText("File Info");

        jLabelglength.setText("Length");

        jLabelgenome.setText("Genome");

        jLabelgenomecomposition.setText("Genome Composition");

        jButtoncompA.setText("%A");
        jButtoncompA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtoncompAActionPerformed(evt);
            }
        });

        jButtoncompT.setText("%T");
        jButtoncompT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtoncompTActionPerformed(evt);
            }
        });

        jButtoncompC.setText("%C");
        jButtoncompC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtoncompCActionPerformed(evt);
            }
        });

        jButtoncompG.setText("%G");
        jButtoncompG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtoncompGActionPerformed(evt);
            }
        });

        jTextAreagseq.setColumns(20);
        jTextAreagseq.setRows(5);
        jScrollPane1.setViewportView(jTextAreagseq);

        jMenu1.setText("File     ");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuopen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuopen.setText("Open");
        jMenuopen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuopenActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuopen);

        jMenuItemdownload.setText("Download from NCBI");
        jMenuItemdownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemdownloadActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItemdownload);

        jMenuexit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuexit.setText("Exit");
        jMenuexit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuexitActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuexit);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("OriginFinder     ");
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItempredict.setText("Automatic Prediction");
        jMenuItempredict.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItempredictActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItempredict);

        jMenuskew.setText("Skew");

        jMenucummulative.setText("Cumulative");

        jMenuItemCgc.setText("CumulativeGC");
        jMenuItemCgc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCgcActionPerformed(evt);
            }
        });
        jMenucummulative.add(jMenuItemCgc);

        jMenuItemCat.setText("CumulativeAT");
        jMenuItemCat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCatActionPerformed(evt);
            }
        });
        jMenucummulative.add(jMenuItemCat);

        jMenuItemCmk.setText("CumulativeMK");
        jMenuItemCmk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCmkActionPerformed(evt);
            }
        });
        jMenucummulative.add(jMenuItemCmk);

        jMenuItemCry.setText("CumulativeRY");
        jMenuItemCry.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCryActionPerformed(evt);
            }
        });
        jMenucummulative.add(jMenuItemCry);

        jMenuskew.add(jMenucummulative);

        jMenuItemgc.setText("GCskew");
        jMenuItemgc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemgcActionPerformed(evt);
            }
        });
        jMenuskew.add(jMenuItemgc);

        jMenuItemat.setText("ATskew");
        jMenuItemat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItematActionPerformed(evt);
            }
        });
        jMenuskew.add(jMenuItemat);

        jMenuItemmk.setText("MKskew");
        jMenuItemmk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemmkActionPerformed(evt);
            }
        });
        jMenuskew.add(jMenuItemmk);

        RYskew.setText("RYskew");
        RYskew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RYskewActionPerformed(evt);
            }
        });
        jMenuskew.add(RYskew);

        jMenu3.add(jMenuskew);

        jMenucorr.setText("Correlation Method");

        jMenuItemcorr.setText("Correlation by Window");
        jMenuItemcorr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcorrActionPerformed(evt);
            }
        });
        jMenucorr.add(jMenuItemcorr);

        jMenuItemcorrwhole.setText("Correlation for Whole Genome");
        jMenuItemcorrwhole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcorrwholeActionPerformed(evt);
            }
        });
        jMenucorr.add(jMenuItemcorrwhole);

        jMenuItemcrosscorrelation.setText("Cross-Correlation");
        jMenuItemcrosscorrelation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcrosscorrelationActionPerformed(evt);
            }
        });
        jMenucorr.add(jMenuItemcrosscorrelation);

        jMenu3.add(jMenucorr);

        jMenuItemzcurve.setText("Z-curve");
        jMenuItemzcurve.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemzcurveActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemzcurve);

        jMenuItemuserdefined.setText("Skew Calculator");
        jMenuItemuserdefined.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemuserdefinedActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItemuserdefined);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Pattern Search     ");
        jMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu4ActionPerformed(evt);
            }
        });

        jMenuItemsearch.setText("Search Sequence");
        jMenuItemsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemsearchActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemsearch);

        jMenuItemdnabox.setText("DnaA box");
        jMenuItemdnabox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemdnaboxActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemdnabox);

        jMenuItemyeastacs.setText("Yeast ACS");
        jMenuItemyeastacs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemyeastacsActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemyeastacs);

        jMenuItemwtmatrix.setText("Weight-Matrix Search");
        jMenuItemwtmatrix.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemwtmatrixActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemwtmatrix);

        jMenuItemextractseq.setText("Extract Sequence");
        jMenuItemextractseq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemextractseqActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItemextractseq);

        jMenuBar1.add(jMenu4);

        jMenuProfile.setText("DNA Analysis  ");

        jMenuItembending.setText("DNA Bending Analysis");
        jMenuItembending.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItembendingActionPerformed(evt);
            }
        });
        jMenuProfile.add(jMenuItembending);

        jMenuBar1.add(jMenuProfile);

        jMenu5.setText("Information   ");
        jMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu5ActionPerformed(evt);
            }
        });

        jMenu9.setText("Shannon's Entropy");

        jMenuItemwholegenomeentropy.setText("Whole Genome Order one");
        jMenuItemwholegenomeentropy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemwholegenomeentropyActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItemwholegenomeentropy);

        jMenuItemsheno1.setText("Order one");
        jMenuItemsheno1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemsheno1ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItemsheno1);

        jMenuItemgcentropy.setText("GC-Entropy");
        jMenuItemgcentropy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemgcentropyActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItemgcentropy);

        jMenuItematentropy.setText("AT-Entropy");
        jMenuItematentropy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItematentropyActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItematentropy);

        jMenu5.add(jMenu9);

        jMenuItemrenyien.setText("Renyi's Entropy");
        jMenuItemrenyien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemrenyienActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItemrenyien);

        jMenuItemengx.setText("Binary Entropy");
        jMenuItemengx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemengxActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItemengx);

        jMenuItemjensonshannon.setText("Redundancy-plot");
        jMenuItemjensonshannon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemjensonshannonActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItemjensonshannon);

        jMenuBar1.add(jMenu5);

        jMenuview.setText("Composition    ");

        jMenucomposition.setText("Nucleotide");

        jMenuItemcompall.setText("All");
        jMenuItemcompall.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompallActionPerformed(evt);
            }
        });
        jMenucomposition.add(jMenuItemcompall);

        jMenuItemcompA.setText("Composition of A");
        jMenuItemcompA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompAActionPerformed(evt);
            }
        });
        jMenucomposition.add(jMenuItemcompA);

        jMenuItemcompC.setText("Composition of C");
        jMenuItemcompC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompCActionPerformed(evt);
            }
        });
        jMenucomposition.add(jMenuItemcompC);

        jMenuItemcompG.setText("Composition of G");
        jMenuItemcompG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompGActionPerformed(evt);
            }
        });
        jMenucomposition.add(jMenuItemcompG);

        jMenuItemcompT.setText("Composition of T");
        jMenuItemcompT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompTActionPerformed(evt);
            }
        });
        jMenucomposition.add(jMenuItemcompT);

        jMenuview.add(jMenucomposition);

        jMenu6.setText("Pyr/Pur");

        jMenuItemcompopyr.setText("Pyridine");
        jMenuItemcompopyr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompopyrActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItemcompopyr);

        jMenuItemcompopur.setText("Purine");
        jMenuItemcompopur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompopurActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItemcompopur);

        jMenuItemcompopp.setText("Both");
        jMenuItemcompopp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompoppActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItemcompopp);

        jMenuview.add(jMenu6);

        jMenu7.setText("Amino/Keto");

        jMenuItemcompoamino.setText("Amino");
        jMenuItemcompoamino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompoaminoActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItemcompoamino);

        jMenuItemcompoketo.setText("Keto");
        jMenuItemcompoketo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompoketoActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItemcompoketo);

        jMenuItemcompoak.setText("Both");
        jMenuItemcompoak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompoakActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItemcompoak);

        jMenuview.add(jMenu7);

        jMenu8.setText("Strong/Weak");

        jMenuItemcompostrong.setText("Strong");
        jMenuItemcompostrong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompostrongActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItemcompostrong);

        jMenuItemcompoweak.setText("Weak");
        jMenuItemcompoweak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcompoweakActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItemcompoweak);

        jMenuItemcomposw.setText("Both");
        jMenuItemcomposw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemcomposwActionPerformed(evt);
            }
        });
        jMenu8.add(jMenuItemcomposw);

        jMenuview.add(jMenu8);

        jMenuBar1.add(jMenuview);

        jMenu2.setText("Help");

        jMenuItemsettings.setText("Settings");
        jMenuItemsettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemsettingsActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemsettings);

        jMenuItemabout.setText("About ORIS");
        jMenuItemabout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemaboutActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemabout);

        jMenuItemguide.setText("User Guide");
        jMenuItemguide.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemguideActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemguide);

        jMenuItemjmath.setText("Jmathplot");
        jMenuItemjmath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemjmathActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItemjmath);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelinfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabelopened, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelgi, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldinfo, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
                    .addComponent(jTextFieldopened, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jTextFieldgi, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                        .addComponent(jLabelaccession)
                        .addGap(30, 30, 30)
                        .addComponent(jTextFieldaccession, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(39, 39, 39))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 553, Short.MAX_VALUE)
                .addComponent(jLabelgenome, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(340, 340, 340))
            .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelgenomecomposition, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButtoncompC)
                                        .addComponent(jButtoncompG, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(jButtoncompT)
                                    .addComponent(jButtoncompA)))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabelglength)))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextFieldglength, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldpercentC, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                                    .addComponent(jTextFieldpercentG, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addComponent(jTextFieldpercentA, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldpercentT, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 959, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelopened, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldopened, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelinfo, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldgi, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelgi, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelaccession, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldaccession, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelgenome, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 84, Short.MAX_VALUE)
                        .addComponent(jLabelgenomecomposition, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldglength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelglength))
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldpercentA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtoncompA))
                        .addGap(36, 36, 36)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldpercentT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtoncompT))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldpercentC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtoncompC))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldpercentG, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtoncompG))
                        .addGap(69, 69, 69)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextFieldopenedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldopenedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldopenedActionPerformed

    private void jMenuopenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuopenActionPerformed
        // TODO add your handling code here:

        JFileChooser chooser = new JFileChooser(DEFAULT_OPEN_DIR);

        int status = chooser.showOpenDialog(jLabelopened);

        if (status == JFileChooser.APPROVE_OPTION) {
            File chosenFile = chooser.getSelectedFile();
            try {
                jTextFieldopened.setText(chosenFile.getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(Form2.class.getName()).log(Level.SEVERE, null, ex);
            }
            Readfile ob = new Readfile();
            filedata = ob.newread(jTextFieldopened.getText());

            //clear all fields for new file read
            jTextFieldrotation.setText("0000000");
            jTextAreagseq.setText(null);
            jTextFieldaccession.setText(null);
            jTextFieldgi.setText(null);
            jTextFieldglength.setText(null);
            jTextFieldinfo.setText(null);
            jTextFieldpercentA.setText(null);
            jTextFieldpercentG.setText(null);
            jTextFieldpercentC.setText(null);
            jTextFieldpercentT.setText(null);

            //set new file data
            jTextAreagseq.setText(filedata);
            if (!(ob.returngi().isEmpty())) {
                jTextFieldgi.setText(ob.returngi());
            }
            if (!(ob.returnaccession().isEmpty())) {
                jTextFieldaccession.setText(ob.returnaccession());
            }
            if (!(ob.returninfo().isEmpty())) {
                jTextFieldinfo.setText(ob.returninfo());
            }
            jTextFieldglength.setText(ob.returnglength());
            sequencelength = Integer.parseInt(ob.returnglength());
            jTextFieldpercentA.setText(ob.returnpercentA() + "%");
            jTextFieldpercentC.setText(ob.returnpercentC() + "%");
            jTextFieldpercentG.setText(ob.returnpercentG() + "%");
            jTextFieldpercentT.setText(ob.returnpercentT() + "%");
            filereadflag = 1;

        }
    }//GEN-LAST:event_jMenuopenActionPerformed

    private void jMenuexitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuexitActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jMenuexitActionPerformed

    private void jMenuItemgcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemgcActionPerformed
        // TODO add your handling code here:

        if (filereadflag == 1) {
            Skewform ob = new Skewform("gc");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jMenuItemgcActionPerformed

    private void jMenuItematActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItematActionPerformed
        // TODO add your handling code here:

        if (filereadflag == 1) {
            Skewform ob = new Skewform("at");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItematActionPerformed

    private void jMenuItemCgcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCgcActionPerformed
        // TODO add your handling code here:

        if (filereadflag == 1) {
            Skewform ob = new Skewform("cgc");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemCgcActionPerformed

    private void jMenuItemcorrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcorrActionPerformed
        // TODO add your handling code here:
        //    Readfile ob = new Readfile();
//        Correlation ob2 = new Correlation(ob.returnseq());
        //      ob2.start();
        if (filereadflag == 1) {
            Correlationform ob = new Correlationform("corr");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jMenuItemcorrActionPerformed

    private void jMenuItemzcurveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemzcurveActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Zcurveform ob = new Zcurveform();
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jMenuItemzcurveActionPerformed

    private void jMenuItemaboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemaboutActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(rootPane, "ORIS Origin Software\nhttp://ccbb.jnu.ac.in/oris/\nVersion 1.0 \nNo Warranty,Open Source, GPL\n", "About", 1);
    }//GEN-LAST:event_jMenuItemaboutActionPerformed

    private void jButtonfindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonfindActionPerformed
        // TODO add your handling code here:
        int stringemptyflag = 0;
        if (jTextFieldfind.getText().isEmpty()) {
            stringemptyflag = 1;
        }

        if (filereadflag == 1 && stringemptyflag == 0) {

            //do upper case in search if not
            jTextFieldfind.setText(jTextFieldfind.getText().toUpperCase());
            //remove spaces from search string
            String searchstr = jTextFieldfind.getText();
            while (searchstr.contains(" ")) {
                searchstr = searchstr.replaceFirst(" ", "");
            }
            Highlighter h = jTextAreagseq.getHighlighter();
            h.removeAllHighlights();
            int[] pos;

            Readfile ob = new Readfile();

            //System.out.printf("%s\t", jTextFieldfind.getText());
            int searchlen = searchstr.length();
            //System.out.printf(" length is %d", searchlen);
            Searchseq ob2;
            ob2 = new Searchseq(ob.returnseq(), searchstr.toCharArray(), 0, 0, 0, 0, null, false);
            ob2.run();
            pos = ob2.posarray;
            if (pos != null) {
                int offset;
                for (int i = 0; i < pos.length; i++) {
                    //System.out.printf("%d\n",pos[i]);
                    offset = pos[i] / 70;
                    // System.out.printf("offset=%d\n", offset);
                    try {
                        
                        String thisOS= System.getProperty("os.name");
                        if(thisOS.contains("Windows")){
                            //for windows
                         h.addHighlight((pos[i] - 1) + (offset * 2), ((pos[i] - 1) + searchlen + (offset * 2)), DefaultHighlighter.DefaultPainter);
                        }else{
                           //for linux
                            h.addHighlight((pos[i] - 1) + (offset), ((pos[i] - 1) + searchlen + (offset)), DefaultHighlighter.DefaultPainter);
                        }
                        

                        
                    } catch (BadLocationException ex) {
                        Logger.getLogger(Form2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, "No File Read or Search String is empty!!!");
        }


    }//GEN-LAST:event_jButtonfindActionPerformed

    private void jMenuItemsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemsearchActionPerformed
        // TODO add your handling code here:

        if (filereadflag == 1) {
            Searchform ob = new Searchform();
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jMenuItemsearchActionPerformed

    private void jButtonopenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonopenActionPerformed
        // TODO add your handling code here:

        JFileChooser chooser = new JFileChooser(DEFAULT_OPEN_DIR);
        //chooser.addChoosableFileFilter(ff);

        int status = chooser.showOpenDialog(jLabelopened);

        if (status == JFileChooser.APPROVE_OPTION) {
            File chosenFile = chooser.getSelectedFile();
            try {
                jTextFieldopened.setText(chosenFile.getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(Form2.class.getName()).log(Level.SEVERE, null, ex);
            }
            Readfile ob = new Readfile();
            filedata = ob.newread(jTextFieldopened.getText());
            //clear all fields for new file read
            jTextFieldrotation.setText("0000000");
            jTextAreagseq.setText(null);
            jTextFieldaccession.setText(null);
            jTextFieldgi.setText(null);
            jTextFieldglength.setText(null);
            jTextFieldinfo.setText(null);
            jTextFieldpercentA.setText(null);
            jTextFieldpercentG.setText(null);
            jTextFieldpercentC.setText(null);
            jTextFieldpercentT.setText(null);
            jTextAreagseq.setText(filedata);

            //set new data
            jTextFieldgi.setText(ob.returngi());
            jTextFieldaccession.setText(ob.returnaccession());
            jTextFieldinfo.setText(ob.returninfo());
            jTextFieldglength.setText(ob.returnglength());
            sequencelength = Integer.parseInt(ob.returnglength());
            jTextFieldpercentA.setText(ob.returnpercentA() + "%");
            jTextFieldpercentC.setText(ob.returnpercentC() + "%");
            jTextFieldpercentG.setText(ob.returnpercentG() + "%");
            jTextFieldpercentT.setText(ob.returnpercentT() + "%");

            filereadflag = 1;

        }
    }//GEN-LAST:event_jButtonopenActionPerformed

    private void jMenuItemCatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCatActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("cat");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jMenuItemCatActionPerformed

    private void jMenuItemcorrwholeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcorrwholeActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Wholecorr ob = new Wholecorr();
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jMenuItemcorrwholeActionPerformed

    private void jButtoncompAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtoncompAActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compA");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jButtoncompAActionPerformed

    private void jButtoncompGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtoncompGActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compG");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jButtoncompGActionPerformed

    private void jButtoncompTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtoncompTActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compT");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jButtoncompTActionPerformed

    private void jButtoncompCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtoncompCActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compC");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jButtoncompCActionPerformed

    private void jMenuItemcompAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompAActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compA");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompAActionPerformed

    private void jMenuItemcompCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompCActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compC");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompCActionPerformed

    private void jMenuItemcompGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompGActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compG");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompGActionPerformed

    private void jMenuItemcompTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompTActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compT");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompTActionPerformed

    private void jMenuItemcompallActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompallActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compall");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompallActionPerformed

    private void jMenuItembendingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItembendingActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Trimerbendindform ob = new Trimerbendindform();
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jMenuItembendingActionPerformed

    private void jMenuItemcompopyrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompopyrActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("comppyr");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompopyrActionPerformed

    private void jMenuItemcompopurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompopurActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("comppur");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompopurActionPerformed

    private void jMenuItemcompoppActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompoppActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("comppyrpur");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompoppActionPerformed

    private void jMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu5ActionPerformed

    private void jMenuItemsheno1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemsheno1ActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("shentropy1");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemsheno1ActionPerformed

    private void jMenuItemcompoweakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompoweakActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compoweak");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompoweakActionPerformed

    private void jMenuItemcompoaminoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompoaminoActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compoam");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompoaminoActionPerformed

    private void jMenuItemcompoketoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompoketoActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compoke");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompoketoActionPerformed

    private void jMenuItemcompoakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompoakActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compoak");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompoakActionPerformed

    private void jMenuItemcompostrongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcompostrongActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("compostrng");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcompostrongActionPerformed

    private void jMenuItemcomposwActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcomposwActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("composw");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcomposwActionPerformed

    private void jMenuItemrenyienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemrenyienActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("renyien");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemrenyienActionPerformed

    private void jMenuItemdnaboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemdnaboxActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Searchdnabox ob = new Searchdnabox();
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemdnaboxActionPerformed

    private void jMenuItemengxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemengxActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("entropygx");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemengxActionPerformed

    private void jMenuItemcrosscorrelationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemcrosscorrelationActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Correlationform ob = new Correlationform("crosscorr");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemcrosscorrelationActionPerformed

    private void jMenuItemsettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemsettingsActionPerformed
        // TODO add your handling code here:
        Settingsform ob = new Settingsform();
        ob.setVisible(true);
    }//GEN-LAST:event_jMenuItemsettingsActionPerformed

    private void jMenuItemyeastacsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemyeastacsActionPerformed
        // TODO add your handling code here:
        SearchACSform ob = new SearchACSform();
        ob.setVisible(true);
    }//GEN-LAST:event_jMenuItemyeastacsActionPerformed

    private void jMenuItemuserdefinedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemuserdefinedActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("userdefined");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemuserdefinedActionPerformed

    private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu3ActionPerformed

    private void jMenuItemdownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemdownloadActionPerformed
        // TODO add your handling code here:

        try {
            URL downloadurl = new URL("ftp://ftp.ncbi.nlm.nih.gov/genomes/");
            Desktop.getDesktop().browse(downloadurl.toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jMenuItemdownloadActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jButtondownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtondownloadActionPerformed
        // TODO add your handling code here:
        try {
            URL downloadurl = new URL("ftp://ftp.ncbi.nlm.nih.gov/genomes/");
            Desktop.getDesktop().browse(downloadurl.toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButtondownloadActionPerformed

    private void jMenuItemwtmatrixActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemwtmatrixActionPerformed
        // TODO add your handling code here:
        Matrixsearch ob = new Matrixsearch();
        ob.setVisible(true);
    }//GEN-LAST:event_jMenuItemwtmatrixActionPerformed

    private void jMenuItemwholegenomeentropyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemwholegenomeentropyActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Readfile ob = new Readfile();
            wholegenomeshannon ob2 = new wholegenomeshannon(ob.returnseq());
            ob2.run();
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemwholegenomeentropyActionPerformed

    private void jMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu4ActionPerformed

    private void jMenuItemextractseqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemextractseqActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Readfile ob = new Readfile();
            Extractseq ob2 = new Extractseq(ob.returnseq());
            ob2.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemextractseqActionPerformed

    private void jMenuItemguideActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemguideActionPerformed
        // TODO add your handling code here
        UserGuide ob = new UserGuide();
        ob.setVisible(true);
    }//GEN-LAST:event_jMenuItemguideActionPerformed

    private void jMenuItemjmathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemjmathActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Copyright © 2003, Yann RICHET All rights reserved.\nhttps://code.google.com/p/jmathplot/", "About Jmathplot.jar", 1);
    }//GEN-LAST:event_jMenuItemjmathActionPerformed

    private void jMenuItemjensonshannonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemjensonshannonActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("redundancy");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jMenuItemjensonshannonActionPerformed

    private void jButtonrotateantiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonrotateantiActionPerformed
        // TODO add your handling code here:
        int index = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter # nt to rotate"));
        Readfile ob = new Readfile();
        //char[] result = null;
        //char[] orig = ob.returnseq();
        if (index > 0 && index <= sequencelength) {
            ob.rotateseq(index, 0);
            int rvalue = Integer.parseInt(jTextFieldrotation.getText()) - index;
            jTextFieldrotation.setText(String.valueOf(rvalue));

        } else {
            JOptionPane.showMessageDialog(null, "Please check the values");
        }


    }//GEN-LAST:event_jButtonrotateantiActionPerformed

    private void jButtonrotateclockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonrotateclockActionPerformed
        // TODO add your handling code here:
        int index = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter # nt to rotate"));
        Readfile ob = new Readfile();
        //char[] result = null;
        //char[] orig = ob.returnseq();
        if (index > 0 && index <= sequencelength) {
            ob.rotateseq(index, 1);
            int rvalue = Integer.parseInt(jTextFieldrotation.getText()) + index;
            jTextFieldrotation.setText(String.valueOf(rvalue));

        } else {
            JOptionPane.showMessageDialog(null, "Please check the values");
        }

    }//GEN-LAST:event_jButtonrotateclockActionPerformed

    private void jMenuItempredictActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItempredictActionPerformed
        // TODO add your handling code here:
        Predictionform ob = new Predictionform();
        ob.setVisible(true);

    }//GEN-LAST:event_jMenuItempredictActionPerformed

    private void jMenuItemmkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemmkActionPerformed
        // TODO add your handling code here:

        if (filereadflag == 1) {
            Skewform ob = new Skewform("mk");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jMenuItemmkActionPerformed

    private void jMenuItemCmkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCmkActionPerformed
        // TODO add your handling code here:

        if (filereadflag == 1) {
            Skewform ob = new Skewform("cmk");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }

    }//GEN-LAST:event_jMenuItemCmkActionPerformed

    private void RYskewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RYskewActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("ry");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_RYskewActionPerformed

    private void jMenuItemCryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCryActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("cry");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemCryActionPerformed

    private void jMenuItemgcentropyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemgcentropyActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("shentropyGC");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItemgcentropyActionPerformed

    private void jMenuItematentropyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItematentropyActionPerformed
        // TODO add your handling code here:
        if (filereadflag == 1) {
            Skewform ob = new Skewform("shentropyAT");
            ob.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "No File Read !!!");
        }
    }//GEN-LAST:event_jMenuItematentropyActionPerformed

//function to get path of ORIS
    public static String getjarPath() throws UnsupportedEncodingException {
        URL url = Form2.class.getProtectionDomain().getCodeSource().getLocation();
        String jarpath = URLDecoder.decode(url.getFile(), "UTF-8");
        String parentpath = new File(jarpath).getParentFile().getPath();
        return parentpath;
    }

    /**
     * display Form2
     */
    public static void callmain() {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Form2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new Form2().setVisible(true);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Form2.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Form2.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem RYskew;
    private javax.swing.JButton jButtoncompA;
    private javax.swing.JButton jButtoncompC;
    private javax.swing.JButton jButtoncompG;
    private javax.swing.JButton jButtoncompT;
    private javax.swing.JButton jButtondownload;
    private javax.swing.JButton jButtonfind;
    private javax.swing.JButton jButtonopen;
    private javax.swing.JButton jButtonrotateanti;
    private javax.swing.JButton jButtonrotateclock;
    private javax.swing.JLabel jLabelaccession;
    private javax.swing.JLabel jLabelgenome;
    private javax.swing.JLabel jLabelgenomecomposition;
    private javax.swing.JLabel jLabelgi;
    private javax.swing.JLabel jLabelglength;
    private javax.swing.JLabel jLabelinfo;
    private javax.swing.JLabel jLabelopened;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemCat;
    private javax.swing.JMenuItem jMenuItemCgc;
    private javax.swing.JMenuItem jMenuItemCmk;
    private javax.swing.JMenuItem jMenuItemCry;
    private javax.swing.JMenuItem jMenuItemabout;
    private javax.swing.JMenuItem jMenuItemat;
    private javax.swing.JMenuItem jMenuItematentropy;
    private javax.swing.JMenuItem jMenuItembending;
    private javax.swing.JMenuItem jMenuItemcompA;
    private javax.swing.JMenuItem jMenuItemcompC;
    private javax.swing.JMenuItem jMenuItemcompG;
    private javax.swing.JMenuItem jMenuItemcompT;
    private javax.swing.JMenuItem jMenuItemcompall;
    private javax.swing.JMenuItem jMenuItemcompoak;
    private javax.swing.JMenuItem jMenuItemcompoamino;
    private javax.swing.JMenuItem jMenuItemcompoketo;
    private javax.swing.JMenuItem jMenuItemcompopp;
    private javax.swing.JMenuItem jMenuItemcompopur;
    private javax.swing.JMenuItem jMenuItemcompopyr;
    private javax.swing.JMenuItem jMenuItemcompostrong;
    private javax.swing.JMenuItem jMenuItemcomposw;
    private javax.swing.JMenuItem jMenuItemcompoweak;
    private javax.swing.JMenuItem jMenuItemcorr;
    private javax.swing.JMenuItem jMenuItemcorrwhole;
    private javax.swing.JMenuItem jMenuItemcrosscorrelation;
    private javax.swing.JMenuItem jMenuItemdnabox;
    private javax.swing.JMenuItem jMenuItemdownload;
    private javax.swing.JMenuItem jMenuItemengx;
    private javax.swing.JMenuItem jMenuItemextractseq;
    private javax.swing.JMenuItem jMenuItemgc;
    private javax.swing.JMenuItem jMenuItemgcentropy;
    private javax.swing.JMenuItem jMenuItemguide;
    private javax.swing.JMenuItem jMenuItemjensonshannon;
    private javax.swing.JMenuItem jMenuItemjmath;
    private javax.swing.JMenuItem jMenuItemmk;
    private javax.swing.JMenuItem jMenuItempredict;
    private javax.swing.JMenuItem jMenuItemrenyien;
    private javax.swing.JMenuItem jMenuItemsearch;
    private javax.swing.JMenuItem jMenuItemsettings;
    private javax.swing.JMenuItem jMenuItemsheno1;
    private javax.swing.JMenuItem jMenuItemuserdefined;
    private javax.swing.JMenuItem jMenuItemwholegenomeentropy;
    private javax.swing.JMenuItem jMenuItemwtmatrix;
    private javax.swing.JMenuItem jMenuItemyeastacs;
    private javax.swing.JMenuItem jMenuItemzcurve;
    private javax.swing.JMenu jMenuProfile;
    private javax.swing.JMenu jMenucomposition;
    private javax.swing.JMenu jMenucorr;
    private javax.swing.JMenu jMenucummulative;
    private javax.swing.JMenuItem jMenuexit;
    private javax.swing.JMenuItem jMenuopen;
    private javax.swing.JMenu jMenuskew;
    private javax.swing.JMenu jMenuview;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextAreagseq;
    private javax.swing.JTextField jTextFieldaccession;
    private javax.swing.JTextField jTextFieldfind;
    private javax.swing.JTextField jTextFieldgi;
    private javax.swing.JTextField jTextFieldglength;
    private javax.swing.JTextField jTextFieldinfo;
    private javax.swing.JTextField jTextFieldopened;
    private javax.swing.JTextField jTextFieldpercentA;
    private javax.swing.JTextField jTextFieldpercentC;
    private javax.swing.JTextField jTextFieldpercentG;
    private javax.swing.JTextField jTextFieldpercentT;
    private javax.swing.JTextField jTextFieldrotation;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables
}
