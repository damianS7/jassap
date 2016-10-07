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

import javax.swing.JDialog;

import net.miginfocom.swing.MigLayout;

import javax.swing.JPanel;
import javax.swing.JButton;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import com.jassap.chat.Room;
import com.jassap.chat.RoomModerator;
import com.jassap.server.JassapServer;

import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


public class Rooms extends JDialog {
	private static final long serialVersionUID = -7985614931111845166L;
	private RoomList list;
	private JTextField roomName;
	private JSpinner maxUsers;
	private JTextArea description;
	private JTextField mod;
	
	public Rooms() {
		super();
		setModal(true);
		getContentPane().setLayout(new MigLayout("", "[::100px][200px:n,grow][200px:n,grow]", "[][][][grow][][][]"));
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "cell 0 0 1 4,grow");
		
		list = new RoomList(JassapServer.roomDatabase.getRooms());
		scrollPane.setViewportView(list);
		
		JLabel lblRoom = new JLabel("Room");
		getContentPane().add(lblRoom, "cell 1 0,alignx left");
		
		roomName = new JTextField();
		roomName.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(roomName, "cell 2 0,growx");
		roomName.setColumns(10);
		
		JLabel lblMaxUsers = new JLabel("Max users");
		getContentPane().add(lblMaxUsers, "cell 1 1,alignx left");
		
		maxUsers = new JSpinner();
		maxUsers.setModel(new SpinnerNumberModel(20, 1, 1000, 1));
		getContentPane().add(maxUsers, "cell 2 1,growx");
		
		JLabel lblDescription = new JLabel("Description");
		getContentPane().add(lblDescription, "cell 1 2");
		
		JScrollPane scrollPane_1 = new JScrollPane();
		getContentPane().add(scrollPane_1, "cell 2 2 1 2,grow");
		
		description = new JTextArea();
		scrollPane_1.setViewportView(description);
		
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
						JassapServer.roomDatabase.removeRoom(name);
						list.removeRoom(name);
						JassapServer.roomDatabase.save();
					}
				}
			}
		});
		getContentPane().add(btnDelete, "cell 0 4,growx");
		
		JButton btnAddmodify = new JButton("Add/Modify");
		btnAddmodify.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = roomName.getText();
				String desc = description.getText();
				int maxu = Integer.parseInt(maxUsers.getModel().getValue().toString());

				if (name.isEmpty() || desc.isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Room name/description cannot be empty!");
					return;
				}
				
				Room r = new Room(name, desc, maxu);
				String[] mods = mod.getText().split(",");
				
				for (String mod : mods) {
					r.addRoomModerator(new RoomModerator(mod.trim()));
				}
				
				JassapServer.roomDatabase.addRoom(r);
				list.addRoom(name);
				JassapServer.roomDatabase.save();
			}
		});
		
		JLabel lblMod = new JLabel("Mod");
		getContentPane().add(lblMod, "cell 1 4,alignx left");
		
		mod = new JTextField();
		mod.setHorizontalAlignment(SwingConstants.RIGHT);
		getContentPane().add(mod, "cell 2 4,growx");
		mod.setColumns(10);
		getContentPane().add(btnAddmodify, "cell 2 5,growx");
		
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		getContentPane().add(panel, "cell 2 6,grow");
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		panel.add(btnCancel);
		
		pack();
		setLocationRelativeTo(JassapServer.ui.frame);
		setVisible(true);
	}
}
