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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.AbstractTableModel;

import com.jassap.server.JassapServer;
import com.jassap.server.ServerUser;

/**
 * Contiene una lista con las conexiones abiertas actualmente
 * Logged/IP/Connection time/Button for kick 
 * @author jian
 */
public class Connections extends JDialog {
	private static final long serialVersionUID = 3587077023662371517L;
	private ClientsTable table;

	public Connections() {
		super(JassapServer.ui.frame, "Server configuration");
		getContentPane().setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		table = new ClientsTable();
		scrollPane.setViewportView(table);
		updateList();
		pack();
		setLocationRelativeTo(JassapServer.ui.frame);
		setVisible(true);
		
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					updateList();
					try {
						Thread.sleep(2500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void updateList() {
		((ClientsTableModel) table.getModel()).removeRows();
		for (ServerUser c : JassapServer.server.getUsers()) {
			addUser(c);
		}
		
		//((ClientsTableModel) table.getModel()).addRow(new Object[]{"0", "192.168.0.1"});
	}
	
	private void addUser(ServerUser u) {
		ClientsTableModel m = ((ClientsTableModel) table.getModel());
		Object[] o = {m.getRowCount(), u.getConnection().getAddress()};
		m.addRow(o);
	}
}

class ClientsTable extends JTable implements MouseListener {
	private static final long serialVersionUID = 6436640853915099376L;
	private JPopupMenu popMenu;
	private ClientsTableModel model;
	public ClientsTable() {
		super();
		addMouseListener(this);
		model = new ClientsTableModel();
		popMenu = new JPopupMenu();
		
		JMenuItem kickItem = new JMenuItem("Kick");
		kickItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ip = (String) model.getValueAt(getSelectedRow(), "Address");
				ServerUser c = JassapServer.server.getUserByIp(ip);
				JassapServer.server.kick(c);
			}
		});
		
		popMenu.add(kickItem);
		
		setModel(model);
	}
		
	private void showTablePopMenu(MouseEvent e) {
		// Si hay un menu insertado
        if (popMenu instanceof JPopupMenu) {
            // Se muestra ...
            if (e.isPopupTrigger()) {
                // Numero de fila donde esta el puntero
                int row = rowAtPoint(e.getPoint());
                // Si la fila esta seleccionada
                //if (isRowSelected(row)) {
                    // Se muestra el menu en la posicion donde esta el puntero
                    popMenu.show(e.getComponent(), e.getX(), e.getY());
                //}
            }
        }
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		showTablePopMenu(e);
	}

	public void mouseReleased(MouseEvent e) {
		showTablePopMenu(e);
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}
}


class ClientsTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 44205591644423089L;
	private String[] columns = { "#", "Address"};
	protected List<Object[]> rows = new ArrayList<Object[]>();
	
	public void removeRows() {
		rows.clear();
        fireTableDataChanged();
	}
	
	public void addRow(Object[] values) {
        rows.add(values);
        fireTableRowsInserted(0, 0);
    }
	
	public void removeRow(int row) {
		if(row == -1) {
			return;
		}
		
        rows.remove(row);
        fireTableRowsDeleted(row, row);
    }
	
	public int getColumnIndex(String nameColumn) {
		for (int i = 0; i < columns.length; i++) {
			if(columns[i].equals(nameColumn)) {
				return i;
			}
		}
		return -1;
	}
	
	public int getRowIndexByValue(int columnIndex, String value) {
		if(columnIndex > columns.length || columnIndex < 0) {
			return -1;
		}
		
		for (int i = 0; i < rows.size(); i++) {
			if(rows.get(i)[columnIndex].equals(value)) {
				return i;
			}
		}
		return -1;
	}
	
	public int getRowIndexByValue(String nameColumn, String value) {
		return getRowIndexByValue(getColumnIndex(nameColumn), value);
	}
	
	public int getRowCount() {
		return rows.size();
	}

	public int getColumnCount() {
		return columns.length;
	}
	
	public Object getValueAt(int rowIndex, String column) {
		return rows.get(rowIndex)[getColumnIndex(column)];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		return rows.get(rowIndex)[columnIndex];
	}
	
	@Override
	public String getColumnName(int column) {
		//return super.getColumnName(column);
		return columns[column];
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		//return super.isCellEditable(rowIndex, columnIndex);
		return false;
	}
	
}