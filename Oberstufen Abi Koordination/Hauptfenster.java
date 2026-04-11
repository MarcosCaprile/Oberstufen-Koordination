import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Hauptfenster extends JFrame {

    private final SchuelerVerwaltung schuelerVerwaltung;
    private final KomiteeVerwaltung komiteeVerwaltung;
    private final EventVerwaltung eventVerwaltung;

    private final JPanel contentPanel;
    private final Map<String, JButton> navButtons;

    public Hauptfenster(SchuelerVerwaltung sv, KomiteeVerwaltung kv, EventVerwaltung ev) {
        this.schuelerVerwaltung = sv;
        this.komiteeVerwaltung = kv;
        this.eventVerwaltung = ev;
        this.navButtons = new LinkedHashMap<>();

        setTitle("Schülerverwaltung");
        setSize(1280, 820);
        setMinimumSize(new Dimension(1100, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UIStyle.BG);
        setContentPane(root);

        root.add(createTopNavigation(), BorderLayout.NORTH);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(UIStyle.BG);
        root.add(contentPanel, BorderLayout.CENTER);

        zeigeDashboard();
    }

    private JPanel createTopNavigation() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyle.BORDER));
        top.setPreferredSize(new Dimension(0, 84));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 18));
        left.setOpaque(false);

        JPanel logo = new JPanel();
        logo.setBackground(UIStyle.BLUE);
        logo.setPreferredSize(new Dimension(34, 34));
        logo.setLayout(new GridBagLayout());
        JLabel logoIcon = new JLabel("👥");
        logoIcon.setForeground(Color.WHITE);
        logo.add(logoIcon);

        JLabel title = new JLabel("Schülerverwaltung");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(UIStyle.TEXT);

        left.add(logo);
        left.add(title);

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 22));
        nav.setOpaque(false);

        JButton dashboardBtn = createNavButton("Dashboard");
        JButton schuelerBtn = createNavButton("Schüler");
        JButton komiteeBtn = createNavButton("Komitees");
        JButton eventBtn = createNavButton("Events");

        dashboardBtn.addActionListener(e -> zeigeDashboard());
        schuelerBtn.addActionListener(e -> zeigeSchueler());
        komiteeBtn.addActionListener(e -> zeigeKomitees());
        eventBtn.addActionListener(e -> zeigeEvents());

        navButtons.put("Dashboard", dashboardBtn);
        navButtons.put("Schüler", schuelerBtn);
        navButtons.put("Komitees", komiteeBtn);
        navButtons.put("Events", eventBtn);

        nav.add(dashboardBtn);
        nav.add(schuelerBtn);
        nav.add(komiteeBtn);
        nav.add(eventBtn);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(left, BorderLayout.WEST);
        wrapper.add(nav, BorderLayout.CENTER);

        top.add(wrapper, BorderLayout.CENTER);
        return top;
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 6, 10, 6));
        button.setContentAreaFilled(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setFont(new Font("SansSerif", Font.PLAIN, 15));
        button.setForeground(UIStyle.MUTED);
        return button;
    }

    private void setActiveNav(String active) {
        for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
            JButton btn = entry.getValue();
            if (entry.getKey().equals(active)) {
                btn.setForeground(UIStyle.BLUE);
                btn.setFont(new Font("SansSerif", Font.BOLD, 15));
            } else {
                btn.setForeground(UIStyle.MUTED);
                btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
            }
        }
    }

    private void zeigePanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void zeigeDashboard() {
        setActiveNav("Dashboard");
        zeigePanel(new DashboardPanel(
                schuelerVerwaltung,
                komiteeVerwaltung,
                eventVerwaltung,
                this::zeigeSchueler,
                this::zeigeKomitees,
                this::zeigeEvents
        ));
    }

    private void zeigeSchueler() {
        setActiveNav("Schüler");
        zeigePanel(new SchuelerPanel(schuelerVerwaltung, eventVerwaltung));
    }

    private void zeigeKomitees() {
        setActiveNav("Komitees");
        zeigePanel(new KomiteePanel(komiteeVerwaltung, schuelerVerwaltung));
    }

    private void zeigeEvents() {
        setActiveNav("Events");
        zeigePanel(new EventPanel(eventVerwaltung, schuelerVerwaltung));
    }


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