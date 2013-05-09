/*
 *  This file is part of frcjcss.
 *
 *  frcjcss is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  frcjcss is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with frcjcss.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.wpi.first.wpilibj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;

/**
 * A Victor speed controller emulation for FRC.
 * @author Nick DiRienzo, Patrick Jameson
 * @version 11.12.2010.3
 */
public class Victor implements ComponentListener, ActionListener {

    private double speed;
    private boolean isGraphRunning;

    private JFrame frame;
    private JButton startStop;
    
    private SpeedGrapher graph;

    /**
     * Creates a new Victor speed controller.
     * @param channel The Digital Sidecar channel it should be connected to.
     */
    public Victor(int channel) {
            
        }

	/**
     * Sets the value of the Victor using a value between -1.0 and +1.0.
     * @param speed The speed value of the Victor between -1.0 and +1.0.
     */
    public void set(double speed) {
        this.speed = speed;

    }

    /**
     * Gets the most recent value of the Victor.
     * @return The most recent value of the Victor from -1.0 and +1.0.
     */
    public double get() {
        return speed;
    }
    
    //add pidWrite method?
    
	public void componentResized(ComponentEvent e) {
		graph.setGraphSize(frame.getWidth(), frame.getHeight());
		graph.repaint();
	}
    
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startStop) {
			startStop.setText((isGraphRunning ? "Start" : "Stop") + " Graph");
			isGraphRunning = !isGraphRunning;
		}
	}
	
	//extra stuffs
	public void componentShown(ComponentEvent e) {}
	public void componentHidden(ComponentEvent e) {}
	public void componentMoved(ComponentEvent e) {}

}