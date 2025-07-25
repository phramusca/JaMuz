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
package jamuz.database;

import java.util.Date;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DbVersion {
	private final int version;
	private final Date upgradeStart;
	private final Date upgradeEnd;

	public DbVersion(int version, Date upgradeStart, Date upgradeEnd) {
		this.version = version;
		this.upgradeStart = upgradeStart;
		this.upgradeEnd = upgradeEnd;
	}

	public int getVersion() {
		return version;
	}

	public Date getUpgradeStart() {
		return upgradeStart;
	}

	public Date getUpgradeEnd() {
		return upgradeEnd;
	}
}
