// UIStyle.java
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class UIStyle {

    public static final Color BG = new Color(245, 247, 251);
    public static final Color SURFACE = Color.WHITE;
    public static final Color BORDER = new Color(229, 231, 235);
    public static final Color TEXT = new Color(15, 23, 42);
    public static final Color MUTED = new Color(71, 85, 105);

    public static final Color BLUE = new Color(79, 70, 229);
    public static final Color GREEN = new Color(16, 185, 129);
    public static final Color PURPLE = new Color(124, 58, 237);
    public static final Color ORANGE = new Color(249, 115, 22);
    public static final Color RED = new Color(239, 68, 68);

    public static final Color SOFT_BLUE = new Color(219, 234, 254);
    public static final Color SOFT_GREEN = new Color(220, 252, 231);
    public static final Color SOFT_PURPLE = new Color(233, 213, 255);
    public static final Color SOFT_ORANGE = new Color(255, 237, 213);
    public static final Color SOFT_RED = new Color(254, 226, 226);

    public static Font titleFont() {
        return new Font("SansSerif", Font.BOLD, 32);
    }

    public static Font sectionFont() {
        return new Font("SansSerif", Font.BOLD, 18);
    }

    public static Font bodyFont() {
        return new Font("SansSerif", Font.PLAIN, 14);
    }

    public static Font smallFont() {
        return new Font("SansSerif", Font.PLAIN, 12);
    }

    public static Border cardBorder() {
        return new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(20, 20, 20, 20)
        );
    }

    public static Border panelPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    public static JPanel createCard() {
        JPanel card = new RoundedPanel(20);
        card.setBackground(SURFACE);
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(20, 20, 20, 20));
        return card;
    }

    public static JPanel createBigActionButton(String text, Color bg, Runnable action) {
        JPanel card = new RoundedPanel(20);
        card.setBackground(bg);
        card.setLayout(new GridBagLayout());
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setForeground(Color.WHITE);

        card.add(label);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(bg.darker());
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(bg);
            }

            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });

        return card;
    }

    public static JButton createPrimaryButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(TEXT);
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(10, 18, 10, 18)
        ));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JButton createIconButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(color);
        button.setBackground(Color.WHITE);
        button.setOpaque(true);
        button.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(4, 8, 4, 8)
        ));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static JLabel createMutedLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MUTED);
        label.setFont(bodyFont());
        return label;
    }

    public static JLabel createChip(String text, Color bg, Color fg) {
        JLabel label = new JLabel(text);
        label.setOpaque(true);
        label.setBackground(bg);
        label.setForeground(fg);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setBorder(new EmptyBorder(4, 10, 4, 10));
        return label;
    }

    public static JTextField createSearchField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(bodyFont());
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyle.BORDER, 1, true),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setToolTipText(placeholder);
        return field;
    }
}