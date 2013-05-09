package edu.wpi.first.wpilibj;

import com.wildstangs.subjects.base.DoubleSubject;
import com.wildstangs.subjects.base.IObserver;
import com.wildstangs.subjects.base.Subject;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class DoubleSubjectGraph implements ComponentListener, ActionListener, IObserver {

    private double value;
    private long startTime;
    private boolean isGraphRunning;
    private JFrame frame;
    private JLabel valueLabel;
    private JButton startStop;
    private SpeedGrapher graph;

    public DoubleSubjectGraph(){
        
    }
    /**
     * Creates a new double subject grapher.
     */
    public DoubleSubjectGraph(String name, Subject subject) {
        frame = new JFrame("Double Subject: " + name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setResizable(false);
        frame.setLocation(510, 0);
        frame.setLayout(new BorderLayout());
        frame.setPreferredSize(new Dimension(300, 320));

        //Tells the current value of the subject in % above the graph.
        valueLabel = new JLabel("Current Value: " + (value * 100) + "%");
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

        //Attach to the subject
        subject.attach(this);
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
        valueLabel.setText((int) ((value * 100) * 10) / 10.0 + "%");
    }

    @Override
    public void acceptNotification(Subject subjectThatCaused) {
        this.value = ((DoubleSubject) subjectThatCaused).getValue();
    }
}