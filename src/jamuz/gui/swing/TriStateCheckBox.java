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
package jamuz.gui.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;

/**
 *
 * @author https://community.oracle.com/message/5711113#5711113
 * with little modifications by phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class TriStateCheckBox extends JCheckBox {
   
	/**
	 *
	 */
	public static enum State {
 
	  /**
	   *
	   */
	  SELECTED, 

	  /**
	   *
	   */
	  UNSELECTED, 

	  /**
	   *
	   */
	  ALL 
  };
  
  /**
   * Creates an initially unselected check box button with no text, no icon.
   */
  public TriStateCheckBox() {
      this(null, State.ALL);
  }
  
  /**
   * Creates a check box with text and icon,
   * and specifies whether or not it is initially selected.
   *
   * @param text the text of the check box.
     * @param initial
   */
  public TriStateCheckBox (String text, State initial) {      
    super.setText(text);
    setModel(new TriStateModel(initial));
   
    //some UI settings
    setRolloverEnabled( false );
    
    /*
    List<Object> gradient = new LinkedList<Object>();
    gradient.add( 0 );
    gradient.add( 0 );
    gradient.add( new ColorUIResource(Color.white) );
    gradient.add( new ColorUIResource(Color.white) );
    gradient.add( new ColorUIResource(Color.white) );    
    UIManager.put("CheckBox.gradient", gradient); //get rid of gradient
    */
  }
  
 
  /**
   * Set the new state to either CHECKED, PARTIAL or UNCHECKED.
     * @param state
   */
  public void setState(State state) 
  {
    ((TriStateModel) model).setState(state);
  }
  
  /**
   * Return the current state, which is determined by the selection status of
   * the model.
     * @return 
   */
  public State getState() 
  {
    return ((TriStateModel) model).getState();
  } 
  
  @Override
  public void setSelected(boolean selected) 
  {
    ((TriStateModel) model).setSelected(selected);    
  } 
  
   
  @Override
  public void paintComponent( Graphics g ) 
  {
    super.paintComponent( g );
    
    if(((TriStateModel) model).getState() == State.ALL) 
    {      
      Graphics2D g2 = (Graphics2D) g;
      
      int cx = getWidth() / 2;
      int cy = getHeight() / 2;
      
      g2.setColor( Color.darkGray );      
      g2.fillRect( cx - 2,  cy - 2,  5 , 5 );  
    }    
  }
  
  /** The model for the button */
  private static class TriStateModel extends JToggleButton.ToggleButtonModel
  {      
    protected State state;  
 
    public TriStateModel(State state)
    {
      this.state = state;
    }
   
    @Override
    public boolean isSelected()
    {      
      return state == State.SELECTED;
    } 
 
    public State getState() {
      return state;
    }
    
    public void setState(State state) {
      this.state = state;
      fireStateChanged();
    }
    
    @Override
    public void setPressed(boolean pressed)
    {      
		if (pressed)
		{
			switch(state)
			{
			case UNSELECTED: 
				state = State.SELECTED;
				break;
			case ALL: 
				state = State.UNSELECTED;
				break;
			case SELECTED: 
				state = State.ALL;
				break;
			}        
		}
    }
    
    @Override
    public void setSelected(boolean selected)
    {       
      if (selected) {
        this.state = State.SELECTED;
      } else {
        this.state = State.UNSELECTED;
      }      
    }
  } 

  
  
}
