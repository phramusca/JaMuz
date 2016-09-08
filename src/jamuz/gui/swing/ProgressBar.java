/*
 * Copyright (C) 2013 phramusca
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

import jamuz.Jamuz;
import jamuz.utils.StringManager;
import java.util.logging.Level;
import javax.swing.JProgressBar;

//TODO: Transformer toutes les progress bar pour utiliser cette classe !

/**
 * Progress bar extension
 * @author raph
 */
public class ProgressBar extends JProgressBar {
	
	private int index = 0;
	private int msgMax = 60;
	private boolean displayAsPercent = false;

	/**
	 * Create a new progress bar
	 */
	public ProgressBar() {
		this.setMaximum(0);
	}
   
    
	/**
	 * Reset progress bar
	 */
	public void reset() {
		this.setIndeterminate(false);
		this.setMaximum(0);
		this.index = 0;
		this.displayAsPercent=false;
		this.setString("");  //NOI18N
		this.setValue(0);
	}
	
	/**
	 * Setup progress bar
	 * @param max
	 */
	public void setup(int max) {
		this.reset();
		this.setMaximum(max);
	}
	
	/**
	 * Set the message maximum length
	 * @param msgMax
	 */
	public void setMsgMax(int msgMax) {
		this.msgMax=msgMax;
//		this.msgMax=this.msgMax-4;
//		this.msgMax=this.msgMax/2;
	}

	/**
	 * Display text as percentage
	 */
	public void displayAsPercent() {
		this.displayAsPercent = true;
	}
	
	/**
	 * Progress by 1
	 * @param msg
	 */
	public void progress(String msg) {
		this.index++;
		this.setValue(this.index);
		this.setMsg(msg);
        this.setIndeterminate(false);
	}

	/**
	 * Progress to index
	 * @param msg
	 * @param index
	 */
	public void progress(String msg, int index) {
		this.index=index;
		this.setValue(this.index);
		this.setMsg(msg);
	}
	
	/**
	 * Set progress bar in indeterminate mode
	 * @param msg
	 */
	public void setIndeterminate(String msg) {
		this.setIndeterminate(true);
		this.setString(msg);
	}
	
	private void setMsg(String msg) {
		//Truncate msg if too long 
		if(msg.length()>this.msgMax) {
			msg=StringManager.Left(msg, this.msgMax)+"(..)";  //NOI18N
		}
		if(this.displayAsPercent) {
			msg=String.valueOf(this.index*100/this.getMaximum())+"%"+" "+msg;
		}
		else {
			msg=String.valueOf(this.index)+"/"+String.valueOf(this.getMaximum())+" "+msg;  //NOI18N
		}
		this.setString(msg);
	}
}
