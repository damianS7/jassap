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

import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MenuLeft extends MenuBar {
	private JPanel panel;
	
	public MenuLeft() {
		super();
		panel = new JPanel();
		panel.setLayout(new MigLayout("", "[100px:n,grow][100px:n,grow]", "[][][][][][]"));
		
		JButton btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				start();
			}
		});
		panel.add(btnStart, "cell 0 0,growx");
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stop();
			}
		});
		
		JButton btnPreferences = new JButton("Preferences");
		btnPreferences.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editPreferences();
			}
		});
		panel.add(btnPreferences, "cell 1 0,growx");
		panel.add(btnStop, "cell 0 1,growx");
		
		JButton btnRooms = new JButton("Rooms");
		btnRooms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editRooms();
			}
		});
		
		JButton btnRestart = new JButton("Restart");
		btnRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restart();
			}
		});
		
		JButton btnAccounts = new JButton("Accounts");
		btnAccounts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editAccounts();
			}
		});
		panel.add(btnAccounts, "cell 1 1,growx");
		panel.add(btnRestart, "cell 0 2,growx");
		panel.add(btnRooms, "cell 1 2, growx");
		
		JButton btnConnections = new JButton("Connections");
		btnConnections.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				editConnections();
			}
		});
		
		JButton btnClearLog = new JButton("Clear log");
		btnClearLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				clearLog();
			}
		});
		panel.add(btnClearLog, "cell 0 3,growx");
		panel.add(btnConnections, "cell 1 3,growx");
		
		JButton btnKickAll = new JButton("Kick All");
		btnKickAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				kickAll();
			}
		});
		panel.add(btnKickAll, "cell 0 4,growx");
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exit();
			}
		});
		panel.add(btnExit, "cell 1 4,growx");
	}
	
	public JPanel getPanel() {
		return panel;
	}
}
