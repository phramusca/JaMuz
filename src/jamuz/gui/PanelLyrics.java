/*
 * Copyright (C) 2014 phramusca ( https://github.com/phramusca/JaMuz/ )
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jamuz.gui;

import jamuz.FileInfoInt;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import jamuz.utils.Inter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelLyrics extends javax.swing.JPanel {

    /**
     * Creates new form PanelLyrics
     */
    public PanelLyrics() {
        initComponents();
        initExtended();
    }
    
    /**
     * set text
     * @param lyrics
     */
    public static void setText(String lyrics) {
        jTextPaneLyrics.setText(lyrics);
        jTextPaneLyrics.setCaretPosition(0); //Scroll to top
    }
    
	/**
	 *
	 * @param currentPosition
	 * @param length
	 */
	public static void setPosition(int currentPosition, int length) {
        if(jCheckBoxLyricsScroll.isSelected()) {
            jTextPaneLyrics.setCaretPosition((currentPosition*jTextPaneLyrics.getDocument().getLength())/(length*1000));
            
            //TODO: Allow user to be able to sync by moving caret
//        int caret = jTextPaneLyrics.getCaretPosition();
//        jTextPaneLyrics.setCaretPosition(((currentPosition*(jTextPaneLyrics.getDocument().getLength()-caret))/(length*1000))+caret);
        }
    }

    private void initExtended() {   
        //TODO: Make this an option
//        jTextPaneLyrics.setFont(new java.awt.Font("DejaVu Sans", 1, 48)); // NOI18N
        
        //TODO: How to change jTextPaneLyrics background ? 
        //Looks like nimbus look&feel is preventing changing background
        //=> BUG LISTED: https://bugs.openjdk.java.net/browse/JDK-8058704
        //Indeed, this example works: http://stackoverflow.com/questions/19435181/how-to-set-default-background-color-for-jtextpane
//TODO: Try this workaround: https://community.oracle.com/thread/1356459?tstart=0        

//This one does not help: http://stackoverflow.com/questions/22674575/jtextpane-background-color
//        Color bgColor = Color.BLACK;
//        UIDefaults defaults = new UIDefaults();
//        defaults.put("EditorPane[Enabled].backgroundPainter", bgColor);
//        jTextPaneLyrics.putClientProperty("Nimbus.Overrides", defaults);
//        jTextPaneLyrics.putClientProperty("Nimbus.Overrides.InheritDefaults", true);
//        jTextPaneLyrics.setBackground(bgColor);
        
        
        //This is needed to apply the ALIGN_CENTER attribute
        StyledDocument doc = jTextPaneLyrics.getStyledDocument();
        Style style = doc.addStyle("centered", null);
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), doc.getStyle("centered"), false);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPaneLyrics = new javax.swing.JTextPane();
        jButtonLyricsSave = new javax.swing.JButton();
        jButtonLyricsCancel = new javax.swing.JButton();
        jCheckBoxLyricsScroll = new javax.swing.JCheckBox();

        jTextPaneLyrics.setBackground(java.awt.Color.lightGray);
        jTextPaneLyrics.setFont(new java.awt.Font("DejaVu Sans", 1, 48)); // NOI18N
        jScrollPane3.setViewportView(jTextPaneLyrics);

        jButtonLyricsSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        jButtonLyricsSave.setText(Inter.get("Button.Save")); // NOI18N
        jButtonLyricsSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLyricsSaveActionPerformed(evt);
            }
        });

        jButtonLyricsCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        jButtonLyricsCancel.setText(Inter.get("Button.Cancel")); // NOI18N
        jButtonLyricsCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLyricsCancelActionPerformed(evt);
            }
        });

        jCheckBoxLyricsScroll.setText("Auto Scroll");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(62, 378, Short.MAX_VALUE)
                .addComponent(jCheckBoxLyricsScroll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLyricsCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLyricsSave)
                .addContainerGap())
            .addComponent(jScrollPane3)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonLyricsSave)
                    .addComponent(jButtonLyricsCancel)
                    .addComponent(jCheckBoxLyricsScroll))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonLyricsSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLyricsSaveActionPerformed
        FileInfoInt myFileInfo = getPlayingSong();
        if(myFileInfo!=null) {
            myFileInfo.saveMetadataLyrics(jTextPaneLyrics.getText());
        }
    }//GEN-LAST:event_jButtonLyricsSaveActionPerformed

	private FileInfoInt getPlayingSong() {
		FileInfoInt fileInfoInt = null;
		if(PanelMain.getQueueModel().getPlayingSong()!=null) {
			fileInfoInt = PanelMain.getQueueModel().getPlayingSong().getFile();
		}
		return fileInfoInt;
	}
	
    private void jButtonLyricsCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLyricsCancelActionPerformed
        FileInfoInt myFileInfo = getPlayingSong();
        if(myFileInfo!=null) {
			setText(myFileInfo.getLyrics());
		}
    }//GEN-LAST:event_jButtonLyricsCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonLyricsCancel;
    private javax.swing.JButton jButtonLyricsSave;
    private static javax.swing.JCheckBox jCheckBoxLyricsScroll;
    private javax.swing.JScrollPane jScrollPane3;
    private static javax.swing.JTextPane jTextPaneLyrics;
    // End of variables declaration//GEN-END:variables
}
