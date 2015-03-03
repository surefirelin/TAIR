/**
 * Copyright (c) 2014, the TEE2 AUTHORS.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * Neither the name of the University of Bari nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * GNU GENERAL PUBLIC LICENSE - Version 3, 29 June 2007
 *
 */
package di.uniba.it.tee2.gui;

import di.uniba.it.tee2.search.SearchResult;

/**
 *
 * @author pierpaolo
 */
public class SearchResultPanel extends javax.swing.JPanel {

    private SearchResult sr;

    /**
     * Creates new form SearchResultPanel
     */
    public SearchResultPanel(SearchResult sr) {
        this.sr = sr;
        initComponents();
        myInit();
    }

    private void myInit() {
        idL.setText(sr.getId());
        snipTA.setText(sr.getSnip());
        scoreL.setText(String.valueOf(sr.getScore()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        idL = new javax.swing.JLabel();
        scoreL = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        snipTA = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(120, 60));
        setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        idL.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        idL.setToolTipText("");
        jPanel1.add(idL);

        scoreL.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel1.add(scoreL);

        add(jPanel1, java.awt.BorderLayout.NORTH);

        snipTA.setEditable(false);
        snipTA.setColumns(40);
        snipTA.setLineWrap(true);
        snipTA.setRows(3);
        snipTA.setMinimumSize(new java.awt.Dimension(100, 40));
        snipTA.setPreferredSize(new java.awt.Dimension(480, 60));
        jScrollPane1.setViewportView(snipTA);

        add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jButton1.setText("Doc");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        add(jButton1, java.awt.BorderLayout.EAST);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DocDialog docDialog = new DocDialog(null, true, sr.getId(), sr.getSnip());
        docDialog.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel idL;
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel scoreL;
    private javax.swing.JTextArea snipTA;
    // End of variables declaration//GEN-END:variables
}
