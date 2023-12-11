/*
 * Copyright (C) 2017 phramusca <phramusca@gmail.com>
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

import jamuz.AppVersion;
import jamuz.FileInfoInt;
import jamuz.ICallBackVersionCheck;
import jamuz.Jamuz;
import jamuz.AppVersionCheck;
import jamuz.gui.swing.ListElement;
import jamuz.gui.swing.ProgressBar;
import jamuz.process.check.FolderInfo;
import jamuz.utils.FileSystem;
import jamuz.utils.Inter;
import jamuz.utils.OS;
import jamuz.utils.Popup;
import jamuz.utils.ProcessAbstract;
import jamuz.utils.StringManager;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class PanelOptions extends javax.swing.JPanel {

	/**
	 *
	 */
	protected static ProgressBar progressBarCheckedFlag;
	private Frame parent;
	private final ProgressBar progressBarVersionCheck;
	private AppVersionCheck versionCheck;

	/**
	 * Creates new form PanelOptions
	 */
	public PanelOptions() {
		initComponents();
		progressBarVersionCheck = (ProgressBar) jProgressBarUpdate;
		progressBarCheckedFlag = (ProgressBar) jProgressBarResetChecked;
	}

	/**
	 * extended init
	 *
	 * @param parent
	 */
	public void initExtended(Frame parent) {
		this.parent = parent;
		fillMachineList();
		jListGenres.setModel(Jamuz.getGenreListModel());
		long size = Long.parseLong(Jamuz.getOptions().get("log.cleanup.keep.size.bytes", "2000000000"));
		jSpinnerBytes.getModel().setValue(size);
		jLabelBytes.setText("(" + Inter.get("Label.Keep") + " " + StringManager.humanReadableByteCount(size, false) + ")");
		jListTags.setModel(Jamuz.getTagsModel());
		versionCheck = new AppVersionCheck(new ICallBackVersionCheck() {
			@Override
			public void onNewVersion(AppVersion appVersion) {
				progressBarVersionCheck.reset();
				jLabelVersionCheck.setText("<html><a href='#'>"
						+ appVersion.toString()
						+ "<BR/>A new version is available! Click to update.</a></html>");
				enableVersionCheck(true);
			}

			@Override
			public void onCheck(AppVersion appVersion, String msg) {
				jLabelVersionCheck.setText("<html>"
						+ appVersion.toString()
						+ "<BR/>" + msg + "</html>");
			}

			@Override
			public void onCheckResult(AppVersion appVersion, String msg) {
				jLabelVersionCheck.setText("<html>"
						+ appVersion.toString()
						+ "<BR/>" + msg + "</html>");
				enableVersionCheck(true);
				if (!appVersion.isNewVersion()) {
					File updateCacheFolder = Jamuz.getFile("", "data", "cache", "system", "update");
					FileUtils.deleteQuietly(updateCacheFolder);
				}
			}

			@Override
			public void onUnzipCount(AppVersion appVersion) {
				progressBarVersionCheck.setIndeterminate("Counting files in " + appVersion.getAssetFile().getName());
			}

			@Override
			public void onUnzipStart() {
				progressBarVersionCheck.setupAsPercentage();
			}

			@Override
			public void onUnzipProgress(AppVersion appVersion, String filename, int percentComplete) {
				progressBarVersionCheck.progress("Unzipping " + appVersion.getAssetFile().getName() + " (" + filename + ")", percentComplete);
			}

			@Override
			public void onDownloadRequest(AppVersion appVersion) {
				jLabelVersionCheck.setText(appVersion.toString());
				progressBarVersionCheck.setIndeterminate("Downloading " + appVersion.getAssetFile().getName());
			}

			@Override
			public void onDownloadStart() {
				progressBarVersionCheck.setupAsPercentage();
			}

			@Override
			public void onDownloadProgress(AppVersion appVersion, int progress) {
				progressBarVersionCheck.progress("Downloading " + appVersion.getAssetFile().getName(), progress);
			}
		});
		startVersionCheck();
	}

	private void startVersionCheck() {
		enableVersionCheck(false);
		versionCheck.start(jCheckBoxVersionPreRelease.isSelected());
	}

	/**
	 *
	 */
	public static void fillMachineList() {
		fillMachineList((DefaultListModel) jListMachines.getModel());  //NOI18N
		jListMachines.setSelectedValue(new ListElement(Jamuz.getMachine().getName(), ""), true);
	}

	private static void fillMachineList(DefaultListModel listModel) {
		listModel.clear();
		Jamuz.getDb().listModel().getMachineListModel(listModel);
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
			Jamuz.getDb().path().updateCheckedFlagReset(checkedFlag);
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
			super("Thread.PanelOptions.SaveTags");
		}

		/**
		 *
		 */
		@Override
		public void run() {
			try {
				ArrayList<FileInfoInt> filesToSave = new ArrayList<>();
				String sql = "SELECT F.*, P.strPath, P.checked, P.copyRight, 0 AS albumRating, 0 AS percentRated, 'INFO' AS status, P.mbId AS pathMbId, P.modifDate AS pathModifDate "
						+ " FROM file F JOIN path P ON F.idPath=P.idPath WHERE saved=0";
				ProgressBar progressBar = (ProgressBar) jProgressBarSaveTags;

				progressBar.setIndeterminate("Retrieving list");
				Jamuz.getDb().file().getFiles(filesToSave, sql);

				progressBar.setup(filesToSave.size());
				for (Iterator<FileInfoInt> iterator = filesToSave.iterator(); iterator.hasNext();) {
					FileInfoInt file = iterator.next();
					//TODO: Maybe offer not to read tags. 
					//This would be speed up the process but needs a scan library first
					//Can also be useful (why is that?) so we can modify tags in dB and save that to tags.
					file.readMetadata(true);
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
        jPanelResetCheckedFlag = new javax.swing.JPanel();
        jButtonResetCheckedFlagKO = new javax.swing.JButton();
        jButtonResetCheckedFlagWarning = new javax.swing.JButton();
        jButtonResetCheckedFlagOK = new javax.swing.JButton();
        jProgressBarResetChecked = new jamuz.gui.swing.ProgressBar();
        jPanelSaveFiles = new javax.swing.JPanel();
        jProgressBarSaveTags = new jamuz.gui.swing.ProgressBar();
        jButtonRetagAllFiles = new javax.swing.JButton();
        jPanelOptionsTags = new javax.swing.JPanel();
        jScrollPaneOptionsMachines2 = new javax.swing.JScrollPane();
        jListTags = new javax.swing.JList();
        jButtonTagsEdit = new javax.swing.JButton();
        jButtonTagsDel = new javax.swing.JButton();
        jButtonTagsAdd = new javax.swing.JButton();
        jPanelCleanupLogFolder = new javax.swing.JPanel();
        jProgressBarCleanupLogs = new jamuz.gui.swing.ProgressBar();
        jButtonCleanupLogs = new javax.swing.JButton();
        jSpinnerBytes = new javax.swing.JSpinner();
        jLabelCleanup = new javax.swing.JLabel();
        jLabelBytes = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneShortcuts = new javax.swing.JTextPane();
        jPanel1 = new javax.swing.JPanel();
        jProgressBarUpdate = new jamuz.gui.swing.ProgressBar();
        jLabelVersionCheck = new javax.swing.JLabel();
        jCheckBoxVersionPreRelease = new javax.swing.JCheckBox();
        jButtonVersionCheck = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("inter/Bundle"); // NOI18N
        jPanelOptionsMachines.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("PanelMain.jPanelOptionsMachines.border.title"))); // NOI18N
        jPanelOptionsMachines.setName("PanelOptionsMachines"); // NOI18N

        jListMachines.setModel(new DefaultListModel());
        jListMachines.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPaneOptionsMachines.setViewportView(jListMachines);

        jButtonMachinesEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/application_form_edit.png"))); // NOI18N
        jButtonMachinesEdit.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonMachinesEdit.setName("PanelOptionsMachinesEditButton"); // NOI18N
        jButtonMachinesEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonMachinesEditActionPerformed(evt);
            }
        });

        jButtonMachinesDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bin.png"))); // NOI18N
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
                .addGroup(jPanelOptionsMachinesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelOptionsMachinesLayout.createSequentialGroup()
                        .addComponent(jButtonMachinesEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonMachinesDel))
                    .addComponent(jScrollPaneOptionsMachines, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelOptionsGenres.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("PanelMain.jPanelOptionsGenres.border.title"))); // NOI18N

        jListGenres.setModel(new DefaultListModel());
        jScrollPaneOptionsMachines1.setViewportView(jListGenres);

        jButtonGenresEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/application_form_edit.png"))); // NOI18N
        jButtonGenresEdit.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonGenresEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenresEditActionPerformed(evt);
            }
        });

        jButtonGenresDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bin.png"))); // NOI18N
        jButtonGenresDel.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonGenresDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonGenresDelActionPerformed(evt);
            }
        });

        jButtonGenresAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
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
                    .addComponent(jScrollPaneOptionsMachines1, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addGroup(jPanelOptionsGenresLayout.createSequentialGroup()
                        .addComponent(jButtonGenresAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGenresEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonGenresDel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanelResetCheckedFlag.setBorder(javax.swing.BorderFactory.createTitledBorder(Inter.get("Label.Scan.ResetCheckFlag"))); // NOI18N

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

        javax.swing.GroupLayout jPanelResetCheckedFlagLayout = new javax.swing.GroupLayout(jPanelResetCheckedFlag);
        jPanelResetCheckedFlag.setLayout(jPanelResetCheckedFlagLayout);
        jPanelResetCheckedFlagLayout.setHorizontalGroup(
            jPanelResetCheckedFlagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelResetCheckedFlagLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelResetCheckedFlagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelResetCheckedFlagLayout.createSequentialGroup()
                        .addComponent(jProgressBarResetChecked, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(3, 3, 3))
                    .addGroup(jPanelResetCheckedFlagLayout.createSequentialGroup()
                        .addComponent(jButtonResetCheckedFlagKO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonResetCheckedFlagWarning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonResetCheckedFlagOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanelResetCheckedFlagLayout.setVerticalGroup(
            jPanelResetCheckedFlagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelResetCheckedFlagLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelResetCheckedFlagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonResetCheckedFlagKO)
                    .addComponent(jButtonResetCheckedFlagWarning)
                    .addComponent(jButtonResetCheckedFlagOK))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarResetChecked, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanelSaveFiles.setBorder(javax.swing.BorderFactory.createTitledBorder("Save files"));

        jProgressBarSaveTags.setMinimumSize(new java.awt.Dimension(1, 23));
        jProgressBarSaveTags.setString(" "); // NOI18N
        jProgressBarSaveTags.setStringPainted(true);

        jButtonRetagAllFiles.setText(Inter.get("PanelMain.jButton2.text")); // NOI18N
        jButtonRetagAllFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRetagAllFilesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelSaveFilesLayout = new javax.swing.GroupLayout(jPanelSaveFiles);
        jPanelSaveFiles.setLayout(jPanelSaveFilesLayout);
        jPanelSaveFilesLayout.setHorizontalGroup(
            jPanelSaveFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSaveFilesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelSaveFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButtonRetagAllFiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jProgressBarSaveTags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelSaveFilesLayout.setVerticalGroup(
            jPanelSaveFilesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSaveFilesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonRetagAllFiles)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarSaveTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelOptionsTags.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(Inter.get("Label.Tags")))); // NOI18N

        jListTags.setModel(new DefaultListModel());
        jScrollPaneOptionsMachines2.setViewportView(jListTags);

        jButtonTagsEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/application_form_edit.png"))); // NOI18N
        jButtonTagsEdit.setText(bundle.getString("Button.Edit")); // NOI18N
        jButtonTagsEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTagsEditActionPerformed(evt);
            }
        });

        jButtonTagsDel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/bin.png"))); // NOI18N
        jButtonTagsDel.setText(bundle.getString("Button.Delete")); // NOI18N
        jButtonTagsDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonTagsDelActionPerformed(evt);
            }
        });

        jButtonTagsAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/add.png"))); // NOI18N
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
                    .addComponent(jScrollPaneOptionsMachines2)
                    .addGroup(jPanelOptionsTagsLayout.createSequentialGroup()
                        .addComponent(jButtonTagsAdd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonTagsEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonTagsDel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanelCleanupLogFolder.setBorder(javax.swing.BorderFactory.createTitledBorder("Cleanup Log folder"));

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

        javax.swing.GroupLayout jPanelCleanupLogFolderLayout = new javax.swing.GroupLayout(jPanelCleanupLogFolder);
        jPanelCleanupLogFolder.setLayout(jPanelCleanupLogFolderLayout);
        jPanelCleanupLogFolderLayout.setHorizontalGroup(
            jPanelCleanupLogFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCleanupLogFolderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelCleanupLogFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCleanupLogFolderLayout.createSequentialGroup()
                        .addComponent(jButtonCleanupLogs)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelBytes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCleanupLogFolderLayout.createSequentialGroup()
                        .addComponent(jLabelCleanup)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSpinnerBytes, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1))
                    .addComponent(jProgressBarCleanupLogs, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelCleanupLogFolderLayout.setVerticalGroup(
            jPanelCleanupLogFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelCleanupLogFolderLayout.createSequentialGroup()
                .addGroup(jPanelCleanupLogFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jSpinnerBytes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCleanup)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCleanupLogFolderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCleanupLogs)
                    .addComponent(jLabelBytes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jProgressBarCleanupLogs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTextPaneShortcuts.setEditable(false);
        jTextPaneShortcuts.setText("Keyboard Shortcuts:\n\n- ALT+ SHIFT + P : Play\n- ALT+ SHIFT + RIGHT : Next track\n- ALT+ SHIFT + LEFT : Previous track\n- ALT+ SHIFT + F : Forward\n- ALT+ SHIFT + R : Rewind\n- ALT+ SHIFT + W : Pullup\n- ALT+ SHIFT + E : Clear tracks       \n- ALT+ SHIFT + F1 : Set rating 1\n- ALT+ SHIFT + F2 : Set rating 2\n- ALT+ SHIFT + F3 : Set rating 3\n- ALT+ SHIFT + F4 : Set rating 4\n- ALT+ SHIFT + F5 : Set rating 5");
        jTextPaneShortcuts.setEnabled(false);
        jTextPaneShortcuts.setFocusable(false);
        jScrollPane1.setViewportView(jTextPaneShortcuts);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Version"));

        jProgressBarUpdate.setString(""); // NOI18N
        jProgressBarUpdate.setStringPainted(true);

        jLabelVersionCheck.setText(" "); // NOI18N
        jLabelVersionCheck.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabelVersionCheckMouseClicked(evt);
            }
        });

        jCheckBoxVersionPreRelease.setText("Pre-release ?");

        jButtonVersionCheck.setText("Check");
        jButtonVersionCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVersionCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabelVersionCheck, javax.swing.GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVersionCheck)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckBoxVersionPreRelease))
                    .addComponent(jProgressBarUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelVersionCheck)
                    .addComponent(jCheckBoxVersionPreRelease)
                    .addComponent(jButtonVersionCheck))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBarUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanelOptionsLayout = new javax.swing.GroupLayout(jPanelOptions);
        jPanelOptions.setLayout(jPanelOptionsLayout);
        jPanelOptionsLayout.setHorizontalGroup(
            jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanelOptionsLayout.createSequentialGroup()
                        .addComponent(jPanelOptionsGenres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelOptionsTags, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelOptionsLayout.createSequentialGroup()
                        .addComponent(jPanelOptionsMachines, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1)))
                .addGap(29, 29, 29)
                .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelSaveFiles, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelResetCheckedFlag, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanelCleanupLogFolder, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanelOptionsLayout.setVerticalGroup(
            jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelOptionsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelOptionsLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanelCleanupLogFolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelSaveFiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanelResetCheckedFlag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanelOptionsLayout.createSequentialGroup()
                        .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jPanelOptionsMachines, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelOptionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelOptionsGenres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanelOptionsTags, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
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
		DialogOptions.main(parent, ((ListElement) jListMachines.getSelectedValue()).getValue());
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
					Jamuz.getDb().machine().delete(machineToDelete);
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
		} else if (!input.isBlank()) {  //NOI18N 
			Jamuz.getDb().genre().insert(input);
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
					Jamuz.getDb().genre().update((String) jListGenres.getSelectedValue(), input);
					PanelMain.fillGenreLists();
					jListGenres.setModel(Jamuz.getGenreListModel());
				}
			}
		}
    }//GEN-LAST:event_jButtonGenresEditActionPerformed

    private void jButtonGenresDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonGenresDelActionPerformed
		if (jListGenres.getSelectedIndex() > -1) {
			int n = JOptionPane.showConfirmDialog(
					this, MessageFormat.format(
							Inter.get("Msg.Options.DeleteGenre"),
							jListGenres.getSelectedValue()), //NOI18N 
					Inter.get("Label.Confirm"), //NOI18N 
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				Jamuz.getDb().genre().delete((String) jListGenres.getSelectedValue());
				PanelMain.fillGenreLists();
				jListGenres.setModel(Jamuz.getGenreListModel());
			}
		}
    }//GEN-LAST:event_jButtonGenresDelActionPerformed

    private void jButtonRetagAllFilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRetagAllFilesActionPerformed
		//To re-save all files and remove all remaining unwanted tags, 
		//	especially ID3v1 and POPM (popularity meter) that Guayadeque can use and  
		//messes up with the syncing if not used with extra care 
		//TODO: Add a reset "saved" field in file table, as done for CheckedFlag 

		int n = JOptionPane.showConfirmDialog(
				this, "Save all files ?", //NOI18N 
				Inter.get("Label.Confirm"), //NOI18N 
				JOptionPane.YES_NO_OPTION);
		if (n == JOptionPane.YES_OPTION) {
			SaveTags saveTags = new SaveTags();
			saveTags.start();
		}
    }//GEN-LAST:event_jButtonRetagAllFilesActionPerformed

    private void jButtonResetCheckedFlagWarningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetCheckedFlagWarningActionPerformed
		resetCheckedFlag(FolderInfo.CheckedFlag.OK_WARNING);
    }//GEN-LAST:event_jButtonResetCheckedFlagWarningActionPerformed

    private void jButtonResetCheckedFlagOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonResetCheckedFlagOKActionPerformed
		resetCheckedFlag(FolderInfo.CheckedFlag.OK);
    }//GEN-LAST:event_jButtonResetCheckedFlagOKActionPerformed

    private void jButtonTagsEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTagsEditActionPerformed
		if (jListTags.getSelectedIndex() > -1) {
			String newTag = JOptionPane.showInputDialog(null,
					Inter.get("Msg.Options.Tag.New"),
					jListTags.getSelectedValue());  //NOI18N 
			if (newTag != null) {
				int n = JOptionPane.showConfirmDialog(
						this, MessageFormat.format(
								Inter.get("Msg.Options.Tag.Update"),
								jListTags.getSelectedValue(), newTag), //NOI18N 
						Inter.get("Label.Confirm"), //NOI18N 
						JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION) {
					if (Jamuz.getDb().tag().update((String) jListTags.getSelectedValue(), newTag)) {
						if (Jamuz.getDb().fileTag().updateModifDate(newTag)) {
							refreshListTagsModel();
						}
					}
				}
			}
		}
    }//GEN-LAST:event_jButtonTagsEditActionPerformed

	/**
	 *
	 */
	public static void refreshListTagsModel() {
		Jamuz.readTags();
		jListTags.setModel(Jamuz.getTagsModel());
	}

    private void jButtonTagsDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTagsDelActionPerformed
		if (jListTags.getSelectedIndex() > -1) {
			int n = JOptionPane.showConfirmDialog(
					this, MessageFormat.format(
							Inter.get("Msg.Options.Tag.Delete"),
							jListTags.getSelectedValue()), //NOI18N 
					Inter.get("Label.Confirm"), //NOI18N 
					JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION) {
				if (Jamuz.getDb().tag().delete((String) jListTags.getSelectedValue())) {
					Popup.warning("Problem deleting tag. It is probably applied to at least a track, so cannot delete it.");  //NOI18N
					refreshListTagsModel();
				}
			}
		}
    }//GEN-LAST:event_jButtonTagsDelActionPerformed

    private void jButtonTagsAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonTagsAddActionPerformed
		String input = JOptionPane.showInputDialog(null, Inter.get("Msg.Options.Tag.Enter"), "");  //NOI18N 
		DefaultListModel model = (DefaultListModel) jListTags.getModel();
		if (model.contains(input)) {
			Popup.warning(MessageFormat.format(Inter.get("Msg.Options.Tag.Exists"), input));  //NOI18N 
		} else if (!input.isBlank()) {  //NOI18N 
			Jamuz.getDb().tag().insert(input);
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
		jLabelBytes.setText("(" + Inter.get("Label.Keep") + " " + StringManager.humanReadableByteCount(size, false) + ")");
    }//GEN-LAST:event_jSpinnerBytesStateChanged

	private boolean lockUpdate = false;

    private void jLabelVersionCheckMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelVersionCheckMouseClicked
		if (!lockUpdate) {
			lockUpdate = true;
			AppVersion appVersion = this.versionCheck.getAppVersion();
			if (appVersion.isNewVersion()) {
				int n = JOptionPane.showConfirmDialog(
						this, "Make sure that you do not have any process running (merge, export, check, server, ...).\n\nProceed with update?", //NOI18N
						Inter.get("Label.Confirm"), //NOI18N
						JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
				if (n == JOptionPane.YES_OPTION) {
					new Thread() {
						@Override
						public void run() {
							try {
								String command = null;
								if (OS.isUnix()) {
									File update_script = Jamuz.getFile("update_linux.sh", "data", "cache", "system", "update", appVersion.getLatestVersion(), "JaMuz", "data", "system", "update");
									Set<PosixFilePermission> permissions = Files.getPosixFilePermissions(Path.of(update_script.getAbsolutePath()));
									if (!permissions.contains(PosixFilePermission.OWNER_EXECUTE)) {
										permissions.add(PosixFilePermission.OWNER_EXECUTE);
										Files.setPosixFilePermissions(Path.of(update_script.getAbsolutePath()), permissions);
									}
									command = "x-terminal-emulator -e bash " + update_script.getAbsolutePath() + " " + appVersion.getCurrentVersion() + " " + appVersion.getLatestVersion();
								} else if (OS.isWindows()) {
									File update_script = Jamuz.getFile("update_windows.ps1", "data", "cache", "system", "update", appVersion.getLatestVersion(), "JaMuz", "data", "system", "update");
									command = "cmd /c start powershell.exe -ExecutionPolicy Bypass -File " + update_script.getAbsolutePath() + " " + appVersion.getCurrentVersion() + " " + appVersion.getLatestVersion();
								}
								if (command != null) {
									Runtime.getRuntime().exec(command);
									System.exit(0);
								}
							} catch (IOException ex) {
								jLabelVersionCheck.setText("<html>Launching update to " + appVersion.getLatestVersion() + " has <b>failed</b>:"
										+ "<BR/>" + ex.getLocalizedMessage() + " </html>");
								lockUpdate = false;
							}
						}
					}.start();
				} else {
					lockUpdate = false;
				}
			}
		}
    }//GEN-LAST:event_jLabelVersionCheckMouseClicked

	private void enableVersionCheck(boolean enable) {
		jCheckBoxVersionPreRelease.setEnabled(enable);
		jButtonVersionCheck.setEnabled(enable);
	}

    private void jButtonVersionCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVersionCheckActionPerformed
		startVersionCheck();
    }//GEN-LAST:event_jButtonVersionCheckActionPerformed

	/**
	 *
	 */
	public class CleanupLog extends ProcessAbstract {

		/**
		 *
		 */
		public CleanupLog() {
			super("Thread.PanelOptions.CleanupLog");
		}

		/**
		 *
		 */
		@Override
		public void run() {
			try {
				File logPath = new File(Jamuz.getLogPath());
				if (logPath.exists() && logPath.isDirectory()) {
					ProgressBar progressBar = (ProgressBar) jProgressBarCleanupLogs;
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
								if (index > progressBar.getMaximum()) {
									progressBar.setMaximum(index + 10);
								}
								progressBar.progress(logFolder.getName());
								if (logFolder.list().length > 0) {
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
						long totalSize = 0;
						long totalDeleted = 0;

						long maxSize = Long.parseLong(Jamuz.getOptions().get("log.cleanup.keep.size.bytes", "2000000000"));
						int nbFoldersDeleted = 0;
						for (FolderInfo folderInfo : foldersInfo) {
							checkAbort();
							progressBar.progress(folderInfo.folder.getName());
							totalSize += folderInfo.size;
							//FIXME ZZZ GLOBAL Replace all System.out and .err by JaMuz.getLogger
							System.out.println(folderInfo.folder.getName()
									+ " (" + StringManager.humanReadableByteCount(
											folderInfo.size, true) + ")"
									+ " [" + StringManager.humanReadableByteCount(
											totalSize, true) + "]");
							if (totalSize > maxSize) {
								folderInfo.folder.delete();
								try {
									FileUtils.deleteDirectory(folderInfo.folder);
									totalDeleted += folderInfo.size;
									nbFoldersDeleted++;
								} catch (IOException ex) {
									Logger.getLogger(PanelOptions.class.getName()).log(Level.SEVERE, null, ex);
								}
							}
						}
						progressBar.reset();
						progressBar.setString("Cleanup complete : deleted "
								+ StringManager.humanReadableByteCount(totalDeleted, true)
								+ " (" + nbFoldersDeleted + "/" + foldersInfo.size() + ")");
					}
				}
			} catch (InterruptedException ex) {
				Popup.info("Aborted by user");
			}
		}

		class FolderInfo {

			public File folder;
			public long size;

			FolderInfo(File folder) {
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
    private javax.swing.JButton jButtonVersionCheck;
    private javax.swing.JCheckBox jCheckBoxVersionPreRelease;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabelBytes;
    private javax.swing.JLabel jLabelCleanup;
    private javax.swing.JLabel jLabelVersionCheck;
    private static javax.swing.JList jListGenres;
    private static javax.swing.JList jListMachines;
    private static javax.swing.JList jListTags;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelCleanupLogFolder;
    private javax.swing.JPanel jPanelOptionsGenres;
    private javax.swing.JPanel jPanelOptionsMachines;
    private javax.swing.JPanel jPanelOptionsTags;
    private javax.swing.JPanel jPanelResetCheckedFlag;
    private javax.swing.JPanel jPanelSaveFiles;
    private static javax.swing.JProgressBar jProgressBarCleanupLogs;
    private static javax.swing.JProgressBar jProgressBarResetChecked;
    private static javax.swing.JProgressBar jProgressBarSaveTags;
    private javax.swing.JProgressBar jProgressBarUpdate;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneOptionsMachines;
    private javax.swing.JScrollPane jScrollPaneOptionsMachines1;
    private javax.swing.JScrollPane jScrollPaneOptionsMachines2;
    private javax.swing.JSpinner jSpinnerBytes;
    private javax.swing.JTextPane jTextPaneShortcuts;
    // End of variables declaration//GEN-END:variables
}
