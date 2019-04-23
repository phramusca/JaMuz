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

package jamuz.process.check;

import jamuz.Jamuz;
import jamuz.gui.swing.ProgressBar;
import jamuz.utils.Inter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class PanelDuplicate extends javax.swing.JPanel {

	private CheckDisplay checkDisplay;
	
    /**
     * Creates new form PanelLyrics
     */
    public PanelDuplicate() {
        initComponents();
    }
    
	void init(FolderInfo folderInfo) {
		init(folderInfo, false);
	}
	
	void init(FolderInfo folderInfo, boolean analyse) {
		new Thread("Thread.PanelDuplicate.init") {
			@Override
			public void run() {
				if(analyse) {
					folderInfo.browse(false, true, (ProgressBar)jProgressBarCheckDialog);
					try {
						folderInfo.analyse((ProgressBar)jProgressBarCheckDialog);
						folderInfo.analyseMatch(0, (ProgressBar)jProgressBarCheckDialog); //Analyse first match
					} catch (CloneNotSupportedException ex) {
						Logger.getLogger(DialogDuplicate.class.getName()).log(Level.SEVERE, null, ex);
					}
					folderInfo.analyseMatchTracks();
					folderInfo.setAction(); 
					((ProgressBar)jProgressBarCheckDialog).reset();
				}

				checkDisplay = new CheckDisplay(folderInfo, (ProgressBar)jProgressBarCheckDialog, jCheckBoxCheckAlbumArtistDisplay, jCheckBoxCheckAlbumDisplay, jCheckBoxCheckArtistDisplay, jCheckBoxCheckBPMDisplay, jCheckBoxCheckBitRateDisplay, jCheckBoxCheckCommentDisplay, jCheckBoxCheckCoverDisplay, jCheckBoxCheckFormatDisplay, jCheckBoxCheckGenreDisplay, jCheckBoxCheckLengthDisplay, jCheckBoxCheckSizeDisplay, jCheckBoxCheckYearDisplay, jCheckCheckTitleDisplay, jLabelCheckAlbumArtistTag, jLabelCheckAlbumTag, jLabelCheckNbTracks, jLabelCheckYearTag, jLabelCheckDesc, jLabelCheckID3v1Tag, jLabelCheckMeanBitRateTag, jLabelCheckNbFiles, jLabelCheckReplayGainTag, jLabelCheckGenre, jLabelCoverInfo, jPanelCheckCoverThumb, jScrollPaneCheckTags, jTableCheck);
				checkDisplay.initExtended();
			}
		}.start();
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
        jPanel8 = new javax.swing.JPanel();
        jCheckBoxCheckAlbumDisplay = new javax.swing.JCheckBox();
        jCheckBoxCheckAlbumArtistDisplay = new javax.swing.JCheckBox();
        jLabelCheckAlbumArtistTag = new javax.swing.JLabel();
        jLabelCheckAlbumTag = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jCheckBoxCheckYearDisplay = new javax.swing.JCheckBox();
        jLabelCheckGenre = new javax.swing.JLabel();
        jCheckBoxCheckGenreDisplay = new javax.swing.JCheckBox();
        jLabelCheckYearTag = new javax.swing.JLabel();
        jLabelCheckNbTracks = new javax.swing.JLabel();
        jLabelCheckNbFiles = new javax.swing.JLabel();
        jCheckBoxCheckArtistDisplay = new javax.swing.JCheckBox();
        jCheckCheckTitleDisplay = new javax.swing.JCheckBox();
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
        jPanel6 = new javax.swing.JPanel();
        jLabelCoverInfo = new javax.swing.JLabel();
        jCheckBoxCheckCoverDisplay = new javax.swing.JCheckBox();
        jLabelCheckDesc = new javax.swing.JLabel();
        jScrollPaneCheckTags = new javax.swing.JScrollPane();
        jTableCheck = new jamuz.gui.swing.TableHorizontal();
        jProgressBarCheckDialog = new jamuz.gui.swing.ProgressBar();

        jPanelCheckCoverThumb.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelCheckCoverThumb.setMaximumSize(new java.awt.Dimension(150, 150));
        jPanelCheckCoverThumb.setMinimumSize(new java.awt.Dimension(150, 150));

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

        jPanel8.setBackground(java.awt.Color.lightGray);
        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelCheckAlbumArtistTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckAlbumArtistTag.setText(" "); // NOI18N
        jLabelCheckAlbumArtistTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckAlbumArtistTag.setOpaque(true);

        jLabelCheckAlbumTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckAlbumTag.setText(" "); // NOI18N
        jLabelCheckAlbumTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckAlbumTag.setOpaque(true);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jCheckBoxCheckAlbumDisplay)
                        .addGap(1, 1, 1)
                        .addComponent(jLabelCheckAlbumTag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jCheckBoxCheckAlbumArtistDisplay)
                        .addGap(2, 2, 2)
                        .addComponent(jLabelCheckAlbumArtistTag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxCheckAlbumArtistDisplay, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabelCheckAlbumArtistTag, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxCheckAlbumDisplay)
                    .addComponent(jLabelCheckAlbumTag))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(java.awt.Color.lightGray);
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jCheckBoxCheckYearDisplay.setText(bundle.getString("Tag.Year")); // NOI18N

        jLabelCheckGenre.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckGenre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckGenre.setText("Genre"); // NOI18N
        jLabelCheckGenre.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckGenre.setOpaque(true);

        jCheckBoxCheckGenreDisplay.setText(bundle.getString("Tag.Genre")); // NOI18N

        jLabelCheckYearTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckYearTag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckYearTag.setText("9999"); // NOI18N
        jLabelCheckYearTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckYearTag.setOpaque(true);

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
        jCheckBoxCheckArtistDisplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxCheckArtistDisplayActionPerformed(evt);
            }
        });

        jCheckCheckTitleDisplay.setText(bundle.getString("Tag.Title")); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxCheckGenreDisplay)
                    .addComponent(jCheckBoxCheckYearDisplay))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabelCheckYearTag, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jCheckBoxCheckArtistDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jCheckCheckTitleDisplay)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCheckNbTracks, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelCheckNbFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelCheckGenre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 12, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxCheckYearDisplay, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCheckYearTag)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelCheckNbTracks)
                        .addComponent(jLabelCheckNbFiles)
                        .addComponent(jCheckBoxCheckArtistDisplay)
                        .addComponent(jCheckCheckTitleDisplay)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxCheckGenreDisplay)
                    .addComponent(jLabelCheckGenre))
                .addContainerGap())
        );

        jPanel4.setBackground(java.awt.Color.lightGray);
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jCheckBoxCheckFormatDisplay.setText(bundle.getString("Tag.Format")); // NOI18N

        jCheckBoxCheckLengthDisplay.setText(bundle.getString("Tag.Length")); // NOI18N

        jCheckBoxCheckSizeDisplay.setText(bundle.getString("Tag.Size")); // NOI18N

        jCheckBoxCheckBitRateDisplay.setText(bundle.getString("Tag.BitRate")); // NOI18N

        jLabelCheckMeanBitRateTag.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckMeanBitRateTag.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCheckMeanBitRateTag.setText("999"); // NOI18N
        jLabelCheckMeanBitRateTag.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckMeanBitRateTag.setOpaque(true);

        jCheckBoxCheckBPMDisplay.setText(bundle.getString("Tag.BPM")); // NOI18N

        jCheckBoxCheckCommentDisplay.setText(bundle.getString("Tag.Comment")); // NOI18N

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
                    .addComponent(jCheckBoxCheckCommentDisplay)))
        );

        jPanel6.setBackground(java.awt.Color.lightGray);
        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelCoverInfo.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCoverInfo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCoverInfo.setText("Cover info"); // NOI18N
        jLabelCoverInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCoverInfo.setOpaque(true);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jCheckBoxCheckCoverDisplay)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCoverInfo, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jCheckBoxCheckCoverDisplay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelCoverInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jLabelCheckDesc.setBackground(new java.awt.Color(255, 255, 255));
        jLabelCheckDesc.setText(" "); // NOI18N
        jLabelCheckDesc.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabelCheckDesc.setOpaque(true);

        jTableCheck.setAutoCreateColumnsFromModel(false);
        jTableCheck.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTableCheck.setAutoscrolls(false);
        jScrollPaneCheckTags.setViewportView(jTableCheck);

        jProgressBarCheckDialog.setString("null");
        jProgressBarCheckDialog.setStringPainted(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPaneCheckTags)
                    .addComponent(jLabelCheckDesc, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanelCheckCoverThumb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jProgressBarCheckDialog, javax.swing.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jProgressBarCheckDialog, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelCheckCoverThumb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCheckDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPaneCheckTags, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jCheckBoxCheckArtistDisplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxCheckArtistDisplayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jCheckBoxCheckArtistDisplayActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheckBoxCheckAlbumArtistDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckAlbumDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckArtistDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckBPMDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckBitRateDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckCommentDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckCoverDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckFormatDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckGenreDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckLengthDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckSizeDisplay;
    private javax.swing.JCheckBox jCheckBoxCheckYearDisplay;
    private javax.swing.JCheckBox jCheckCheckTitleDisplay;
    private javax.swing.JLabel jLabelCheckAlbumArtistTag;
    private javax.swing.JLabel jLabelCheckAlbumTag;
    private javax.swing.JLabel jLabelCheckDesc;
    private javax.swing.JLabel jLabelCheckGenre;
    private javax.swing.JLabel jLabelCheckID3v1;
    private javax.swing.JLabel jLabelCheckID3v1Tag;
    private javax.swing.JLabel jLabelCheckMeanBitRateTag;
    private javax.swing.JLabel jLabelCheckNbFiles;
    private javax.swing.JLabel jLabelCheckNbTracks;
    private javax.swing.JLabel jLabelCheckReplayGain;
    private javax.swing.JLabel jLabelCheckReplayGainTag;
    private javax.swing.JLabel jLabelCheckYearTag;
    private javax.swing.JLabel jLabelCoverInfo;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanelCheckCoverThumb;
    private javax.swing.JProgressBar jProgressBarCheckDialog;
    private javax.swing.JScrollPane jScrollPaneCheckTags;
    private javax.swing.JTable jTableCheck;
    // End of variables declaration//GEN-END:variables

	
}
