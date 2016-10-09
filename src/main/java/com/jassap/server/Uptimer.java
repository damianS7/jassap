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
package com.jassap.server;

public class Uptimer implements Runnable {
	private int hours = 0;
	private int minutes = 0;
	private int seconds = 0;
	private boolean running = false;
	
	public void start() {
		running = true;
		new Thread(this).start();
	}
	
	public void stop() {
		running = false;
	}
	
	public int getHours() {
		return hours;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public String getTimer() {
		String zeroH = "";
		String zeroM = "";
		String zeroS = "";
		if(hours < 10) {
			zeroH = "0";
		}
		if(minutes < 10) {
			zeroM = "0";
		}
		if(seconds < 10) {
			zeroS = "0";
		}
		
		return zeroH + hours + ":" + zeroM + minutes + ":" + zeroS + seconds;
	}

	@Override
	public void run() {
		while(running) {
			try {
				Thread.sleep(1000);
				seconds++;
				if(seconds >= 60) {
					minutes++;
					if(minutes >= 60) {
						minutes = 0;
						hours++;
					}
					seconds = 0;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
