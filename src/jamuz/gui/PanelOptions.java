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
import static jamuz.gui.PanelMain.jButtonPlayerPlay;
import jamuz.gui.swing.ListElement;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.check.FolderInfo;
import jamuz.remote.Client;
import jamuz.remote.ICallBackReception;
import jamuz.remote.ServerClient;
import jamuz.utils.Inter;
import jamuz.utils.Popup;
import jamuz.utils.ProcessAbstract;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

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
		jListTags.setModel(Jamuz.getTagsModel());
	}
	
	public static void fillMachineList() {
        //Display machines list
        fillMachineList((DefaultListModel) jListMachines.getModel());  //NOI18N
        //Select current machine
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
	
	/**
	 *
	 */
	public class SaveTags extends ProcessAbstract {

		/**
		 *
		 */
		public SaveTags() {
            super("Thread.PanelMain.SaveTags");
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
        jButtonOptionsMachinesEdit = new javax.swing.JButton();
        jButtonOptionsMachinesDel = new javax.swing.JButton();
        jPanelOptionsGenres = new javax.swing.JPanel();
        jScrollPaneOptionsMachines1 = new javax.swing.JScrollPane();
        jListGenres = new javax.swing.JList();
        jButtonOptionsGenresEdit = new javax.swing.JButton();
        jButtonOptionsGenresDel = new javax.swing.JButton();
        jButtonOptionsGenresAdd = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButtonResetCheckedFlagKO = new javax.swing.JButton();
        jButtonResetCheckedFlagWarning = new javax.swing.JButton();
        jButtonResetCheckedFlagOK = new javax.swing.JButton();
        jProgressBarResetChecked = new jamuz.gui.swing.ProgressBar();
        jPanel4 = new javax.swing.JPanel();
        jProgressBarSaveTags = new jamuz.gui.swing.ProgressBar();
        jButton2 = new javax.swing.JButton();
        jPanelOptionsTags = new javax.swing.JPanel();
        jScrollPaneOptionsMachines2 = new javax.swing.JScrollPane();
        jListTags = new javax.swing.JList();
        jButtonOptionsGenresEdit1 = new javax.swing.JButton();
        jButtonOptionsGenresDel1 = new javax.swing.JButton();
        jButtonOptionsGenresAdd1 = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jPanelOptionsMachines.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), bundle.getString("PanelMain.jPanelOptionsMachines.border.title"))); // NOI18N

        jListMachines.setModel(new DefaultListModel());
        jListMachines.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneOptionsMachines.setViewportView(jListMachines);

        jButtonOptionsMachinesEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/application_form_edit.png"))); // NOI18N
        jButtonOptionsMachinesEdit.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonOptionsMachinesEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionsMachinesEditActionPerformed(evt);
            }
        });

        jButtonOptionsMachinesDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        jButtonOptionsMachinesDel.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonOptionsMachinesDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionsMachinesDelActionPerformed(evt);
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
                    .addComponent(jButtonOptionsMachinesEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonOptionsMachinesDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelOptionsMachinesLayout.setVerticalGroup(
            jPanelOptionsMachinesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsMachinesLayout.createSequentialGroup()
                .addComponent(jButtonOptionsMachinesEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonOptionsMachinesDel))
            .addComponent(jScrollPaneOptionsMachines, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanelOptionsGenres.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), bundle.getString("PanelMain.jPanelOptionsGenres.border.title"))); // NOI18N

        jListGenres.setModel(new DefaultListModel());
        jScrollPaneOptionsMachines1.setViewportView(jListGenres);

        jButtonOptionsGenresEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/application_form_edit.png"))); // NOI18N
        jButtonOptionsGenresEdit.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonOptionsGenresEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionsGenresEditActionPerformed(evt);
            }
        });

        jButtonOptionsGenresDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        jButtonOptionsGenresDel.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonOptionsGenresDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionsGenresDelActionPerformed(evt);
            }
        });

        jButtonOptionsGenresAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/add.png"))); // NOI18N
        jButtonOptionsGenresAdd.setText(bundle.getString("Button.Add")); // NOI18N
        jButtonOptionsGenresAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionsGenresAddActionPerformed(evt);
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
                    .addComponent(jButtonOptionsGenresDel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonOptionsGenresEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonOptionsGenresAdd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelOptionsGenresLayout.setVerticalGroup(
            jPanelOptionsGenresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsGenresLayout.createSequentialGroup()
                .addGroup(jPanelOptionsGenresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneOptionsMachines1)
                    .addGroup(jPanelOptionsGenresLayout.createSequentialGroup()
                        .addComponent(jButtonOptionsGenresAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionsGenresEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionsGenresDel)
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
                        .addComponent(jButtonResetCheckedFlagKO, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonResetCheckedFlagWarning, javax.swing.GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonResetCheckedFlagOK, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)
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
                .addComponent(jProgressBarResetChecked, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(Inter.get("PanelMain.jPanel4.border.title"))); // NOI18N

        jProgressBarSaveTags.setMinimumSize(new java.awt.Dimension(1, 23));
        jProgressBarSaveTags.setString(" "); // NOI18N
        jProgressBarSaveTags.setStringPainted(true);

        jButton2.setText(Inter.get("PanelMain.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBarSaveTags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarSaveTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelOptionsTags.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(Inter.get("Label.Tags")))); // NOI18N

        jListTags.setModel(new DefaultListModel());
        jScrollPaneOptionsMachines2.setViewportView(jListTags);

        jButtonOptionsGenresEdit1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/application_form_edit.png"))); // NOI18N
        jButtonOptionsGenresEdit1.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonOptionsGenresEdit1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionsGenresEdit1ActionPerformed(evt);
            }
        });

        jButtonOptionsGenresDel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/bin.png"))); // NOI18N
        jButtonOptionsGenresDel1.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonOptionsGenresDel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionsGenresDel1ActionPerformed(evt);
            }
        });

        jButtonOptionsGenresAdd1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/add.png"))); // NOI18N
        jButtonOptionsGenresAdd1.setText(bundle.getString("Button.Add")); // NOI18N
        jButtonOptionsGenresAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOptionsGenresAdd1ActionPerformed(evt);
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
                    .addComponent(jButtonOptionsGenresDel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonOptionsGenresEdit1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonOptionsGenresAdd1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelOptionsTagsLayout.setVerticalGroup(
            jPanelOptionsTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsTagsLayout.createSequentialGroup()
                .addGroup(jPanelOptionsTagsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPaneOptionsMachines2, javax.swing.GroupLayout.DEFAULT_SIZE, 182, Short.MAX_VALUE)
                    .addGroup(jPanelOptionsTagsLayout.createSequentialGroup()
                        .addComponent(jButtonOptionsGenresAdd1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionsGenresEdit1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonOptionsGenresDel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelOptionsLayout.setVerticalGroup(
            jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsLayout.createSequentialGroup()
                .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelOptionsLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanelOptionsMachines, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelOptionsGenres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelOptionsTags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanelOptionsLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelOptions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonResetCheckedFlagKOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetCheckedFlagKOActionPerformed
        resetCheckedFlag(FolderInfo.CheckedFlag.KO); 
    }//GEN-LAST:event_jButtonResetCheckedFlagKOActionPerformed

    private void jButtonOptionsMachinesEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionsMachinesEditActionPerformed
         DialogOptions.main(((ListElement) jListMachines.getSelectedValue()).getValue()); 
    }//GEN-LAST:event_jButtonOptionsMachinesEditActionPerformed

    private void jButtonOptionsMachinesDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionsMachinesDelActionPerformed
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
    }//GEN-LAST:event_jButtonOptionsMachinesDelActionPerformed

    private void jButtonOptionsGenresAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionsGenresAddActionPerformed
		String input = JOptionPane.showInputDialog(null, Inter.get("Msg.Options.EnterGenre"), "");  //NOI18N 
        DefaultListModel model = (DefaultListModel) jListGenres.getModel(); 
        if (model.contains(input)) { 
            Popup.warning(MessageFormat.format(Inter.get("Msg.Options.GenreExists"), input));  //NOI18N 
        } else if (!input.equals("")) {  //NOI18N 
            Jamuz.getDb().insertGenre(input); 
            PanelMain.fillGenreLists(); 
			jListGenres.setModel(Jamuz.getGenreListModel());
        }
    }//GEN-LAST:event_jButtonOptionsGenresAddActionPerformed

    private void jButtonOptionsGenresEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionsGenresEditActionPerformed
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
    }//GEN-LAST:event_jButtonOptionsGenresEditActionPerformed

    private void jButtonOptionsGenresDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionsGenresDelActionPerformed
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
    }//GEN-LAST:event_jButtonOptionsGenresDelActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //To re-save all files and remove all remaining unwanted tags, especially ID3v1 and POPM (popularity meter) that Guayadeque can use and  
		//messes up with the syncing if not used with extra care 
		//TODO: Add a reset "saved" field in file table, as done for CheckedFlag 

		SaveTags saveTags = new SaveTags(); 
		saveTags.start(); 
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButtonResetCheckedFlagWarningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetCheckedFlagWarningActionPerformed
        resetCheckedFlag(FolderInfo.CheckedFlag.OK_WARNING); 
    }//GEN-LAST:event_jButtonResetCheckedFlagWarningActionPerformed

    private void jButtonResetCheckedFlagOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetCheckedFlagOKActionPerformed
        resetCheckedFlag(FolderInfo.CheckedFlag.OK); 
    }//GEN-LAST:event_jButtonResetCheckedFlagOKActionPerformed

    private void jButtonOptionsGenresEdit1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionsGenresEdit1ActionPerformed
        if (jListTags.getSelectedIndex() > -1) { 
            String input = JOptionPane.showInputDialog(null, Inter.get("Msg.Options.Tag.New"), jListTags.getSelectedValue());  //NOI18N 
            if (input != null) { 
                int n = JOptionPane.showConfirmDialog( 
                        this, MessageFormat.format(Inter.get("Msg.Options.Tag.Update"), jListTags.getSelectedValue(), input), //NOI18N 
                        Inter.get("Label.Confirm"), //NOI18N 
                        JOptionPane.YES_NO_OPTION); 
                if (n == JOptionPane.YES_OPTION) { 
                    Jamuz.getDb().updateTag((String) jListTags.getSelectedValue(), input); 
                    Jamuz.readTags(); 
					jListTags.setModel(Jamuz.getTagsModel());
                } 
            } 
        } 
    }//GEN-LAST:event_jButtonOptionsGenresEdit1ActionPerformed

    private void jButtonOptionsGenresDel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionsGenresDel1ActionPerformed
        if (jListTags.getSelectedIndex() > -1) { 
            int n = JOptionPane.showConfirmDialog( 
                    this, MessageFormat.format(Inter.get("Msg.Options.Tag.Delete"), jListTags.getSelectedValue()), //NOI18N 
                    Inter.get("Label.Confirm"), //NOI18N 
                    JOptionPane.YES_NO_OPTION); 
            if (n == JOptionPane.YES_OPTION) {
                Jamuz.getDb().deleteTag((String) jListTags.getSelectedValue()); 
                Jamuz.readTags(); 
				jListTags.setModel(Jamuz.getTagsModel());
            } 
        } 
    }//GEN-LAST:event_jButtonOptionsGenresDel1ActionPerformed

    private void jButtonOptionsGenresAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonOptionsGenresAdd1ActionPerformed
        String input = JOptionPane.showInputDialog(null, Inter.get("Msg.Options.Tag.Enter"), "");  //NOI18N 
        DefaultListModel model = (DefaultListModel) jListTags.getModel(); 
        if (model.contains(input)) { 
            Popup.warning(MessageFormat.format(Inter.get("Msg.Options.Tag.Exists"), input));  //NOI18N 
        } else if (!input.equals("")) {  //NOI18N 
            Jamuz.getDb().insertTag(input); 
            Jamuz.readTags(); 
			jListTags.setModel(Jamuz.getTagsModel());
        }
    }//GEN-LAST:event_jButtonOptionsGenresAdd1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButtonOptionsGenresAdd;
    private javax.swing.JButton jButtonOptionsGenresAdd1;
    private javax.swing.JButton jButtonOptionsGenresDel;
    private javax.swing.JButton jButtonOptionsGenresDel1;
    private javax.swing.JButton jButtonOptionsGenresEdit;
    private javax.swing.JButton jButtonOptionsGenresEdit1;
    private javax.swing.JButton jButtonOptionsMachinesDel;
    private javax.swing.JButton jButtonOptionsMachinesEdit;
    private javax.swing.JButton jButtonResetCheckedFlagKO;
    private javax.swing.JButton jButtonResetCheckedFlagOK;
    private javax.swing.JButton jButtonResetCheckedFlagWarning;
    private javax.swing.JList jListGenres;
    private static javax.swing.JList jListMachines;
    private javax.swing.JList jListTags;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelOptionsGenres;
    private javax.swing.JPanel jPanelOptionsMachines;
    private javax.swing.JPanel jPanelOptionsTags;
    private static javax.swing.JProgressBar jProgressBarResetChecked;
    private static javax.swing.JProgressBar jProgressBarSaveTags;
    private javax.swing.JScrollPane jScrollPaneOptionsMachines;
    private javax.swing.JScrollPane jScrollPaneOptionsMachines1;
    private javax.swing.JScrollPane jScrollPaneOptionsMachines2;
    // End of variables declaration//GEN-END:variables
}
