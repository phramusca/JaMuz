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

package jamuz.process.merge;

import jamuz.database.DbInfo;
import jamuz.FileInfo;
import jamuz.database.StatSourceSQL;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */

// FIXME ZZZ MERGE Remove this StatSourceJaMuzRemote class as no more needed OR:
// WARNING: May be needed for:
// - playcounter insertion
// - original path
// - more ?
// => If so, document it clear here and make NOT appear in gui combo
public class StatSourceJaMuzRemote extends StatSourceSQL {

	/**
	 *
	 * @param dbInfo
	 * @param name
	 * @param rootPath
	 */
	public StatSourceJaMuzRemote(DbInfo dbInfo, String name, String rootPath) {
        super(dbInfo, name, rootPath, true, true, false, true, true, true);
    }

	/**
	 *
	 * @return
	 */
	@Override
    public boolean setUp(boolean isRemote) {
        return true;
    }

    /**
     * Set update statistics parameters
     * @param file
     * @throws SQLException
     */
    @Override
    protected void setUpdateStatisticsParameters(FileInfo file) throws SQLException {
    }
	
	/**
	 *
	 * @param tags
	 * @param file
	 * @return
	 */
	@Override
	public boolean getTags(ArrayList<String> tags, FileInfo file) {
		return true;
	}
}
