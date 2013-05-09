package com.wildstangs.graph;

import edu.wpi.first.wpilibj.*;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DoubleGraph implements ComponentListener, ActionListener{

    private double realValue;
    private double value;
    private long startTime;
    private boolean isGraphRunning;
    private JFrame frame;
    private JLabel valueLabel;
    private JButton startStop;
    private SpeedGrapher graph;

    
    public DoubleGraph(){
        
    }
    /**
     * Creates a new double subject grapher.
     */
    public DoubleGraph(String name ,int x , int y ) {
        frame = new JFrame("Double: " + name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setLocation(x, y);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(300, 320));

        //Tells the current value of the subject in % above the graph.
        valueLabel = new JLabel("Current Value: " + (value ) );
        frame.add(valueLabel, BorderLayout.NORTH);

        //Allows user to stop the movement of the graph. Button located under the graph.
        startStop = new JButton("Stop Graph");
        startStop.addActionListener(this);
        frame.add(startStop, BorderLayout.SOUTH);

        //Makes the actual graph.
        graph = new SpeedGrapher(300, 300);
        frame.add(graph, BorderLayout.CENTER);

        startTime = 0;
        isGraphRunning = true;

        frame.addComponentListener(this);

        frame.pack();
        frame.setVisible(true);

    }

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
    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void update() {
        if (System.currentTimeMillis() - startTime > 35 && isGraphRunning) {
            graph.appendSpeed(value);
            startTime = System.currentTimeMillis();
        }
        valueLabel.setText("Value: " + realValue );
    }

    public void updateWithValue(double value, double maxValue) {
        this.realValue = value;
        this.value = value/ maxValue;
        update(); 
    }
}