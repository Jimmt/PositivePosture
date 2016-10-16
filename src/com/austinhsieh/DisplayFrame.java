package com.austinhsieh;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DisplayFrame extends JFrame implements WindowListener {
    PositivePosture pp;
    DisplayPanel panel;
    CustomLabel calibratingLabel, captureLabel;

    public DisplayFrame(PositivePosture pp, String name, int width, int height) {
        super(name);

        this.pp = pp;
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(this);
        panel = new DisplayPanel();

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());
        containerPanel.add(panel, BorderLayout.CENTER);
        JPanel uiPanel = new JPanel();
        uiPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        uiPanel.setBackground(Color.DARK_GRAY);

        calibratingLabel = new CustomLabel("Calibrating...");
        calibratingLabel.setVisible(false);
        uiPanel.add(calibratingLabel);
        uiPanel.add(Box.createHorizontalStrut(100));
        CustomButton calibrateButton = new CustomButton("Calibrate");
        uiPanel.add(calibrateButton);
        calibrateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pp.runCalibrateCapture();
            }
        });

        CustomButton runButton = new CustomButton("Run");
        uiPanel.add(runButton);
        runButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pp.runCapture();
            }
        });
        uiPanel.add(Box.createHorizontalStrut(100));
        captureLabel = new CustomLabel("Running...");
        uiPanel.add(captureLabel);
        captureLabel.setVisible(false);

        containerPanel.add(uiPanel, BorderLayout.SOUTH);
        add(containerPanel);
        setVisible(true);
    }

    @Override
    public void windowActivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent arg0) {
        if (pp.captureThread != null)
            pp.captureThread.run = false;
    }

    @Override
    public void windowDeactivated(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent arg0) {
        // TODO Auto-generated method stub

    }
}
