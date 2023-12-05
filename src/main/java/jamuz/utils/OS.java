/*
 * Copyright (C) 2011 phramusca <phramusca@gmail.com>
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

package jamuz.utils;

/**
 * OS detection class
 * @author phramusca <phramusca@gmail.com>
 */
public class OS {

	private static String name;
	private static boolean isWindows=false;
	private static boolean isUnix=false;
//	protected static boolean isMac=false;
//	protected static boolean isSolaris=false;
 
	/**
	 * Return if machine is running under Windows
	 * @return
	 */
	public static boolean isWindows() {
		return isWindows;
	}
	
	/**
	 * Return if machine is running under Unix/Linux
	 * @return
	 */
	public static boolean isUnix() {
		return isUnix;
	}
	
	/**
	 * Detect OS on the machine
	 * @return
	 */
	public static boolean detect() {
		name = System.getProperty("os.name").toLowerCase();  //NOI18N
		if(name.contains("win")) {  //NOI18N
			isWindows=true;
		}
		else if(name.contains("nix") || name.contains("nux")) {  //NOI18N
			isUnix=true;
		}
//		else if(os.indexOf("mac") >= 0) {
//			isMac=true;
//		}
//		else if(os.indexOf("sunos") >= 0) {
//			isSolaris=true;
//		}
		else {
			//OS could not be detected or not supported !
			Popup.error(java.text.MessageFormat.format(Inter.get("Error.OSnotSupported"), new Object[] {name}));  //NOI18N
			return false;
		}
		return true;
	}

	/**
	 * Return OS name.
	 * @return
	 */
	public static String getName() {
		return name;
	}
}
