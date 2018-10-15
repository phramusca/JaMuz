/*
 * Copyright (C) 2015 phramusca ( https://github.com/phramusca/JaMuz/ )
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
import jamuz.Jamuz;
import jamuz.IconBuffer;
import jamuz.gui.DialogCoverDisplay;
import jamuz.gui.PanelCover;
import jamuz.gui.PanelMain;
import jamuz.gui.PanelSelect;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.TableColumn;
import jamuz.gui.swing.ProgressBar;
import jamuz.gui.swing.TableCellListener;
import jamuz.gui.swing.TableColumnModel;
import jamuz.gui.swing.WrapLayout;
import jamuz.utils.Inter;
import jamuz.utils.Swing;
import jamuz.utils.Utils;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.Collections;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogCheck extends javax.swing.JDialog {

    private static FolderInfo folder;
    private static TableColumnModel columnModel;

    /**
     * Progress bar
     */
    protected static ProgressBar progressBar;
    private static ProgressBar progressBarCover;
    
    /**
     * Creates new form PanelDialogCheck
     * @param parent
     * @param modal
     * @param folder
     */
    public DialogCheck(java.awt.Frame parent, boolean modal, FolderInfo folder) {
        super(parent, modal);
        initComponents();

        DialogCheck.folder = folder;
        columnModel = new TableColumnModel(); //To avoid duplicate columns to be added again and again
            //(we could also have tested if it had columns instead of deleting/recreating)
        initExtended();
        displayFolder();
    }

     /**
     * @param folder 
	 */
	public static void main(FolderInfo folder) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {  //NOI18N
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
			Jamuz.getLogger().severe(ex.toString());
		}
		//</editor-fold>

        DialogCheck dialog = new DialogCheck(new JFrame(), true, folder);
        //Get screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //Set dialog size to x% of screen size
        screenSize.height = screenSize.height * 85/100;
        screenSize.width = screenSize.width * 95/100;
        dialog.setSize(screenSize);
        //Center the dialog on screen
        dialog.setLocationRelativeTo(dialog.getParent());
        //Change title
        //TODO: Color in red words not found in match and in green the ones found 
        dialog.setTitle(folder.getRelativePath());
        //Display
        dialog.setVisible(true);
	}
    
    private class GenreButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String genre = e.getActionCommand();
            applyGenre(genre);
        }
    }

    private void initExtended() {
		  
        //Filling genres list
        ActionListener actionListener = new GenreButtonListener();
        jPanelGenre.setLayout(new WrapLayout(FlowLayout.LEADING, 0, 0));
        for(String genre : Jamuz.getGenres()) {
            ImageIcon icon = IconBuffer.getCoverIcon(genre, IconBuffer.IconVersion.NORMAL_30, "genre");
            JButton button;
            if(icon!=null) {
                button = new JButton(icon);
                button.setText(genre); //TODO: Display as an option
            }
            else {
                button = new JButton(genre);
            }
            button.setActionCommand(genre);
            button.addActionListener(actionListener);
            button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); //Counter clockwise
            button.setBackground(Color.LIGHT_GRAY);
            jPanelGenre.add(button);
        }
        jPanelGenre.validate();
        jPanelGenre.repaint();

        progressBar = (ProgressBar)jProgressBarCheckDialog;
        progressBarCover = (ProgressBar) jProgressBarCover;
        
        //Set table's model
        jTableCheck.setModel(folder.getFilesAudioTableModel());
        
		//Assigning XTableColumnModel to allow show/hide columns
		jTableCheck.setColumnModel(columnModel);
		//Adding columns from model
		jTableCheck.createDefaultColumnsFromModel();
		
		TableColumn column;

		//	0:  "Filename"
		column = jTableCheck.getColumnModel().getColumn(0);
		column.setMinWidth(100);

		//	1:  "Disc # (new)"
		//	2:  "Disc #"
		column = jTableCheck.getColumnModel().getColumn(1);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		column = jTableCheck.getColumnModel().getColumn(2);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		
		//	3:  "Track # (new)"
		//	4:  "Track #"
		column = jTableCheck.getColumnModel().getColumn(3);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		column = jTableCheck.getColumnModel().getColumn(4);
		column.setMinWidth(55);
		column.setMaxWidth(55);
		
		//	5:  "Artist (new)"
		//	6:  "Artist"
		column = jTableCheck.getColumnModel().getColumn(5);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		column = jTableCheck.getColumnModel().getColumn(6);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		//	7:  "Title (new)"
		column = jTableCheck.getColumnModel().getColumn(7);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		//	8:  "Title"
		column = jTableCheck.getColumnModel().getColumn(8);
		column.setMinWidth(50);
		column.setPreferredWidth(100);
		
		//	9:  "Genre (new)"
			//Render "Genre" column with a combo box
		column = jTableCheck.getColumnModel().getColumn(9);
		JComboBox comboBox = new JComboBox(PanelMain.getComboGenre());
		column.setCellEditor(new DefaultCellEditor(comboBox));
			//set its width
		column.setMinWidth(100);
		column.setMaxWidth(200);
		column.setPreferredWidth(100);
		//	10: "Genre"
		column = jTableCheck.getColumnModel().getColumn(10);
		column.setMinWidth(80);
		column.setPreferredWidth(80);
		
		//	11: "Album (new)"
		//	12: "Album"
		column = jTableCheck.getColumnModel().getColumn(11);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		column = jTableCheck.getColumnModel().getColumn(12);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	13: "Year (new)"
		//	14: "Year"
		column = jTableCheck.getColumnModel().getColumn(13);
		column.setMinWidth(50);
		column.setPreferredWidth(50);
		column = jTableCheck.getColumnModel().getColumn(14);
		column.setMinWidth(50);
		column.setPreferredWidth(50);
		
		//	15: "BitRate"
		column = jTableCheck.getColumnModel().getColumn(15);
		column.setMinWidth(50);
		column.setPreferredWidth(50);
		
		//	16: "Length"
		column = jTableCheck.getColumnModel().getColumn(16);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	17: "Format"
		column = jTableCheck.getColumnModel().getColumn(17);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	18: "Size"
		column = jTableCheck.getColumnModel().getColumn(18);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	19: "Album Artist (new)"
		//	20: "Album Artist"
		column = jTableCheck.getColumnModel().getColumn(19);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		column = jTableCheck.getColumnModel().getColumn(20);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	21: "Comment (new)"
		//	22: "Comment"
		column = jTableCheck.getColumnModel().getColumn(21);
		column.setMinWidth(20);
		column.setPreferredWidth(20);
		column = jTableCheck.getColumnModel().getColumn(22);
		column.setMinWidth(80);
		column.setPreferredWidth(100);
		
		//	23: "Cover"
		column = jTableCheck.getColumnModel().getColumn(23);
		column.setMinWidth(IconBufferCover.getCoverIconSize());
		column.setMaxWidth(IconBufferCover.getCoverIconSize());
		jTableCheck.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int col = jTableCheck.convertColumnIndexToModel(jTableCheck.columnAtPoint(evt.getPoint()));
				if (col == 23) {
                    //Getting selected File
                    int selectedRow = jTableCheck.getSelectedRow();
                    //TODO: Does not work after having used moveRow function:
                    //Find a way to get corresponding rowIndex in getFilesAudio(),
                    //maybe based on filename
                    selectedRow = jTableCheck.convertRowIndexToModel(selectedRow);
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
		column = jTableCheck.getColumnModel().getColumn(24);
		column.setMinWidth(40);
		column.setPreferredWidth(40);
		column = jTableCheck.getColumnModel().getColumn(25);
		column.setMinWidth(40);
		column.setPreferredWidth(40);
		
		//Hide all columns except permanent ones (filename, track#, trackTotal, disc#, discTotal)
		PanelMain.setColumnVisible(columnModel, 5, 25, false);
		
		//need to change jScrollPane's header height, NOT jTableTags's if not bug !
		Dimension d = jTableCheck.getTableHeader().getPreferredSize();
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
		TableCellListener tcl = new TableCellListener(jTableCheck, action);		
	}
    
    private static void displayMatchTracks() {
        folder.analyseMatchTracks();
        folder.getFilesAudioTableModel().fireTableDataChanged();
        displayMatchColumns(folder.getResults());
	}
    
    private static void displayMatchTracks(int colId) {
        //Need to analyse the whole column so that errorLevels are properly set
        folder.analyseMatchTracks(colId);
        folder.getFilesAudioTableModel().fireTableDataChanged();
        displayMatchColumn(folder.getResults(), colId);
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
        jPanelCheckFolder = new javax.swing.JPanel();
        jButtonCheckOK = new javax.swing.JButton();
        jButtonCheckKO = new javax.swing.JButton();
        jButtonCheckDelete = new javax.swing.JButton();
        jButtonCheckSaveTags = new javax.swing.JButton();
        jProgressBarCheckDialog = new jamuz.gui.swing.ProgressBar();
        jButtonCheckKO1Library = new javax.swing.JButton();
        jPanelCheckTags = new javax.swing.JPanel();
        jPanelCheckCoverThumb = new jamuz.gui.PanelCover();
        jComboBoxCheckMatches = new javax.swing.JComboBox();
        jComboBoxCheckDuplicates = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jLabelCheckNbTracks = new javax.swing.JLabel();
        jLabelCheckNbFiles = new javax.swing.JLabel();
        jCheckBoxCheckArtistDisplay = new javax.swing.JCheckBox();
        jCheckCheckTitleDisplay = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jButtonCheckApplyYear = new javax.swing.JButton();
        jTextFieldCheckYear = new javax.swing.JTextField();
        jCheckBoxCheckYearDisplay = new javax.swing.JCheckBox();
        jLabelCheckYearTag = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabelCoverInfo = new javax.swing.JLabel();
        jCheckBoxCheckCoverDisplay = new javax.swing.JCheckBox();
        jProgressBarCover = new jamuz.gui.swing.ProgressBar();
        jLabelCoverFound = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jButtonCheckSearch = new javax.swing.JButton();
        jTextFieldCheckAlbumArtist = new javax.swing.JTextField();
        jTextFieldCheckAlbum = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButtonCheckApplyArtist = new javax.swing.JButton();
        jButtonCheckApplyAlbum = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButtonVArtist = new javax.swing.JButton();
        jButtonVAlbum = new javax.swing.JButton();
        jButtonCheckApplyAlbumArtist = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jCheckBoxCheckAlbumDisplay = new javax.swing.JCheckBox();
        jCheckBoxCheckAlbumArtistDisplay = new javax.swing.JCheckBox();
        jLabelCheckAlbumTag = new javax.swing.JLabel();
        jLabelCheckAlbumArtistTag = new javax.swing.JLabel();
        jPanelGenre = new javax.swing.JPanel();
        jCheckBoxCheckGenreDisplay = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jCheckBoxCheckFormatDisplay = new javax.swing.JCheckBox();
        jCheckBoxCheckLengthDisplay = new javax.swing.JCheckBox();
        jCheckBoxCheckSizeDisplay = new javax.swing.JCheckBox();
        jCheckBoxCheckBitRateDisplay = new javax.swing.JCheckBox();
        jLabelCheckMeanBitRateTag = new javax.swing.JLabel();
        jCheckBoxCheckBPMDisplay = new javax.swing.JCheckBox();
        jCheckBoxCheckCommentDisplay = new javax.swing.JCheckBox();
        jLabelCheckReplayGain = new javax.swing.JLabel();
        jLabelCheckReplayGainTag = new javax.swing.JLabel();
        jLabelCheckID3v1 = new javax.swing.JLabel();
        jLabelCheckID3v1Tag = new javax.swing.JLabel();
        jButtonSelectOriginal = new javax.swing.JButton();
        jButtonSelectDuplicate = new javax.swing.JButton();
        jButtonCheckScanner = new javax.swing.JButton();
        jScrollPaneCheckTags = new javax.swing.JScrollPane();
        jTableCheck = new jamuz.gui.swing.TableHorizontal();
        jButtonCheckUp = new javax.swing.JButton();
        jButtonCheckDown = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jButtonCheckEditTag = new javax.swing.JButton();
        jButtonCheckOpen = new javax.swing.JButton();
        jButtonCheckSingleFolder = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jButtonPlayerPrevious = new javax.swing.JButton();
        jButtonCheckPreview = new javax.swing.JButton();
        jButtonPlayerForward = new javax.swing.JButton();
        jButtonPlayerNext = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelCheckFolder.setBackground(java.awt.Color.lightGray);
        jPanelCheckFolder.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonCheckOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jButtonCheckOK.setText(bundle.getString("Button.OK")); // NOI18N
        jButtonCheckOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckOKActionPerformed(evt);
            }
        });

        jButtonCheckKO.setBackground(java.awt.Color.red);
        jButtonCheckKO.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        jButtonCheckKO.setText(bundle.getString("Button.KO")); // NOI18N
        jButtonCheckKO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckKOActionPerformed(evt);
            }
        });

        jButtonCheckDelete.setBackground(java.awt.Color.black);
        jButtonCheckDelete.setForeground(java.awt.Color.white);
        jButtonCheckDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        jButtonCheckDelete.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonCheckDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckDeleteActionPerformed(evt);
            }
        });

        jButtonCheckSaveTags.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jButtonCheckSaveTags.setText(bundle.getString("Button.Save")); // NOI18N
        jButtonCheckSaveTags.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckSaveTagsActionPerformed(evt);
            }
        });

        jProgressBarCheckDialog.setString("null");
        jProgressBarCheckDialog.setStringPainted(true);

        jButtonCheckKO1Library.setBackground(java.awt.Color.red);
        jButtonCheckKO1Library.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        jButtonCheckKO1Library.setText(bundle.getString("Check.KO.Library")); // NOI18N
        jButtonCheckKO1Library.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckKO1LibraryActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCheckFolderLayout = new javax.swing.GroupLayout(jPanelCheckFolder);
        jPanelCheckFolder.setLayout(jPanelCheckFolderLayout);
        jPanelCheckFolderLayout.setHorizontalGroup(
            jPanelCheckFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCheckFolderLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonCheckSaveTags, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckKO)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckKO1Library)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckOK)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarCheckDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanelCheckFolderLayout.setVerticalGroup(
            jPanelCheckFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCheckFolderLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanelCheckFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCheckSaveTags)
                    .addComponent(jButtonCheckDelete)
                    .addComponent(jButtonCheckKO)
                    .addComponent(jButtonCheckOK)
                    .addComponent(jProgressBarCheckDialog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCheckKO1Library))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanelCheckTags.setBorder(null);

        jPanelCheckCoverThumb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelCheckCoverThumb.setMaximumSize(new java.awt.Dimension(150, 150));
        jPanelCheckCoverThumb.setMinimumSize(new java.awt.Dimension(150, 150));
        jPanelCheckCoverThumb.setPreferredSize(new java.awt.Dimension(150, 150));
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

        jComboBoxCheckMatches.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxCheckMatchesActionPerformed(evt);
            }
        });

        jPanel2.setBackground(java.awt.Color.lightGray);
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelCheckNbTracks.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckNbTracks.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckNbTracks.setText("99"); // NOI18N
        jLabelCheckNbTracks.setToolTipText(Inter.get("Tooltip.NumberOfMatchTracks")); // NOI18N
        jLabelCheckNbTracks.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckNbTracks.setOpaque(true);

        jLabelCheckNbFiles.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckNbFiles.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckNbFiles.setText("99"); // NOI18N
        jLabelCheckNbFiles.setToolTipText(bundle.getString("Tooltip.NumberOfFiles")); // NOI18N
        jLabelCheckNbFiles.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckNbFiles.setOpaque(true);

        jCheckBoxCheckArtistDisplay.setText(bundle.getString("Tag.Artist")); // NOI18N
        jCheckBoxCheckArtistDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckArtistDisplayItemStateChanged(evt);
            }
        });

        jCheckCheckTitleDisplay.setText(bundle.getString("Tag.Title")); // NOI18N
        jCheckCheckTitleDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckCheckTitleDisplayItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabelCheckNbTracks, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCheckNbFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxCheckArtistDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckCheckTitleDisplay)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 4, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCheckNbTracks)
                    .addComponent(jLabelCheckNbFiles)
                    .addComponent(jCheckBoxCheckArtistDisplay)
                    .addComponent(jCheckCheckTitleDisplay))
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jPanel5.setBackground(java.awt.Color.lightGray);
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonCheckApplyYear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/apply_to_all.png"))); // NOI18N
        jButtonCheckApplyYear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckApplyYearActionPerformed(evt);
            }
        });

        jTextFieldCheckYear.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jCheckBoxCheckYearDisplay.setText(bundle.getString("Tag.Year")); // NOI18N
        jCheckBoxCheckYearDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckYearDisplayItemStateChanged(evt);
            }
        });

        jLabelCheckYearTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckYearTag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckYearTag.setText("9999"); // NOI18N
        jLabelCheckYearTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckYearTag.setOpaque(true);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxCheckYearDisplay)
                    .addComponent(jButtonCheckApplyYear, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelCheckYearTag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldCheckYear, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextFieldCheckYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonCheckApplyYear))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCheckYearTag)
                    .addComponent(jCheckBoxCheckYearDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel6.setBackground(java.awt.Color.lightGray);
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelCoverInfo.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCoverInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCoverInfo.setText("Cover info"); // NOI18N
        jLabelCoverInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCoverInfo.setOpaque(true);

        jCheckBoxCheckCoverDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckCoverDisplayItemStateChanged(evt);
            }
        });

        jProgressBarCover.setString(" "); // NOI18N
        jProgressBarCover.setStringPainted(true);

        jLabelCoverFound.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCoverFound.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCoverFound.setText("Cover found"); // NOI18N
        jLabelCoverFound.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCoverFound.setOpaque(true);

        jLabel3.setText("Search:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jCheckBoxCheckCoverDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCoverInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCoverFound, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jProgressBarCover, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCheckBoxCheckCoverDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelCoverInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCoverFound)
                    .addComponent(jLabel3))
                .addGap(2, 2, 2)
                .addComponent(jProgressBarCover, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel7.setBackground(java.awt.Color.lightGray);
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonCheckSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/search_plus.png"))); // NOI18N
        jButtonCheckSearch.setToolTipText("Query Last.FM and MusicBrainz for matches");
        jButtonCheckSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckSearchActionPerformed(evt);
            }
        });

        jLabel2.setText(Inter.get("Tag.Album")); // NOI18N

        jButtonCheckApplyArtist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/apply_to_all.png"))); // NOI18N
        jButtonCheckApplyArtist.setToolTipText("Apply Artist");
        jButtonCheckApplyArtist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckApplyArtistActionPerformed(evt);
            }
        });

        jButtonCheckApplyAlbum.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/apply_to_all.png"))); // NOI18N
        jButtonCheckApplyAlbum.setToolTipText("Apply Album");
        jButtonCheckApplyAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckApplyAlbumActionPerformed(evt);
            }
        });

        jLabel1.setText(Inter.get("Tag.Artist")); // NOI18N

        jButtonVArtist.setText("VA"); // NOI18N
        jButtonVArtist.setToolTipText("Set \"Various Artists\" in artist input box");
        jButtonVArtist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVArtistActionPerformed(evt);
            }
        });

        jButtonVAlbum.setText("VA"); // NOI18N
        jButtonVAlbum.setToolTipText("Set \"Various Albums\" in album input box");
        jButtonVAlbum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVAlbumActionPerformed(evt);
            }
        });

        jButtonCheckApplyAlbumArtist.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/apply_to_all.png"))); // NOI18N
        jButtonCheckApplyAlbumArtist.setToolTipText("Apply Album Artist");
        jButtonCheckApplyAlbumArtist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckApplyAlbumArtistActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addGap(3, 3, 3)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldCheckAlbum)
                    .addComponent(jTextFieldCheckAlbumArtist))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonVAlbum, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonVArtist, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCheckApplyAlbum)
                    .addComponent(jButtonCheckApplyAlbumArtist))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCheckSearch)
                    .addComponent(jButtonCheckApplyArtist)))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTextFieldCheckAlbumArtist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonVArtist))
                    .addComponent(jButtonCheckApplyAlbumArtist, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonCheckApplyArtist, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCheckApplyAlbum)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jTextFieldCheckAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonVAlbum))
                    .addComponent(jButtonCheckSearch))
                .addGap(0, 0, 0))
        );

        jPanel8.setBackground(java.awt.Color.lightGray);
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jCheckBoxCheckAlbumDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckAlbumDisplayItemStateChanged(evt);
            }
        });

        jCheckBoxCheckAlbumArtistDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckAlbumArtistDisplayItemStateChanged(evt);
            }
        });

        jLabelCheckAlbumTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckAlbumTag.setText(" "); // NOI18N
        jLabelCheckAlbumTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckAlbumTag.setOpaque(true);

        jLabelCheckAlbumArtistTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckAlbumArtistTag.setText(" "); // NOI18N
        jLabelCheckAlbumArtistTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckAlbumArtistTag.setOpaque(true);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jCheckBoxCheckAlbumDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCheckAlbumTag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jCheckBoxCheckAlbumArtistDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCheckAlbumArtistTag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBoxCheckAlbumArtistDisplay)
                    .addComponent(jLabelCheckAlbumArtistTag))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxCheckAlbumDisplay, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelCheckAlbumTag, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        jPanelGenre.setBackground(java.awt.Color.lightGray);
        jPanelGenre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jCheckBoxCheckGenreDisplay.setText(bundle.getString("Tag.Genre")); // NOI18N
        jCheckBoxCheckGenreDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckGenreDisplayItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanelGenreLayout = new javax.swing.GroupLayout(jPanelGenre);
        jPanelGenre.setLayout(jPanelGenreLayout);
        jPanelGenreLayout.setHorizontalGroup(
            jPanelGenreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGenreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxCheckGenreDisplay)
                .addContainerGap(391, Short.MAX_VALUE))
        );
        jPanelGenreLayout.setVerticalGroup(
            jPanelGenreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelGenreLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxCheckGenreDisplay)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBackground(java.awt.Color.lightGray);
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jCheckBoxCheckFormatDisplay.setText(bundle.getString("Tag.Format")); // NOI18N
        jCheckBoxCheckFormatDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckFormatDisplayItemStateChanged(evt);
            }
        });

        jCheckBoxCheckLengthDisplay.setText(bundle.getString("Tag.Length")); // NOI18N
        jCheckBoxCheckLengthDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckLengthDisplayItemStateChanged(evt);
            }
        });

        jCheckBoxCheckSizeDisplay.setText(bundle.getString("Tag.Size")); // NOI18N
        jCheckBoxCheckSizeDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckSizeDisplayItemStateChanged(evt);
            }
        });

        jCheckBoxCheckBitRateDisplay.setText(bundle.getString("Tag.BitRate")); // NOI18N
        jCheckBoxCheckBitRateDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckBitRateDisplayItemStateChanged(evt);
            }
        });

        jLabelCheckMeanBitRateTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckMeanBitRateTag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckMeanBitRateTag.setText("999"); // NOI18N
        jLabelCheckMeanBitRateTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckMeanBitRateTag.setOpaque(true);

        jCheckBoxCheckBPMDisplay.setText(bundle.getString("Tag.BPM")); // NOI18N
        jCheckBoxCheckBPMDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckBPMDisplayItemStateChanged(evt);
            }
        });

        jCheckBoxCheckCommentDisplay.setText(bundle.getString("Tag.Comment")); // NOI18N
        jCheckBoxCheckCommentDisplay.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCheckCommentDisplayItemStateChanged(evt);
            }
        });

        jLabelCheckReplayGain.setText("ReplayGain"); // NOI18N

        jLabelCheckReplayGainTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckReplayGainTag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckReplayGainTag.setText("yes"); // NOI18N
        jLabelCheckReplayGainTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckReplayGainTag.setOpaque(true);

        jLabelCheckID3v1.setText("ID3v1"); // NOI18N

        jLabelCheckID3v1Tag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckID3v1Tag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckID3v1Tag.setText("yes"); // NOI18N
        jLabelCheckID3v1Tag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckID3v1Tag.setOpaque(true);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabelCheckReplayGain)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCheckReplayGainTag, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jCheckBoxCheckCommentDisplay)
                        .addGroup(jPanel4Layout.createSequentialGroup()
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jCheckBoxCheckBitRateDisplay)
                                .addComponent(jLabelCheckID3v1))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabelCheckID3v1Tag, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabelCheckMeanBitRateTag, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxCheckBPMDisplay)
                    .addComponent(jCheckBoxCheckFormatDisplay)
                    .addComponent(jCheckBoxCheckSizeDisplay)
                    .addComponent(jCheckBoxCheckLengthDisplay))
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCheckReplayGain)
                    .addComponent(jLabelCheckReplayGainTag)
                    .addComponent(jCheckBoxCheckFormatDisplay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCheckID3v1Tag)
                    .addComponent(jLabelCheckID3v1)
                    .addComponent(jCheckBoxCheckSizeDisplay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCheckMeanBitRateTag)
                    .addComponent(jCheckBoxCheckBitRateDisplay)
                    .addComponent(jCheckBoxCheckLengthDisplay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxCheckBPMDisplay)
                    .addComponent(jCheckBoxCheckCommentDisplay))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jButtonSelectOriginal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        jButtonSelectOriginal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectOriginalActionPerformed(evt);
            }
        });

        jButtonSelectDuplicate.setText(Inter.get("Button.Select")); // NOI18N
        jButtonSelectDuplicate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSelectDuplicateActionPerformed(evt);
            }
        });

        jButtonCheckScanner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/application_form_edit.png"))); // NOI18N
        jButtonCheckScanner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckScannerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelCheckTagsLayout = new javax.swing.GroupLayout(jPanelCheckTags);
        jPanelCheckTags.setLayout(jPanelCheckTagsLayout);
        jPanelCheckTagsLayout.setHorizontalGroup(
            jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCheckTagsLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanelCheckCoverThumb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCheckTagsLayout.createSequentialGroup()
                        .addComponent(jPanelGenre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanelCheckTagsLayout.createSequentialGroup()
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanelCheckTagsLayout.createSequentialGroup()
                        .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButtonSelectDuplicate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanelCheckTagsLayout.createSequentialGroup()
                                .addComponent(jButtonSelectOriginal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonCheckScanner, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxCheckMatches, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBoxCheckDuplicates, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        jPanelCheckTagsLayout.setVerticalGroup(
            jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCheckTagsLayout.createSequentialGroup()
                .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelCheckCoverThumb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelCheckTagsLayout.createSequentialGroup()
                        .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBoxCheckMatches, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                            .addComponent(jButtonSelectOriginal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonCheckScanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxCheckDuplicates, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSelectDuplicate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelGenre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelCheckTagsLayout.createSequentialGroup()
                        .addGroup(jPanelCheckTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanelCheckTagsLayout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanelCheckTagsLayout.createSequentialGroup()
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jTableCheck.setAutoCreateColumnsFromModel(false);
        jTableCheck.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCheck.setAutoscrolls(false);
        jScrollPaneCheckTags.setViewportView(jTableCheck);

        jButtonCheckUp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/arrow_up.png"))); // NOI18N
        jButtonCheckUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckUpActionPerformed(evt);
            }
        });

        jButtonCheckDown.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/arrow_down.png"))); // NOI18N
        jButtonCheckDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckDownActionPerformed(evt);
            }
        });

        jPanel9.setBackground(java.awt.Color.lightGray);
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonCheckEditTag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/external.png"))); // NOI18N
        jButtonCheckEditTag.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonCheckEditTag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckEditTagActionPerformed(evt);
            }
        });

        jButtonCheckOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/folder.png"))); // NOI18N
        jButtonCheckOpen.setText(Inter.get("Button.Open")); // NOI18N
        jButtonCheckOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckOpenActionPerformed(evt);
            }
        });

        jButtonCheckSingleFolder.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/restart_services.png"))); // NOI18N
        jButtonCheckSingleFolder.setText(Inter.get("Button.ReCheck")); // NOI18N
        jButtonCheckSingleFolder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckSingleFolderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonCheckOpen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckEditTag)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckSingleFolder)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCheckOpen)
                    .addComponent(jButtonCheckEditTag)
                    .addComponent(jButtonCheckSingleFolder))
                .addContainerGap())
        );

        jPanel10.setBackground(java.awt.Color.lightGray);
        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButtonPlayerPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/resultset-premier-icone-4160-16.png"))); // NOI18N
        jButtonPlayerPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayerPreviousActionPerformed(evt);
            }
        });

        jButtonCheckPreview.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/music.png"))); // NOI18N
        jButtonCheckPreview.setToolTipText("null");
        jButtonCheckPreview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCheckPreviewActionPerformed(evt);
            }
        });

        jButtonPlayerForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/resultset_next.png"))); // NOI18N
        jButtonPlayerForward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayerForwardActionPerformed(evt);
            }
        });

        jButtonPlayerNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/last-resultset-icone-5121-16.png"))); // NOI18N
        jButtonPlayerNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPlayerNextActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonPlayerPrevious)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCheckPreview)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonPlayerNext)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonPlayerForward)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonPlayerForward)
                    .addComponent(jButtonPlayerNext)
                    .addComponent(jButtonPlayerPrevious)
                    .addComponent(jButtonCheckPreview))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/delete.png"))); // NOI18N
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonCheckDown)
                    .addComponent(jButtonCheckUp)
                    .addComponent(jButtonDelete, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags, javax.swing.GroupLayout.DEFAULT_SIZE, 808, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelCheckFolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanelCheckTags, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanelCheckTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelCheckFolder, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButtonCheckUp, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonCheckDown, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonDelete))
                    .addComponent(jScrollPaneCheckTags, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCheckSingleFolderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckSingleFolderActionPerformed
        enableAddOptions(false);
        folder.reCheck();
    }//GEN-LAST:event_jButtonCheckSingleFolderActionPerformed

    private void jButtonCheckPreviewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckPreviewActionPerformed
        PanelMain.getQueueModel().clear();
        folder.queueAll();
