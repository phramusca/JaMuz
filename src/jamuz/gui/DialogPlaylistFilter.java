/*
 * Copyright (C) 2011 phramusca ( https://github.com/phramusca/JaMuz/ )
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

import jamuz.Playlist;
import jamuz.process.check.FolderInfo.CheckedFlag;
import jamuz.Jamuz;
import jamuz.Playlist.Field;
import static jamuz.Playlist.Field.ALBUM;
import static jamuz.Playlist.Field.ALBUMARTIST;
import static jamuz.Playlist.Field.ARTIST;
import static jamuz.Playlist.Field.GENRE;
import static jamuz.Playlist.Field.LASTPLAYED;
import static jamuz.Playlist.Field.PLAYCOUNTER;
import static jamuz.Playlist.Field.PLAYLIST;
import static jamuz.Playlist.Field.RATING;
import static jamuz.Playlist.Field.TITLE;
import static jamuz.Playlist.Field.TRACKNO;
import static jamuz.Playlist.Field.YEAR;
import jamuz.Playlist.Filter;
import jamuz.process.check.FolderInfo.CopyRight;
import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.util.Arrays;
import javax.swing.DefaultComboBoxModel;

/**
 * JDialog extension to add/modify Stat source
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class DialogPlaylistFilter extends javax.swing.JDialog {

    //TODO: Replace current filters by the one in PanelSelect
    //then replace filters in PanelSelect by a Playlist selection combo
    
    
	private final Filter filter;
	private final int filterIndex;
	private final Playlist playlist;
	
	/** Creates new form StatSourceGUI
	 * @param parent
	 * @param modal  
     * @param playlist  
     * @param filterIndex  
	 */
    public DialogPlaylistFilter(java.awt.Frame parent, boolean modal, Playlist playlist, int filterIndex) {
        super(parent, modal);
        initComponents();

		//TODO: Find a better way of resizing (as 2 controls are always hidden)
		Dimension dim = this.getSize();
		dim.height = dim.height-20-20-20-6-6-6;
		this.setSize(dim);

        jTextFieldPlayslitValue.setVisible(false);
        jComboBoxPlaylistFilter.setVisible(false);
        jSpinnerPlaylistFilter.setVisible(false);
        jComboBoxCheckedFlagFilter.setVisible(false);
		jComboBoxCopyRightFilter.setVisible(false);
        
        this.playlist = playlist;
		this.filterIndex = filterIndex;
        if(filterIndex>-1) {
            this.filter = playlist.getFilters().get(filterIndex);
        }
        else {
            this.filter = new Filter(-1, Field.GENRE, Playlist.Operator.IS, ""); //NOI18N
        }
        
		//Fill combo boxes
        jComboBoxPlaylistFilter.setModel(new DefaultComboBoxModel(Jamuz.getPlaylists().toArray()));
        DefaultComboBoxModel modelFilter = (DefaultComboBoxModel) jComboBoxPlaylistFilter.getModel();
        modelFilter.removeElement(playlist);
        
        DefaultComboBoxModel modelField = new DefaultComboBoxModel(Field.values());
//        modelField=new DefaultComboBoxModel(Field.values());
        jComboBoxPlaylistField.setModel(modelField);

		jComboBoxCheckedFlagFilter.setModel(new DefaultComboBoxModel(CheckedFlag.values()));
		
		jComboBoxCopyRightFilter.setModel(new DefaultComboBoxModel(CopyRight.values()));
		
		//Set field
//		int indexField = Arrays.asList(Field.values()).indexOf(this.filter.getFieldName());
        int indexField = modelField.getIndexOf(this.filter.getField());
		jComboBoxPlaylistField.setSelectedIndex(indexField);
		
		//Set operator (as model changes when field changes, need to get id from that model)
		DefaultComboBoxModel modelOperator = (DefaultComboBoxModel) jComboBoxPlaylistOperator.getModel();
		int indexOperator = modelOperator.getIndexOf(this.filter.getOperator());
		jComboBoxPlaylistOperator.setSelectedIndex(indexOperator);
		
		//Set value
		switch (this.filter.getField()) {
            case GENRE:
            case ALBUM:
            case ARTIST:
            case ALBUMARTIST:
            case TITLE:
			case TAG:
            case YEAR: 
            case LASTPLAYED:
				jTextFieldPlayslitValue.setText(this.filter.getValue());
				break;

            case RATING:
            case ALBUMRATING:
            case PERCENTRATED:
            case TRACKNO:
            case PLAYCOUNTER:
	            jSpinnerPlaylistFilter.setValue(Double.valueOf(this.filter.getValue()));
		        break;

            case PLAYLIST:
				jComboBoxPlaylistFilter.setSelectedItem(this.filter.getValue());
				break;
				
			case CHECKEDFLAG:
				jComboBoxCheckedFlagFilter.setSelectedIndex(Integer.valueOf(this.filter.getValue()));
				break;
				
			case COPYRIGHT:
				jComboBoxCopyRightFilter.setSelectedIndex(Integer.valueOf(this.filter.getValue()));
				break;
        }
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButtonCancel = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();
        jSpinnerPlaylistFilter = new javax.swing.JSpinner();
        jComboBoxPlaylistFilter = new javax.swing.JComboBox();
        jTextFieldPlayslitValue = new javax.swing.JTextField();
        jComboBoxPlaylistOperator = new javax.swing.JComboBox();
        jComboBoxPlaylistField = new javax.swing.JComboBox();
        jComboBoxCheckedFlagFilter = new javax.swing.JComboBox();
        jComboBoxCopyRightFilter = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("jamuz/Bundle"); // NOI18N
        setTitle(bundle.getString("DialogPlaylistFilter.title")); // NOI18N
        setModal(true);
        setResizable(false);

        jButtonCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/cancel.png"))); // NOI18N
        jButtonCancel.setText(bundle.getString("Button.Cancel")); // NOI18N
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });

        jButtonSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/jamuz/ressources/accept.png"))); // NOI18N
        jButtonSave.setText(bundle.getString("Button.Save")); // NOI18N
        jButtonSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSaveActionPerformed(evt);
            }
        });

        jSpinnerPlaylistFilter.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 5.0d, 0.1d));

        jComboBoxPlaylistFilter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Playlist sample 1", "Playlist sample 2", "Playlist sample 3", "Playlist sample 4" }));

        jComboBoxPlaylistOperator.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBoxPlaylistField.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBoxPlaylistField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPlaylistFieldActionPerformed(evt);
            }
        });

        jComboBoxCheckedFlagFilter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-1", "0", "1", "2", "3", "4" }));

        jComboBoxCopyRightFilter.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-1", "0", "1", "2", "3", "4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxPlaylistFilter, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTextFieldPlayslitValue, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jComboBoxPlaylistField, 0, 134, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxPlaylistOperator, 0, 161, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButtonCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonSave))
                    .addComponent(jSpinnerPlaylistFilter, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBoxCheckedFlagFilter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxCopyRightFilter, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxPlaylistField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxPlaylistOperator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldPlayslitValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxPlaylistFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxCheckedFlagFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBoxCopyRightFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addComponent(jSpinnerPlaylistFilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonSave)
                    .addComponent(jButtonCancel))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        boolean doSave = true;
        //Get field
		Field field = (Field)jComboBoxPlaylistField.getSelectedItem();
		//Get value
		String value=""; //NOI18N
		switch (field) {
			case GENRE:
			case ALBUM:
			case ARTIST:
			case ALBUMARTIST:
			case TITLE:
			case TAG:
			case YEAR:
			case LASTPLAYED:
            case ADDEDDATE:
				value = jTextFieldPlayslitValue.getText();
				break;
				
			case RATING:
            case ALBUMRATING:
            case PERCENTRATED:
			case TRACKNO:
			case PLAYCOUNTER:
				value=jSpinnerPlaylistFilter.getValue().toString();
				break;
				
			case PLAYLIST:
				Playlist playlistValue = (Playlist) jComboBoxPlaylistFilter.getSelectedItem();
				if(playlistValue!=null) {
                    value = String.valueOf(playlistValue.getId());
                }
                else {
                    doSave=false;
                }
				break;
                
            case CHECKEDFLAG:
				CheckedFlag checkedFlag = (CheckedFlag) jComboBoxCheckedFlagFilter.getSelectedItem();
				value = String.valueOf(checkedFlag.getValue());
				break;
				
			case COPYRIGHT:
				CopyRight copyRight = (CopyRight) jComboBoxCopyRightFilter.getSelectedItem();
				value = String.valueOf(copyRight.getValue());
				break;
		}

        if(doSave) {
            //Update filter
            this.filter.setField(field);
            this.filter.setOperator((Playlist.Operator)jComboBoxPlaylistOperator.getSelectedItem());
            this.filter.setValue(value);

            if(this.filterIndex>-1) {
                this.playlist.setFilter(this.filterIndex, this.filter);
            }
            else {
                this.playlist.addFilter(this.filter);
            }

            //Display filters in list
            PanelPlaylists.displayFilters(this.playlist);
            PanelPlaylists.fillPlayList();
            //Close this GUI
            this.dispose();
        }
	}//GEN-LAST:event_jButtonSaveActionPerformed

	private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelActionPerformed
		this.dispose();
	}//GEN-LAST:event_jButtonCancelActionPerformed

    private void jComboBoxPlaylistFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPlaylistFieldActionPerformed
        Field field = (Field) jComboBoxPlaylistField.getSelectedItem();
		Playlist.Operator[] operatorsToDisplay= new Playlist.Operator[]{};
        
        //Selecting opertors to display according to field value
        if(field!=null) { //At panel load, none is selected ...
            
            switch(field) {
                case ALBUMRATING:
                    jSpinnerPlaylistFilter.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 5.0d, 0.1d));
                    break;
                case PERCENTRATED:
                    jSpinnerPlaylistFilter.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 1));
                    break;
                case TRACKNO:
                case PLAYCOUNTER:
                    jSpinnerPlaylistFilter.setModel(new javax.swing.SpinnerNumberModel());
                    break;
                case RATING:
                    jSpinnerPlaylistFilter.setModel(new javax.swing.SpinnerNumberModel(0, 0, 5, 1));
                    break;
            }
			
			jTextFieldPlayslitValue.setVisible(false);    
			jSpinnerPlaylistFilter.setVisible(false);
			jComboBoxCheckedFlagFilter.setVisible(false);
			jComboBoxCopyRightFilter.setVisible(false);
			jComboBoxPlaylistFilter.setVisible(false);
            
            switch (field) {
                case GENRE:
                case ALBUM:
                case ARTIST:
                case ALBUMARTIST:
                case TITLE:
				case TAG:
                    //String values
                    jTextFieldPlayslitValue.setVisible(true);
                    operatorsToDisplay = new Playlist.Operator[]{
                        Playlist.Operator.CONTAINS,
                        Playlist.Operator.DOESNOTCONTAIN,
                        Playlist.Operator.IS,
                        Playlist.Operator.ISNOT,
                        Playlist.Operator.STARTSWITH,
                        Playlist.Operator.ENDSWITH
                    };
                    break;

                case YEAR: //TODO: Use a Formatted text field (YYYY format) or a validator
                    jTextFieldPlayslitValue.setVisible(true);
                    operatorsToDisplay = new Playlist.Operator[]{
                        Playlist.Operator.IS,
                        Playlist.Operator.ISNOT,
                        Playlist.Operator.STARTSWITH,
                        Playlist.Operator.ENDSWITH,
                        Playlist.Operator.DATELESSTHAN,
                        Playlist.Operator.DATEGREATERTHAN
                    };
                    break;

                case LASTPLAYED: //TODO: Use a date picker
                case ADDEDDATE:
                    jTextFieldPlayslitValue.setVisible(true);
                    operatorsToDisplay = new Playlist.Operator[]{
                        Playlist.Operator.IS,
                        Playlist.Operator.ISNOT,
                        Playlist.Operator.DATELESSTHAN,
                        Playlist.Operator.DATEGREATERTHAN
                    };
                    break;

                case RATING:
                case ALBUMRATING:
                case PERCENTRATED:
                case TRACKNO:
                case PLAYCOUNTER:
                    //Numeric values
                    jTextFieldPlayslitValue.setVisible(false);
                    jComboBoxPlaylistFilter.setVisible(false);
                    jSpinnerPlaylistFilter.setVisible(true);
                    jComboBoxCheckedFlagFilter.setVisible(false);
					jComboBoxCopyRightFilter.setVisible(false);
                    operatorsToDisplay = new Playlist.Operator[]{
                        Playlist.Operator.NUMIS,
                        Playlist.Operator.NUMISNOT,
                        Playlist.Operator.LESSTHAN,
                        Playlist.Operator.GREATERTHAN
                    };
                    break;

                case PLAYLIST:
					jComboBoxPlaylistFilter.setVisible(true);
                    operatorsToDisplay = new Playlist.Operator[]{
                        Playlist.Operator.IS,
                        Playlist.Operator.ISNOT
                    };
                    break;

                case CHECKEDFLAG:
                    jComboBoxCheckedFlagFilter.setVisible(true);
                    operatorsToDisplay = new Playlist.Operator[]{
                        Playlist.Operator.IS,
                        Playlist.Operator.ISNOT,
                        Playlist.Operator.LESSTHAN,
                        Playlist.Operator.GREATERTHAN
                    };
                    break;
					
				case COPYRIGHT:
					jComboBoxCopyRightFilter.setVisible(true);
                    operatorsToDisplay = new Playlist.Operator[]{
                        Playlist.Operator.IS,
                        Playlist.Operator.ISNOT
                    };
                    break;
            }
        }

        jComboBoxPlaylistOperator.setModel(new DefaultComboBoxModel(operatorsToDisplay));
    }//GEN-LAST:event_jComboBoxPlaylistFieldActionPerformed

    /**
	 * Open the GUI 
     * @param playlist
     * @param filterIndex
     * @param isInde
	 */
    public static void main(final Playlist playlist, final int filterIndex, final boolean isInde) {
        java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
            public void run() {

                DialogPlaylistFilter dialog = new DialogPlaylistFilter(new javax.swing.JFrame(), true, playlist, filterIndex);
				
                //Only playlist can be selected if match is INDE
                if (isInde) {
                    jComboBoxPlaylistField.setSelectedItem(Field.PLAYLIST);
                    jComboBoxPlaylistField.setEnabled(false);
                }
                
				//Set dialog to mouse location
				PointerInfo a = MouseInfo.getPointerInfo();
				Point b  = a.getLocation();
				int x = (int)b.getX();
				int y = (int)b.getY();
				//size of the screen
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				if(x + dialog.getWidth()>screenSize.getWidth() ) {
					x=(int)screenSize.getWidth() - dialog.getWidth();
				}
				if(y + dialog.getHeight()>screenSize.getHeight()) {
					y=(int)screenSize.getHeight()- dialog.getHeight();
				}
				dialog.setLocation(x, y);
				
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancel;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JComboBox jComboBoxCheckedFlagFilter;
    private javax.swing.JComboBox jComboBoxCopyRightFilter;
    private static javax.swing.JComboBox jComboBoxPlaylistField;
    private javax.swing.JComboBox jComboBoxPlaylistFilter;
    private javax.swing.JComboBox jComboBoxPlaylistOperator;
    private javax.swing.JSpinner jSpinnerPlaylistFilter;
    private javax.swing.JTextField jTextFieldPlayslitValue;
    // End of variables declaration//GEN-END:variables

}
