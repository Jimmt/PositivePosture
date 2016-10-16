package com.austinhsieh;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;

public class CustomLabel extends JLabel {
    public CustomLabel(String str) {
        super(str);
        setForeground(Color.white);
        this.setFont(new Font("Arial", Font.ITALIC, 30));
    }
}
