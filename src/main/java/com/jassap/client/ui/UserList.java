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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import com.jassap.chat.Roles;
import com.jassap.client.JassapClient;
import com.jassap.server.JassapServer;

/**
 * Lista para los usuarios de las salas que implementa eventos para abrir
 * conversaciones al hacer click en el nombre del usuario y un menu popup
 * al hacer click derecho sobre el nombre de un usuario de la lista.
 * 
 * Este menu es para admin/mods y esta restringido a usuarios comunes.
 * @author danjian
 */
public class UserList extends JassapList {
	private static final long serialVersionUID = 3861971951317131589L;
	private JPopupMenu menu;
	private List<String> users = new ArrayList<String>();
	
	public UserList(String room) {
		super();
		menu = new JPopupMenu();
		menu.addMouseListener(this);
		JMenuItem kick = new JMenuItem("Kick");
		kick.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String reason = JOptionPane.showInputDialog(null,
						"Reason?", "Ok",
						JOptionPane.YES_NO_OPTION);
				
				if (reason != null) {
					String user = getModel().getElementAt(getSelectedIndex());
					
					JassapClient.client.kick(room, user, reason);
				}
			}
		});
		
		JMenuItem ban = new JMenuItem("Ban");
		ban.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String time = JOptionPane.showInputDialog(null,
						"Ban time?", "Ban",
						JOptionPane.YES_NO_OPTION);
				
				String reason = JOptionPane.showInputDialog(null,
						"Reason?", "Ok",
						JOptionPane.YES_NO_OPTION);
				
				if (time != null && reason != null) {
					int t = Integer.parseInt(time);
					String user = getModel().getElementAt(getSelectedIndex());
					
					JassapClient.client.ban(room, user, t, reason);
				}
			}
		});
		JMenuItem mute = new JMenuItem("Mute");
		mute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String time = JOptionPane.showInputDialog(null,
						"Ban time?", "Ban",
						JOptionPane.YES_NO_OPTION);
				
				String reason = JOptionPane.showInputDialog(null,
						"Reason?", "Ok",
						JOptionPane.YES_NO_OPTION);
				
				if (time != null && reason != null) {
					int t = Integer.parseInt(time);
					String user = getModel().getElementAt(getSelectedIndex());
					
					JassapClient.client.mute(room, user, t, reason);
				}
			}
		});
		menu.add(ban);
		menu.add(kick);
		menu.add(mute);
	}
	
	public void addUser(String username) {
		users.add(username);
		model.addElement(username);
	}
	
	public void addUsers(List<String> usersc) {
		for (String user : usersc) {
			addUser(user);
		}
	}
	
	public void removeUser(String username) {
		users.remove(username);
		model.removeElement(username);
	}
	
	private void showPopupMenu(MouseEvent e) {
		if(JassapClient.client.getClientUser().getAccount().getRole()
				== Roles.USER) {
			return;
		}
		
		if (e.isPopupTrigger()) {
			// Numero de fila donde esta el puntero
			//int row = model.rowAtPoint(e.getPoint());
			setSelectedIndex(locationToIndex(e.getPoint()));
			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		showPopupMenu(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		showPopupMenu(e);
		
		if(e.getClickCount() == 2) {
			JassapClient.ui.addTab(new ConversationTab(model.get(getSelectedIndex())));
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

}
