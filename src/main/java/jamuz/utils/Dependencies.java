package jamuz.utils;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/*
 * Copyright (C) 2022 phramusca <phramusca@gmail.com>
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

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class Dependencies {
	
	public static void check(Component cmpnt) {
		if(OS.isUnix()) {
			boolean isApt = check(null, "apt", "", "install packages");
			check(cmpnt, "mplayer", isApt?"apt://mplayer":"https://mplayerhq.hu/", "play tracks");
			check(cmpnt, "metaflac", isApt?"apt://flac":"https://xiph.org/flac/", "store replaygain in FLAC files");
			check(cmpnt, "mp3gain", "https://snapcraft.io/mp3gain", "compute replaygain for MP3 files");
			//FIXME Z Add "aplay" (from Mplayer.getAudioCards())
		}
	}
    
    public static boolean checkDocker(Component cmpnt) {
        String url = "https://rancherdesktop.io/";
		if(OS.isUnix()) {
            if(check(null, "apt", "", "install packages")) {
                url = "https://docs.docker.com/engine/install/debian/#install-using-the-repository";
            } else {
                url = "https://docs.docker.com/engine/install/";
            }
		}
        return check(cmpnt, "docker", url, "play tracks");
	}
	
	private static boolean check(Component cmpnt, String linuxCommand, String url, String reason) {
		List<String> cmdArray = new ArrayList<>();
		cmdArray.add(linuxCommand);
		Runtime runtime = Runtime.getRuntime();
		try {
			String[] stockArr = new String[cmdArray.size()];
			stockArr = cmdArray.toArray(stockArr);
			Process process = runtime.exec(stockArr);
			process.waitFor();
			return true;
		} catch (IOException | InterruptedException ex) {
			if(cmpnt!=null) {
				//https://www.w3cschoool.com/tutorial/linux-error-codes
				if(ex.getMessage().contains("error=2") || ex.getMessage().contains("error=3")) {
					int n = JOptionPane.showConfirmDialog(
							cmpnt, "\"" + linuxCommand + "\" is required in order to " + reason + ". Would you like to install it?",
							Inter.get("Label.Confirm"),  //NOI18N
							JOptionPane.YES_NO_OPTION);
					if((n == JOptionPane.YES_OPTION)) {
						Desktop.openBrowser(url);
					}
				} else {
					Popup.error(ex);
				}
			}
		}
		return false;
	}
}
