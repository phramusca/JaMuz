/*
 * Copyright (C) 2015 phramusca <phramusca@gmail.com>
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

import jamuz.utils.Popup;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class Options {

	private String filename = "";
	private Properties properties;

	/**
	 *
	 */
	protected InputStream input = null;

	/**
	 *
	 */
	protected Options() {
		this.properties = new Properties();
	}

	/**
	 *
	 * @param filename
	 */
	public Options(String filename) {
		this();
		this.filename = filename;
		try {
			this.input = new FileInputStream(filename);
		} catch (FileNotFoundException ex) {
			Popup.error(ex);
		}
	}

	/**
	 *
	 * @param key
	 * @return
	 */
	public String get(String key) {
		return properties.getProperty(key, "{Missing}");
	}

	/**
	 *
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String get(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}

	/**
	 *
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		properties.setProperty(key, value);
	}

	/**
	 *
	 * @return
	 */
	public boolean save() {
		try (OutputStream output = new FileOutputStream(filename)) {
			properties.store(output, null);
			return true;
		} catch (IOException ex) {
			Popup.error(ex);
			return false;
		}
	}

	/**
	 *
	 * @return
	 */
	public boolean read() {
		if (input == null) {
			return false;
		}
		try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
			properties.load(reader);
			return true;
		} catch (IOException ex) {
			Popup.error(ex);
			return false;
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				Popup.error(ex);
			}
		}
	}
}
