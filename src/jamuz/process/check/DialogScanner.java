/*
 * Copyright (C) 2016 phramusca ( https://github.com/phramusca/JaMuz/ )
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
package jamuz.process.check;

import jamuz.FileInfoInt;
import jamuz.Jamuz;
import jamuz.gui.swing.TableModel;
import jamuz.utils.Inter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogScanner extends javax.swing.JDialog {

    private static TableModel tableModel;
    /**
     * Creates new form NewJFrame
     * @param parent
     * @param modal
     * @param path
     */
    public DialogScanner(java.awt.Frame parent, boolean modal, String path) {
        super(parent, modal);
        initComponents();
        jTextFieldPath.setText(path);
		jLabelAlbum.setText("%b : "+Inter.get("Tag.Album"));
		jLabelAlbumArtist.setText("%z : "+Inter.get("Tag.AlbumArtist"));
		jLabelArtist.setText("%a : "+Inter.get("Tag.Artist"));
		jLabelComment.setText("%c : "+Inter.get("Tag.Comment"));
		jLabelDiscNo.setText("%d : "+Inter.get("Tag.DiscNo"));
		jLabelDiscTotal.setText("%x : "+Inter.get("Tag.DiscTotal"));
		jLabelTitle.setText("%t : "+Inter.get("Tag.Title"));
		jLabelTrackNo.setText("%n : "+Inter.get("Tag.TrackNo"));
		jLabelTrackTotal.setText("%l : "+Inter.get("Tag.TrackTotal"));
		jLabelYear.setText("%y : "+Inter.get("Tag.Year"));
        patterns = new ArrayList<>();
		try {
			File f = Jamuz.getFile("Patterns.txt", "data");
			if(!f.exists()) {
				f.createNewFile();
			}
			patterns = Files.readAllLines(Paths.get(f.getAbsolutePath()), Charset.defaultCharset());
		} catch (IOException ex) {
			Jamuz.getLogger().log(Level.SEVERE, null, ex);
		}
        jTextFieldPattern.getDocument().addDocumentListener(new DocumentListener(){ 
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyPattern();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyPattern();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Not used when document is plain text
            }
        });
		tableModel = (TableModel) jTableScanner.getModel();
		String[] columnNames =  { "#", Inter.get("Tag.AlbumArtist"), Inter.get("Tag.Album"), Inter.get("Tag.TrackNo"), "/", 
            Inter.get("Tag.DiscNo"), "/", Inter.get("Tag.Artist"), Inter.get("Tag.Title"), Inter.get("Tag.Year"), Inter.get("Tag.Comment")};  //NOI18N
		Object[][] data = {
			{0, "Default", "Default", "Default", "Default", "Default", "Default", "Default", "Default", "Default", "Default"}  //NOI18N
		};
		tableModel.setModel(columnNames, data);
		tableModel.clear();
		//Adding columns from model: Cannot be done automatically on properties
		// as done, in initComponents, before setColumnModel which removes the columns ...
		jTableScanner.createDefaultColumnsFromModel();
		
		TableColumn column;
		
        column = jTableScanner.getColumnModel().getColumn(0); //# extracted
		column.setMinWidth(20);
		column.setMaxWidth(20);
        column = jTableScanner.getColumnModel().getColumn(1); //album artist
		column.setMinWidth(50);
		column.setPreferredWidth(150);
        column = jTableScanner.getColumnModel().getColumn(2); //album
		column.setMinWidth(80);
		column.setPreferredWidth(150);
        column = jTableScanner.getColumnModel().getColumn(3); //track no
		column.setMinWidth(50);
		column.setMaxWidth(50);
        column = jTableScanner.getColumnModel().getColumn(4); //track total
		column.setMinWidth(50);
		column.setMaxWidth(50);
        column = jTableScanner.getColumnModel().getColumn(5); //disc no
		column.setMinWidth(40);
		column.setMaxWidth(40);
        column = jTableScanner.getColumnModel().getColumn(6); //disc total
		column.setMinWidth(30);
		column.setMaxWidth(30);
        column = jTableScanner.getColumnModel().getColumn(7); //artist
		column.setMinWidth(50);
		column.setPreferredWidth(100);
        column = jTableScanner.getColumnModel().getColumn(8); //title
		column.setMinWidth(50);
		column.setPreferredWidth(150);
        column = jTableScanner.getColumnModel().getColumn(9); //year
		column.setMinWidth(50);
		column.setPreferredWidth(50);
        column = jTableScanner.getColumnModel().getColumn(10); //comment
		column.setMinWidth(20);
		column.setPreferredWidth(20);

        //TODO: Do the same for other badly called mouseClicked event on JTable instead of below
        //http://stackoverflow.com/questions/10467258/netbeans-how-do-i-add-a-valuechanged-listener-to-a-jtable-from-the-design-gu
        
        jTableScanner.getSelectionModel().addListSelectionListener((ListSelectionEvent evt) -> {
			//Getting selected File
			int selectedRow = jTableScanner.getSelectedRow();
			if (selectedRow >= 0) {
				//convert to model index (as sortable model)
				selectedRow = jTableScanner.convertRowIndexToModel(selectedRow);
				jTextFieldPattern.setText(patterns.get(selectedRow));
				applyPattern(); 	  //NOI18N
			}
		});
        
        for(String pattern : patterns) {
            Map<String, String> extracted = PatternProcessor.getMap(path, pattern);
			addRow(extracted);
        }
        
        //Enable row tableSorter (cannot be done if model is empty)
		if(tableModel.getRowCount()>0) {
			//Enable auto sorter
			jTableScanner.setAutoCreateRowSorter(true);
			//Sort by action, result
			TableRowSorter<TableModel> tableSorter = new TableRowSorter<>(tableModel);
			jTableScanner.setRowSorter(tableSorter);
			List <RowSorter.SortKey> sortKeys = new ArrayList<>();
			sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
//			sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
			tableSorter.setSortKeys(sortKeys);
			jTableScanner.getSelectionModel().setSelectionInterval(0, 0);
		}
		else {
			jTableScanner.setAutoCreateRowSorter(false);
		}
        
        
    }
    
    private static void addRow(Map<String, String> extracted) {
        
//        String[] columnNames =  { "#", Inter.get("Tag.AlbumArtist"), 
//				Inter.get("Tag.Album"), Inter.get("Tag.TrackNo"), "/", 
//				Inter.get("Tag.DiscNo"), "/", Inter.get("Tag.Artist"), 
//				Inter.get("Tag.Title"), Inter.get("Tag.Year"), 
//				Inter.get("Tag.Comment")};  //NOI18N
        
        Object[] data = new Object[]{
            extracted.size(),
			extracted.containsKey("%z")?extracted.get("%z"):"", 
            extracted.containsKey("%b")?extracted.get("%b"):"", 
            extracted.containsKey("%n")?extracted.get("%n"):"", 
            extracted.containsKey("%l")?extracted.get("%l"):"", 
            extracted.containsKey("%d")?extracted.get("%d"):"", 
            extracted.containsKey("%x")?extracted.get("%x"):"", 
            extracted.containsKey("%a")?extracted.get("%a"):"", 
            extracted.containsKey("%t")?extracted.get("%t"):"", 
            extracted.containsKey("%y")?extracted.get("%y"):"", 
            extracted.containsKey("%c")?extracted.get("%c"):""  //NOI18N
		};
		tableModel.addRow(data);
	}
    
    private List<String> patterns;
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextFieldPath = new javax.swing.JTextField();
        jScrollPaneCheckTags3 = new javax.swing.JScrollPane();
        jTableScanner = new jamuz.gui.swing.TableHorizontal();
        jTextFieldPattern = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButtonCancel = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelYearValue = new javax.swing.JLabel();
        jLabelTrackTotal = new javax.swing.JLabel();
        jLabelTrackNoValue = new javax.swing.JLabel();
        jLabelAlbumArtistValue = new javax.swing.JLabel();
        jLabelDiscNo = new javax.swing.JLabel();
        jLabelDiscNoValue = new javax.swing.JLabel();
        jLabelYear = new javax.swing.JLabel();
        jLabelCommentValue = new javax.swing.JLabel();
        jLabelTrackNo = new javax.swing.JLabel();
        jLabelAlbumValue = new javax.swing.JLabel();
        jLabelComment = new javax.swing.JLabel();
        jLabelArtist = new javax.swing.JLabel();
        jLabelTitle = new javax.swing.JLabel();
        jLabelTrackTotalValue = new javax.swing.JLabel();
        jLabelArtistValue = new javax.swing.JLabel();
        jLabelDiscTotalValue = new javax.swing.JLabel();
        jLabelDiscTotal = new javax.swing.JLabel();
        jLabelAlbum = new javax.swing.JLabel();
        jLabelAlbumArtist = new javax.swing.JLabel();
        jLabelTitleValue = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTextFieldPath.setEditable(false);

        jTableScanner.setAutoCreateColumnsFromModel(false);
        jTableScanner.setModel(new jamuz.gui.swing.TableModel());
        jTableScanner.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPaneCheckTags3.setViewportView(jTableScanner);

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jButtonCancel.setText(bundle.getString("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        jButtonSave.setText(bundle.getString("Button.Save")); // NOI18N
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addComponent(jButtonCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSave)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jLabelYearValue.setText("jLabelYearValue");

        jLabelTrackTotal.setText("jLabelTrackTotal");

        jLabelTrackNoValue.setText("jLabelTrackNoValue");

        jLabelAlbumArtistValue.setText("jLabelAlbumArtistValue");

        jLabelDiscNo.setText("jLabelDiscNo");

        jLabelDiscNoValue.setText("jLabelDiscNoValue");

        jLabelYear.setText("jLabelYear");

        jLabelCommentValue.setText("jLabelCommentValue");

        jLabelTrackNo.setText("jLabelTrackNo");

        jLabelAlbumValue.setText("jLabelAlbumValue");

        jLabelComment.setText("jLabelComment");

        jLabelArtist.setText("jLabelArtist");

        jLabelTitle.setText("jLabelTitle");

        jLabelTrackTotalValue.setText("jLabelTrackTotalValue");

        jLabelArtistValue.setText("jLabelArtistValue");

        jLabelDiscTotalValue.setText("jLabelDiscTotalValue");

        jLabelDiscTotal.setText("jLabelDiscTotal");

        jLabelAlbum.setText("jLabelAlbum");

        jLabelAlbumArtist.setText("jLabelAlbumArtist");

        jLabelTitleValue.setText("jLabelTitleValue");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelTrackTotal)
                    .addComponent(jLabelDiscTotal)
                    .addComponent(jLabelTrackNo)
                    .addComponent(jLabelDiscNo)
                    .addComponent(jLabelYear))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelDiscNoValue)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelTrackTotalValue)
                            .addComponent(jLabelTrackNoValue)
                            .addComponent(jLabelDiscTotalValue)
                            .addComponent(jLabelYearValue))
                        .addGap(59, 59, 59)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabelTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelComment, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelAlbumValue)
                                    .addComponent(jLabelTitleValue)
                                    .addComponent(jLabelCommentValue)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabelAlbumArtist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelArtist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelArtistValue)
                                    .addComponent(jLabelAlbumArtistValue))))))
                .addContainerGap(244, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTrackNo)
                    .addComponent(jLabelTrackNoValue)
                    .addComponent(jLabelArtist)
                    .addComponent(jLabelArtistValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTrackTotal)
                    .addComponent(jLabelTrackTotalValue)
                    .addComponent(jLabelAlbumArtist)
                    .addComponent(jLabelAlbumArtistValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDiscTotal)
                    .addComponent(jLabelDiscTotalValue)
                    .addComponent(jLabelAlbum)
                    .addComponent(jLabelAlbumValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDiscNo)
                    .addComponent(jLabelDiscNoValue)
                    .addComponent(jLabelTitle)
                    .addComponent(jLabelTitleValue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelYear)
                    .addComponent(jLabelYearValue)
                    .addComponent(jLabelComment)
                    .addComponent(jLabelCommentValue))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldPath)
                    .addComponent(jTextFieldPattern)
                    .addComponent(jScrollPaneCheckTags3)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextFieldPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldPattern, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButtonCancelActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        String pattern = jTextFieldPattern.getText();
        
        if(!patterns.contains(pattern)) {
            patterns.add(pattern);
            File f = Jamuz.getFile("Patterns.txt", "data");
            try {
                try (Writer output = new BufferedWriter(new FileWriter(f.getAbsolutePath(), true))) {
                    output.append(pattern).append("\n");
                }
            } catch (IOException ex) {
                Logger.getLogger(DialogScanner.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        DialogCheck.applyPattern(pattern);
        this.dispose();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void applyPattern() {
		FileInfoInt file = PatternProcessor.get(jTextFieldPath.getText(), jTextFieldPattern.getText());
        jLabelAlbumValue.setText(file.getAlbum());
        jLabelAlbumArtistValue.setText(file.getAlbumArtist());
        jLabelArtistValue.setText(file.getArtist());
        jLabelCommentValue.setText(file.getComment());
        jLabelDiscNoValue.setText(String.valueOf(file.getDiscNo()));
        jLabelDiscTotalValue.setText(String.valueOf(file.getDiscTotal()));
        jLabelTitleValue.setText(file.getTitle());
        jLabelTrackNoValue.setText(String.valueOf(file.getTrackNo()));
        jLabelTrackTotalValue.setText(String.valueOf(file.getTrackTotal()));
        jLabelYearValue.setText(file.getYear());
		
    }

    /**
     * @param args the command line arguments
     * @param path
     */
    public static void main(String args[], String path) {
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogScanner.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        DialogScanner dialog = new DialogScanner(new JFrame(), true, path);
        dialog.setLocationRelativeTo(dialog.getParent());
        dialog.setVisible(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JLabel jLabelAlbum;
    private javax.swing.JLabel jLabelAlbumArtist;
    private javax.swing.JLabel jLabelAlbumArtistValue;
    private javax.swing.JLabel jLabelAlbumValue;
    private javax.swing.JLabel jLabelArtist;
    private javax.swing.JLabel jLabelArtistValue;
    private javax.swing.JLabel jLabelComment;
    private javax.swing.JLabel jLabelCommentValue;
    private javax.swing.JLabel jLabelDiscNo;
    private javax.swing.JLabel jLabelDiscNoValue;
    private javax.swing.JLabel jLabelDiscTotal;
    private javax.swing.JLabel jLabelDiscTotalValue;
    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelTitleValue;
    private javax.swing.JLabel jLabelTrackNo;
    private javax.swing.JLabel jLabelTrackNoValue;
    private javax.swing.JLabel jLabelTrackTotal;
    private javax.swing.JLabel jLabelTrackTotalValue;
    private javax.swing.JLabel jLabelYear;
    private javax.swing.JLabel jLabelYearValue;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private static javax.swing.JScrollPane jScrollPaneCheckTags3;
    private static javax.swing.JTable jTableScanner;
    private javax.swing.JTextField jTextFieldPath;
    private javax.swing.JTextField jTextFieldPattern;
    // End of variables declaration//GEN-END:variables
}
