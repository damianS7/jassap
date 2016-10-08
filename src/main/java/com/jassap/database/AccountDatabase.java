/*
 * Copyright (C) 2013 by danjian <josepwnz@gmail.com>
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
package com.jassap.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.jassap.chat.Roles;

/**
 * Clase para interactuar con el fichero .json que contiene las cuentas
 * junto con la informacion de estas
 * @author danjian
 */
public class AccountDatabase extends JsonDatabase {
	private static final Logger LOGGER = Logger.getLogger(AccountDatabase.class
			.getName());
	private List<Account> accounts = new ArrayList<Account>();

	public AccountDatabase(File file) {
		super(file);
	}

	/**
	 * @return Una copia de las cuentas cargadas desde el fichero .json
	 */
	public List<Account> getAccounts() {
		return new ArrayList<Account>(accounts);
	}

	/**
	 * @return La cuenta con el nombre de usuario buscado o null si no la
	 * encuentra
	 */
	public Account getAccount(String user) {
		for (Account account : getAccounts()) {
			if (account.getUser().equals(user)) {
				return account;
			}
		}
		return null;
	}

	/**
	 * Añade una nueva cuenta a la db
	 * @param acc
	 */
	public void addAccount(Account acc) {
		accounts.add(acc);
	}

	/**
	 * Borra una cuenta a partir de su nombre
	 * @param userName
	 */
	public void removeAccount(String userName) {
		Account r = getAccount(userName);
		if (r != null) {
			accounts.remove(r);
		}
	}
	//"rhaegon": {"password":"123456","role":"admin"},
	public int load() {
		super.load();

		for (String username : jsonData.keySet()) {
			String password = jsonData.getJSONObject(username).getString("password");
			//Roles role = Privileges.valueOf(jsonData.getJSONObject(username).getString("role"));
			Roles role = Roles.USER;
			try {
				Account acc = new Account(username, password);
				acc.setRole(role);
				accounts.add(acc);
			} catch (JSONException e) {
				LOGGER.severe(e.getMessage());
				continue;
			}
		}

		return accounts.size();
	}

	@Override
	public void save() {
		jsonData = new JSONObject();
		for (Account acc : getAccounts()) {
			JSONObject accData = new JSONObject();
			accData.put("password", acc.getPass());
			accData.put("password", acc.getRole());
			jsonData.put(acc.getUser(), accData);
		}

		super.save();
	}
}
