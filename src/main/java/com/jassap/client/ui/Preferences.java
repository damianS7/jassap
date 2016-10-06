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
package com.jassap.client.ui;

import javax.swing.JDialog;

import com.jassap.client.JassapClient;

import net.miginfocom.swing.MigLayout;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JCheckBox;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingConstants;

public class Preferences extends JDialog {
	private static final long serialVersionUID = 2711954362741187209L;
	private JTextField server;
	private JTextField port;
	private JTextField username;
	private JPasswordField password;
	private JTextField phone;
	private JTextField avatar;
	private JCheckBox saveLogs;
	
	public Preferences() {
		super(JassapClient.ui.frame, "Preferences");
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
		
		server = new JTextField(JassapClient.clientProperties.getAddress());
		server.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(server, "cell 1 0,growx");
		server.setColumns(10);
		
		JLabel lblPort = new JLabel("Port");
		panel_1.add(lblPort, "cell 0 1,alignx left");
		
		port = new JTextField(Integer.toString(JassapClient.clientProperties.getPort()));
		port.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(port, "cell 1 1,growx");
		port.setColumns(10);
		
		JLabel lblUsername = new JLabel("Username");
		panel_1.add(lblUsername, "cell 0 2,alignx left");
		
		username = new JTextField(JassapClient.clientProperties.getUsername());
		username.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(username, "cell 1 2,growx");
		username.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		panel_1.add(lblPassword, "cell 0 3,alignx left");
		
		password = new JPasswordField(JassapClient.clientProperties.getPassword());
		password.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(password, "cell 1 3,growx");
		
		JLabel lblPhone = new JLabel("Phone");
		panel_1.add(lblPhone, "cell 0 4,alignx left");
		
		phone = new JTextField(JassapClient.clientProperties.getPhone());
		phone.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(phone, "cell 1 4,growx");
		phone.setColumns(10);
		
		JLabel lblAvatar = new JLabel("Avatar");
		panel_1.add(lblAvatar, "cell 0 5,alignx left");
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, "cell 1 5,grow");
		panel_2.setLayout(new MigLayout("", "[114px,grow][]", "[25px]"));
		
		avatar = new JTextField(JassapClient.clientProperties.getAvatarPath());
		avatar.setHorizontalAlignment(SwingConstants.RIGHT);
		avatar.setEditable(false);
		panel_2.add(avatar, "cell 0 0,growx,aligny center");
		avatar.setColumns(10);
		
		JButton btnPath = new JButton("...");
		btnPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setCurrentDirectory(new File("."));
				int selected = chooser.showSaveDialog(JassapClient.ui.frame);
				
				if(selected == JFileChooser.APPROVE_OPTION) {
					avatar.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		panel_2.add(btnPath, "cell 1 0,alignx left,aligny top");
		
		JLabel lblSaveLogs = new JLabel("Save logs");
		panel_1.add(lblSaveLogs, "cell 0 6,alignx left");
		
		saveLogs = new JCheckBox("");
		saveLogs.setSelected(JassapClient.clientProperties.getSaveLogs());
		panel_1.add(saveLogs, "cell 1 6,alignx right");
		
		pack();
		setLocationRelativeTo(JassapClient.ui.frame);
		setVisible(true);
	}
	
	public void save() {
		JassapClient.clientProperties.setUsername(username.getText());
		JassapClient.clientProperties.setAvatarPath(avatar.getText());
		JassapClient.clientProperties.setPhone(phone.getText());
		JassapClient.clientProperties.setAddress(server.getText());
		JassapClient.clientProperties.setPort(Integer.parseInt(port.getText()));
		JassapClient.clientProperties.setPassword(new String(password.getPassword()));
		JassapClient.clientProperties.setSaveLogs(Boolean.toString(saveLogs.isSelected()));
		
		try {
			JassapClient.clientProperties.save();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		dispose();
	}
}
