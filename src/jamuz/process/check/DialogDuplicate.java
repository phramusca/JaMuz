/*
 * Copyright (C) 2019 phramusca ( https://github.com/phramusca/ )
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

import jamuz.IconBufferCover;
import jamuz.gui.DialogCoverDisplay;
import jamuz.gui.PanelCover;
import jamuz.gui.PanelMain;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableCellListener;
import jamuz.gui.swing.TableColumnModel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 *
 * @author phramusca ( https://github.com/phramusca/ )
 */
public class DialogDuplicate extends javax.swing.JDialog {

	private final FolderInfo folder;
	private final DuplicateInfo duplicateInfo;
//	private static TableColumnModel columnModel;
	protected static ProgressBar progressBar;

	/**
	 * Creates new form DialogDuplicate
	 * @param parent
	 * @param modal
	 * @param folder
	 * @param duplicateInfo
	 */
	public DialogDuplicate(java.awt.Frame parent, boolean modal, FolderInfo folder, DuplicateInfo duplicateInfo) {
		super(parent, modal);
		initComponents();
		this.folder = folder;
		this.duplicateInfo = duplicateInfo;
		
		//Display folder
		Map<String, FolderInfoResult> results = folder.getResults(); 
		jLabelCheckAlbumTag.setText(results.get("album").getDisplayText()); //NOI18N
        jLabelCheckAlbumArtistTag.setText(results.get("albumArtist").getDisplayText()); //NOI18N
        jLabelCheckYearTag.setText(results.get("year").getDisplayText());
		jLabelCheckDesc.setText(folder.toString());
		
		BufferedImage myImage = folder.getFirstCoverFromTags();
		PanelCover coverImg = (PanelCover) jPanelCheckCoverThumb;
		if(myImage!=null) {
			coverImg.setImage(myImage);
		}
		else {
			coverImg.setImage(null);
			if(folder.getFilesImage().size()>0) {
				jPanelCheckCoverThumb.setEnabled(true);
			}
			else {
				jPanelCheckCoverThumb.setEnabled(false);
			}
		}
		jTableCheck.setModel(folder.getFilesAudioTableModel());		
		setTable(jTableCheck);
		
		//Display potential duplicate
		
		FolderInfo duplicateFolderInfo = duplicateInfo.getFolderInfo();
		progressBar = (ProgressBar)jProgressBarDuplicate;
		duplicateFolderInfo.browse(false, true, progressBar);
		try {
			duplicateFolderInfo.analyse(progressBar);
			duplicateFolderInfo.analyseMatch(0, progressBar); //Analyse first match
		} catch (CloneNotSupportedException ex) {
			Logger.getLogger(DialogDuplicate.class.getName()).log(Level.SEVERE, null, ex);
		}
		duplicateFolderInfo.analyseMatchTracks();
		duplicateFolderInfo.setAction(); 
		progressBar.reset();
		
		Map<String, FolderInfoResult> resultsDuplicate = duplicateFolderInfo.getResults(); 
		jLabelCheckAlbumTag1.setText(resultsDuplicate.get("album").getDisplayText()); //NOI18N
        jLabelCheckAlbumArtistTag1.setText(resultsDuplicate.get("albumArtist").getDisplayText()); //NOI18N
        jLabelCheckYearTag1.setText(resultsDuplicate.get("year").getDisplayText());
		jLabelCheckDesc1.setText(duplicateFolderInfo.toString());

		BufferedImage imageDuplicate = duplicateFolderInfo.getFirstCoverFromTags();
		PanelCover coverImgDup = (PanelCover) jPanelCheckCoverThumb1;
		if(imageDuplicate!=null) {
			coverImgDup.setImage(imageDuplicate);
		}
		else {
			coverImgDup.setImage(null);
			if(duplicateFolderInfo.getFilesImage().size()>0) {
				jPanelCheckCoverThumb1.setEnabled(true);
			}
			else {
				jPanelCheckCoverThumb1.setEnabled(false);
			}
		}
		jTableCheck1.setModel(duplicateFolderInfo.getFilesAudioTableModel());
		setTable(jTableCheck1);
		
	}
	
