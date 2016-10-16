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

    public DisplayPanel() {
        camPic = new BufferedImage(1280, 1000, BufferedImage.TYPE_INT_ARGB);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    public void drawImage(BufferedImage image) {
        camPic = image;
        Image scaledCamPic = camPic.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        getGraphics().drawImage(scaledCamPic, 0, 0, null);
    }

    public void drawFaceRectangle(Color color, int left, int top, int width, int height) {
        Graphics2D g2d = (Graphics2D) getGraphics();
        g2d.setStroke(new BasicStroke(5));
        g2d.setColor(color);
        g2d.drawRect(left, top, width, height);
    }
}
