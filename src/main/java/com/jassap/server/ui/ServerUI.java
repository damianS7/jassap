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

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ServerUI {
	public JFrame frame;
	public MenuBar menuBar;
	public MenuLeft menuLeft;
	public StatsBar statsBar;
	public OutputLog outputLog;
	
	public ServerUI() {
		setLookAndFeel();
		frame = new JFrame();
		menuBar = new MenuBar();
		menuLeft = new MenuLeft();
		statsBar = new StatsBar();
		outputLog = new OutputLog();
		initComponents();
	}
	
	private void initComponents() {
		frame.setSize(900, 300);
		frame.setTitle("JassapServer - Simple chat application");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(menuBar.getBar());
		frame.getContentPane().add(menuLeft.getPanel(), BorderLayout.WEST);
		frame.getContentPane().add(statsBar, BorderLayout.SOUTH);
		frame.getContentPane().add(outputLog, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private void setLookAndFeel() {
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
	