	private void setTable(JTable table) {
        
		//Assigning XTableColumnModel to allow show/hide columns
		table.setColumnModel(new TableColumnModel());
		//Adding columns from model
		table.createDefaultColumnsFromModel();
		
		TableColumn column;

		//	0:  "Filename"
		column = table.getColumnModel().getColumn(0);
		column.setMinWidth(100);

		//	1:  "Disc # (new)"
		//	2:  "Disc #"
		column = table.getColumnModel().getColumn(1);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		column = table.getColumnModel().getColumn(2);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		
		//	3:  "Track # (new)"
		//	4:  "Track #"
		column = table.getColumnModel().getColumn(3);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		column = table.getColumnModel().getColumn(4);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		
		//	5:  "Artist (new)"
		//	6:  "Artist"
		column = table.getColumnModel().getColumn(5);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		column = table.getColumnModel().getColumn(6);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		//	7:  "Title (new)"
		column = table.getColumnModel().getColumn(7);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		//	8:  "Title"
		column = table.getColumnModel().getColumn(8);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		//	9:  "Genre (new)"
			//Render "Genre" column with a combo box
		column = table.getColumnModel().getColumn(9);
		JComboBox comboBox = new JComboBox(PanelMain.getComboGenre());
		column.setCellEditor(new DefaultCellEditor(comboBox));
			//set its width
		column.setMinWidth(100);
		column.setMaxWidth(200);
		column.setPreferredWidth(100);
		//	10: "Genre"
		column = table.getColumnModel().getColumn(10);
		column.setMinWidth(80);
		column.setPreferredWidth(80);
		
		//	11: "Album (new)"
		//	12: "Album"
		column = table.getColumnModel().getColumn(11);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		column = table.getColumnModel().getColumn(12);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	13: "Year (new)"
		//	14: "Year"
		column = table.getColumnModel().getColumn(13);
		column.setMinWidth(50);
		column.setPreferredWidth(50);
		column = table.getColumnModel().getColumn(14);
		column.setMinWidth(50);
		column.setPreferredWidth(50);
		
		//	15: "BitRate"
		column = table.getColumnModel().getColumn(15);
		column.setMinWidth(50);
		column.setPreferredWidth(50);
		
		//	16: "Length"
		column = table.getColumnModel().getColumn(16);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	17: "Format"
		column = table.getColumnModel().getColumn(17);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	18: "Size"
		column = table.getColumnModel().getColumn(18);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	19: "Album Artist (new)"
		//	20: "Album Artist"
		column = table.getColumnModel().getColumn(19);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		column = table.getColumnModel().getColumn(20);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	21: "Comment (new)"
		//	22: "Comment"
		column = table.getColumnModel().getColumn(21);
		column.setMinWidth(20);
		column.setPreferredWidth(20);
		column = table.getColumnModel().getColumn(22);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	23: "Cover"
		column = table.getColumnModel().getColumn(23);
		column.setMinWidth(IconBufferCover.getCoverIconSize());
		column.setMaxWidth(IconBufferCover.getCoverIconSize());
		table.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int col = table.convertColumnIndexToModel(table.columnAtPoint(evt.getPoint()));
				if (col == 23) {
                    //Getting selected File
                    int selectedRow = table.getSelectedRow();
                    //TODO: Does not work after having used moveRow function:
                    //Find a way to get corresponding rowIndex in getFilesAudio(),
                    //maybe based on filename
                    selectedRow = table.convertRowIndexToModel(selectedRow);
//                    FileInfoDisplay file = folder.getFilesAudioTableModel().getFiles().get(selectedRow);
                    
                    if(selectedRow<folder.getFilesAudio().size()) { 
                        //Getting from there as it contains the covers
                        DialogCoverDisplay.main(folder.getFilesAudio().get(selectedRow).getCoverImage()); 	
                    }
				}
			}
		});
		
		//	24: "BPMDisplay"
		//	25: "BPMDisplay"
		column = table.getColumnModel().getColumn(24);
		column.setMinWidth(40);
		column.setPreferredWidth(40);
		column = table.getColumnModel().getColumn(25);
		column.setMinWidth(40);
		column.setPreferredWidth(40);
		
		//Hide all columns except permanent ones (filename, track#, trackTotal, disc#, discTotal)
		PanelMain.setColumnVisible((TableColumnModel)table.getColumnModel(), 5, 25, false);
		
		//need to change jScrollPane's header height, NOT jTableTags's if not bug !
		Dimension d = table.getTableHeader().getPreferredSize();
		d.height = 34;
		jScrollPaneCheckTags.getColumnHeader().setPreferredSize(d);
		
		Action action;
		action = new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				TableCellListener tcl = (TableCellListener)e.getSource();
				displayMatchTracks(tcl.getColumn());
			}
		};
		TableCellListener tcl = new TableCellListener(table, action);	
	}

	private void displayMatchTracks(int colId) {
        //Need to analyse the whole column so that errorLevels are properly set
        folder.analyseMatchTracks(colId);
        folder.getFilesAudioTableModel().fireTableDataChanged();
    }
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelCheckCoverThumb = new jamuz.gui.PanelCover();
        jPanelCheckCoverThumb1 = new jamuz.gui.PanelCover();
        jLabelCheckAlbumArtistTag = new javax.swing.JLabel();
        jLabelCheckAlbumTag = new javax.swing.JLabel();
        jLabelCheckYearTag = new javax.swing.JLabel();
        jLabelCheckDesc = new javax.swing.JLabel();
        jLabelCheckDesc1 = new javax.swing.JLabel();
        jScrollPaneCheckTags = new javax.swing.JScrollPane();
        jTableCheck = new jamuz.gui.swing.TableHorizontal();
        jScrollPaneCheckTags1 = new javax.swing.JScrollPane();
        jTableCheck1 = new jamuz.gui.swing.TableHorizontal();
        jLabelCheckAlbumArtistTag1 = new javax.swing.JLabel();
        jLabelCheckAlbumTag1 = new javax.swing.JLabel();
        jLabelCheckYearTag1 = new javax.swing.JLabel();
        jProgressBarDuplicate = new jamuz.gui.swing.ProgressBar();
        jLabelCheckChecked = new javax.swing.JLabel();
        jLabelCheckAlbumArtistTag3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelCheckCoverThumb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelCheckCoverThumb.setMaximumSize(new java.awt.Dimension(150, 150));
        jPanelCheckCoverThumb.setMinimumSize(new java.awt.Dimension(150, 150));
        jPanelCheckCoverThumb.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelCheckCoverThumbMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelCheckCoverThumbLayout = new javax.swing.GroupLayout(jPanelCheckCoverThumb);
        jPanelCheckCoverThumb.setLayout(jPanelCheckCoverThumbLayout);
        jPanelCheckCoverThumbLayout.setHorizontalGroup(
            jPanelCheckCoverThumbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );
        jPanelCheckCoverThumbLayout.setVerticalGroup(
            jPanelCheckCoverThumbLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );

        jPanelCheckCoverThumb1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelCheckCoverThumb1.setMaximumSize(new java.awt.Dimension(150, 150));
        jPanelCheckCoverThumb1.setMinimumSize(new java.awt.Dimension(150, 150));
        jPanelCheckCoverThumb1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanelCheckCoverThumb1MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanelCheckCoverThumb1Layout = new javax.swing.GroupLayout(jPanelCheckCoverThumb1);
        jPanelCheckCoverThumb1.setLayout(jPanelCheckCoverThumb1Layout);
        jPanelCheckCoverThumb1Layout.setHorizontalGroup(
            jPanelCheckCoverThumb1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );
        jPanelCheckCoverThumb1Layout.setVerticalGroup(
            jPanelCheckCoverThumb1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 148, Short.MAX_VALUE)
        );

        jLabelCheckAlbumArtistTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckAlbumArtistTag.setText(" "); // NOI18N
        jLabelCheckAlbumArtistTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckAlbumArtistTag.setOpaque(true);

        jLabelCheckAlbumTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckAlbumTag.setText(" "); // NOI18N
        jLabelCheckAlbumTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckAlbumTag.setOpaque(true);

        jLabelCheckYearTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckYearTag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckYearTag.setText("9999"); // NOI18N
        jLabelCheckYearTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckYearTag.setOpaque(true);

        jLabelCheckDesc.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckDesc.setText(" "); // NOI18N
        jLabelCheckDesc.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckDesc.setOpaque(true);

        jLabelCheckDesc1.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckDesc1.setText(" "); // NOI18N
        jLabelCheckDesc1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckDesc1.setOpaque(true);

        jTableCheck.setAutoCreateColumnsFromModel(false);
        jTableCheck.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCheck.setAutoscrolls(false);
        jScrollPaneCheckTags.setViewportView(jTableCheck);

        jTableCheck1.setAutoCreateColumnsFromModel(false);
        jTableCheck1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCheck1.setAutoscrolls(false);
        jScrollPaneCheckTags1.setViewportView(jTableCheck1);

        jLabelCheckAlbumArtistTag1.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckAlbumArtistTag1.setText(" "); // NOI18N
        jLabelCheckAlbumArtistTag1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckAlbumArtistTag1.setOpaque(true);

        jLabelCheckAlbumTag1.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckAlbumTag1.setText(" "); // NOI18N
        jLabelCheckAlbumTag1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckAlbumTag1.setOpaque(true);

        jLabelCheckYearTag1.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckYearTag1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckYearTag1.setText("9999"); // NOI18N
        jLabelCheckYearTag1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckYearTag1.setOpaque(true);

        jProgressBarDuplicate.setString(""); // NOI18N
        jProgressBarDuplicate.setStringPainted(true);

        jLabelCheckChecked.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckChecked.setText(" "); // NOI18N
        jLabelCheckChecked.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckChecked.setOpaque(true);

        jLabelCheckAlbumArtistTag3.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckAlbumArtistTag3.setText(" "); // NOI18N
        jLabelCheckAlbumArtistTag3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckAlbumArtistTag3.setOpaque(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelCheckCoverThumb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 738, Short.MAX_VALUE)
                        .addComponent(jPanelCheckCoverThumb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPaneCheckTags)
                            .addComponent(jLabelCheckDesc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelCheckAlbumTag, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelCheckAlbumArtistTag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelCheckChecked, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelCheckYearTag, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelCheckDesc1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabelCheckYearTag1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelCheckAlbumArtistTag3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jLabelCheckAlbumTag1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPaneCheckTags1)
                            .addComponent(jLabelCheckAlbumArtistTag1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jProgressBarDuplicate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelCheckCoverThumb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanelCheckCoverThumb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBarDuplicate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCheckAlbumTag)
                    .addComponent(jLabelCheckAlbumArtistTag1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCheckAlbumArtistTag)
                    .addComponent(jLabelCheckAlbumTag1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCheckChecked)
                    .addComponent(jLabelCheckYearTag)
                    .addComponent(jLabelCheckYearTag1)
                    .addComponent(jLabelCheckAlbumArtistTag3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCheckDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCheckDesc1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneCheckTags, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addComponent(jScrollPaneCheckTags1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanelCheckCoverThumbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelCheckCoverThumbMouseClicked
//        DialogCoverSelect.main(folder, (int)jLabelCoverInfo.getLocationOnScreen().getX());
    }//GEN-LAST:event_jPanelCheckCoverThumbMouseClicked

    private void jPanelCheckCoverThumb1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelCheckCoverThumb1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanelCheckCoverThumb1MouseClicked

	/**
	 * @param parentSize
	 * @param folder
	 * @param duplicateInfo
	 */
	public static void main(Dimension parentSize, FolderInfo folder, DuplicateInfo duplicateInfo) {
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
			java.util.logging.Logger.getLogger(DialogDuplicate.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		
		//</editor-fold>

		DialogDuplicate dialog = new DialogDuplicate(new JFrame(), true, folder, duplicateInfo);
				
        //Set dialog size to x% of parent size
        parentSize.height = parentSize.height * 85/100;
        parentSize.width = parentSize.width * 95/100;
        dialog.setSize(parentSize);
        //Center the dialog on screen
        dialog.setLocationRelativeTo(dialog.getParent());
        //Display
        dialog.setVisible(true);
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JLabel jLabelCheckAlbumArtistTag;
    private static javax.swing.JLabel jLabelCheckAlbumArtistTag1;
    private static javax.swing.JLabel jLabelCheckAlbumArtistTag3;
    private static javax.swing.JLabel jLabelCheckAlbumTag;
    private static javax.swing.JLabel jLabelCheckAlbumTag1;
    private static javax.swing.JLabel jLabelCheckChecked;
    private static javax.swing.JLabel jLabelCheckDesc;
    private static javax.swing.JLabel jLabelCheckDesc1;
    private static javax.swing.JLabel jLabelCheckYearTag;
    private static javax.swing.JLabel jLabelCheckYearTag1;
    public static javax.swing.JPanel jPanelCheckCoverThumb;
    public static javax.swing.JPanel jPanelCheckCoverThumb1;
    private static javax.swing.JProgressBar jProgressBarDuplicate;
    private static javax.swing.JScrollPane jScrollPaneCheckTags;
    private static javax.swing.JScrollPane jScrollPaneCheckTags1;
    private static javax.swing.JTable jTableCheck;
    private static javax.swing.JTable jTableCheck1;
    // End of variables declaration//GEN-END:variables
}
