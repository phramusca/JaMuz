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
		
		//Display legend
		StringBuilder sb = new StringBuilder();
		sb.append("<html>")
				.append("<table>")
					.append("<tr><td>")
						.append("%z").append(" : ").append(Inter.get("Tag.AlbumArtist"))	//album artist
						.append("</td><td>")
						.append("%a").append(" : ").append(Inter.get("Tag.Artist"))		//artist
					.append("</td></tr>")
					.append("<tr><td>")
						.append("%b").append(" : ").append(Inter.get("Tag.Album"))		//album
						.append("</td><td>")
						.append("%t").append(" : ").append(Inter.get("Tag.Title"))		//title
					.append("</td></tr>")
					.append("<tr><td>")
						.append("%n").append(" : ").append(Inter.get("Tag.TrackNo"))		//track#
						.append("</td><td>")
						.append("%l").append(" : ").append(Inter.get("Tag.TrackTotal"))	//# of tracks
					.append("</td></tr>")
					.append("<tr><td>")
						.append("%d").append(" : ").append(Inter.get("Tag.DiscNo"))		//disc#
						.append("</td><td>")
						.append("%x").append(" : ").append(Inter.get("Tag.DiscTotal"))	//# of discs
					.append("</td></tr>")
					.append("<tr><td>")
						.append("%y").append(" : ").append(Inter.get("Tag.Year"))			//year
							.append("</td><td>")
						.append("%c").append(" : ").append(Inter.get("Tag.Comment")) //comment
					.append("</td></tr>")
				.append("</table>")
			.append("</html>");
		jLabelScannerLegend.setText(sb.toString());

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
        
        //Get jTableSync model
		tableModel = (TableModel) jTableScanner.getModel();
		//Set the model
		String[] columnNames =  { "#", Inter.get("Tag.AlbumArtist"), Inter.get("Tag.Album"), Inter.get("Tag.TrackNo"), "/", 
            Inter.get("Tag.DiscNo"), "/", Inter.get("Tag.Artist"), Inter.get("Tag.Title"), Inter.get("Tag.Year"), Inter.get("Tag.Comment")};  //NOI18N
        
		Object[][] data = {
			{0, "Default", "Default", "Default", "Default", "Default", "Default", "Default", "Default", "Default", "Default"}  //NOI18N
		};
		tableModel.setModel(columnNames, data);
		//Clear the table
		tableModel.clear();
		//Adding columns from model. Cannot be done automatically on properties
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
        
//        String[] columnNames =  { "#", Inter.get("Tag.AlbumArtist"), Inter.get("Tag.Album"), Inter.get("Tag.TrackNo"), "/", 
//            Inter.get("Tag.DiscNo"), "/", Inter.get("Tag.Artist"), Inter.get("Tag.Title"), Inter.get("Tag.Year"), Inter.get("Tag.Comment")};  //NOI18N
        
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
        jButtonCancel = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jLabelExtracted = new javax.swing.JLabel();
        jScrollPaneCheckTags3 = new javax.swing.JScrollPane();
        jTableScanner = new jamuz.gui.swing.TableHorizontal();
        jTextFieldPattern = new javax.swing.JTextField();
        jLabelScannerLegend = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTextFieldPath.setEditable(false);

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

        jLabelExtracted.setText("jLabelExtracted");

        jTableScanner.setAutoCreateColumnsFromModel(false);
        jTableScanner.setModel(new jamuz.gui.swing.TableModel());
        jTableScanner.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPaneCheckTags3.setViewportView(jTableScanner);

        jLabelScannerLegend.setText("Legend"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCheckTags3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabelExtracted, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelScannerLegend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonCancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jTextFieldPath, javax.swing.GroupLayout.DEFAULT_SIZE, 1185, Short.MAX_VALUE)
                    .addComponent(jTextFieldPattern))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextFieldPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldPattern, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabelExtracted, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSave))
                    .addComponent(jLabelScannerLegend, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags3, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
                .addContainerGap())
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
        jLabelExtracted.setText(PatternProcessor.toString(jTextFieldPath.getText(), jTextFieldPattern.getText()));
        
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
    private javax.swing.JLabel jLabelExtracted;
    private javax.swing.JLabel jLabelScannerLegend;
    private static javax.swing.JScrollPane jScrollPaneCheckTags3;
    private static javax.swing.JTable jTableScanner;
    private javax.swing.JTextField jTextFieldPath;
    private javax.swing.JTextField jTextFieldPattern;
    // End of variables declaration//GEN-END:variables
}
