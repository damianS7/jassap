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

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import net.miginfocom.swing.MigLayout;

import com.jassap.client.JassapClient;

public class ConversationTab extends TabChat {
	private static final long serialVersionUID = 1832222818132204595L;
	private TabChat instance;
	
	public ConversationTab(String title) {
		super(title);
		instance = this;
		outputBox.setEditable(false);
		setLayout(new MigLayout("", "[332px,grow][100px]", "[202px,grow][80px]"));
		
		JScrollPane outScroll = new JScrollPane();
		outScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		outScroll.setViewportView(outputBox);
		add(outScroll, "cell 0 0 2 1,grow");
		
		JScrollPane inScroll = new JScrollPane();
		inScroll.setViewportView(inputBox);
		add(inScroll, "cell 0 1,grow");
		
		JPanel panel = new JPanel();
		add(panel, "cell 1 1,growx,aligny top");
		panel.setLayout(new MigLayout("", "[grow]", "[][]"));
		
		JButton btnSend = new JButton("Send");
		panel.add(btnSend, "cell 0 0,growx,aligny top");
		
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JassapClient.ui.removeTab(instance);
			}
		});
		panel.add(btnExit, "cell 0 1,growx,aligny top");
	}
}
