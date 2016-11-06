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
import jamuz.Jamuz;
import jamuz.IconBuffer;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import jamuz.gui.swing.WrapLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
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
     */
    public DialogTag(java.awt.Frame parent, boolean modal, FileInfoInt file) {
        super(parent, modal);
        initComponents();
        DialogTag.file = file;
        initExtended();
    }

     /**
     * @param file 
	 */
	public static void main(FileInfoInt file) {
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

        DialogTag dialog = new DialogTag(new JFrame(), true, file);
        //Center the dialog on screen
        dialog.setLocationRelativeTo(dialog.getParent());
        //Change title
        //TODO: Color in red words not found in match and in green the ones found 
        dialog.setTitle(file.getArtist().concat(" | ").concat(file.getTitle()).concat(" | (").concat(file.getAlbum()).concat(")"));
        //Display
        dialog.setVisible(true);
	}
    
    private class GenreButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String tag = e.getActionCommand();
			
			//FIXME: This is confusing as if we do not save (by closing window), the tags
			//appear to be though changed while it is not saved in database
			// = > add a "Cancel" button that revert changes
			// = > Revert when window is closed using classic top-right button
			// = > Make sure NOT to revert on OK (disposing dialog there too ...)
            file.toggleTag(tag);
            highlightTag(tag, file.getTags().contains(tag));
        }
    }

    public class Tag {
        private final int id;
        private final String value;

        public Tag(int id, String value) {
            this.id = id;
            this.value = value;
        }

        public int getId() {
            return id;
        }

        public String getValue() {
            return value;
        }
        
        
    }
    
    private void initExtended() {
		  
        //Filling genres list
        ActionListener actionListener = new GenreButtonListener();
        jPanelGenre.setLayout(new WrapLayout(FlowLayout.LEADING, 0, 0));
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
            jPanelGenre.add(button);
        }
        jPanelGenre.validate();
        jPanelGenre.repaint();
        
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

        jPanelGenre = new javax.swing.JPanel();
        jButtonSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanelGenre.setBackground(java.awt.Color.lightGray);
        jPanelGenre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanelGenreLayout = new javax.swing.GroupLayout(jPanelGenre);
        jPanelGenre.setLayout(jPanelGenreLayout);
        jPanelGenreLayout.setHorizontalGroup(
            jPanelGenreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );
        jPanelGenreLayout.setVerticalGroup(
            jPanelGenreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 165, Short.MAX_VALUE)
        );

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        jButtonSave.setText(bundle.getString("Button.Save")); // NOI18N
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelGenre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonSave)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelGenre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSave)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        Jamuz.getDb().deleteTagFiles(file.getIdFile());
		Jamuz.getDb().insertTagFiles(getHighlightedTags(), file.getIdFile());
        PanelMain.displayFileInfo();
        //Close this GUI
        this.dispose();
    }//GEN-LAST:event_jButtonSaveActionPerformed

    public static ArrayList<String> getHighlightedTags() {
        ArrayList<String> tags = new ArrayList<>();
        Component[] components = jPanelGenre.getComponents();
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
    
    public static void highlightTags() {
        Component[] components = jPanelGenre.getComponents();
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
    
    public static void highlightTag(String tag, boolean highlight) {
        Component[] components = jPanelGenre.getComponents();
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
    private javax.swing.JButton jButtonSave;
    private static javax.swing.JPanel jPanelGenre;
    // End of variables declaration//GEN-END:variables
}
