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

import jamuz.process.check.PanelCheck;
import jamuz.FileInfoInt;
import jamuz.process.check.FolderInfo;
import jamuz.IconBufferCover;
import jamuz.IconBuffer;
import jamuz.Jamuz;
import jamuz.gui.swing.ListElement;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;
import jamuz.gui.swing.ListModelSelector;
import jamuz.gui.swing.ListCellRendererSelector;
import jamuz.gui.swing.PopupListener;
import jamuz.gui.swing.TableColumnModel;
import jamuz.gui.swing.TableModel;
import jamuz.player.Mplayer;
import jamuz.player.Mplayer.AudioCard;
import jamuz.utils.Desktop;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.ProcessAbstract;
import jamuz.utils.Swing;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JMenu;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelSelect extends javax.swing.JPanel {

    private static TableModel tableModel;
    private static final TableColumnModel TABLE_COLUMN_MODEL = new TableColumnModel();
    
    private static FillMainListThread tFillMainTable;
    private static FillListsThread tFilllists;
    
    private static ArrayList<FileInfoInt> fileInfoList;

    private static String selGenre; //TODO: Deriver ListElement et l'appliquer a selGenre (pour affichage icone de la même façon)
	private static ListElement selArtist;
	private static ListElement selAlbum; 
	//FIXME PLAYER ex: Album "Charango" is either from Morcheeba or Yannick Noah
    //BUT seen as only one album in Select tab

	/**
	 *
	 */
    public static String[] comboCopyRights;

    /**
     * Return file list
     * @return
     */
    public static ArrayList<FileInfoInt> getFileInfoList() {
        return fileInfoList;
    }
    
    /**
     * Creates new form PanelSelect
     */
    public PanelSelect() {
        initComponents();
    }

	/**
	 *
	 */
	public static void refreshTable() {
        		
        //clear the table
		jTableSelect.setRowSorter(null);
		tableModel.clear();
        jLabelSelectedSummary.setText("");
		//Fill selection lists (genre, album, artist)
		fileInfoList= new ArrayList<>();
		fillListsInThread("all");  //NOI18N
        
        //TODO: This clears user year selection (though used) so it is confusing.
        //But it is needed to refresh after a check/scan to get the potential new min/max year
        //Need a way to address both issues
        setYearSpinners(); 
	}

    //TODO: Put that in enum CheckedFlag
    private Color getColor(FolderInfo.CheckedFlag checkdFlag) {
        Color color=Color.WHITE;
        switch(checkdFlag) {
            case KO:
                color=Color.RED;
                break;
            case OK:
                color=new Color(0, 128, 0);
                break;
            case OK_WARNING:
                color=Color.ORANGE;
                break;
            case UNCHECKED:
                color=Color.WHITE;
                break;
        }
        return color;
    }
    
    /**
     * extended init
     */
    public void initExtended() {

        jCheckBoxSelectCheckedFlag0.setBackground(
				getColor(FolderInfo.CheckedFlag.UNCHECKED));
        jCheckBoxSelectCheckedFlag1.setBackground(
				getColor(FolderInfo.CheckedFlag.KO));
        jCheckBoxSelectCheckedFlag2.setBackground(
				getColor(FolderInfo.CheckedFlag.OK_WARNING));
        jCheckBoxSelectCheckedFlag3.setBackground(
				getColor(FolderInfo.CheckedFlag.OK));
        
        comboCopyRights = new String[5];
        comboCopyRights[0] = Inter.get("Label.BestOf.NotDefined");  //NOI18N
        comboCopyRights[1] = Inter.get("Label.BestOf.OwnPhysical");  //NOI18N
        comboCopyRights[2] = Inter.get("Label.BestOf.OwnDigital");  //NOI18N
        comboCopyRights[3] = Inter.get("Label.BestOf.Contributed");  //NOI18N
        comboCopyRights[4] = Inter.get("Label.BestOf.NoSupport");  //NOI18N
        
        jComboBoxBestOfCopyRight.removeAllItems();
        jComboBoxBestOfCopyRight.addItem(Inter.get("Label.All"));  //NOI18N
        for (String copyRight : comboCopyRights) {
            jComboBoxBestOfCopyRight.addItem(copyRight);
        }

        //TODO: Use the same method as for year to have real bpm range
        SpinnerModel bpmModel; 
        bpmModel = new SpinnerNumberModel(0, -500, 220, 20);
        jSpinnerSelectBpmFrom.setModel(bpmModel);
        bpmModel = new SpinnerNumberModel(220, 0, 5000, 20); 
        jSpinnerSelectBpmTo.setModel(bpmModel);
        
        
        //Menu listener
        ActionListener menuListener = (ActionEvent e) -> {
			JMenuItem source = (JMenuItem)(e.getSource());
			String sourceTxt=source.getText();
			if(sourceTxt.equals(Inter.get("Button.Edit"))) { //NOI18N
				menuEdit();
			}
			else if(sourceTxt.equals(Inter.get("MainGUI.jButtonSelectQueue.text"))) { //NOI18N
				menuQueue();
			}
			else if(sourceTxt.equals(Inter.get("MainGUI.jButtonSelectQueueAll.text"))) { //NOI18N
				menuQueueAll();
			}
			else if(sourceTxt.equals("Preview")) { //NOI18N
				menuPreview();
			}
			else if(sourceTxt.equals(Inter.get("Label.Check"))) { //NOI18N
				menuCheck();
			}
			else {
				Popup.error(Inter.get("UNKNOWN MENU ITEM"));
			}
		};
        
        JMenuItem  menuItem = new JMenuItem(Inter.get("MainGUI.jButtonSelectQueue.text")); //NOI18N
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
        menuItem = new JMenuItem(Inter.get("MainGUI.jButtonSelectQueueAll.text")); //NOI18N
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
		menuItem = new JMenuItem("Preview"); //NOI18N
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
        menuItem = new JMenuItem(Inter.get("Label.Check")); //NOI18N
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
		//Add links menu items
        File f = Jamuz.getFile("AudioLinks.txt", "data");
        if(f.exists()) {
            JMenu menuLinks = new JMenu(Inter.get("Label.Links")); //NOI18N
            List<String> lines;
            try {
                lines = Files.readAllLines(Paths.get(f.getAbsolutePath()), Charset.defaultCharset());
                for(String line : lines) {
                    if(line.contains("|")) {
                        String[] splitted = line.split("\\|");
                        JMenuItem menuItem1 = new JMenuItem(new OpenUrlAction(splitted[0], splitted[1]));
                        menuLinks.add(menuItem1);
                    }
                }
                jPopupMenu1.add(menuLinks);
            } catch (IOException ex) {
                Jamuz.getLogger().log(Level.SEVERE, null, ex);
            }
        }
		//TODO: Add " (external)" to menu name
		menuItem = new JMenuItem(Inter.get("Button.Edit")); //NOI18N
        menuItem.addActionListener(menuListener);
        jPopupMenu1.add(menuItem);
 
        //Add listener to components that can bring up popup menus.
        MouseListener popupListener = new PopupListener(jPopupMenu1);
        jTableSelect.addMouseListener(popupListener);

        //Add a list renderer to display albums covers
        jListSelectAlbum.setBackground(Color.WHITE);
        ListCellRendererSelector rendererAlbum = new ListCellRendererSelector();
        rendererAlbum.setPreferredSize(new Dimension(0, IconBufferCover.getCoverIconSize()));
        jListSelectAlbum.setCellRenderer(rendererAlbum);
        
        //Add a list renderer to display albums covers
        jListSelectArtist.setBackground(Color.WHITE);
        ListCellRendererSelector rendererArtist = new ListCellRendererSelector();
        rendererArtist.setPreferredSize(new Dimension(0, IconBufferCover.getCoverIconSize()));
        jListSelectArtist.setCellRenderer(rendererArtist);
        
        //Add a list cell renderer to display genre icons
        jListSelectGenre.setBackground(Color.WHITE);
        ListCellRendererGenre rendererGenre = new ListCellRendererGenre();
        rendererGenre.setPreferredSize(new Dimension(0, IconBuffer.iconSize));
        jListSelectGenre.setCellRenderer(rendererGenre);

		jComboBoxSoundCard.setModel(new DefaultComboBoxModel(mplayer.getAudioCards().toArray()));
		
        //Get table model
		tableModel = (TableModel) jTableSelect.getModel();
		PanelMain.initSelectTable(tableModel, jTableSelect, TABLE_COLUMN_MODEL);

        selGenre="%";  //NOI18N
        selArtist = new ListElement("%", "artist"); //NOI18N
		selAlbum = new ListElement("%", "album"); //NOI18N
        
        setYearSpinners(); 
		refreshTable();
		
	}
    
	class OpenUrlAction extends AbstractAction {
        
        private final String url;
        
        public OpenUrlAction(String text, String url) {
            super(text, null);
            this.url = url;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = jTableSelect.getSelectedRow(); 		
			if(selectedRow>=0) { 	
				//convert to model index (as sortable model) 		
				selectedRow = jTableSelect.convertRowIndexToModel(selectedRow); 
				FileInfoInt myFileInfo = fileInfoList.get(selectedRow);

				Desktop.openBrowser(url.replaceAll("<album>", 
						myFileInfo.getAlbumArtist().concat(" ")
								.concat(myFileInfo.getAlbum())));			 		
			}
        }
    }
	
    private static void setYearSpinners() {
        double minYear = Jamuz.getDb().getYear("MIN"); //NOI18N
        minYear = (10*Math.floor(minYear/10));
        double maxYear = Jamuz.getDb().getYear("MAX"); //NOI18N
        maxYear = (10*Math.ceil(maxYear/10));
        SpinnerModel yearModel; 
        yearModel = new SpinnerNumberModel(minYear, minYear, maxYear, 10.0);
        jSpinnerSelectYearFrom.setModel(yearModel);
        //Make the year be formatted without a thousands separator.
        jSpinnerSelectYearFrom.setEditor(new JSpinner.NumberEditor(jSpinnerSelectYearFrom, "#")); //NOI18N
        
        yearModel = new SpinnerNumberModel(maxYear, minYear, maxYear, 10.0); 
        jSpinnerSelectYearTo.setModel(yearModel);
        //Make the year be formatted without a thousands separator.
        jSpinnerSelectYearTo.setEditor(new JSpinner.NumberEditor(jSpinnerSelectYearTo, "#")); //NOI18N
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
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanelSelect = new javax.swing.JPanel();
        jSplitPaneSelect = new javax.swing.JSplitPane();
        jPanelSelectSelector = new javax.swing.JPanel();
        jSplitPaneSelectArtistAlbum = new javax.swing.JSplitPane();
        jPanelSelectAlbum = new javax.swing.JPanel();
        jScrollPaneSelectAlbum = new javax.swing.JScrollPane();
        jListSelectAlbum = new javax.swing.JList();
        jRadioSelectAlbumName = new javax.swing.JRadioButton();
        jRadioSelectAlbumDate = new javax.swing.JRadioButton();
        jRadioSelectAlbumRating = new javax.swing.JRadioButton();
        jPanelSelectArtist = new javax.swing.JPanel();
        jScrollPaneSelectArtist = new javax.swing.JScrollPane();
        jListSelectArtist = new javax.swing.JList();
        jRadioSelectArtistName = new javax.swing.JRadioButton();
        jRadioSelectArtistRating = new javax.swing.JRadioButton();
        jScrollPaneSelectGenre = new javax.swing.JScrollPane();
        jListSelectGenre = new javax.swing.JList();
        jPanelSelectFilters = new javax.swing.JPanel();
        jPanelSelectYear = new javax.swing.JPanel();
        jSpinnerSelectYearFrom = new javax.swing.JSpinner();
        jSpinnerSelectYearTo = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jSpinnerSelectBpmFrom = new javax.swing.JSpinner();
        jSpinnerSelectBpmTo = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jSpinnerSelectBpmFrom1 = new javax.swing.JSpinner();
        jSpinnerSelectBpmTo1 = new javax.swing.JSpinner();
        jPanelSelectStatus = new javax.swing.JPanel();
        jCheckBoxSelectCheckedFlag0 = new javax.swing.JCheckBox();
        jCheckBoxSelectCheckedFlag3 = new javax.swing.JCheckBox();
        jCheckBoxSelectCheckedFlag2 = new javax.swing.JCheckBox();
        jCheckBoxSelectCheckedFlag1 = new javax.swing.JCheckBox();
        jPanelSelectRating = new javax.swing.JPanel();
        jCheckBoxSelectRating0 = new javax.swing.JCheckBox();
        jCheckBoxSelectRating1 = new javax.swing.JCheckBox();
        jCheckBoxSelectRating2 = new javax.swing.JCheckBox();
        jCheckBoxSelectRating3 = new javax.swing.JCheckBox();
        jCheckBoxSelectRating4 = new javax.swing.JCheckBox();
        jCheckBoxSelectRating5 = new javax.swing.JCheckBox();
        jCheckBoxSelectUpdate = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jComboBoxBestOfCopyRight = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabelPreviewDisplay = new javax.swing.JLabel();
        jComboBoxSoundCard = new javax.swing.JComboBox<>();
        jButtonPreviewStop = new javax.swing.JButton();
        jPanelSelectTracks = new javax.swing.JPanel();
        jScrollPaneSelect = new javax.swing.JScrollPane();
        jTableSelect = new javax.swing.JTable();
        jToggleButtonSelectShowExtra = new javax.swing.JToggleButton();
        jToggleButtonSelectShowFile = new javax.swing.JToggleButton();
        jToggleButtonSelectShowBasic = new javax.swing.JToggleButton();
        jLabelSelectDisplay = new javax.swing.JLabel();
        jToggleButtonSelectShowStats = new javax.swing.JToggleButton();
        jLabelSelected = new javax.swing.JLabel();
        jLabelSelectedSummary = new javax.swing.JLabel();
        jToggleButtonSelectShowRights = new javax.swing.JToggleButton();

        jSplitPaneSelect.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPaneSelectArtistAlbum.setDividerLocation(300);

        jListSelectAlbum.setModel(new DefaultListModel());
        jListSelectAlbum.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSelectAlbum.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSelectAlbumValueChanged(evt);
            }
        });
        jScrollPaneSelectAlbum.setViewportView(jListSelectAlbum);

        buttonGroup1.add(jRadioSelectAlbumName);
        jRadioSelectAlbumName.setText("Name");
        jRadioSelectAlbumName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioSelectAlbumNameItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioSelectAlbumDate);
        jRadioSelectAlbumDate.setText("Date");
        jRadioSelectAlbumDate.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioSelectAlbumDateItemStateChanged(evt);
            }
        });

        buttonGroup1.add(jRadioSelectAlbumRating);
        jRadioSelectAlbumRating.setSelected(true);
        jRadioSelectAlbumRating.setText("Rating");
        jRadioSelectAlbumRating.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioSelectAlbumRatingItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanelSelectAlbumLayout = new javax.swing.GroupLayout(jPanelSelectAlbum);
        jPanelSelectAlbum.setLayout(jPanelSelectAlbumLayout);
        jPanelSelectAlbumLayout.setHorizontalGroup(
            jPanelSelectAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectAlbumLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSelectAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSelectAlbumLayout.createSequentialGroup()
                        .addComponent(jRadioSelectAlbumName)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioSelectAlbumDate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioSelectAlbumRating)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPaneSelectAlbum, javax.swing.GroupLayout.DEFAULT_SIZE, 335, Short.MAX_VALUE)))
        );
        jPanelSelectAlbumLayout.setVerticalGroup(
            jPanelSelectAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSelectAlbumLayout.createSequentialGroup()
                .addGroup(jPanelSelectAlbumLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioSelectAlbumName)
                    .addComponent(jRadioSelectAlbumDate)
                    .addComponent(jRadioSelectAlbumRating))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneSelectAlbum))
        );

        jSplitPaneSelectArtistAlbum.setRightComponent(jPanelSelectAlbum);

        jListSelectArtist.setModel(new DefaultListModel());
        jListSelectArtist.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSelectArtist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSelectArtistValueChanged(evt);
            }
        });
        jScrollPaneSelectArtist.setViewportView(jListSelectArtist);

        buttonGroup2.add(jRadioSelectArtistName);
        jRadioSelectArtistName.setText("Name");
        jRadioSelectArtistName.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioSelectArtistNameItemStateChanged(evt);
            }
        });

        buttonGroup2.add(jRadioSelectArtistRating);
        jRadioSelectArtistRating.setSelected(true);
        jRadioSelectArtistRating.setText("Rating");
        jRadioSelectArtistRating.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jRadioSelectArtistRatingItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanelSelectArtistLayout = new javax.swing.GroupLayout(jPanelSelectArtist);
        jPanelSelectArtist.setLayout(jPanelSelectArtistLayout);
        jPanelSelectArtistLayout.setHorizontalGroup(
            jPanelSelectArtistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneSelectArtist, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanelSelectArtistLayout.createSequentialGroup()
                .addComponent(jRadioSelectArtistName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioSelectArtistRating)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelSelectArtistLayout.setVerticalGroup(
            jPanelSelectArtistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSelectArtistLayout.createSequentialGroup()
                .addGroup(jPanelSelectArtistLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioSelectArtistName)
                    .addComponent(jRadioSelectArtistRating))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneSelectArtist, javax.swing.GroupLayout.DEFAULT_SIZE, 360, Short.MAX_VALUE))
        );

        jSplitPaneSelectArtistAlbum.setLeftComponent(jPanelSelectArtist);

        jListSelectGenre.setModel(new DefaultListModel());
        jListSelectGenre.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListSelectGenre.setSelectionBackground(java.awt.Color.lightGray);
        jListSelectGenre.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListSelectGenreValueChanged(evt);
            }
        });
        jScrollPaneSelectGenre.setViewportView(jListSelectGenre);

        jPanelSelectYear.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setText(Inter.get("Tag.Year")); // NOI18N

        jLabel1.setText("BPM");

        jLabel3.setText("Album Rating");
        jLabel3.setEnabled(false);

        jSpinnerSelectBpmFrom1.setEnabled(false);

        jSpinnerSelectBpmTo1.setEnabled(false);

        javax.swing.GroupLayout jPanelSelectYearLayout = new javax.swing.GroupLayout(jPanelSelectYear);
        jPanelSelectYear.setLayout(jPanelSelectYearLayout);
        jPanelSelectYearLayout.setHorizontalGroup(
            jPanelSelectYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectYearLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSelectYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSelectYearLayout.createSequentialGroup()
                        .addGroup(jPanelSelectYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSelectYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinnerSelectYearFrom)
                            .addComponent(jSpinnerSelectBpmFrom))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelSelectYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSpinnerSelectYearTo)
                            .addComponent(jSpinnerSelectBpmTo)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSelectYearLayout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerSelectBpmFrom1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerSelectBpmTo1)))
                .addContainerGap())
        );
        jPanelSelectYearLayout.setVerticalGroup(
            jPanelSelectYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectYearLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSelectYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jSpinnerSelectYearFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerSelectYearTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSelectYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jSpinnerSelectBpmTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerSelectBpmFrom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSelectYearLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jSpinnerSelectBpmTo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSpinnerSelectBpmFrom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelSelectStatus.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jCheckBoxSelectCheckedFlag0.setText("?"); // NOI18N
        jCheckBoxSelectCheckedFlag0.setOpaque(true);

        jCheckBoxSelectCheckedFlag3.setSelected(true);
        jCheckBoxSelectCheckedFlag3.setText(Inter.get("Check.OK")); // NOI18N
        jCheckBoxSelectCheckedFlag3.setOpaque(true);

        jCheckBoxSelectCheckedFlag2.setSelected(true);
        jCheckBoxSelectCheckedFlag2.setText(Inter.get("Check.OK")); // NOI18N
        jCheckBoxSelectCheckedFlag2.setOpaque(true);

        jCheckBoxSelectCheckedFlag1.setText(Inter.get("Check.KO")); // NOI18N
        jCheckBoxSelectCheckedFlag1.setOpaque(true);

        javax.swing.GroupLayout jPanelSelectStatusLayout = new javax.swing.GroupLayout(jPanelSelectStatus);
        jPanelSelectStatus.setLayout(jPanelSelectStatusLayout);
        jPanelSelectStatusLayout.setHorizontalGroup(
            jPanelSelectStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectStatusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelSelectStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSelectStatusLayout.createSequentialGroup()
                        .addComponent(jCheckBoxSelectCheckedFlag0, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBoxSelectCheckedFlag3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSelectStatusLayout.createSequentialGroup()
                        .addComponent(jCheckBoxSelectCheckedFlag1, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBoxSelectCheckedFlag2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelSelectStatusLayout.setVerticalGroup(
            jPanelSelectStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectStatusLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelSelectStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxSelectCheckedFlag0)
                    .addComponent(jCheckBoxSelectCheckedFlag3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelSelectStatusLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxSelectCheckedFlag2)
                    .addComponent(jCheckBoxSelectCheckedFlag1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelSelectRating.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jCheckBoxSelectRating0.setSelected(true);
        jCheckBoxSelectRating0.setText("?"); // NOI18N

        jCheckBoxSelectRating1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/1starU.png"))); // NOI18N
        jCheckBoxSelectRating1.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/1starC.png"))); // NOI18N

        jCheckBoxSelectRating2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/2starU.png"))); // NOI18N
        jCheckBoxSelectRating2.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/2starC.png"))); // NOI18N

        jCheckBoxSelectRating3.setSelected(true);
        jCheckBoxSelectRating3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/3starU.png"))); // NOI18N
        jCheckBoxSelectRating3.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/3starC.png"))); // NOI18N

        jCheckBoxSelectRating4.setSelected(true);
        jCheckBoxSelectRating4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/4starU.png"))); // NOI18N
        jCheckBoxSelectRating4.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/4starC.png"))); // NOI18N

        jCheckBoxSelectRating5.setSelected(true);
        jCheckBoxSelectRating5.setToolTipText("");
        jCheckBoxSelectRating5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/5starU.png"))); // NOI18N
        jCheckBoxSelectRating5.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/5star.png"))); // NOI18N

        javax.swing.GroupLayout jPanelSelectRatingLayout = new javax.swing.GroupLayout(jPanelSelectRating);
        jPanelSelectRating.setLayout(jPanelSelectRatingLayout);
        jPanelSelectRatingLayout.setHorizontalGroup(
            jPanelSelectRatingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectRatingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSelectRatingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxSelectRating2)
                    .addComponent(jCheckBoxSelectRating0)
                    .addComponent(jCheckBoxSelectRating1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanelSelectRatingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxSelectRating3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBoxSelectRating4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jCheckBoxSelectRating5, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanelSelectRatingLayout.setVerticalGroup(
            jPanelSelectRatingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectRatingLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSelectRatingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSelectRatingLayout.createSequentialGroup()
                        .addComponent(jCheckBoxSelectRating0)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxSelectRating1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxSelectRating2))
                    .addGroup(jPanelSelectRatingLayout.createSequentialGroup()
                        .addComponent(jCheckBoxSelectRating5)
                        .addGap(10, 10, 10)
                        .addComponent(jCheckBoxSelectRating4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxSelectRating3)))
                .addContainerGap())
        );

        jCheckBoxSelectUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/update.png"))); // NOI18N
        jCheckBoxSelectUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxSelectUpdateActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jComboBoxBestOfCopyRight.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jComboBoxBestOfCopyRight, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jComboBoxBestOfCopyRight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        javax.swing.GroupLayout jPanelSelectFiltersLayout = new javax.swing.GroupLayout(jPanelSelectFilters);
        jPanelSelectFilters.setLayout(jPanelSelectFiltersLayout);
        jPanelSelectFiltersLayout.setHorizontalGroup(
            jPanelSelectFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectFiltersLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanelSelectFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSelectFiltersLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxSelectUpdate))
                    .addGroup(jPanelSelectFiltersLayout.createSequentialGroup()
                        .addComponent(jPanelSelectRating, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelSelectStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelSelectYear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanelSelectFiltersLayout.setVerticalGroup(
            jPanelSelectFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectFiltersLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanelSelectFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelSelectRating, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelSelectStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanelSelectYear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSelectFiltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jCheckBoxSelectUpdate))
                .addGap(0, 0, 0))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelPreviewDisplay.setBackground(new java.awt.Color(236, 231, 231));

        jComboBoxSoundCard.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButtonPreviewStop.setText(Inter.get("Button.Stop")); // NOI18N
        jButtonPreviewStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPreviewStopActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jButtonPreviewStop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxSoundCard, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addComponent(jLabelPreviewDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabelPreviewDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPreviewStop)
                    .addComponent(jComboBoxSoundCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout jPanelSelectSelectorLayout = new javax.swing.GroupLayout(jPanelSelectSelector);
        jPanelSelectSelector.setLayout(jPanelSelectSelectorLayout);
        jPanelSelectSelectorLayout.setHorizontalGroup(
            jPanelSelectSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectSelectorLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneSelectGenre, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPaneSelectArtistAlbum)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelSelectSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanelSelectFilters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanelSelectSelectorLayout.setVerticalGroup(
            jPanelSelectSelectorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPaneSelectGenre)
            .addComponent(jSplitPaneSelectArtistAlbum, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSelectSelectorLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanelSelectFilters, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jSplitPaneSelect.setLeftComponent(jPanelSelectSelector);

        jTableSelect.setAutoCreateColumnsFromModel(false);
        jTableSelect.setModel(new jamuz.gui.swing.TableModel());
        jTableSelect.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableSelect.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableSelect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jTableSelectMousePressed(evt);
            }
        });
        jScrollPaneSelect.setViewportView(jTableSelect);

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jToggleButtonSelectShowExtra.setText(bundle.getString("Label.Extra")); // NOI18N
        jToggleButtonSelectShowExtra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSelectShowExtraActionPerformed(evt);
            }
        });

        jToggleButtonSelectShowFile.setText(bundle.getString("Label.File")); // NOI18N
        jToggleButtonSelectShowFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSelectShowFileActionPerformed(evt);
            }
        });

        jToggleButtonSelectShowBasic.setSelected(true);
        jToggleButtonSelectShowBasic.setText(bundle.getString("Label.Basic")); // NOI18N
        jToggleButtonSelectShowBasic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSelectShowBasicActionPerformed(evt);
            }
        });

        jLabelSelectDisplay.setText(bundle.getString("Label.Display")); // NOI18N

        jToggleButtonSelectShowStats.setText(bundle.getString("Label.Statistics")); // NOI18N
        jToggleButtonSelectShowStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSelectShowStatsActionPerformed(evt);
            }
        });

        jLabelSelected.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        jLabelSelected.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelSelected.setText("Selected In Lists"); // NOI18N

        jLabelSelectedSummary.setText(" ");

        jToggleButtonSelectShowRights.setText("Rights");
        jToggleButtonSelectShowRights.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButtonSelectShowRightsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSelectTracksLayout = new javax.swing.GroupLayout(jPanelSelectTracks);
        jPanelSelectTracks.setLayout(jPanelSelectTracksLayout);
        jPanelSelectTracksLayout.setHorizontalGroup(
            jPanelSelectTracksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectTracksLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSelectTracksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSelectTracksLayout.createSequentialGroup()
                        .addComponent(jLabelSelectDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButtonSelectShowBasic)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButtonSelectShowStats)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButtonSelectShowFile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButtonSelectShowExtra)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButtonSelectShowRights)
                        .addGap(33, 33, 33)
                        .addComponent(jLabelSelectedSummary, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabelSelected, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addComponent(jScrollPaneSelect, javax.swing.GroupLayout.DEFAULT_SIZE, 1053, Short.MAX_VALUE)
        );
        jPanelSelectTracksLayout.setVerticalGroup(
            jPanelSelectTracksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSelectTracksLayout.createSequentialGroup()
                .addComponent(jLabelSelected)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneSelect, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addGroup(jPanelSelectTracksLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jToggleButtonSelectShowBasic)
                    .addComponent(jLabelSelectDisplay)
                    .addComponent(jToggleButtonSelectShowStats)
                    .addComponent(jToggleButtonSelectShowFile)
                    .addComponent(jToggleButtonSelectShowExtra)
                    .addComponent(jLabelSelectedSummary)
                    .addComponent(jToggleButtonSelectShowRights))
                .addContainerGap())
        );

        jSplitPaneSelect.setBottomComponent(jPanelSelectTracks);

        javax.swing.GroupLayout jPanelSelectLayout = new javax.swing.GroupLayout(jPanelSelect);
        jPanelSelect.setLayout(jPanelSelectLayout);
        jPanelSelectLayout.setHorizontalGroup(
            jPanelSelectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneSelect, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanelSelectLayout.setVerticalGroup(
            jPanelSelectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPaneSelect)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1053, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanelSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 580, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanelSelect, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void menuCheck() {
        int selectedRow = jTableSelect.getSelectedRow(); 		
		if(selectedRow>=0) { 	
			//convert to model index (as sortable model) 		
			selectedRow = jTableSelect.convertRowIndexToModel(selectedRow); 
			FileInfoInt myFileInfo = fileInfoList.get(selectedRow);

            PanelCheck.check(myFileInfo.getIdPath());			 		
		}
    }
    private static final Mplayer mplayer= new Mplayer();
	private void menuPreview() {
		//Getting selected File 		
		int selectedRow = jTableSelect.getSelectedRow(); 			
		if(selectedRow>=0) { 	
			//convert to model index (as sortable model) 		
			selectedRow = jTableSelect.convertRowIndexToModel(selectedRow); 
			FileInfoInt fileInfoInt = fileInfoList.get(selectedRow); 	
			mplayer.setAudioCard((AudioCard)jComboBoxSoundCard.getSelectedItem());
			jLabelPreviewDisplay.setText(fileInfoInt.getTrackNo()+" "+fileInfoInt.getTitle());
			mplayer.play(fileInfoInt.getFullPath().getAbsolutePath(), false);
		}
	}
	
	/**
	 *
	 */
	public static void stopMplayer() {
		mplayer.stop();
		jLabelPreviewDisplay.setText("");
	}
	
    private void menuQueue() {
        //Getting selected File 		
		int selectedRow = jTableSelect.getSelectedRow(); 			
		if(selectedRow>=0) { 	
			//convert to model index (as sortable model) 		
			selectedRow = jTableSelect.convertRowIndexToModel(selectedRow); 
			FileInfoInt myFileInfo = fileInfoList.get(selectedRow); 	
            
            //TODO: Bug when adding to queue. JList is not refreshed
            //Something's wrong b/w model and JList
			PanelMain.addToQueue(myFileInfo, Jamuz.getDb().getRootPath()); 		
		}
    }
    
    private void menuQueueAll() {
        //Getting selected File 		
		int selectedRow = jTableSelect.getSelectedRow(); 			
		if(selectedRow>=0) {	
            PanelMain.getQueueModel().clear();
            for(FileInfoInt myFileInfo : fileInfoList) {
                PanelMain.addToQueue(myFileInfo, Jamuz.getDb().getRootPath()); 	
            }
		}
    }
    
    private void menuEdit() {
        //Getting selected File 		
		int selectedRow = jTableSelect.getSelectedRow(); 		
		if(selectedRow>=0) { 	
			//convert to model index (as sortable model) 		
			selectedRow = jTableSelect.convertRowIndexToModel(selectedRow); 
			FileInfoInt myFileInfo = fileInfoList.get(selectedRow);
			PanelMain.editLocation(Jamuz.getDb().getRootPath()+myFileInfo.getRelativeFullPath());	  			 		
		}
    }
    
	/**
	 *
	 * @param field
	 * @param value
	 */
	public static void setSelected(String field, String value) {
        //Select "Select" jPane
        PanelMain.selectTab(Inter.get("PanelMain.panelSelect.TabConstraints.tabTitle")); //NOI18N
        
        enableSelect(false);
        
        //Need to remove all filters to be sure to get requested field value
//        setYearSpinners();
        jCheckBoxSelectRating0.setSelected(true);
        jCheckBoxSelectRating1.setSelected(true);
        jCheckBoxSelectRating2.setSelected(true);
        jCheckBoxSelectRating3.setSelected(true);
        jCheckBoxSelectRating4.setSelected(true);
        jCheckBoxSelectRating5.setSelected(true);
        jCheckBoxSelectCheckedFlag0.setSelected(true);
        jCheckBoxSelectCheckedFlag1.setSelected(true);
        jCheckBoxSelectCheckedFlag2.setSelected(true);
        jCheckBoxSelectCheckedFlag3.setSelected(true);
        
        selGenre="%"; 
		selArtist = new ListElement("%", "artist"); //NOI18N
		selAlbum = new ListElement("%", "album"); //NOI18N
        switch (field) {
            case "genre":
                //NOI18N
                selGenre=value;
                break;
            case "artist":
                //NOI18N
                selArtist=new ListElement(value, "artist");
                break;
            case "album":
                //NOI18N
                selAlbum=new ListElement(value, "album");
                break;
        }
        
        fillListsInThread("all", false);
    }

    private static String albumSqlOrder= "albumRating DESC, year DESC, album";
    private static String artistSqlOrder= "albumRating DESC, albumArtist";
    
    //TODO: Do not re-query Db each time, use cached "RAMed" dB (and use through the whole project)
    //Warning: potential threading issues
    
    /**
	 * Fill genre, artist and album lists
	 * @param field
	 */
	private static void fillLists(String field, boolean getSelection) throws InterruptedException {
		enableSelect(false);
        if(getSelection) {
            try {
                selGenre=jListSelectGenre.getSelectedValue() == null ? "%" : (String) (jListSelectGenre.getSelectedValue());  //NOI18N
                selArtist=jListSelectArtist.getSelectedValue() == null ? new ListElement("%", "artist") : ((ListElement) jListSelectArtist.getSelectedValue());  //NOI18N
                selAlbum=jListSelectAlbum.getSelectedValue() == null ? new ListElement("%", "album") : ((ListElement) jListSelectAlbum.getSelectedValue());  //NOI18N
            }
            catch(Exception ex) {
                Popup.error(ex);
            }
        }

		if(!field.equals("album")){  //NOI18N
			if(field.equals("all")) {  //NOI18N
                tFilllists.checkAbort();
                selGenre=(String) fillSelectorlistGenre(jListSelectGenre, selGenre, "genre", "%", "%", "%");  //NOI18N
			}
			//For "all" (startup) and "genre" changes only
			if(!field.equals("artist")) {  //NOI18N
                tFilllists.checkAbort();
                selArtist=(ListElement) fillSelectorlist(jListSelectArtist, selArtist, "artist", selGenre, "%", "%", artistSqlOrder);  //NOI18N
			}
            tFilllists.checkAbort();
			//Refresh album list
            selAlbum = (ListElement) fillSelectorlist(jListSelectAlbum, selAlbum, "album", selGenre, selArtist.getValue(), "%",  albumSqlOrder);  //NOI18N
            jListSelectAlbum.setSelectedIndex(0);
            selAlbum=jListSelectAlbum.getSelectedValue() == null ? new ListElement("%", "album") : ((ListElement) jListSelectAlbum.getSelectedValue());  //NOI18N
		}
		
        String labelSelected="";
        if(!selAlbum.getValue().equals("%")) {
            labelSelected=selAlbum.toString();
        }
        else if(!selArtist.getValue().equals("%")) {
            labelSelected=selArtist.toString();
        }
        else if(!selGenre.equals("%")) {
            labelSelected=selGenre;
        }
        jLabelSelected.setText(labelSelected);
        
        //Fill jTable
        tFilllists.checkAbort();
        fillSelectTable();
		enableSelect(true);
	}

    private static class FillListsThread extends ProcessAbstract {
		private final String field;
        private final boolean getSelection;

        public FillListsThread(String field, boolean getSelection, String name) {
            super(name);
            this.field = field;
            this.getSelection = getSelection;
        }
        
		@Override
		public void run() {
            this.resetAbort();
            
            try {
                fillLists(field, getSelection);
            } catch (InterruptedException ex) {
//                Popup.info(Inter.get("Msg.Process.Aborted"));  //NOI18N
            }
		}
	}
    
    private static class FillMainListThread extends ProcessAbstract {

        public FillMainListThread(String name) {
            super(name);
        }
		
        @Override
		public void run() {
			this.resetAbort();
            
            try {

                jTableSelect.setRowSorter(null);
                //Refresh files list
                tableModel.clear();
                fileInfoList.clear();

                boolean[] selRatings = new boolean[6]; 
                selRatings[0]=jCheckBoxSelectRating0.isSelected();
                selRatings[1]=jCheckBoxSelectRating1.isSelected();
                selRatings[2]=jCheckBoxSelectRating2.isSelected();
                selRatings[3]=jCheckBoxSelectRating3.isSelected();
                selRatings[4]=jCheckBoxSelectRating4.isSelected();
                selRatings[5]=jCheckBoxSelectRating5.isSelected();

                boolean[] selCheckedFlag = new boolean[4]; 
                selCheckedFlag[0]=jCheckBoxSelectCheckedFlag0.isSelected();
                selCheckedFlag[1]=jCheckBoxSelectCheckedFlag1.isSelected();
                selCheckedFlag[2]=jCheckBoxSelectCheckedFlag2.isSelected();
                selCheckedFlag[3]=jCheckBoxSelectCheckedFlag3.isSelected();

                int yearFrom=((Double) jSpinnerSelectYearFrom.getValue()).intValue();
                int yearTo=((Double) jSpinnerSelectYearTo.getValue()).intValue();
                
                float bpmFrom = ((Integer) jSpinnerSelectBpmFrom.getValue()).floatValue();
                float bpmTo = ((Integer) jSpinnerSelectBpmTo.getValue()).floatValue();
                
                int copyRight = jComboBoxBestOfCopyRight.getSelectedIndex() -1;
                
                jLabelSelectedSummary.setText(Jamuz.getDb().getFilesStats(selGenre, selArtist.getValue(), selAlbum.getValue(), selRatings, selCheckedFlag, 
                        yearFrom, yearTo, bpmFrom, bpmTo, copyRight));
                
                if(selAlbum.getValue().equals("%") && selArtist.getValue().equals("%") && selGenre.equals("%")) {
                    //Not really usefull, take a lot of ressources for too little so exiting
                    throw new InterruptedException();
                }
                
                Jamuz.getDb().getFiles(fileInfoList, selGenre, selArtist.getValue(), selAlbum.getValue(), selRatings, selCheckedFlag, 
                        yearFrom, yearTo, bpmFrom, bpmTo, copyRight);

                for (FileInfoInt myFileInfoInt : fileInfoList) {
                    this.checkAbort();
                    PanelMain.addRowSelect(tableModel, myFileInfoInt);
                }
                //Displaying or not genre, artist and album, if selected or not
                if(jToggleButtonSelectShowBasic.isSelected()) {
					PanelMain.setColumnVisible(TABLE_COLUMN_MODEL, 4, selGenre.equals("%"));  //NOI18N
					PanelMain.setColumnVisible(TABLE_COLUMN_MODEL, 1, selAlbum.getValue().equals("%"));  //NOI18N
					PanelMain.setColumnVisible(TABLE_COLUMN_MODEL, 0, checkList(0));  //NOI18N
					PanelMain.setColumnVisible(TABLE_COLUMN_MODEL, 12, checkList(12));  //NOI18N
				}
				
                //Enable row tableSorter (cannot be done if model is empty)
                if(tableModel.getRowCount()>0) {
                    //Enable auto sorter
                    jTableSelect.setAutoCreateRowSorter(true);
                    //Sort by action, result
                    TableRowSorter<TableModel> tableSorter = new TableRowSorter<>(tableModel);
                    jTableSelect.setRowSorter(tableSorter);
                    List <RowSorter.SortKey> sortKeys = new ArrayList<>();

                    //{"Artist", "Album", "Track #", "Title", "Genre", "Year", "BitRate", "File",
                    //"Length"BPMDisplayFormat", "Size", "BPM", "Album Artist", "Comment", "Disc #"
                    //"nbCovers", "Play counter", "Rating", "Added", "Last Played"};
//                    sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                    sortKeys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                    sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
                    tableSorter.setSortKeys(sortKeys);
                }
                else {
                    jTableSelect.setAutoCreateRowSorter(false);
                }
            } catch (InterruptedException ex) {
                //Nothing to do here. Process has simply been aborted
            }
		}
	}
	
    private static boolean checkList(int columnIndex) {
        if(selArtist.getValue().equals("%") && selAlbum.getValue().equals("%")) { //NOI18N
            return true;
        }
        List list = tableModel.getList(columnIndex);
        list = FolderInfo.group(list, "toString");  //NOI18N
        if(list.size()!=1) {
            return true;
        }
        else {
            String listArtist = (String) list.get(0);
            String artist = selArtist.getValue();
            if(!artist.equals("%") && !listArtist.equals(artist)) { //NOI18N
                //TODO: check that not equal to albumArtist too
                return true;
            }
            if(!selAlbum.getValue().equals("%")) { //NOI18N
                String albumArtist = selAlbum.getFile().getAlbumArtist();
                if(!listArtist.equals(albumArtist)) {
                    //TODO: check that not equal to artist too
                    return true;
                }
            }
        }
        return false;
    }

    private static void fillSelectorlist(DefaultListModel myListModel, String field, String selGenre, String selArtist, String selAlbum) {
        fillSelectorlist(myListModel, field, selGenre, selArtist, selAlbum, "");
    }
    
    
	private static void fillSelectorlist(DefaultListModel myListModel, String field, 
            String selGenre, String selArtist, String selAlbum, String sqlOrder) {
        boolean[] selRatings = new boolean[6]; 
        selRatings[0]=jCheckBoxSelectRating0.isSelected();
        selRatings[1]=jCheckBoxSelectRating1.isSelected();
        selRatings[2]=jCheckBoxSelectRating2.isSelected();
        selRatings[3]=jCheckBoxSelectRating3.isSelected();
        selRatings[4]=jCheckBoxSelectRating4.isSelected();
        selRatings[5]=jCheckBoxSelectRating5.isSelected();

        boolean[] selCheckedFlag = new boolean[4]; 
        selCheckedFlag[0]=jCheckBoxSelectCheckedFlag0.isSelected();
        selCheckedFlag[1]=jCheckBoxSelectCheckedFlag1.isSelected();
        selCheckedFlag[2]=jCheckBoxSelectCheckedFlag2.isSelected();
        selCheckedFlag[3]=jCheckBoxSelectCheckedFlag3.isSelected();
        
        int yearFrom=((Double) jSpinnerSelectYearFrom.getValue()).intValue();
        int yearTo=((Double) jSpinnerSelectYearTo.getValue()).intValue();
        
//        int yearFrom=(Integer) jSpinnerSelectYearFrom.getValue();
//        int yearTo=(Integer) jSpinnerSelectYearTo.getValue();
        
        float bpmFrom = ((Integer) jSpinnerSelectBpmFrom.getValue()).floatValue();
        float bpmTo = ((Integer) jSpinnerSelectBpmTo.getValue()).floatValue();
        
        int copyRight = jComboBoxBestOfCopyRight.getSelectedIndex() -1;
        
		Jamuz.getDb().fillSelectorList(myListModel, field, 
                selGenre, selArtist, selAlbum, selRatings, selCheckedFlag, 
                yearFrom, yearTo, bpmFrom, bpmTo, 
                copyRight, sqlOrder);
	}
    
    private static Object fillSelectorlistGenre(JList list, Object selection, String field, String selGenre, String selArtist, String selAlbum) {
        DefaultListModel model = new DefaultListModel();
        fillSelectorlist(model, field, selGenre, selArtist, selAlbum);  //NOI18N
        list.setModel(model);
        if(model.contains(selection)) {
            list.setSelectedValue(selection, true);
        }
        else {
            selection="%"; //NOI18N
        }
        return selection;
    }
    
    private static Object fillSelectorlist(JList jList, Object selection, String field, 
        String selGenre, String selArtist, String selAlbum, String sqlOrder) {
        ListModelSelector model = new ListModelSelector();
        fillSelectorlist(model, field, selGenre, selArtist, selAlbum, sqlOrder);  //NOI18N
        model.loadIconsInThread();
        jList.setModel(model);
        
        if(model.contains(selection)) {
            jList.setSelectedValue(selection, true);
        }
        else {
            selection=new ListElement("%", field); //NOI18N
        }
        return selection;
    }
    
    /**
     * Fills select ta's table
     */
    public static void fillSelectTable() {
		//Stop any previously running thread and wait for it to end
		if(tFillMainTable!=null) {
			tFillMainTable.abort();
			try {
				tFillMainTable.join();
			} catch (InterruptedException ex) {
				Popup.error(ex);
			}
		}

		// Démarrage du thread
		tFillMainTable = new FillMainListThread("Thread.PanelSelect.fillSelectTable");
		tFillMainTable.start();
	}
        
    private static boolean isSelectAdjusting=false;
    
    private static void enableSelect(boolean enable) {

        Swing.enableComponents(jPanelSelectRating, enable);
        Swing.enableComponents(jPanelSelectStatus, enable);
//        jCheckBoxSelectUp.setEnabled(enable);
		jListSelectGenre.setEnabled(enable);
		jListSelectArtist.setEnabled(enable);
		jListSelectAlbum.setEnabled(enable);
		jTableSelect.setEnabled(enable);
		
		isSelectAdjusting=!enable;
	}
    
    private void jListSelectAlbumValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSelectAlbumValueChanged
        if(!isSelectAdjusting && !evt.getValueIsAdjusting()) {
            fillListsInThread("album"); 	  //NOI18N
        }
    }//GEN-LAST:event_jListSelectAlbumValueChanged

    private static void fillListsInThread(String field) {
        fillListsInThread(field, true);
    }
    
    private static void fillListsInThread(String field, boolean getSelection) {
		//Stop any previously running thread and wait for it to end
		if(tFilllists!=null) {
			tFilllists.abort();
			try {
				tFilllists.join();
			} catch (InterruptedException ex) {
				Popup.error(ex);
			}
		}

		// Démarrage du thread
		tFilllists = new FillListsThread(field, getSelection, "Thread.PanelSelect.fillListsInThread");
		tFilllists.start();
        
	}
    
    private void jListSelectArtistValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSelectArtistValueChanged
        if(!isSelectAdjusting && !evt.getValueIsAdjusting()) {
            fillListsInThread("artist"); 	  //NOI18N
        }
    }//GEN-LAST:event_jListSelectArtistValueChanged

    private void jCheckBoxSelectUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxSelectUpdateActionPerformed
        refreshTable();
    }//GEN-LAST:event_jCheckBoxSelectUpdateActionPerformed

    private void jListSelectGenreValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListSelectGenreValueChanged
        if(!isSelectAdjusting && !evt.getValueIsAdjusting()) {
            fillListsInThread("genre"); 	  //NOI18N
        }
    }//GEN-LAST:event_jListSelectGenreValueChanged

    private void jTableSelectMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableSelectMousePressed

        // If Right mouse click, select the line under mouse
        if ( SwingUtilities.isRightMouseButton( evt ) )
        {
            Point p = evt.getPoint();
            int rowNumber = jTableSelect.rowAtPoint( p );
            ListSelectionModel model = jTableSelect.getSelectionModel();
            model.setSelectionInterval( rowNumber, rowNumber );
        }
        //In all cases, display selected file
        //TODO: Use a better listner (onChange) to handle selections using keyboard !
        //Example: http://www.developpez.net/forums/d1141644/java/interfaces-graphiques-java/awt-swing/jtable-lancer-traitement-moment-selection-ligne/
        //Getting selected File
