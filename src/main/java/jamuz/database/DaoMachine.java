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
package jamuz.database;

/**
 *
 * @author phramusca <phramusca@gmail.com>
 */
public class DaoMachine {

    private final DbConn dbConn;
    private final DaoMachineWrite daoMachineWrite;

    /**
     *
     * @param dbConn
     */
    public DaoMachine(DbConn dbConn) {
        this.dbConn = dbConn;
        this.daoMachineWrite = new DaoMachineWrite(dbConn);
    }

    /**
     * This is to reach writing operations (insert, update, delete)
     *
     * @return
     */
    public DaoMachineWrite lock() {
        return daoMachineWrite;
    }

    
}
