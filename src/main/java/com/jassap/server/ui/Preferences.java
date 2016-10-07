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
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.miginfocom.swing.MigLayout;

import com.jassap.server.JassapServer;

public class Preferences extends JDialog {
	private static final long serialVersionUID = -977672460313857263L;
	private JTextField address;
	private JTextField port;
	private JTextField maxConnections;

	public Preferences() {
		super(JassapServer.ui.frame, "Preferences");
		setModal(true);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		panel.add(btnSave);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new MigLayout("", "[150px:n,grow][250px:n,grow]", "[][][][][][][]"));
		
		JLabel lblServer = new JLabel("Server");
		panel_1.add(lblServer, "cell 0 0,alignx left");
		
		address = new JTextField(JassapServer.serverProperties.getAddress());
		address.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(address, "cell 1 0,growx");
		address.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		panel_1.add(lblPort, "cell 0 1,alignx left");
		
		port = new JTextField(Integer.toString(JassapServer.serverProperties.getPort()));
		port.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(port, "cell 1 1,growx");
		port.setColumns(10);
		
		JLabel lblUsername = new JLabel("Max connections");
		panel_1.add(lblUsername, "cell 0 2,alignx left");
		
		maxConnections = new JTextField(Integer.toString(JassapServer.serverProperties.getMaxConnections()));
		maxConnections.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(maxConnections, "cell 1 2,growx");
		maxConnections.setColumns(10);
		
		pack();
		setLocationRelativeTo(JassapServer.ui.frame);
		setVisible(true);
	}
	
	public void save() {
		JassapServer.serverProperties.setAddress(address.getText());
		JassapServer.serverProperties.setPort(Integer.parseInt(port.getText()));
		JassapServer.serverProperties.setMaxConnections(maxConnections.getText());
		try {
			JassapServer.serverProperties.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		dispose();
	}
}