//        int selectedRow = jTableSelect.getSelectedRow();
//        //convert to model index (as sortable model)
//        selectedRow = jTableSelect.convertRowIndexToModel(selectedRow);
//        if(selectedRow>=0) {
//            FileInfoInt myFileInfo = fileInfoList.get(selectedRow);
//            PanelMain.displayFileInfo(myFileInfo, false);
//        }
//        else {
//            Popup.info(Inter.get("Error.YouMustSelectArow")); 		  //NOI18N
//        }

    }//GEN-LAST:event_jTableSelectMousePressed

    private void jToggleButtonSelectShowBasicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSelectShowBasicActionPerformed
        PanelMain.setBasicVisible(TABLE_COLUMN_MODEL, jToggleButtonSelectShowBasic.isSelected());
    }//GEN-LAST:event_jToggleButtonSelectShowBasicActionPerformed

    private void jToggleButtonSelectShowExtraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSelectShowExtraActionPerformed
        PanelMain.setExtraVisible(TABLE_COLUMN_MODEL, jToggleButtonSelectShowExtra.isSelected());
        if(jToggleButtonSelectShowExtra.isSelected()) {
            jTableSelect.setRowHeight(IconBufferCover.getCoverIconSize());
        }
        else {
            jTableSelect.setRowHeight(16);
        }
    }//GEN-LAST:event_jToggleButtonSelectShowExtraActionPerformed

    private void jToggleButtonSelectShowFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSelectShowFileActionPerformed
        PanelMain.setFileVisible(TABLE_COLUMN_MODEL, jToggleButtonSelectShowFile.isSelected());
    }//GEN-LAST:event_jToggleButtonSelectShowFileActionPerformed

    private void jToggleButtonSelectShowStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSelectShowStatsActionPerformed
        PanelMain.setStatsVisible(TABLE_COLUMN_MODEL, jToggleButtonSelectShowStats.isSelected());
    }//GEN-LAST:event_jToggleButtonSelectShowStatsActionPerformed

    private void jRadioSelectAlbumNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioSelectAlbumNameItemStateChanged
        JRadioButton source = (JRadioButton) evt.getSource();
        if(source.isSelected()) {
            albumSqlOrder = "album, year DESC, albumRating DESC";
            refreshTable();
        }
    }//GEN-LAST:event_jRadioSelectAlbumNameItemStateChanged

    private void jRadioSelectAlbumDateItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioSelectAlbumDateItemStateChanged
        JRadioButton source = (JRadioButton) evt.getSource();
        if(source.isSelected()) {
            albumSqlOrder = "year DESC, album, albumRating DESC";
            refreshTable();
        }
    }//GEN-LAST:event_jRadioSelectAlbumDateItemStateChanged

    private void jRadioSelectAlbumRatingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioSelectAlbumRatingItemStateChanged
        JRadioButton source = (JRadioButton) evt.getSource();
        if(source.isSelected()) {
            albumSqlOrder = "albumRating DESC, year DESC, album";
            refreshTable();
        }
    }//GEN-LAST:event_jRadioSelectAlbumRatingItemStateChanged

    private void jRadioSelectArtistNameItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioSelectArtistNameItemStateChanged
        JRadioButton source = (JRadioButton) evt.getSource();
        if(source.isSelected()) {
            artistSqlOrder = "albumArtist, albumRating DESC";
            refreshTable();
        }
    }//GEN-LAST:event_jRadioSelectArtistNameItemStateChanged

    private void jRadioSelectArtistRatingItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jRadioSelectArtistRatingItemStateChanged
        JRadioButton source = (JRadioButton) evt.getSource();
        if(source.isSelected()) {
            artistSqlOrder = "albumRating DESC, albumArtist";
            refreshTable();
        }
    }//GEN-LAST:event_jRadioSelectArtistRatingItemStateChanged

    private void jButtonPreviewStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPreviewStopActionPerformed
        stopMplayer();
    }//GEN-LAST:event_jButtonPreviewStopActionPerformed

    private void jToggleButtonSelectShowRightsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButtonSelectShowRightsActionPerformed
		PanelMain.setRightsVisible(TABLE_COLUMN_MODEL, jToggleButtonSelectShowRights.isSelected());
    }//GEN-LAST:event_jToggleButtonSelectShowRightsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButtonPreviewStop;
    private static javax.swing.JCheckBox jCheckBoxSelectCheckedFlag0;
    private static javax.swing.JCheckBox jCheckBoxSelectCheckedFlag1;
    private static javax.swing.JCheckBox jCheckBoxSelectCheckedFlag2;
    private static javax.swing.JCheckBox jCheckBoxSelectCheckedFlag3;
    private static javax.swing.JCheckBox jCheckBoxSelectRating0;
    private static javax.swing.JCheckBox jCheckBoxSelectRating1;
    private static javax.swing.JCheckBox jCheckBoxSelectRating2;
    private static javax.swing.JCheckBox jCheckBoxSelectRating3;
    private static javax.swing.JCheckBox jCheckBoxSelectRating4;
    private static javax.swing.JCheckBox jCheckBoxSelectRating5;
    private static javax.swing.JButton jCheckBoxSelectUpdate;
    private static javax.swing.JComboBox jComboBoxBestOfCopyRight;
    private javax.swing.JComboBox<String> jComboBoxSoundCard;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private static javax.swing.JLabel jLabelPreviewDisplay;
    private javax.swing.JLabel jLabelSelectDisplay;
    private static javax.swing.JLabel jLabelSelected;
    private static javax.swing.JLabel jLabelSelectedSummary;
    private static javax.swing.JList jListSelectAlbum;
    private static javax.swing.JList jListSelectArtist;
    protected static javax.swing.JList jListSelectGenre;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanelSelect;
    private javax.swing.JPanel jPanelSelectAlbum;
    private javax.swing.JPanel jPanelSelectArtist;
    private javax.swing.JPanel jPanelSelectFilters;
    private static javax.swing.JPanel jPanelSelectRating;
    private javax.swing.JPanel jPanelSelectSelector;
    private static javax.swing.JPanel jPanelSelectStatus;
    private javax.swing.JPanel jPanelSelectTracks;
    private javax.swing.JPanel jPanelSelectYear;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JRadioButton jRadioSelectAlbumDate;
    private javax.swing.JRadioButton jRadioSelectAlbumName;
    private javax.swing.JRadioButton jRadioSelectAlbumRating;
    private javax.swing.JRadioButton jRadioSelectArtistName;
    private javax.swing.JRadioButton jRadioSelectArtistRating;
    private javax.swing.JScrollPane jScrollPaneSelect;
    private javax.swing.JScrollPane jScrollPaneSelectAlbum;
    private javax.swing.JScrollPane jScrollPaneSelectArtist;
    private javax.swing.JScrollPane jScrollPaneSelectGenre;
    private static javax.swing.JSpinner jSpinnerSelectBpmFrom;
    private static javax.swing.JSpinner jSpinnerSelectBpmFrom1;
    private static javax.swing.JSpinner jSpinnerSelectBpmTo;
    private static javax.swing.JSpinner jSpinnerSelectBpmTo1;
    private static javax.swing.JSpinner jSpinnerSelectYearFrom;
    private static javax.swing.JSpinner jSpinnerSelectYearTo;
    private javax.swing.JSplitPane jSplitPaneSelect;
    private javax.swing.JSplitPane jSplitPaneSelectArtistAlbum;
    private static javax.swing.JTable jTableSelect;
    private static javax.swing.JToggleButton jToggleButtonSelectShowBasic;
    private javax.swing.JToggleButton jToggleButtonSelectShowExtra;
    private javax.swing.JToggleButton jToggleButtonSelectShowFile;
    private javax.swing.JToggleButton jToggleButtonSelectShowRights;
    private javax.swing.JToggleButton jToggleButtonSelectShowStats;
    // End of variables declaration//GEN-END:variables
}
