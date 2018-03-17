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
package jamuz.process.video;

import java.util.List;
import javax.swing.JSpinner;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogVideoCleanup extends javax.swing.JDialog {

	private List<VideoAbstract> files;
	
	/**
	 * Creates new form DialogCleanup
	 * @param parent
	 * @param modal
	 * @param files
	 */
	public DialogVideoCleanup(java.awt.Frame parent, boolean modal, List<VideoAbstract> files) {
		super(parent, modal);
		initComponents();
		
		((JSpinner.DefaultEditor) jSpinnerVideoCleanupNbSeasonToKeep.getEditor()).getTextField().setEditable(false);
		((JSpinner.DefaultEditor) jSpinnerVideoCleanupNbEpisodeToKeep.getEditor()).getTextField().setEditable(false);
		this.files = files;
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
        jCheckBoxCleanupAllTvShows = new javax.swing.JCheckBox();
        jSpinnerVideoCleanupNbSeasonToKeep = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jSpinnerVideoCleanupNbEpisodeToKeep = new javax.swing.JSpinner();
        jLabel4 = new javax.swing.JLabel();
        jCheckBoxCleanupAllMovies1 = new javax.swing.JCheckBox();
        jCheckBoxCleanupKeepEnded = new javax.swing.JCheckBox();
        jCheckBoxCleanupKeepCanceled = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jCheckBoxCleanupAllMovies = new javax.swing.JCheckBox();
        jButtonVidecoConfirmCleanup = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("TV Shows"));

        jCheckBoxCleanupAllTvShows.setText("Keep all episodes");
        jCheckBoxCleanupAllTvShows.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCleanupAllTvShowsItemStateChanged(evt);
            }
        });

        jSpinnerVideoCleanupNbSeasonToKeep.setModel(new javax.swing.SpinnerNumberModel(0, 0, null, 1));
        jSpinnerVideoCleanupNbSeasonToKeep.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinnerVideoCleanupNbSeasonToKeepStateChanged(evt);
            }
        });

        jLabel1.setText("previous watched SEASON(s)");

        jLabel2.setText("Keep");

        jLabel3.setText("Keep");

        jSpinnerVideoCleanupNbEpisodeToKeep.setModel(new javax.swing.SpinnerNumberModel(1, 0, null, 1));

        jLabel4.setText("watched EPISODE(s) in current season");

        jCheckBoxCleanupAllMovies1.setSelected(true);
        jCheckBoxCleanupAllMovies1.setText("Keep all unwatched episodes");
        jCheckBoxCleanupAllMovies1.setEnabled(false);

        jCheckBoxCleanupKeepEnded.setSelected(true);
        jCheckBoxCleanupKeepEnded.setText("Keep ended series");

        jCheckBoxCleanupKeepCanceled.setText("Keep canceled series");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBoxCleanupAllTvShows)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSpinnerVideoCleanupNbEpisodeToKeep, javax.swing.GroupLayout.DEFAULT_SIZE, 55, Short.MAX_VALUE)
                            .addComponent(jSpinnerVideoCleanupNbSeasonToKeep))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jCheckBoxCleanupAllMovies1)
                    .addComponent(jCheckBoxCleanupKeepEnded)
                    .addComponent(jCheckBoxCleanupKeepCanceled))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxCleanupAllTvShows)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jSpinnerVideoCleanupNbSeasonToKeep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jSpinnerVideoCleanupNbEpisodeToKeep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxCleanupKeepEnded)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxCleanupKeepCanceled)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBoxCleanupAllMovies1)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Films"));

        jCheckBoxCleanupAllMovies.setSelected(true);
        jCheckBoxCleanupAllMovies.setText("Keep all movies");
        jCheckBoxCleanupAllMovies.setEnabled(false);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheckBoxCleanupAllMovies)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jCheckBoxCleanupAllMovies)
                .addGap(0, 12, Short.MAX_VALUE))
        );

        jButtonVidecoConfirmCleanup.setText("Cleanup ...");
        jButtonVidecoConfirmCleanup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVidecoConfirmCleanupActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonVidecoConfirmCleanup))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonVidecoConfirmCleanup)
                    .addComponent(cancelButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void jButtonVidecoConfirmCleanupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVidecoConfirmCleanupActionPerformed
		if(jCheckBoxCleanupAllMovies.isSelected() && jCheckBoxCleanupAllTvShows.isSelected()) {
			dispose();
		} else if(!jCheckBoxCleanupAllTvShows.isSelected()) {
			DialogVideoCleanupConfirm.main(files, 
					(Integer)jSpinnerVideoCleanupNbSeasonToKeep.getValue(),
					(Integer)jSpinnerVideoCleanupNbEpisodeToKeep.getValue(),
					jCheckBoxCleanupKeepEnded.isSelected(), jCheckBoxCleanupKeepCanceled.isSelected());
		}
    }//GEN-LAST:event_jButtonVidecoConfirmCleanupActionPerformed

    private void jCheckBoxCleanupAllTvShowsItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCleanupAllTvShowsItemStateChanged
        jSpinnerVideoCleanupNbSeasonToKeep.setEnabled(!jCheckBoxCleanupAllTvShows.isSelected());
		jCheckBoxCleanupKeepEnded.setEnabled(!jCheckBoxCleanupAllTvShows.isSelected());
		jCheckBoxCleanupKeepCanceled.setEnabled(!jCheckBoxCleanupAllTvShows.isSelected());
		
		if(!jCheckBoxCleanupAllTvShows.isSelected() 
				&& ((Integer)jSpinnerVideoCleanupNbSeasonToKeep.getValue())<=0) {
			jSpinnerVideoCleanupNbEpisodeToKeep.setEnabled(true);
		} else {
			jSpinnerVideoCleanupNbEpisodeToKeep.setEnabled(false);
		}
    }//GEN-LAST:event_jCheckBoxCleanupAllTvShowsItemStateChanged

    private void jSpinnerVideoCleanupNbSeasonToKeepStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinnerVideoCleanupNbSeasonToKeepStateChanged
        jSpinnerVideoCleanupNbEpisodeToKeep.setEnabled(
				((Integer)jSpinnerVideoCleanupNbSeasonToKeep.getValue())<=0);
    }//GEN-LAST:event_jSpinnerVideoCleanupNbSeasonToKeepStateChanged

	/**
	 * @param files
	 */
	public static void main(List<VideoAbstract> files) {
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
			java.util.logging.Logger.getLogger(DialogVideoCleanup.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>
		
		/* Create and display the dialog */
		java.awt.EventQueue.invokeLater(() -> {
			DialogVideoCleanup dialog = new DialogVideoCleanup(
					new javax.swing.JFrame(), true, files);
			//Center the dialog
			dialog.setLocationRelativeTo(dialog.getParent());
			dialog.setVisible(true);
		});
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton jButtonVidecoConfirmCleanup;
    private javax.swing.JCheckBox jCheckBoxCleanupAllMovies;
    private javax.swing.JCheckBox jCheckBoxCleanupAllMovies1;
    private javax.swing.JCheckBox jCheckBoxCleanupAllTvShows;
    private javax.swing.JCheckBox jCheckBoxCleanupKeepCanceled;
    private javax.swing.JCheckBox jCheckBoxCleanupKeepEnded;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSpinner jSpinnerVideoCleanupNbEpisodeToKeep;
    private javax.swing.JSpinner jSpinnerVideoCleanupNbSeasonToKeep;
    // End of variables declaration//GEN-END:variables
}
