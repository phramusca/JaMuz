/*
 * Copyright (C) 2017 phramusca ( https://github.com/phramusca/JaMuz/ )
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
import jamuz.Jamuz;
import jamuz.gui.swing.ListElement;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.check.FolderInfo;
import jamuz.utils.FileSystem;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.ProcessAbstract;
import jamuz.utils.StringManager;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelOptions extends javax.swing.JPanel {

	protected static ProgressBar progressBarCheckedFlag;
	
	/**
	 * Creates new form PanelOptions
	 */
	public PanelOptions() {
		initComponents();
	}

	/**
     * extended init
     */
    public void initExtended() {
		fillMachineList();
		progressBarCheckedFlag = (ProgressBar)jProgressBarResetChecked;
		jListGenres.setModel(Jamuz.getGenreListModel());
		long size = Long.valueOf(Jamuz.getOptions().get("log.cleanup.keep.size.bytes", "2000000000"));
		jSpinnerBytes.getModel().setValue(size);
		jLabelBytes.setText("("+Inter.get("Label.Keep")+" "+StringManager.humanReadableByteCount(size, false)+")");
		jListTags.setModel(Jamuz.getTagsModel());
	}
	
	public static void fillMachineList() {
        fillMachineList((DefaultListModel) jListMachines.getModel());  //NOI18N
        jListMachines.setSelectedValue(new ListElement(Jamuz.getMachine().getName(), ""), true);
    }
	
	private static void fillMachineList(DefaultListModel listModel) {
        listModel.clear();
        Jamuz.getDb().getMachineListModel(listModel);
    }

	private void resetCheckedFlag(FolderInfo.CheckedFlag checkedFlag) {
        
        //TODO: Update message to display checkedFlag
        int n = JOptionPane.showConfirmDialog(null, 
                Inter.get("Question.Scan.ResetCheckFlag"), //NOI18N
                Inter.get("Question.Scan.ResetCheckFlag.Title"), //NOI18N
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (n == JOptionPane.YES_OPTION) {
            enableResetCheckedFlagButtons(false);
            progressBarCheckedFlag.setIndeterminate(Inter.get("Msg.Check.Scan.ResetCheckedFlag")); //NOI18N
            Jamuz.getDb().setCheckedFlagReset(checkedFlag);
            progressBarCheckedFlag.reset();
            enableResetCheckedFlagButtons(true);
        }
    }
    
    private void enableResetCheckedFlagButtons(boolean enable) {
        jButtonResetCheckedFlagKO.setEnabled(enable);
        jButtonResetCheckedFlagOK.setEnabled(enable);
        jButtonResetCheckedFlagWarning.setEnabled(enable);
    }

	public class SaveTags extends ProcessAbstract {

		public SaveTags() {
            super("Thread.PanelOptions.SaveTags");
        }
                
        @Override
        public void run() {
            try {
                ArrayList<FileInfoInt> filesToSave = new ArrayList<>();
				String sql = "SELECT F.*, P.strPath, P.checked, P.copyRight, 0 AS albumRating, 0 AS percentRated "
						+ " FROM file F JOIN path P ON F.idPath=P.idPath WHERE F.deleted=0 AND P.deleted=0 AND saved=0";
				ProgressBar progressBar = (ProgressBar)jProgressBarSaveTags;
				
				progressBar.setIndeterminate("Retrieving list");
				Jamuz.getDb().getFiles(filesToSave, sql);
				
				progressBar.setup(filesToSave.size());
				for (Iterator<FileInfoInt> iterator = filesToSave.iterator(); iterator.hasNext();) {
					FileInfoInt file = iterator.next();
					//TODO: Maybe offer not to read tags. 
					//This would be speed up the process but needs a scan library first
					//Can also be useful (why is that?) so we can modify tags in dB and save that to tags.
					file.readTags(true);
//					file.getCoverImage(); //NOTE: This is needed if above not done
					file.getLyrics();
					file.saveTags(true);
					progressBar.progress(file.getFilename());
					iterator.remove();
					checkAbort();
				}
				progressBar.reset();
            } catch (InterruptedException ex) {
				Popup.info("Aborted by user");
            } 
        }
    }
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.JPanel jPanelOptions = new javax.swing.JPanel();
        jPanelOptionsMachines = new javax.swing.JPanel();
        jScrollPaneOptionsMachines = new javax.swing.JScrollPane();
        jListMachines = new javax.swing.JList();
        jButtonMachinesEdit = new javax.swing.JButton();
        jButtonMachinesDel = new javax.swing.JButton();
        jPanelOptionsGenres = new javax.swing.JPanel();
        jScrollPaneOptionsMachines1 = new javax.swing.JScrollPane();
        jListGenres = new javax.swing.JList();
        jButtonGenresEdit = new javax.swing.JButton();
        jButtonGenresDel = new javax.swing.JButton();
        jButtonGenresAdd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButtonResetCheckedFlagKO = new javax.swing.JButton();
        jButtonResetCheckedFlagWarning = new javax.swing.JButton();
        jButtonResetCheckedFlagOK = new javax.swing.JButton();
        jProgressBarResetChecked = new jamuz.gui.swing.ProgressBar();
        jPanel4 = new javax.swing.JPanel();
        jProgressBarSaveTags = new jamuz.gui.swing.ProgressBar();
        jButtonRetagAllFiles = new javax.swing.JButton();
        jPanelOptionsTags = new javax.swing.JPanel();
        jScrollPaneOptionsMachines2 = new javax.swing.JScrollPane();
        jListTags = new javax.swing.JList();
        jButtonTagsEdit = new javax.swing.JButton();
        jButtonTagsDel = new javax.swing.JButton();
        jButtonTagsAdd = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jProgressBarCleanupLogs = new jamuz.gui.swing.ProgressBar();
        jButtonCleanupLogs = new javax.swing.JButton();
        jSpinnerBytes = new javax.swing.JSpinner();
        jLabelCleanup = new javax.swing.JLabel();
        jLabelBytes = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jPanelOptionsMachines.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), bundle.getString("PanelMain.jPanelOptionsMachines.border.title"))); // NOI18N

        jListMachines.setModel(new DefaultListModel());
        jListMachines.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneOptionsMachines.setViewportView(jListMachines);

        jButtonMachinesEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/application_form_edit.png"))); // NOI18N
        jButtonMachinesEdit.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonMachinesEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMachinesEditActionPerformed(evt);
            }
        });

        jButtonMachinesDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        jButtonMachinesDel.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonMachinesDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMachinesDelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelOptionsMachinesLayout = new javax.swing.GroupLayout(jPanelOptionsMachines);
        jPanelOptionsMachines.setLayout(jPanelOptionsMachinesLayout);
        jPanelOptionsMachinesLayout.setHorizontalGroup(
            jPanelOptionsMachinesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsMachinesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneOptionsMachines, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOptionsMachinesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonMachinesEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonMachinesDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelOptionsMachinesLayout.setVerticalGroup(
            jPanelOptionsMachinesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsMachinesLayout.createSequentialGroup()
                .addComponent(jButtonMachinesEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonMachinesDel))
            .addComponent(jScrollPaneOptionsMachines, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanelOptionsGenres.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), bundle.getString("PanelMain.jPanelOptionsGenres.border.title"))); // NOI18N

        jListGenres.setModel(new DefaultListModel());
        jScrollPaneOptionsMachines1.setViewportView(jListGenres);

        jButtonGenresEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/application_form_edit.png"))); // NOI18N
        jButtonGenresEdit.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonGenresEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenresEditActionPerformed(evt);
            }
        });

        jButtonGenresDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        jButtonGenresDel.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonGenresDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenresDelActionPerformed(evt);
            }
        });

        jButtonGenresAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/add.png"))); // NOI18N
        jButtonGenresAdd.setText(bundle.getString("Button.Add")); // NOI18N
        jButtonGenresAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenresAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelOptionsGenresLayout = new javax.swing.GroupLayout(jPanelOptionsGenres);
        jPanelOptionsGenres.setLayout(jPanelOptionsGenresLayout);
        jPanelOptionsGenresLayout.setHorizontalGroup(
            jPanelOptionsGenresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsGenresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneOptionsMachines1, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOptionsGenresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonGenresDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGenresEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonGenresAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelOptionsGenresLayout.setVerticalGroup(
            jPanelOptionsGenresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsGenresLayout.createSequentialGroup()
                .addGroup(jPanelOptionsGenresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneOptionsMachines1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanelOptionsGenresLayout.createSequentialGroup()
                        .addComponent(jButtonGenresAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGenresEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGenresDel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(Inter.get("Label.Scan.ResetCheckFlag"))); // NOI18N

        jButtonResetCheckedFlagKO.setText(Inter.get("Check.KO")); // NOI18N
        jButtonResetCheckedFlagKO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetCheckedFlagKOActionPerformed(evt);
            }
        });

        jButtonResetCheckedFlagWarning.setText(Inter.get("Check.OK.Warning")); // NOI18N
        jButtonResetCheckedFlagWarning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetCheckedFlagWarningActionPerformed(evt);
            }
        });

        jButtonResetCheckedFlagOK.setText(Inter.get("Check.OK")); // NOI18N
        jButtonResetCheckedFlagOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonResetCheckedFlagOKActionPerformed(evt);
            }
        });

        jProgressBarResetChecked.setMinimumSize(new java.awt.Dimension(1, 23));
        jProgressBarResetChecked.setString(" "); // NOI18N
        jProgressBarResetChecked.setStringPainted(true);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jProgressBarResetChecked, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButtonResetCheckedFlagKO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonResetCheckedFlagWarning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonResetCheckedFlagOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonResetCheckedFlagKO)
                    .addComponent(jButtonResetCheckedFlagWarning)
                    .addComponent(jButtonResetCheckedFlagOK))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarResetChecked, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Save files"));

        jProgressBarSaveTags.setMinimumSize(new java.awt.Dimension(1, 23));
        jProgressBarSaveTags.setString(" "); // NOI18N
        jProgressBarSaveTags.setStringPainted(true);

        jButtonRetagAllFiles.setText(Inter.get("PanelMain.jButton2.text")); // NOI18N
        jButtonRetagAllFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRetagAllFilesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonRetagAllFiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBarSaveTags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonRetagAllFiles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarSaveTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelOptionsTags.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(Inter.get("Label.Tags")))); // NOI18N

        jListTags.setModel(new DefaultListModel());
        jScrollPaneOptionsMachines2.setViewportView(jListTags);

        jButtonTagsEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/application_form_edit.png"))); // NOI18N
        jButtonTagsEdit.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonTagsEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTagsEditActionPerformed(evt);
            }
        });

        jButtonTagsDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        jButtonTagsDel.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonTagsDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTagsDelActionPerformed(evt);
            }
        });

        jButtonTagsAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/add.png"))); // NOI18N
        jButtonTagsAdd.setText(bundle.getString("Button.Add")); // NOI18N
        jButtonTagsAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTagsAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelOptionsTagsLayout = new javax.swing.GroupLayout(jPanelOptionsTags);
        jPanelOptionsTags.setLayout(jPanelOptionsTagsLayout);
        jPanelOptionsTagsLayout.setHorizontalGroup(
            jPanelOptionsTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsTagsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneOptionsMachines2, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelOptionsTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonTagsDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonTagsEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonTagsAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelOptionsTagsLayout.setVerticalGroup(
            jPanelOptionsTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsTagsLayout.createSequentialGroup()
                .addGroup(jPanelOptionsTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneOptionsMachines2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanelOptionsTagsLayout.createSequentialGroup()
                        .addComponent(jButtonTagsAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonTagsEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonTagsDel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Cleanup Log folder"));

        jProgressBarCleanupLogs.setMinimumSize(new java.awt.Dimension(1, 23));
        jProgressBarCleanupLogs.setString(" "); // NOI18N
        jProgressBarCleanupLogs.setStringPainted(true);

        jButtonCleanupLogs.setText("Cleanup");
        jButtonCleanupLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCleanupLogsActionPerformed(evt);
            }
        });

        jSpinnerBytes.setModel(new javax.swing.SpinnerNumberModel(Long.valueOf(2000000000L), Long.valueOf(1000000000L), Long.valueOf(100000000000L), Long.valueOf(1000000L)));
        jSpinnerBytes.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerBytesStateChanged(evt);
            }
        });

        jLabelCleanup.setText(Inter.get("Label.Keep")); // NOI18N

        jLabelBytes.setText("(Keep 2 Go)"); // NOI18N

        jLabel1.setText("bytes"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jButtonCleanupLogs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabelCleanup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerBytes, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1))
                    .addComponent(jProgressBarCleanupLogs, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerBytes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCleanup)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCleanupLogs)
                    .addComponent(jLabelBytes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jProgressBarCleanupLogs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelOptionsLayout = new javax.swing.GroupLayout(jPanelOptions);
        jPanelOptions.setLayout(jPanelOptionsLayout);
        jPanelOptionsLayout.setHorizontalGroup(
            jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelOptionsLayout.createSequentialGroup()
                        .addComponent(jPanelOptionsGenres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelOptionsTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanelOptionsMachines, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelOptionsLayout.setVerticalGroup(
            jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelOptionsLayout.createSequentialGroup()
                        .addComponent(jPanelOptionsMachines, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelOptionsGenres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelOptionsTags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanelOptionsLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonResetCheckedFlagKOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetCheckedFlagKOActionPerformed
        resetCheckedFlag(FolderInfo.CheckedFlag.KO); 
    }//GEN-LAST:event_jButtonResetCheckedFlagKOActionPerformed

    private void jButtonMachinesEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMachinesEditActionPerformed
         DialogOptions.main(((ListElement) jListMachines.getSelectedValue()).getValue()); 
    }//GEN-LAST:event_jButtonMachinesEditActionPerformed

    private void jButtonMachinesDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonMachinesDelActionPerformed
        if (jListMachines.getSelectedIndex() > -1) { 
			String machineToDelete = (((ListElement) jListMachines.getSelectedValue()).getValue()); 
//            String machineToDelete = jListMachines.getSelectedValue().toString(); 
            if (!machineToDelete.equals(Jamuz.getMachine().getName())) { 
                int n = JOptionPane.showConfirmDialog( 
                        this, Inter.get("Question.DeleteMachineConfirm"), //NOI18N 
                        Inter.get("Label.Confirm"), //NOI18N 
                        JOptionPane.YES_NO_OPTION); 
                if (n == JOptionPane.YES_OPTION) { 
                    Jamuz.getDb().deleteMachine(machineToDelete); 
                    fillMachineList(); 
                } 
            } else { 
                Popup.warning(Inter.get("Error.CannotDeleteCurrentMachine")); //NOI18N 
            }
        } 
    }//GEN-LAST:event_jButtonMachinesDelActionPerformed

    private void jButtonGenresAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenresAddActionPerformed
		String input = JOptionPane.showInputDialog(null, Inter.get("Msg.Options.EnterGenre"), "");  //NOI18N 
        DefaultListModel model = (DefaultListModel) jListGenres.getModel(); 
        if (model.contains(input)) { 
            Popup.warning(MessageFormat.format(Inter.get("Msg.Options.GenreExists"), input));  //NOI18N 
        } else if (!input.equals("")) {  //NOI18N 
            Jamuz.getDb().insertGenre(input); 
            PanelMain.fillGenreLists(); 
			jListGenres.setModel(Jamuz.getGenreListModel());
        }
    }//GEN-LAST:event_jButtonGenresAddActionPerformed

    private void jButtonGenresEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenresEditActionPerformed
		if (jListGenres.getSelectedIndex() > -1) { 
            String input = JOptionPane.showInputDialog(null, Inter.get("Msg.Options.NewGenre"), jListGenres.getSelectedValue());  //NOI18N 
            if (input != null) { 
                int n = JOptionPane.showConfirmDialog( 
                        this, MessageFormat.format(Inter.get("Msg.Options.UpdateGenre"), jListGenres.getSelectedValue(), input), //NOI18N 
                        Inter.get("Label.Confirm"), //NOI18N 
                        JOptionPane.YES_NO_OPTION); 
                if (n == JOptionPane.YES_OPTION) { 
                    Jamuz.getDb().updateGenre((String) jListGenres.getSelectedValue(), input); 
                    PanelMain.fillGenreLists(); 
					jListGenres.setModel(Jamuz.getGenreListModel());
                } 
            } 
        } 
    }//GEN-LAST:event_jButtonGenresEditActionPerformed

    private void jButtonGenresDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenresDelActionPerformed
        if (jListGenres.getSelectedIndex() > -1) { 
            int n = JOptionPane.showConfirmDialog( 
                    this, MessageFormat.format(Inter.get("Msg.Options.DeleteGenre"), jListGenres.getSelectedValue()), //NOI18N 
                    Inter.get("Label.Confirm"), //NOI18N 
                    JOptionPane.YES_NO_OPTION); 
            if (n == JOptionPane.YES_OPTION) { 
                Jamuz.getDb().deleteGenre((String) jListGenres.getSelectedValue()); 
                PanelMain.fillGenreLists(); 
				jListGenres.setModel(Jamuz.getGenreListModel());
            } 
        } 
    }//GEN-LAST:event_jButtonGenresDelActionPerformed

    private void jButtonRetagAllFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRetagAllFilesActionPerformed
        //To re-save all files and remove all remaining unwanted tags, especially ID3v1 and POPM (popularity meter) that Guayadeque can use and  
		//messes up with the syncing if not used with extra care 
		//TODO: Add a reset "saved" field in file table, as done for CheckedFlag 

		SaveTags saveTags = new SaveTags(); 
		saveTags.start(); 
    }//GEN-LAST:event_jButtonRetagAllFilesActionPerformed

    private void jButtonResetCheckedFlagWarningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetCheckedFlagWarningActionPerformed
        resetCheckedFlag(FolderInfo.CheckedFlag.OK_WARNING); 
    }//GEN-LAST:event_jButtonResetCheckedFlagWarningActionPerformed

    private void jButtonResetCheckedFlagOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetCheckedFlagOKActionPerformed
        resetCheckedFlag(FolderInfo.CheckedFlag.OK); 
    }//GEN-LAST:event_jButtonResetCheckedFlagOKActionPerformed

    private void jButtonTagsEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTagsEditActionPerformed
        if (jListTags.getSelectedIndex() > -1) { 
            String input = JOptionPane.showInputDialog(null, Inter.get("Msg.Options.Tag.New"), jListTags.getSelectedValue());  //NOI18N 
            if (input != null) { 
                int n = JOptionPane.showConfirmDialog( 
                        this, MessageFormat.format(Inter.get("Msg.Options.Tag.Update"), jListTags.getSelectedValue(), input), //NOI18N 
                        Inter.get("Label.Confirm"), //NOI18N 
                        JOptionPane.YES_NO_OPTION); 
                if (n == JOptionPane.YES_OPTION) { 
					//FIXME MERGE Renaming a tag issues merge 
					// (and that was before merge with guayadeque and mixxx for user tags) :
					// - The tag is added back
					// - If you then merge forcing JaMuz, all files loose this tag
					// => Make sure that tags are well updated on all sources, incl. JaMuz Remote
                    Jamuz.getDb().updateTag((String) jListTags.getSelectedValue(), input); 
					refreshListTagsModel();
                } 
            } 
        } 
    }//GEN-LAST:event_jButtonTagsEditActionPerformed

	public static void refreshListTagsModel() {
		Jamuz.readTags();
		jListTags.setModel(Jamuz.getTagsModel());
	}
	
    private void jButtonTagsDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTagsDelActionPerformed
        if (jListTags.getSelectedIndex() > -1) { 
            int n = JOptionPane.showConfirmDialog( 
                    this, MessageFormat.format(Inter.get("Msg.Options.Tag.Delete"), jListTags.getSelectedValue()), //NOI18N 
                    Inter.get("Label.Confirm"), //NOI18N 
                    JOptionPane.YES_NO_OPTION); 
            if (n == JOptionPane.YES_OPTION) {
                Jamuz.getDb().deleteTag((String) jListTags.getSelectedValue()); 
                refreshListTagsModel();
            } 
        } 
    }//GEN-LAST:event_jButtonTagsDelActionPerformed

    private void jButtonTagsAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTagsAddActionPerformed
        String input = JOptionPane.showInputDialog(null, Inter.get("Msg.Options.Tag.Enter"), "");  //NOI18N 
        DefaultListModel model = (DefaultListModel) jListTags.getModel(); 
        if (model.contains(input)) { 
            Popup.warning(MessageFormat.format(Inter.get("Msg.Options.Tag.Exists"), input));  //NOI18N 
        } else if (!input.equals("")) {  //NOI18N 
            Jamuz.getDb().insertTag(input); 
            refreshListTagsModel();
        }
    }//GEN-LAST:event_jButtonTagsAddActionPerformed

    private void jButtonCleanupLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCleanupLogsActionPerformed
        CleanupLog cleanupLog = new CleanupLog(); 
		cleanupLog.start(); 
    }//GEN-LAST:event_jButtonCleanupLogsActionPerformed

    private void jSpinnerBytesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerBytesStateChanged
        long size = (Long) jSpinnerBytes.getValue();
		Jamuz.getOptions().set("log.cleanup.keep.size.bytes", String.valueOf(size));
		jLabelBytes.setText("("+Inter.get("Label.Keep")+" "+StringManager.humanReadableByteCount(size, false)+")");
    }//GEN-LAST:event_jSpinnerBytesStateChanged

	public class CleanupLog extends ProcessAbstract {

		public CleanupLog() {
            super("Thread.PanelOptions.CleanupLog");
        }
                
        @Override
        public void run() {
            try {
				File logPath = new File(Jamuz.getLogPath());
				if(logPath.exists() && logPath.isDirectory()) {
					ProgressBar progressBar = (ProgressBar)jProgressBarCleanupLogs;
					File[] logFolders = logPath.listFiles();
					progressBar.setup(0);
					int index = 0;
					if (logFolders != null) {
						//List sub-folders and delete empty ones
						checkAbort();
						ArrayList<File> folders = new ArrayList<>();
						for (File logFolder : logFolders) {
							checkAbort();
							if (logFolder.isDirectory()) {
								index++;
								if(index>progressBar.getMaximum()) {
									progressBar.setMaximum(index+10);
								}
								progressBar.progress(logFolder.getName());
								if(logFolder.list().length>0) {
									folders.add(logFolder);
								} else {
									logFolder.delete();
								}
							}
						}
						//Sort and get folders sizes
						checkAbort();
						Collections.sort(folders, Comparator.reverseOrder());
						ArrayList<FolderInfo> foldersInfo = new ArrayList<>();
						progressBar.setup(folders.size());
						for (File logFolder : folders) {
							checkAbort();
							foldersInfo.add(new FolderInfo(logFolder));
							progressBar.progress(logFolder.getName());
						}
						//Delete if over maxSize
						checkAbort();
						progressBar.setup(foldersInfo.size());
						long totalSize=0;
						long totalDeleted=0;
						
						long maxSize=Long.parseLong(Jamuz.getOptions().get("log.cleanup.keep.size.bytes", "2000000000"));
						int nbFoldersDeleted=0;
						for (FolderInfo folderInfo : foldersInfo) {
							checkAbort();
							progressBar.progress(folderInfo.folder.getName());
							totalSize+=folderInfo.size;
							System.out.println(folderInfo.folder.getName()
									+" ("+StringManager.humanReadableByteCount(
											folderInfo.size, true)+")"
							+" ["+StringManager.humanReadableByteCount(
											totalSize, true)+"]");
							if(totalSize>maxSize) {
								folderInfo.folder.delete();
								try {
									FileUtils.deleteDirectory(folderInfo.folder);
									totalDeleted+=folderInfo.size;
									nbFoldersDeleted++;
								} catch (IOException ex) {
									Logger.getLogger(PanelOptions.class.getName()).log(Level.SEVERE, null, ex);
								}
							}
						}
						progressBar.reset();
						progressBar.setString("Cleanup complete : deleted "
								+StringManager.humanReadableByteCount(totalDeleted, true)
								+" ("+nbFoldersDeleted+"/"+foldersInfo.size()+")");
					}
				}				
            } catch (InterruptedException ex) {
				Popup.info("Aborted by user");
            } 
        }
		
		class FolderInfo {
			public File folder;
			public long size;

			public FolderInfo(File folder) {
				this.folder = folder;
				this.size = FileSystem.size(folder.toPath());
			}
		}
    }
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCleanupLogs;
    private javax.swing.JButton jButtonGenresAdd;
    private javax.swing.JButton jButtonGenresDel;
    private javax.swing.JButton jButtonGenresEdit;
    private javax.swing.JButton jButtonMachinesDel;
    private javax.swing.JButton jButtonMachinesEdit;
    private javax.swing.JButton jButtonResetCheckedFlagKO;
    private javax.swing.JButton jButtonResetCheckedFlagOK;
    private javax.swing.JButton jButtonResetCheckedFlagWarning;
    private javax.swing.JButton jButtonRetagAllFiles;
    private javax.swing.JButton jButtonTagsAdd;
    private javax.swing.JButton jButtonTagsDel;
    private javax.swing.JButton jButtonTagsEdit;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelBytes;
    private javax.swing.JLabel jLabelCleanup;
    private static javax.swing.JList jListGenres;
    private static javax.swing.JList jListMachines;
    private static javax.swing.JList jListTags;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanelOptionsGenres;
    private javax.swing.JPanel jPanelOptionsMachines;
    private javax.swing.JPanel jPanelOptionsTags;
    private static javax.swing.JProgressBar jProgressBarCleanupLogs;
    private static javax.swing.JProgressBar jProgressBarResetChecked;
    private static javax.swing.JProgressBar jProgressBarSaveTags;
    private javax.swing.JScrollPane jScrollPaneOptionsMachines;
    private javax.swing.JScrollPane jScrollPaneOptionsMachines1;
    private javax.swing.JScrollPane jScrollPaneOptionsMachines2;
    private javax.swing.JSpinner jSpinnerBytes;
    // End of variables declaration//GEN-END:variables
}
