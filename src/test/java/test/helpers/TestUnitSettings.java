/*
 * Copyright (C) 2023 phramusca <phramusca@gmail.com>
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
package test.helpers;

import jamuz.DbConnJaMuz;
import jamuz.DbInfo;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class TestUnitSettings {
	
	public static DbConnJaMuz createTempDatabase() throws SQLException, ClassNotFoundException, IOException {
		File tempFile = File.createTempFile("tempJaMuzDbForUnitTests", ".db");
		DbInfo dbInfo = new DbInfo(DbInfo.LibType.Sqlite, tempFile.getAbsolutePath(), "", "");
		DbConnJaMuz dbConnJaMuz = new DbConnJaMuz(dbInfo);
		dbConnJaMuz.getDbConn().connect();
		executeScript(dbConnJaMuz, getFile("JaMuz_creation.sql", "database").getAbsolutePath());
		executeScript(dbConnJaMuz,getFile("JaMuz_insertion_minimal.sql", "database").getAbsolutePath());
		executeScript(dbConnJaMuz,getFile("JaMuz_insertion_optional.sql", "database").getAbsolutePath());
		return dbConnJaMuz;
	}
	
	private static void executeScript(DbConnJaMuz dbConnJaMuz, String script) throws SQLException, ClassNotFoundException, IOException {
		List<String> statements = readCreateTableStatements(script);
		for (String statement : statements) {
			PreparedStatement preparedStatement = dbConnJaMuz.getDbConn().getConnection().prepareStatement(statement);
			preparedStatement.execute();
		}
	}

	private static List<String> readCreateTableStatements(String script) throws IOException {
		Path path = Path.of(script);
		String content = Files.lines(path).collect(Collectors.joining(" "));
		return Arrays.asList(content.split(";"));
	}

	private static String getFolder() {
		File f = new File(".");  //NOI18N
		String appPath = f.getAbsolutePath();
		return appPath.substring(0, appPath.length() - 1);
	}

	private static File getFile(String filename, String... args) {
		String file = getFolder();
		for (String subFolder : args) {
			file = FilenameUtils.concat(file, subFolder); //NOI18N
		}
		file = FilenameUtils.concat(file, filename); //NOI18N
		return new File(file); //NOI18N
	}
}
