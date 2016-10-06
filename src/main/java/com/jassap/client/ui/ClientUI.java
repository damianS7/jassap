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


import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;

public class ClientUI {
	public JFrame frame;
	public MenuBar menuBar;
	private JTabbedPane tabbedPane;

	// Lista (JList) con los nombres de las salas
	public RoomList rooms;
	
	// Array con todos los tabs abiertos
	public List<TabChat> tabs = new ArrayList<TabChat>();
	private JPanel panel;
	private JButton btnConnect;
	
	public ClientUI() {
		setSystemLookAndFeel();
		frame = new JFrame();
		menuBar = new MenuBar();
		rooms = new RoomList();
		initComponents();
		addTab(new LogTab("Server log"));
		
		panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		frame.getContentPane().add(panel, "cell 0 1 2 1,grow");
		
		btnConnect = new JButton("Connect");
		panel.add(btnConnect);
		rooms.addRoom("Room 1");
		rooms.addRoom("Room 2");
		rooms.addRoom("Room 3");
	}
	
	// Borra un "tab" de la lista "tabs" y del tabbedPane
	public void removeTab(TabChat tab) {
		tabs.remove(tab);
		tabbedPane.remove(tab);
	}
	
	// Añade un  tab a la lista y al tabbedPane
	public void addTab(TabChat tab) {
		tabs.add(tab);
		tabbedPane.add(tab.getTitle(), tab);
		tabbedPane.setSelectedComponent(tab);
	}
	
	/**
	 * Obtiene un tab de la lista
	 * @param title El titulo del tab que se desea buscar
	 * @return una instancia de TabChat o null si no es encontrado
	 */
	public TabChat getTab(String title) {
		for (TabChat tabChat : tabs) {
			if(tabChat.getTitle().equals(title)) {
				return tabChat;
			}
		}
		return null;
	}
	
	private void initComponents() {
		frame.setSize(900, 600);
		frame.setTitle("Jassap - Client");
		frame.setJMenuBar(menuBar);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new MigLayout("", "[100px:n][grow]", "[grow][]"));
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, "cell 0 0,grow");
		
		scrollPane.setViewportView(rooms);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, "cell 1 0,grow");
		frame.setVisible(true);
	}

	private void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InstantiationException e) {
            System.out.println(e.getMessage());
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
