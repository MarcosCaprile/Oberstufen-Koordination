import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class UIStyle {

    public static final Color BG = new Color(245, 247, 251);
    public static final Color SURFACE = Color.WHITE;
    public static final Color BORDER = new Color(229, 231, 235);
    public static final Color TEXT = new Color(31, 41, 55);
    public static final Color MUTED = new Color(107, 114, 128);

    public static final Color BLUE = new Color(37, 99, 235);
    public static final Color GREEN = new Color(22, 163, 74);
    public static final Color PURPLE = new Color(147, 51, 234);
    public static final Color ORANGE = new Color(249, 115, 22);
    public static final Color RED = new Color(239, 68, 68);

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
                new EmptyBorder(18, 18, 18, 18)
        );
    }

    public static Border panelPadding(int top, int left, int bottom, int right) {
        return new EmptyBorder(top, left, bottom, right);
    }

    public static JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(SURFACE);
        panel.setBorder(cardBorder());
        return panel;
    }

    public static JButton createPrimaryButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(color);
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
        button.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
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
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(12, 14, 12, 14)
        ));
        field.setToolTipText(placeholder);
        return field;
    }
}