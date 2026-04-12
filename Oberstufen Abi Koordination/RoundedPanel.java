import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class RoundedPanel extends JPanel {

    private int radius;

    public RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
        setBorder(new EmptyBorder(20, 20, 20, 20));
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        g2.dispose();
        super.paintComponent(g);
    }
}