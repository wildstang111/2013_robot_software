/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wildstangs.simulation.solenoids;

import edu.wpi.first.wpilibj.Solenoid;
import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Rick
 */
public class WsSolenoidContainer
{

    private static WsSolenoidContainer object;
    private JFrame solenoidWindow = null;
    private JScrollPane scrollPane = null;
    private JPanel panel = null;
    private JPanel lowerPanel = null;
    private static final int sizeX = 400;
    private static final int frameY = 256;
    private static final int panelY = 384;
    private static final int modules = 2;
    private static final int channels = 8;
    private Solenoid solenoids[][] = new Solenoid[modules][channels];
    private JLabel labels[][] = new JLabel[modules][channels];

    public static WsSolenoidContainer getInstance()
    {
        if (object == null)
        {
            object = new WsSolenoidContainer();
        }
        return object;
    }

    public void add(Solenoid s, int module, int channel)
    {
        if ((module > solenoids.length) || (module < 1))
        {
            return;
        }
        if ((channel > solenoids[0].length) || (channel < 1))
        {
            return;
        }
        solenoids[module - 1][channel - 1] = s;
    }

    public void update()
    {
        for (int cy = 0; cy < modules; cy++)
        {
            for (int cx = 0; cx < channels; cx++)
            {
                Solenoid s = solenoids[cy][cx];
                if (s == null)
                {
                    continue;
                }

                labels[cy][cx].setText(String.format("%d - %d : %s", cy + 1, cx + 1, s.get()));
            }
        }
        solenoidWindow.invalidate();
        solenoidWindow.validate();
    }

    private WsSolenoidContainer()
    {
        solenoidWindow = new JFrame("Solenoids");
        solenoidWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        solenoidWindow.setSize(sizeX, frameY);
        solenoidWindow.setResizable(false);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        lowerPanel = new JPanel();
        lowerPanel.setBackground(Color.white);
        lowerPanel.setPreferredSize(new Dimension(sizeX, panelY));
        lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));

        Font f = new Font("Arial", Font.PLAIN, 18);
        for (int cy = 0; cy < modules; cy++)
        {
            for (int cx = 0; cx < channels; cx++)
            {
                labels[cy][cx] = new JLabel(String.format(String.format("%d - %d : Unused", cy + 1, cx + 1)));
                labels[cy][cx].setFont(f);
                lowerPanel.add(labels[cy][cx]);
            }
        }

        scrollPane = new JScrollPane(lowerPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);
        solenoidWindow.getContentPane().add(panel);
        solenoidWindow.setLocationRelativeTo(null);
        solenoidWindow.setVisible(true);
    }
}
