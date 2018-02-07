/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bioapp;

import genelib.Searchseq;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author jnu
 */
public class Extractseq extends javax.swing.JFrame {

    /**
     * Creates new form Extractseq
     */
    char[] sequence;
    ImageIcon img = new ImageIcon("images/icons/orislogo.png");

    public Extractseq(char[] seq) {
        initComponents();
        sequence = seq;
        this.setResizable(false);
        this.setIconImage(img.getImage());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Extract Sequence");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextFieldstart = new javax.swing.JTextField();
        jTextFieldend = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextFieldfname = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jCheckBoxcomp = new javax.swing.JCheckBox();
        jCheckBoxrev = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Enter start and end positions to get the sequence (positions start from 1)");

        jLabel2.setText("Start :");

        jLabel3.setText("End :");

        jLabel4.setText("Select an option below if required");

        jButton1.setText("GO ");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Enter file name to save as .txt");

        jCheckBoxcomp.setText("Complement");

        jCheckBoxrev.setText("Reverse");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(79, 79, 79)
                        .addComponent(jTextFieldfname, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jTextFieldstart, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(98, 98, 98)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextFieldend, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(160, 160, 160))
                            .addComponent(jLabel1))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBoxrev)
                            .addComponent(jCheckBoxcomp))))
                .addGap(150, 150, 150))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(jLabel4))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(212, 212, 212)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldstart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(31, 31, 31)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxcomp)
                .addGap(18, 18, 18)
                .addComponent(jCheckBoxrev)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldfname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //falgs to know if complement, reverse or reverxse complement is reqd.
        int comflag = 0, revflag = 0, revcomflag = 0;
        int start = 0, end = 0, errorflag = 0;
        String fname = jTextFieldfname.getText().toString();
        start = Integer.parseInt(jTextFieldstart.getText()) - 1;
        end = Integer.parseInt(jTextFieldend.getText()) - 1;

        if (start < 0) {
            errorflag = 1;
            JOptionPane.showMessageDialog(null, "Check start value");
        }

        if ((end > sequence.length - 1) && errorflag == 0) {
            errorflag = 1;
            JOptionPane.showMessageDialog(null, "Check end value");
        }
        if (fname.isEmpty() && errorflag == 0) {
            errorflag = 1;
            JOptionPane.showMessageDialog(null, "Please enter a valid file name");
        }

        if (errorflag == 0) {
            //add save dir before filename
            fname=Form2.DEFAULT_SAVE_DIR+fname;
            char[] extractedseq = new char[end - start + 1];
            //Extract the reqd sequence
            for (int i = 0; i < extractedseq.length; i++) {
                extractedseq[i] = sequence[i + start];
            }

            //System.out.print(extractedseq);
            //check to reverse sequence
            int j = 0;
            if (jCheckBoxrev.isSelected()) {

                char temp;

                j = extractedseq.length - 1;
                for (int i = 0; i < extractedseq.length / 2; i++) {
                    temp = extractedseq[j];
                    extractedseq[j] = extractedseq[i];
                    extractedseq[i] = temp;
                    j--;

                }


            }

            //check to complement sequence
            if (jCheckBoxcomp.isSelected()) {

                for (int i = 0; i < extractedseq.length; i++) {
                    if (extractedseq[i] == 'C') {
                        extractedseq[i] = 'G';
                    } else if (extractedseq[i] == 'G') {
                        extractedseq[i] = 'C';
                    } else if (extractedseq[i] == 'T') {
                        extractedseq[i] = 'A';
                    } else if (extractedseq[i] == 'A') {
                        extractedseq[i] = 'T';
                    } else {
                        //nothing to do
                    }

                }

            }


            //now save extracted sequence in file
            File f = new File(fname + ".txt");
            //ctr to save files in lens of 70 chars
            int ctr = 0;
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(f));
            } catch (IOException ex) {
                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {


                for (int i = 0; i < extractedseq.length; i++) {
                    writer.write(extractedseq[i]);
                    if (i % 70 == 0 && i != 0) {
                        writer.newLine();
                    }

                }
                writer.close();
                JOptionPane.showMessageDialog(null, "File: " + fname + ".txt saved");

            } catch (IOException ex) {

                Logger.getLogger(Searchseq.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Extractseq.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Extractseq.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Extractseq.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Extractseq.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //   new Extractseq().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBoxcomp;
    private javax.swing.JCheckBox jCheckBoxrev;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JTextField jTextFieldend;
    private javax.swing.JTextField jTextFieldfname;
    private javax.swing.JTextField jTextFieldstart;
    // End of variables declaration//GEN-END:variables
}