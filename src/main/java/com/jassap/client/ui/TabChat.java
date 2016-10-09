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

import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Base para todos los tabs
 * @author danjian
 */
public abstract class TabChat extends JPanel {
	private static final long serialVersionUID = -4467915743235869875L;
	protected String title;
	protected JTextArea outputBox;
	protected JTextArea inputBox;
	
	public TabChat(String title) {
		super();
		this.title = title;
		outputBox = new JTextArea();
		inputBox = new JTextArea();
	}
	
	public String getTitle() {
		return title;
	}
	
	public void addText(String text) {
		if(!text.endsWith("\n")) {
			text += "\n";
		}
		outputBox.append(text);
		outputBox.setCaretPosition(outputBox.getDocument().getLength());
	}
	
	public abstract void sendMessage();
}
