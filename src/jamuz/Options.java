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

package jamuz;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import jamuz.utils.Popup;
import java.io.InputStreamReader;

/**
 *
 * @author phramusca ( https://github.com/phramusca/JaMuz/ )
 */
public class Options {
    
    private final String filename;
    private final Properties properties;

    public Options(String filename) {
        this.filename = filename;
        properties = new Properties();
    }

    public String get(String key) {
        return properties.getProperty(key, "{Missing}");
    }
    
    public void set(String key, String value) {
        properties.setProperty(key, value);
    }

    public boolean save() {
        OutputStream output = null;
        try {
            output = new FileOutputStream(filename);
            properties.store(output, null);
            return true;

        } catch (IOException ex) {
            Popup.error(ex);
            return false;
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    Popup.error(ex);
                }
            }
        }
    }
    
    public boolean read() {
        InputStream input = null;
        try {
            input = new FileInputStream(filename);
            properties.load(input);
            return true;

        } catch (IOException ex) {
            Popup.error(ex);
            return false;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Popup.error(ex);
                }
            }
        }
    }
	
	public static String readKey(String keyName) {
        InputStream input = null;
        try {
			input = Options.class.getResourceAsStream("/jamuz/keys.properties");
			Properties keys = new Properties();
            keys.load(input);
            return keys.getProperty(keyName);

        } catch (IOException ex) {
            Popup.error(ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Popup.error(ex);
                }
            }
        }
    }
}
