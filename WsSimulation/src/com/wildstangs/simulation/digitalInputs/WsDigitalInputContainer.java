/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation.digitalInputs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author coder65535
 */
public class WsDigitalInputContainer implements KeyListener
{

    public WsDigitalInputSimulation[] inputs;
    JFrame frame;
    private static WsDigitalInputContainer instance = null;

    private WsDigitalInputContainer()
    {
        inputs = new WsDigitalInputSimulation[16];
        for (int i = 0; i < 16; i++)
        {
            int channel; 
            if (i == 0 ){
                channel = 10; 
            } else if (i >= 10){
                channel = i + 1 ;  
            } else {
                channel = i ; 
            }
            inputs[i] = new WsDigitalInputSimulation(channel);
        }
        frame = new JFrame("Digital Input Emulator");
        JLabel label = new JLabel("Focus and Press 1-9 to toggle Inputs ");
        JLabel label1 = new JLabel("Press 0 to toggle input 10");
        JLabel label2 = new JLabel("Press QWERTY to toggle 11-16");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(300, 100));
        frame.setLayout(new BorderLayout());
        frame.addKeyListener(this);
        frame.setLocation(0, 320);
        frame.add(label, BorderLayout.NORTH);
        frame.add(label1, BorderLayout.CENTER);
        frame.add(label2, BorderLayout.AFTER_LAST_LINE);

        frame.pack();
        frame.setVisible(true);
    }

    public static WsDigitalInputContainer getInstance()
    {
        if (instance == null)
        {
            instance = new WsDigitalInputContainer();
        }
        return instance;
    }

    public void keyPressed(KeyEvent e)
    {
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
        int key = (int) e.getKeyChar() - 48;
        //Handle if it is not a digit
        if (!(0 <= key && key < 10))
        {
            switch (e.getKeyChar()){
                case 'Q':
                case 'q':
                    key = 10; 
                    break; 
                case 'W':
                case 'w':
                    key = 11; 
                    break; 
                case 'E':
                case 'e':
                    key = 12; 
                    break; 
                case 'R':
                case 'r':
                    key = 13; 
                    break; 
                case 'T':
                case 't':
                    key = 14; 
                    break; 
                case 'Y':
                case 'y':
                    key = 15; 
                    break; 
                    
            }
        } 
        if (key <= 15) {
            if (inputs[key] != null) {
                inputs[key].flip();
            }
        }
    }
}
