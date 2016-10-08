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
package com.jassap.server;

import com.jassap.database.Account;

public class Login {
	private Account account;
	private boolean valid = false;
	
	public Login(Account account) {
		this.account = account;
		
		Account a = JassapServer.accountDatabase.getAccount(account.getUser());
		
		if(a != null) {
			if(a.getPass().equals(account.getPass())) {
				valid = true;
				account.setRole(a.getRole());
			}
		}
		
	}
	
	public Account getAccount() {
		return account;
	}
	
	public Login(String username, String password) {
		this(new Account(username, password));
	}
	
	public boolean isValid() {
		return valid;
	}
}
