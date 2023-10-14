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

package jamuz.gui;

import jamuz.FileInfoInt;
import jamuz.IconBuffer;
import jamuz.Jamuz;
import jamuz.gui.swing.WrapLayout;
import jamuz.utils.Inter;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogTag extends javax.swing.JDialog {

    private static FileInfoInt file;

    /**
     * Creates new form PanelDialogCheck
     * @param parent
     * @param modal
     * @param file
	 * @param playlistInfo
	 * @param currentPosition
     */
    public DialogTag(java.awt.Frame parent, boolean modal, FileInfoInt file) {
        super(parent, modal);
        initComponents();
		DialogTag.file = file;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				cancel();
			}
		});
        initExtended();
    }

     /**
	 * @param parent
     * @param file 
	 * @param playlistInfo 
	 * @param currentPosition 
	 */
	public static void main(Frame parent, FileInfoInt file) {
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

        DialogTag dialog = new DialogTag(parent, true, file);
        dialog.setLocationRelativeTo(parent);
        dialog.setTitle(file.getArtist().concat(" | ").concat(file.getTitle()).concat(" | (").concat(file.getAlbum()).concat(")"));
        dialog.setVisible(true);
	}
    
    private class TagActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String tag = e.getActionCommand();
            file.toggleTag(tag);
            highlightTag(tag, file.getTags().contains(tag));
        }
    }

	/**
	 *
	 */
	public class Tag {
        private final int id;
        private final String value;

		/**
		 *
		 * @param id
		 * @param value
		 */
		public Tag(int id, String value) {
            this.id = id;
            this.value = value;
        }

		/**
		 *
		 * @return
		 */
		public int getId() {
            return id;
        }

		/**
		 *
		 * @return
		 */
		public String getValue() {
            return value;
        }
        
        
    }
    
    private void initExtended() {
		  
        //Filling genres list
        ActionListener actionListener = new TagActionListener();
        jPanelTag.setLayout(new WrapLayout(FlowLayout.LEADING, 0, 0));
        for(String tag : Jamuz.getTags()) {
            ImageIcon icon = IconBuffer.getCoverIcon(tag, IconBuffer.IconVersion.NORMAL_30, "tag");
            JButton button= new JButton(tag);
            if(icon!=null) {
                button = new JButton(icon);
                button.setText(tag); //TODO: Display as an option
            }
            button.setActionCommand(tag);
            button.addActionListener(actionListener);
            button.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); //Counter clockwise
            button.setBackground(Color.LIGHT_GRAY);
            jPanelTag.add(button);
        }
        jPanelTag.validate();
        jPanelTag.repaint();
        
        highlightTags();
	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTag = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelTag.setBackground(java.awt.Color.lightGray);
        jPanelTag.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelTagLayout = new javax.swing.GroupLayout(jPanelTag);
        jPanelTag.setLayout(jPanelTagLayout);
        jPanelTagLayout.setHorizontalGroup(
            jPanelTagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );
        jPanelTagLayout.setVerticalGroup(
            jPanelTagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 165, Short.MAX_VALUE)
        );

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/accept.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("inter/Bundle"); // NOI18N
        jButtonSave.setText(bundle.getString("Button.Save")); // NOI18N
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/cancel.png"))); // NOI18N
        jButtonCancel.setText(Inter.get("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelTag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonCancel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSave)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelTag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonCancel))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        file.setTags(getHighlightedTags());
		ArrayList<FileInfoInt> temp = new ArrayList<>();
		temp.add(file);
		Jamuz.getDb().updateFileTags(temp, null);
        displayAndDispose();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
        //FIXME Z This does not seem to cancel anything !!?
		cancel();
    }//GEN-LAST:event_jButtonCancelActionPerformed

	private void cancel() {
		file.readTags();
		displayAndDispose();
	}
	
	private void displayAndDispose() {
		PanelMain.displayTags();
        dispose();
	}
	
	/**
	 *
	 * @return
	 */
	public static ArrayList<String> getHighlightedTags() {
        ArrayList<String> tags = new ArrayList<>();
        Component[] components = jPanelTag.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if(button.getBackground().equals(Color.LIGHT_GRAY)) {
                    tags.add(button.getActionCommand());
                }
            }
        }
        return tags;
    }
    
	/**
	 *
	 */
	public static void highlightTags() {
        Component[] components = jPanelTag.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if(file.getTags().contains(button.getActionCommand())) {
                    button.setBackground(Color.LIGHT_GRAY);
                    button.setIcon(IconBuffer.getCoverIcon(button.getActionCommand(), IconBuffer.IconVersion.NORMAL_30, "tag"));
                }
                else {
                    button.setBackground(Color.DARK_GRAY);
                    button.setIcon(IconBuffer.getCoverIcon(button.getActionCommand(), IconBuffer.IconVersion.GRAY_30, "tag"));
                }
            }
        }
    }
    
	/**
	 *
	 * @param tag
	 * @param highlight
	 */
	public static void highlightTag(String tag, boolean highlight) {
        Component[] components = jPanelTag.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                if(button.getActionCommand().equals(tag)) {
                    button.setBackground(highlight?Color.LIGHT_GRAY:Color.DARK_GRAY);
                    button.setIcon(IconBuffer.getCoverIcon(button.getActionCommand(), 
                            highlight?IconBuffer.IconVersion.NORMAL_30:IconBuffer.IconVersion.GRAY_30, "tag"));
                    return;
                }
            }
        }
    }
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private static javax.swing.JPanel jPanelTag;
    // End of variables declaration//GEN-END:variables
}
