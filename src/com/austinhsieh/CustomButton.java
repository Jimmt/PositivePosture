package com.austinhsieh;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

public class CustomButton extends JButton implements MouseListener {
    String text;
    boolean drawRect = false;
    Color opaqueWhite;
    Color drawColor;

    public CustomButton(String text) {
        super(text);
        
        opaqueWhite = new Color(255, 255, 255, 100);
        drawColor = opaqueWhite;
        
        setContentAreaFilled(false);
        //        setBorderPainted(false);
        //        setOpaque(false);

        setFocusPainted(false);
        Border lineBorder = BorderFactory.createLineBorder(Color.WHITE, 3);
        setBorder(BorderFactory.createCompoundBorder(lineBorder,
        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        setForeground(Color.white);
        this.setFont(new Font("Arial", Font.ITALIC, 30));
        this.text = text;

        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (drawRect) {
            g.setColor(drawColor);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        drawRect = true;
        drawColor = opaqueWhite;
        repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        drawRect = false;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        drawRect = true;
        drawColor = Color.white;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawRect = false;
        repaint();

    }

}
