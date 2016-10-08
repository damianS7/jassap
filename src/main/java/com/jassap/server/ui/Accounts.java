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
package com.jassap.server.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.jassap.chat.Roles;
import com.jassap.database.Account;
import com.jassap.server.JassapServer;

public class Accounts extends JDialog {
	private static final long serialVersionUID = 2016169491948523070L;
	private JTextField username;
	private JPasswordField password1;
	private JPasswordField password2;
	private AccountList list;
	private JComboBox<String> roleBox;

	public Accounts() {
		super();
		setModal(true);
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.WEST);
		panel.setLayout(new MigLayout("", "[150px:n:150px]", "[grow][]"));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, "cell 0 0,grow");

		list = new AccountList(JassapServer.accountDatabase.getAccounts());
		scrollPane.setViewportView(list);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = list.getSelectedValue();
				if (name != null) {
					int reply = JOptionPane.showConfirmDialog(null,
							"Are you sure you want to delete?", "delete?",
							JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION) {
						JassapServer.accountDatabase.removeAccount(name);
						list.removeAccount(name);
						JassapServer.accountDatabase.save();
					}
				}
			}
		});
		panel.add(btnDelete, "cell 0 1,growx");

		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new MigLayout("", "[300px:n,grow][250px:n,grow]",
				"[][][][][][]"));

		JLabel lblUsername = new JLabel("Username");
		panel_1.add(lblUsername, "cell 0 0,alignx left");

		username = new JTextField();
		username.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(username, "cell 1 0,growx");
		username.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		panel_1.add(lblPassword, "cell 0 1,alignx left");

		password1 = new JPasswordField();
		password1.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(password1, "cell 1 1,growx");

		JLabel lblRepeatPassword = new JLabel("Repeat password");
		panel_1.add(lblRepeatPassword, "cell 0 2,alignx left");

		password2 = new JPasswordField();
		password2.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(password2, "cell 1 2,growx");

		roleBox = new JComboBox<String>();
		roleBox.addItem("ADMIN");
		roleBox.addItem("SUPERMOD");
		roleBox.addItem("MOD");
		roleBox.addItem("USER");

		JLabel lblPrivileges = new JLabel("Privileges");
		panel_1.add(lblPrivileges, "cell 0 3,alignx left");
		roleBox.setSelectedItem("USER");
		panel_1.add(roleBox, "cell 1 3,growx");

		JButton btnNewAccount = new JButton("New account");
		btnNewAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String p1 = new String(password1.getPassword());

				if (username.getText().isEmpty() || p1.isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Username or password cannot be empty!");
					return;
				}

				String p2 = new String(password2.getPassword());
				if (!p1.equals(p2)) {
					JOptionPane.showMessageDialog(null,
							"Password does not match!");
					return;
				}

				list.addAccount(username.getText());
				Account acc = new Account(username.getText(), new String(
						password1.getPassword()));
				//System.out.println(Roles.valueOf(roleBox.getSelectedItem()));
				acc.setRole(Roles.valueOf(roleBox.getSelectedItem()
						.toString()));
				JassapServer.accountDatabase.addAccount(acc);
				JassapServer.accountDatabase.save();
			}
		});
		panel_1.add(btnNewAccount, "cell 1 4,growx");

		JPanel panel_2 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_2.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel_2, BorderLayout.SOUTH);

		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		panel_2.add(btnClose);

		pack();
		setLocationRelativeTo(JassapServer.ui.frame);
		setVisible(true);
	}
}
