package com.austinhsieh;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DisplayPanel extends JPanel {
    public BufferedImage camPic;
    public Image scaledCamPic;

    public DisplayPanel() {
        camPic = new BufferedImage(1280, 1000, BufferedImage.TYPE_INT_ARGB);
        setBackground(Color.DARK_GRAY);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    public void drawImage(BufferedImage image) {
        if (image != null) {
            camPic = image;
            scaledCamPic = camPic.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
            getGraphics().drawImage(scaledCamPic, 0, 0, null);
        }
    }

    public void drawFaceRectangle(Color color, int left, int top, int width, int height) {
        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(color);
        g2d.drawRect(left, top, width, height);
    }
}