//        this.dispose();
    }//GEN-LAST:event_jButtonCheckPreviewActionPerformed

    private void jButtonCheckEditTagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckEditTagActionPerformed
        //        FolderInfo folderInfo = process.getFolderInfo();
        //        if(folderInfo!=null) {
            PanelMain.editLocation(folder.getFullPath());
            //        }
    }//GEN-LAST:event_jButtonCheckEditTagActionPerformed

    private void jButtonCheckOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckOKActionPerformed

        if(folder.isWarning()) {
            folder.action=ProcessCheck.Action.WARNING;
        }
        else {
            folder.action=ProcessCheck.Action.OK;
        }
        ReleaseMatch match = folder.getMatch(jComboBoxCheckMatches.getSelectedIndex());
        folder.setMbId(match.getId());
        PanelCheck.addToActionQueue(folder);
        this.dispose();
    }//GEN-LAST:event_jButtonCheckOKActionPerformed

    private void jButtonCheckKOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckKOActionPerformed
        folder.action = ProcessCheck.Action.KO;
        PanelCheck.addToActionQueue(folder);
        this.dispose();
    }//GEN-LAST:event_jButtonCheckKOActionPerformed

    private void jButtonCheckDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckDeleteActionPerformed
        int n = JOptionPane.showConfirmDialog(
					null, Inter.get("Question.Check.DeleteFolderContent"),  //NOI18N
					Inter.get("Label.Confirm"),  //NOI18N
					JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			folder.action = ProcessCheck.Action.DEL;
            PanelCheck.addToActionQueue(folder);
            this.dispose();
		} 
    }//GEN-LAST:event_jButtonCheckDeleteActionPerformed

    private void jButtonCheckSaveTagsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckSaveTagsActionPerformed

        //Check that files are ordered in jTableCheck
        //and, if not, ask user if he wants to continue saving anyway

        //discNo is often not set (-1), especially when there is only one disc
        int discNoTheo=1;
        ArrayList<String> discNos = FolderInfo.group(folder.getFilesAudioTableModel().getFiles(), "getDiscNo");  //NOI18N
        if(discNos.size()==1) {
            if(discNos.contains("-1")) { //NOI18N
                discNoTheo=-1;
            }
        }

        int discNo;
        int trackNoTheo=1;
        int trackNo;
        boolean doSave=true;
        for(FileInfoDisplay file : folder.getFilesAudioTableModel().getFiles()) {
            if(file.isAudioFile) {

                if(!file.discNoFullDisplay.getValue().contains("/") || !file.trackNoFullDisplay.getValue().contains("/")) { //NOI18N
                    //TODO: Try getting trackNo from filename
                    doSave=false;
                    break; //No need to check further
                }

                discNo = Integer.valueOf(file.discNoFullDisplay.getValue().split("/")[0]); //NOI18N
                trackNo = Integer.valueOf(file.trackNoFullDisplay.getValue().split("/")[0]); //NOI18N

                //Check disc ordering
                if(discNo==discNoTheo) {
                    //OK
                }
                else if(discNo==discNoTheo+1) {
                    //OK
                    discNoTheo+=1;
                    trackNoTheo=1;
                }
                else {
                    doSave=false;
                    break; //No need to check further
                }

                //Check track ordering
                if(trackNo==trackNoTheo) {
                    //OK
                    trackNoTheo+=1;
                }
                else {
                    doSave=false;
                    break; //No need to check further
                }
            }
        }

        //TODO: If titles are ok and so is track #, then we shall not ask for confirmation
        if(!doSave) {
            int n = JOptionPane.showConfirmDialog(
                null, Inter.get("Question.Check.TrackOrder"), //NOI18N
                Inter.get("Label.Confirm"),  //NOI18N
                JOptionPane.YES_NO_OPTION);
            doSave = (n == JOptionPane.YES_OPTION);
        }

        if(doSave) {
            folder.action = ProcessCheck.Action.SAVE;
			int i=0;
			for(FileInfoDisplay file : folder.getFilesAudioTableModel().getFiles()) {
				if(file.index<folder.getFilesAudio().size()) {
					folder.getFilesAudio().get(file.index).index=i;
				}
				i++;
			}
			Collections.sort(folder.getFilesAudio(), 
					(FileInfoDisplay o1, FileInfoDisplay o2) -> {
				return o1.index < o2.index ? -1 : o1.index == o2.index ? 0 : 1;
			});

			//Set new image
			BufferedImage image=null;
			PanelCover coverImg = (PanelCover) DialogCheck.jPanelCheckCoverThumb;
			if(coverImg.isCover()) {
				image=coverImg.getImage();
			}
			folder.setNewImage(image);

			PanelCheck.addToActionQueue(folder);
			this.dispose();
        }
    }//GEN-LAST:event_jButtonCheckSaveTagsActionPerformed

    private void jButtonCheckOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckOpenActionPerformed
        jamuz.utils.Desktop.openFolder(folder.getFullPath());
    }//GEN-LAST:event_jButtonCheckOpenActionPerformed

    private void jPanelCheckCoverThumbMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelCheckCoverThumbMouseClicked
        DialogCoverSelect.main(folder, (int)jLabelCoverInfo.getLocationOnScreen().getX());
    }//GEN-LAST:event_jPanelCheckCoverThumbMouseClicked

    private void jComboBoxCheckMatchesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCheckMatchesActionPerformed
        if (jComboBoxCheckMatches.getModel().getSize() > 0) {
            Thread t = new Thread("Thread.PanelCheckDialog.displayMatch") {
                @Override
                public void run() {
                    try {
                        displayMatch(jComboBoxCheckMatches.getSelectedIndex());
                    } catch (CloneNotSupportedException ex) {
                        //Should never happen since FileInfoDisplay implements Cloneable
                        Jamuz.getLogger().log(Level.SEVERE, "applyMatch()", ex); //NOI18N
                    }
                }
            };
            t.start();
        }
    }//GEN-LAST:event_jComboBoxCheckMatchesActionPerformed

    private void jButtonCheckSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckSearchActionPerformed
        Thread t = new Thread("Thread.PanelCheckDialog.searchMatches") {
            @Override
            public void run() {
                enableAddOptions(false);
                //Analyzing right before displaying so that previously added folders are taken into account for duplicate check
//                progressBar.setIndeterminate("");  //NOI18N
                //TODO: Use a real discTotal, or ask user somehow
                folder.searchMatches(jTextFieldCheckAlbum.getText(), 
						jTextFieldCheckAlbumArtist.getText(), -1, -1, 
						progressBar);
                progressBar.reset();
                //Fill matches combo box
                displayMatches();

               
            }
        };
        t.start();
    }//GEN-LAST:event_jButtonCheckSearchActionPerformed

    private void jButtonCheckApplyArtistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckApplyArtistActionPerformed
        folder.setNewArtist(jTextFieldCheckAlbumArtist.getText());
        displayMatchTracks(5);
    }//GEN-LAST:event_jButtonCheckApplyArtistActionPerformed

    private void jCheckBoxCheckSizeDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckSizeDisplayItemStateChanged
        //	18: "Size"
        PanelMain.setColumnVisible(columnModel, 18, jCheckBoxCheckSizeDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckSizeDisplayItemStateChanged

    private void jCheckBoxCheckFormatDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckFormatDisplayItemStateChanged
        //	17: "Format"
        PanelMain.setColumnVisible(columnModel, 17, jCheckBoxCheckFormatDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckFormatDisplayItemStateChanged

    private void jCheckCheckTitleDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckCheckTitleDisplayItemStateChanged
        //	7:  "Title (new)"
        //	8:  "Title"
        PanelMain.setColumnVisible(columnModel, 7, 8, jCheckCheckTitleDisplay.isSelected());
    }//GEN-LAST:event_jCheckCheckTitleDisplayItemStateChanged

    private void jCheckBoxCheckGenreDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckGenreDisplayItemStateChanged
        //	9:  "Genre (new)"
        //	10: "Genre"
        PanelMain.setColumnVisible(columnModel, 9, 10, jCheckBoxCheckGenreDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckGenreDisplayItemStateChanged

    private void jCheckBoxCheckAlbumArtistDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckAlbumArtistDisplayItemStateChanged
        //	19: "Album Artist (new)"
        //	20: "Album Artist"
        PanelMain.setColumnVisible(columnModel, 19, 20, jCheckBoxCheckAlbumArtistDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckAlbumArtistDisplayItemStateChanged

    private void jCheckBoxCheckAlbumDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckAlbumDisplayItemStateChanged
        //	11: "Album (new)"
        //	12: "Album"
        PanelMain.setColumnVisible(columnModel, 11, 12, jCheckBoxCheckAlbumDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckAlbumDisplayItemStateChanged

    private void jCheckBoxCheckCommentDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckCommentDisplayItemStateChanged
        //	21: "Comment (new)"
        //	22: "Comment"
        PanelMain.setColumnVisible(columnModel, 21, 22, jCheckBoxCheckCommentDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckCommentDisplayItemStateChanged

    private void jCheckBoxCheckYearDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckYearDisplayItemStateChanged
        //	13: "Year (new)"
        //	14: "Year"
        PanelMain.setColumnVisible(columnModel, 13, 14, jCheckBoxCheckYearDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckYearDisplayItemStateChanged

    private void jCheckBoxCheckCoverDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckCoverDisplayItemStateChanged
        //	23: "Cover"
        PanelMain.setColumnVisible(columnModel, 23, jCheckBoxCheckCoverDisplay.isSelected());
        if(jCheckBoxCheckCoverDisplay.isSelected()) {
            jTableCheck.setRowHeight(IconBufferCover.getCoverIconSize());
        }
        else {
            jTableCheck.setRowHeight(16);
        }
    }//GEN-LAST:event_jCheckBoxCheckCoverDisplayItemStateChanged

    private void jCheckBoxCheckBitRateDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckBitRateDisplayItemStateChanged
        //	15: "BitRate"
        PanelMain.setColumnVisible(columnModel, 15, jCheckBoxCheckBitRateDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckBitRateDisplayItemStateChanged

    private void jCheckBoxCheckBPMDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckBPMDisplayItemStateChanged
        //	24: "BPM (new)"
        //	25: "BPM"
        PanelMain.setColumnVisible(columnModel, 24, 25, jCheckBoxCheckBPMDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckBPMDisplayItemStateChanged

    private void jCheckBoxCheckLengthDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckLengthDisplayItemStateChanged
        //	16: "Length"
        PanelMain.setColumnVisible(columnModel, 16, jCheckBoxCheckLengthDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckLengthDisplayItemStateChanged

    private void jCheckBoxCheckArtistDisplayItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCheckArtistDisplayItemStateChanged
        //	5:  "Artist (new)"
        //	6:  "Artist"
        PanelMain.setColumnVisible(columnModel, 5, 6, jCheckBoxCheckArtistDisplay.isSelected());
    }//GEN-LAST:event_jCheckBoxCheckArtistDisplayItemStateChanged

    private void jButtonVArtistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVArtistActionPerformed
        jTextFieldCheckAlbumArtist.setText("Various Artists");  //NOI18N
        displayMatchTracks(19);
    }//GEN-LAST:event_jButtonVArtistActionPerformed

    private void jButtonCheckUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckUpActionPerformed

        int[] selectedRows = jTableCheck.getSelectedRows();
        int selectedRow;
        ArrayList<Integer> selectedNew= new ArrayList();
        for(int i=0; i<selectedRows.length; i++) {
            selectedRow = selectedRows[i];
            int newRow = selectedRow-1;
            if(newRow>=0) {
                moveCheckRow(selectedRow, newRow);
                selectedNew.add(newRow);
            }
        }
        jTableCheck.clearSelection();
        for(int i : selectedNew) {
            jTableCheck.addRowSelectionInterval(i, i);
        }
    }//GEN-LAST:event_jButtonCheckUpActionPerformed

    private void jButtonCheckDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckDownActionPerformed
        int[] selectedRows = jTableCheck.getSelectedRows();
        int selectedRow;
        ArrayList<Integer> selectedNew= new ArrayList();
        for(int i=selectedRows.length-1; i>=0; i--) {
            selectedRow = selectedRows[i];
            int newRow = selectedRow+1;
            if(newRow>=0) {
                moveCheckRow(selectedRow, newRow);
                selectedNew.add(newRow);
            }
        }
        jTableCheck.clearSelection();
        for(int i : selectedNew) {
            jTableCheck.addRowSelectionInterval(i, i);
        }
    }//GEN-LAST:event_jButtonCheckDownActionPerformed

    private void jButtonCheckApplyAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckApplyAlbumActionPerformed
        folder.setNewAlbum(jTextFieldCheckAlbum.getText());
        displayMatchTracks(11);
    }//GEN-LAST:event_jButtonCheckApplyAlbumActionPerformed

    private void jButtonCheckApplyYearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckApplyYearActionPerformed
        folder.setNewYear(jTextFieldCheckYear.getText());
        displayMatchTracks(13);
    }//GEN-LAST:event_jButtonCheckApplyYearActionPerformed

    private void jButtonVAlbumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVAlbumActionPerformed
        jTextFieldCheckAlbum.setText("Various Albums");  //NOI18N
        displayMatchTracks(11);
    }//GEN-LAST:event_jButtonVAlbumActionPerformed

    private void jButtonCheckApplyAlbumArtistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckApplyAlbumArtistActionPerformed
        folder.setNewAlbumArtist(jTextFieldCheckAlbumArtist.getText());
        displayMatchTracks(19);
    }//GEN-LAST:event_jButtonCheckApplyAlbumArtistActionPerformed

    private void jButtonCheckKO1LibraryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckKO1LibraryActionPerformed
        folder.action = ProcessCheck.Action.KO_LIBRARY;
        PanelCheck.addToActionQueue(folder);
        this.dispose();
    }//GEN-LAST:event_jButtonCheckKO1LibraryActionPerformed

    private void jButtonSelectOriginalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectOriginalActionPerformed
        jComboBoxCheckMatches.setSelectedIndex(folder.getMatches().size());
    }//GEN-LAST:event_jButtonSelectOriginalActionPerformed

    private void jButtonSelectDuplicateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSelectDuplicateActionPerformed
        DuplicateInfo duplicateInfo = (DuplicateInfo) jComboBoxCheckDuplicates.getSelectedItem();
        String field;
        String label;
        if(!duplicateInfo.getAlbum().equals("")) {
            field = "album";
            label = duplicateInfo.getAlbum();
        }
        else if(!duplicateInfo.getAlbumArtist().equals("")) {
            field = "artist";
            label = duplicateInfo.getAlbumArtist();
        }
        else {
            field = "artist";
            label = "%";
        }
        
        PanelSelect.setSelected(field, label);
        this.dispose();
    }//GEN-LAST:event_jButtonSelectDuplicateActionPerformed

    private void jButtonPlayerNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayerNextActionPerformed
        PanelMain.getQueueModel().next();
    }//GEN-LAST:event_jButtonPlayerNextActionPerformed

    private void jButtonPlayerPreviousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayerPreviousActionPerformed
        PanelMain.getQueueModel().previous();
    }//GEN-LAST:event_jButtonPlayerPreviousActionPerformed

    private void jButtonCheckScannerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCheckScannerActionPerformed
        if(folder.getFilesAudio().size()>-1) {
            FileInfoDisplay file = folder.getFilesAudio().get(0);
            DialogScanner.main(null, folder.getRelativePath()+file.getFilename());
        }
        
    }//GEN-LAST:event_jButtonCheckScannerActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        
        if((JOptionPane.showConfirmDialog(null, Inter.get("Msg.Check.DeleteFilesQuestion"), 
                Inter.get("Label.Confirm"),  //NOI18N
                JOptionPane.YES_NO_OPTION) 
                    == JOptionPane.YES_OPTION)) {
            int[] selectedRows = jTableCheck.getSelectedRows();
            for(int i=selectedRows.length-1; i>=0; i--) {
                int indexSelected = selectedRows[i];
                FileInfoDisplay file = folder.getFilesAudioTableModel().getFiles().get(indexSelected);
                File myFile = file.getFullPath(); 
                myFile.delete();
                if(progressBar!=null) {
                    progressBar.progress(Inter.get("Msg.Check.DeletingFiles"));  //NOI18N
                }
            }
            if(progressBar!=null) {
                progressBar.reset();
            }
			enableAddOptions(false);
			folder.reCheck();
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButtonPlayerForwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPlayerForwardActionPerformed
        PanelMain.forward();
    }//GEN-LAST:event_jButtonPlayerForwardActionPerformed

       /**
	 *
	 * @param enable
	 */
	private static void enableAddOptions(boolean enable) {
		jTextFieldCheckAlbum.setEnabled(enable);
		jTextFieldCheckAlbumArtist.setEnabled(enable);
        jButtonVAlbum.setEnabled(enable);
        jButtonVArtist.setEnabled(enable);
		jButtonCheckSingleFolder.setEnabled(enable);
		jButtonCheckSaveTags.setEnabled(enable);
		jButtonCheckKO.setEnabled(enable);
        jButtonCheckKO1Library.setEnabled(enable);
		jButtonCheckOK.setEnabled(enable);
		jButtonCheckDelete.setEnabled(enable);
        Swing.enableComponents(jPanelGenre, enable);
		//whatever jPanelGenre enabled or not:
		jCheckBoxCheckGenreDisplay.setEnabled(true); 
		jComboBoxCheckMatches.setEnabled(enable);
		jComboBoxCheckDuplicates.setEnabled(enable);
		jTextFieldCheckYear.setEnabled(enable);
		jTableCheck.setEnabled(enable);
		jButtonCheckSearch.setEnabled(enable);
		jButtonCheckApplyArtist.setEnabled(enable);
        jButtonCheckApplyAlbumArtist.setEnabled(enable);
        jButtonCheckApplyAlbum.setEnabled(enable);
        jButtonCheckApplyYear.setEnabled(enable);
		jPanelCheckCoverThumb.setEnabled(enable);
		jButtonCheckEditTag.setEnabled(enable);
		jButtonCheckScanner.setEnabled(enable);
		jButtonSelectOriginal.setEnabled(enable);
	}
    
	/**
	 *
	 * @param pattern
	 */
	public static void applyPattern(String pattern) {

        String entryValue;
        for(FileInfoDisplay file : folder.getFilesAudioTableModel().getFiles()) {            
            for (Map.Entry<String, String> entry : PatternProcessor.getMap(folder.getRelativePath()+file.getFilename(), pattern).entrySet()) {
                entryValue = entry.getValue();
                switch(entry.getKey()) {
                    case "%b": //album
                        jTextFieldCheckAlbum.setText(entryValue);
                        file.setAlbum(entryValue);
//                        displayMatchTracks(11);
                        break;
                    case "%a": //artist
                        file.setArtist(entryValue);
//                        displayMatchTracks(5);
                        break;
                    case "%y": //year
                        jTextFieldCheckYear.setText(entryValue);
                        file.setYear(entryValue);
//                        displayMatchTracks(13);
                        break;
                    case "%t": //title
                        file.setTitle(entryValue);
//                        displayMatchTracks(7);
                        break;
                    case "%z": //album artist
                        jTextFieldCheckAlbumArtist.setText(entryValue);
                        file.setAlbumArtist(entryValue);
//                        displayMatchTracks(19);
                        break;
                    case "%c": //comment
                        file.setComment(entryValue);
//                        displayMatchTracks(21);
                        break;
                    case "%d": //disc#
                        file.setDiscNo(Utils.getInteger(entryValue));
                        break;
                    case "%l": //# of tracks
                        file.setTrackTotal(Utils.getInteger(entryValue));
                        break;
                    case "%n": //track#
                        file.setTrackNo(Utils.getInteger(entryValue));
                        break;
                    case "%x": //# of discs
                        file.setDiscTotal(Utils.getInteger(entryValue));
                        break;
                }
            }
        }
        displayMatchTracks();
        //TODO: Is this not done yet ? >> Re-calculate differences and check AlbumArtist, album, artist, title accordingly
        folder.getFilesAudioTableModel().fireTableDataChanged();
        
    }
	
    /**
	 * Displays a folder in check tab
	 */
	public static  void displayFolder(){
	
		enableAddOptions(false);
		clearCheckTab();
		
		if(folder!=null) {
			progressBar.setIndeterminate(""); //NOI18N
			FolderInfoResult result;

		//Number of tracks
			result = folder.getResults().get("nbFiles");  //NOI18N
			jLabelCheckNbFiles.setText(result.getDisplayText());
			jLabelCheckNbFiles.setToolTipText(result.getDisplayToolTip());
			//Also displayed in displayMatch
		
			if(folder.getFilesAudio().size()<=0) { //No supported audio files on folder
				jTableCheck.setEnabled(true);
				jButtonCheckOK.setBackground(Color.gray);
				jButtonCheckDelete.setEnabled(true);
				jButtonCheckKO.setEnabled(true);
				
				unSelectCheckBoxes();
				
				//Hide track & disc # columns
				PanelMain.setColumnVisible(columnModel, 1, 4, false);
				
				progressBar.reset();
			}
			else {
                //Start this at first, this is in a thread so it goes on
                listCovers();
                
				//Display track & disc # columns
				PanelMain.setColumnVisible(columnModel, 1, 4, true);
				
			//HasID3v1
				result = folder.getResults().get("hasID3v1");  //NOI18N
				jLabelCheckID3v1Tag.setText(result.getDisplayText());

			//isReplayGainDone
				result = folder.getResults().get("isReplayGainDone");  //NOI18N
				jLabelCheckReplayGainTag.setText(result.getDisplayText());
				
			//GENRE
				result = folder.getResults().get("genre"); //NOI18N
                setAddCheckBox(jCheckBoxCheckGenreDisplay, result);  //NOI18N
				if(folder.getNewGenre()!=null) {
					applyGenre(folder.getNewGenre());
				} else if(result.errorLevel>0) {
                    highlightGenre(true);
				}
				else {
                    highlightGenre(result.value, Color.GREEN);
				}

			//COMMENT
//				setAddCheckBox(jCheckBoxCheckCommentDisplay, folder.getResults().get("comment"));  //NOI18N

			//BITRATE
				result = folder.getResults().get("bitRate"); //NOI18N
                setAddCheckBox(jCheckBoxCheckBitRateDisplay, result);  //NOI18N
				jLabelCheckMeanBitRateTag.setText(result.getDisplayText());

			//FORMAT
				setAddCheckBox(jCheckBoxCheckFormatDisplay, folder.getResults().get("format"));  //NOI18N

			//BPM
//				setAddCheckBox(jCheckBoxCheckBPMDisplay, folder.getResults().get("bpm"));  //NOI18N

			//LENGTH
				setAddCheckBox(jCheckBoxCheckLengthDisplay, folder.getResults().get("length"));  //NOI18N

			//SIZE
				setAddCheckBox(jCheckBoxCheckSizeDisplay, folder.getResults().get("size"));  //NOI18N

			//COVER
                result=folder.getResults().get("cover"); //NOI18N
				setAddCheckBox(jCheckBoxCheckCoverDisplay, result, false);  //NOI18N
				
				//By default, display cover selected for Saving 
                BufferedImage myImage;
				if(folder.getNewImage()!=null && folder.action.equals(ProcessCheck.Action.SAVE)) {
					myImage = folder.getNewImage();
				}
				else {
					//Then, display first cover from audio files tags
					myImage = folder.getFirstCoverFromTags();
				}
				PanelCover coverImg = (PanelCover) jPanelCheckCoverThumb;
				if(myImage!=null) {
					coverImg.setImage(myImage);
					jLabelCoverInfo.setText(result.getDisplayText());
				}
				else {
					coverImg.setImage(null);
					if(folder.getFilesImage().size()>0) {
						jPanelCheckCoverThumb.setEnabled(true);
						jLabelCoverInfo.setText(FolderInfoResult.colorField(Inter.get("Label.Select"), 2, true)); //NOI18N
					}
					else {
						jPanelCheckCoverThumb.setEnabled(false);
						jLabelCoverInfo.setText(FolderInfoResult.colorField(Inter.get("Label.None"), 2, true)); //NOI18N
					}
				}
				

			//MATCHES
				displayMatches();
			}
		}
	}
    
	/**
	 *
	 * @param highlight
	 */
	public static void highlightGenre(boolean highlight) {
        Component[] components = jPanelGenre.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if(highlight) {
                    button.setBackground(Color.LIGHT_GRAY);
                    button.setIcon(IconBuffer.getCoverIcon(button.getActionCommand(), IconBuffer.IconVersion.NORMAL_30, "genre"));
                }
                else {
                    button.setBackground(Color.DARK_GRAY);
                    button.setIcon(IconBuffer.getCoverIcon(button.getActionCommand(), IconBuffer.IconVersion.GRAY_30, "genre"));
                }
            }
        }
    }
    
	/**
	 *
	 * @param genre
	 * @param selectedColor
	 */
	public static void highlightGenre(String genre, Color selectedColor) {
        Component[] components = jPanelGenre.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if(button.getActionCommand().equals(genre)) {
                    button.setBackground(selectedColor);
                    button.setIcon(IconBuffer.getCoverIcon(button.getActionCommand(), IconBuffer.IconVersion.NORMAL_30, "genre"));
                }
                else {
                    button.setBackground(Color.DARK_GRAY);
                    button.setIcon(IconBuffer.getCoverIcon(button.getActionCommand(), IconBuffer.IconVersion.GRAY_30, "genre"));
                }
            }
        }
    }
    
    private static void listCovers() {
         Thread t = new Thread("Thread.CoverSelectGUI.listCovers") {
            @Override
            public void run() {
                jLabelCoverFound.setText(Inter.get("Msg.Select.RetrievingCovers")); //NOI18N
                
				List<Cover> coversList=folder.getCoverList();
				progressBarCover.setup(coversList.size());
                for(final Cover cover : coversList) {
                    cover.readImages();
                    if(cover.getType().equals(Cover.CoverType.MB)) {
                        //List all MB covers
                        for(Cover.MbImage mbImage : cover.getCoverArtArchiveList()) {
                            Cover coverMB = new Cover(Cover.CoverType.MB, "", 
									"<html>"+mbImage.getMsg()+"<BR>"+cover.getName()+"</html>"); //NOI18N
                            coverMB.setImage(mbImage.getImage());
                             if(coverMB.getImage()!=null) {
                                 coversList.add(coverMB); 
                                 setCoverIfBetter(cover);
                             }
                        }
                    }
                    else {
                        setCoverIfBetter(cover);
                    }
                    progressBarCover.progress("");
                }
                progressBarCover.reset();
                //Select better image and display a message
				Cover cover = new Cover("NotFoundName", null, "");
				if(coversList.size()>0) {
					cover = Collections.min(coversList); //This is best cover found
				}
				setCover(cover);

            }
        };
        t.start();
        
    }
    
    private static void setCoverIfBetter(Cover cover) {
        if(cover.compareTo(currentCover)<0) {
            setCover(cover);
        }
    }
    
    private static Cover currentCover=new Cover(Cover.CoverType.FILE, "value", "name");
    private static void setCover(Cover cover) {
        BufferedImage image = cover.getImage();
        if(image!=null) {
            //TODO: Display more text: source string + make jlabel bigger and remove "Search:" label (good as it will reduce year panel)
            jLabelCoverFound.setText(cover.getSizeHTML());
            //Display selected image on MainGUI
            PanelCover mainCoverImg = (PanelCover) DialogCheck.jPanelCheckCoverThumb;
            mainCoverImg.setImage(image);
            currentCover=cover;
        }
        else {
            jLabelCoverFound.setText("None found.");
        }
        
        if(folder.getFirstCoverFromTags()==null) {
            if(folder.getFilesImage().size()>0 || image!=null) {
                jPanelCheckCoverThumb.setEnabled(true); //TODO: This does not work on jPanelCheckCoverThumb (check all usages)
                jLabelCoverInfo.setText(FolderInfoResult.colorField(Inter.get("Label.Select"), 2, true)); //NOI18N
            }
            else {
                jPanelCheckCoverThumb.setEnabled(false);
                jLabelCoverInfo.setText(FolderInfoResult.colorField(Inter.get("Label.None"), 2, true)); //NOI18N
            }
        }
        
    }
    
    private static void displayMatches() {
		jComboBoxCheckMatches.removeAllItems();
		//Add matches
        List<ReleaseMatch> matches = folder.getMatches();
        if(matches!=null) {
            for(ReleaseMatch releaseMatch : matches) {
                jComboBoxCheckMatches.addItem("<html>"+releaseMatch.toString()+"</html>"); //NOI18N
            }
        }
		//Add originals
		for(ReleaseMatch myMatch : folder.getOriginals()) {
			jComboBoxCheckMatches.addItem("<html>"+myMatch.toString()+"</html>"); //NOI18N
		}
        jButtonSelectOriginal.setEnabled(folder.getMatches().size()>0);
		//TODO: How to have jComboBoxMatches proper display ???
//		jComboBoxMatches.revalidate();
//		jComboBoxMatches.updateUI();
//		jComboBoxMatches.repaint();
//		jComboBoxMatches.contentsChanged(null);
//		jComboBoxMatches.firePopupMenuWillBecomeVisible();
//		jComboBoxMatches.setVisible(true);
	}
    
    private void displayMatch(int matchId) throws CloneNotSupportedException {
		enableAddOptions(false);
        progressBar.setIndeterminate(Inter.get("Msg.Scan.SearchingMatches"));  //NOI18N
        ReleaseMatch match = folder.getMatch(matchId);//TODO: support match==null (should not happen)
        Map<String, FolderInfoResult> results = folder.getResults(); 
		
        folder.analyseMatch(matchId, progressBar);
		
        progressBar.setIndeterminate(Inter.get("Msg.Scan.AnalyzingMatch"));  //NOI18N
        FolderInfoResult result = folder.getResults().get("nbFiles");  //NOI18N
        jLabelCheckNbFiles.setText(result.getDisplayText());
        jLabelCheckNbFiles.setToolTipText(result.getDisplayToolTip());

    //DUPLICATES
        jComboBoxCheckDuplicates.removeAllItems();
        if(match!=null) {
            if(match.getDuplicates()!=null) {
                if(match.getDuplicates().size()>0) {
                    for(DuplicateInfo duplicate : match.getDuplicates()) {
                        jComboBoxCheckDuplicates.addItem(duplicate);
                    }
                    jButtonSelectDuplicate.setEnabled(true);
                }
                else {
                    jComboBoxCheckDuplicates.addItem(FolderInfoResult.colorField(Inter.get("Label.None"),0));  //NOI18N
                    jButtonSelectDuplicate.setEnabled(false);
                }
            }
        }

    //TRACKS (ARTIST, TITLE, ...)

        //Number of files vs tracks
        result = results.get("nbFiles");  //NOI18N
        jLabelCheckNbFiles.setText(result.getDisplayText());
        jLabelCheckNbFiles.setToolTipText(result.getDisplayToolTip());
        if(match!=null) {
            List<ReleaseMatch.Track> tracks=match.getTracks(progressBar); 
            jLabelCheckNbTracks.setText(String.valueOf(tracks.size()));
        }
        else {
            jLabelCheckNbTracks.setText("");
        }
        progressBar.setIndeterminate(Inter.get("Msg.Scan.AnalyzingMatch"));  //NOI18N
    
        //Display all tracks 
        displayMatchTracks();

        //Need to display albumArtist, Album and year after displaying tracks as 
        //analysis is done there
        if(match!=null) {
            jTextFieldCheckAlbumArtist.setText(match.getArtist());
            jTextFieldCheckAlbum.setText(match.getAlbum());
            result = results.get("year"); //NOI18N
            if (!match.getYear().equals("")) { 				  //NOI18N
                jTextFieldCheckYear.setText(match.getYear()); 			
            }
            else if(!result.value.startsWith("{")){ //NOI18N
                jTextFieldCheckYear.setText(result.value);
            }
            else {
                jTextFieldCheckYear.setText(""); //NOI18N
            }
        }
        else {
            jTextFieldCheckAlbumArtist.setText("");
            jTextFieldCheckAlbum.setText("");
            jTextFieldCheckYear.setText(""); //NOI18N
        }
        jLabelCheckAlbumTag.setText(results.get("album").getDisplayText()); //NOI18N
        jLabelCheckAlbumArtistTag.setText(results.get("albumArtist").getDisplayText()); //NOI18N
        jLabelCheckYearTag.setText(result.getDisplayText());
        jLabelCheckYearTag.setToolTipText(result.getDisplayToolTip());


    //ENABLE (OR NOT) OK, KO and DEL buttons
        enableAddOptions(true);
        jButtonCheckKO.setEnabled(!folder.isValid());
        jButtonCheckOK.setEnabled(folder.isValid());

        if(folder.isValid()) {
            if(folder.isWarning()) {
                jButtonCheckOK.setBackground(Color.orange);
                jButtonCheckKO.setEnabled(true);
                jButtonCheckDelete.setEnabled(true);
            }
            else {
                jButtonCheckOK.setBackground(new Color(0, 128, 0));
                jButtonCheckDelete.setEnabled(false);
            }
        }
        else {
            jButtonCheckOK.setBackground(Color.gray);
            jButtonCheckDelete.setEnabled(true);
        }

        progressBar.reset();
	}

    private static void applyGenre(String genre) {
		if(folder.getFilesAudioTableModel() != null) { //Happens at init, because we need to fillup jComboBoxGenre before tableModelAddTags
            folder.setNewGenre(genre);
            displayMatchTracks(9);
		}
		
		FolderInfoResult result = folder.getResults().get("genre"); //NOI18N
		if(result.errorLevel<=0 && result.value.equals(genre)) {
			highlightGenre(result.value, Color.GREEN);
		}
		else {
			highlightGenre(genre, Color.ORANGE);
		}
	}

    /**
	 * Clear Check tab
	 */
	public static void clearCheckTab() {
		jTextFieldCheckAlbumArtist.setText("");  //NOI18N
		jTextFieldCheckAlbum.setText("");  //NOI18N
		jComboBoxCheckMatches.removeAllItems();
		jComboBoxCheckDuplicates.removeAllItems();
		jTextFieldCheckYear.setText("");  //NOI18N
//		folderInfo.getFilesAudioTableModel().clear();
		PanelCover coverImg = (PanelCover) jPanelCheckCoverThumb;
		coverImg.setImage(null);
		jLabelCoverInfo.setText(" "); //NOI18N
        highlightGenre(false);
		jLabelCheckAlbumArtistTag.setText(" ");  //NOI18N
		jLabelCheckAlbumTag.setText(" ");  //NOI18N
		jLabelCheckYearTag.setText(" ");  //NOI18N
		jLabelCheckID3v1Tag.setText(" ");  //NOI18N
		jLabelCheckReplayGainTag.setText(" ");  //NOI18N
		jLabelCheckNbTracks.setText(" ");  //NOI18N
		jLabelCheckNbFiles.setText(" ");  //NOI18N
		jLabelCheckMeanBitRateTag.setText(" ");  //NOI18N
		
//		jPanelCheckTags.setBorder(javax.swing.BorderFactory.createTitledBorder("Tags")); 
		unSelectCheckBoxes();
	}
    
    private void moveCheckRow(int fromIndex, int toIndex) {
		try {
			folder.getFilesAudioTableModel().moveRow(fromIndex, toIndex);
			displayMatchTracks();
		} catch (CloneNotSupportedException ex) {
			Jamuz.getLogger().log(Level.SEVERE, "moveCheckRow", ex); //NOI18N
		}
	}

	private static void unSelectCheckBoxes() {
		unSelectCheckBox(jCheckBoxCheckArtistDisplay);
		unSelectCheckBox(jCheckCheckTitleDisplay);
		unSelectCheckBox(jCheckBoxCheckGenreDisplay);
		unSelectCheckBox(jCheckBoxCheckAlbumDisplay);
		unSelectCheckBox(jCheckBoxCheckAlbumArtistDisplay);
		unSelectCheckBox(jCheckBoxCheckYearDisplay);
		unSelectCheckBox(jCheckBoxCheckCoverDisplay);
		unSelectCheckBox(jCheckBoxCheckFormatDisplay);
		unSelectCheckBox(jCheckBoxCheckBitRateDisplay);
		unSelectCheckBox(jCheckBoxCheckLengthDisplay);
		unSelectCheckBox(jCheckBoxCheckSizeDisplay);
		unSelectCheckBox(jCheckBoxCheckCommentDisplay);
		unSelectCheckBox(jCheckBoxCheckBPMDisplay);
	}  
    
    private static void unSelectCheckBox(JCheckBox checkbox) {
		checkbox.setSelected(false);
		checkbox.setForeground(Color.BLACK);
	}  
    
    private static void displayMatchColumn(Map<String, FolderInfoResult> results, int colId) {
		
		javax.swing.JCheckBox checkbox=null;
        boolean select=true;
		switch (colId) {
//			case 1: field="discNoFull";	break;
//			case 3: field="trackNoFull"; break;
			case 5: checkbox=jCheckBoxCheckArtistDisplay;	break;
			case 7: checkbox=jCheckCheckTitleDisplay; break;
			case 9: checkbox=jCheckBoxCheckGenreDisplay; break;
			case 11: checkbox=jCheckBoxCheckAlbumDisplay; break; //select=false; break;
			case 13: checkbox=jCheckBoxCheckYearDisplay; select=false; break;
			case 19: checkbox=jCheckBoxCheckAlbumArtistDisplay; break; //select=false; break;
			case 21: checkbox=jCheckBoxCheckCommentDisplay; select=false; break;
			case 24: checkbox=jCheckBoxCheckBPMDisplay; break;
		}
		setAddCheckBox(checkbox, results.get(FolderInfo.getField(colId)), select);  //NOI18N
	}
        
    private static void displayMatchColumns(Map<String, FolderInfoResult> results) {
		Integer[] columns = {5, 7, 9, 11, 13, 19, 21, 24};
        for(int colId : columns) {
			displayMatchColumn(results, colId);
        }
	}

    private static void setAddCheckBox(JCheckBox checkbox, FolderInfoResult result) {
        setAddCheckBox(checkbox, result, true);
    }
    
    private static void setAddCheckBox(JCheckBox checkbox, FolderInfoResult result, boolean select) {
		if(checkbox!=null) {
            if(select) {
                checkbox.setSelected(result.isNotValid());
            }
			checkbox.setForeground(result.getDisplayColor());
		}
	}
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private static javax.swing.JButton jButtonCheckApplyAlbum;
    private static javax.swing.JButton jButtonCheckApplyAlbumArtist;
    private static javax.swing.JButton jButtonCheckApplyArtist;
    private static javax.swing.JButton jButtonCheckApplyYear;
    private static javax.swing.JButton jButtonCheckDelete;
    private javax.swing.JButton jButtonCheckDown;
    protected static javax.swing.JButton jButtonCheckEditTag;
    private static javax.swing.JButton jButtonCheckKO;
    private static javax.swing.JButton jButtonCheckKO1Library;
    private static javax.swing.JButton jButtonCheckOK;
    private static javax.swing.JButton jButtonCheckOpen;
    private static javax.swing.JButton jButtonCheckPreview;
    private static javax.swing.JButton jButtonCheckSaveTags;
    private static javax.swing.JButton jButtonCheckScanner;
    private static javax.swing.JButton jButtonCheckSearch;
    private static javax.swing.JButton jButtonCheckSingleFolder;
    private javax.swing.JButton jButtonCheckUp;
    private javax.swing.JButton jButtonDelete;
    private static javax.swing.JButton jButtonPlayerForward;
    private static javax.swing.JButton jButtonPlayerNext;
    private static javax.swing.JButton jButtonPlayerPrevious;
    private javax.swing.JButton jButtonSelectDuplicate;
    private static javax.swing.JButton jButtonSelectOriginal;
    private static javax.swing.JButton jButtonVAlbum;
    private static javax.swing.JButton jButtonVArtist;
    private static javax.swing.JCheckBox jCheckBoxCheckAlbumArtistDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckAlbumDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckArtistDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckBPMDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckBitRateDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckCommentDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckCoverDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckFormatDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckGenreDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckLengthDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckSizeDisplay;
    private static javax.swing.JCheckBox jCheckBoxCheckYearDisplay;
    private static javax.swing.JCheckBox jCheckCheckTitleDisplay;
    private static javax.swing.JComboBox jComboBoxCheckDuplicates;
    private static javax.swing.JComboBox jComboBoxCheckMatches;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private static javax.swing.JLabel jLabelCheckAlbumArtistTag;
    private static javax.swing.JLabel jLabelCheckAlbumTag;
    private static javax.swing.JLabel jLabelCheckID3v1;
    private static javax.swing.JLabel jLabelCheckID3v1Tag;
    private static javax.swing.JLabel jLabelCheckMeanBitRateTag;
    private static javax.swing.JLabel jLabelCheckNbFiles;
    private static javax.swing.JLabel jLabelCheckNbTracks;
    private static javax.swing.JLabel jLabelCheckReplayGain;
    private static javax.swing.JLabel jLabelCheckReplayGainTag;
    private static javax.swing.JLabel jLabelCheckYearTag;
    private static javax.swing.JLabel jLabelCoverFound;
    private static javax.swing.JLabel jLabelCoverInfo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    public static javax.swing.JPanel jPanelCheckCoverThumb;
    private javax.swing.JPanel jPanelCheckFolder;
    private static javax.swing.JPanel jPanelCheckTags;
    private static javax.swing.JPanel jPanelGenre;
    private static javax.swing.JProgressBar jProgressBarCheckDialog;
    private javax.swing.JProgressBar jProgressBarCover;
    private static javax.swing.JScrollPane jScrollPaneCheckTags;
    private static javax.swing.JTable jTableCheck;
    private static javax.swing.JTextField jTextFieldCheckAlbum;
    private static javax.swing.JTextField jTextFieldCheckAlbumArtist;
    private static javax.swing.JTextField jTextFieldCheckYear;
    // End of variables declaration//GEN-END:variables

}
