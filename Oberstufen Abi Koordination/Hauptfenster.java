// Hauptfenster.java
import javax.swing.*;
    import javax.swing.border.EmptyBorder;
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

            setTitle("Abi-Ball Planer – Verwaltungsportal");
            setSize(1280, 820);
            setMinimumSize(new Dimension(1100, 720));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JPanel root = new JPanel(new BorderLayout());
            root.setBackground(UIStyle.BG);
            setContentPane(root);

            JPanel sidebar = createSidebar();
            root.add(sidebar, BorderLayout.WEST);

            JPanel topBar = createTopBar();
            root.add(topBar, BorderLayout.NORTH);

            contentPanel = new JPanel(new BorderLayout());
            contentPanel.setBackground(new Color(248, 250, 252)); // Figma Hintergrund
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.add(new DashboardPanel(sv, kv, ev, null, null, null), BorderLayout.CENTER);
            root.add(contentPanel, BorderLayout.CENTER);
            add(contentPanel, BorderLayout.CENTER);

          zeigeDashboard();
        }

        private JPanel createSidebar() {
            JPanel sidebar = new JPanel();
            sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
            sidebar.setBackground(Color.WHITE);
            sidebar.setBorder(new EmptyBorder(20, 8, 20, 8));

            // Logo
            JLabel logo = new JLabel("AB");
            logo.setOpaque(true);
            logo.setBackground(UIStyle.BLUE);
            logo.setForeground(Color.WHITE);
            logo.setHorizontalAlignment(SwingConstants.CENTER);
            logo.setPreferredSize(new Dimension(40, 40));
            logo.setMaximumSize(new Dimension(40, 40));

            JLabel title = new JLabel("Abi Ball Planer");
            title.setFont(new Font("SansSerif", Font.BOLD, 16));

            JPanel top = new JPanel();
            top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
            top.setOpaque(false);
            top.setAlignmentX(Component.LEFT_ALIGNMENT);

            top.add(logo);
            top.add(Box.createVerticalStrut(8));
            top.add(title);

            sidebar.add(top);
            sidebar.add(Box.createVerticalStrut(30));

            // 🔥 NAV BUTTONS MIT RICHTIGER LOGIK

            JButton dashboardBtn = createSidebarButton("🏠 Übersicht");
            dashboardBtn.addActionListener(e -> zeigeDashboard());

            JButton schuelerBtn = createSidebarButton("👥 Schüler");
            schuelerBtn.addActionListener(e -> zeigeSchueler());

            JButton komiteeBtn = createSidebarButton("👨‍👩‍👧‍👦 Komitees");
            komiteeBtn.addActionListener(e -> zeigeKomitees());

            JButton eventBtn = createSidebarButton("📅 Events");
            eventBtn.addActionListener(e -> zeigeEvents());

            // 🔥 WICHTIG: Buttons speichern für Active State
            navButtons.put("Übersicht", dashboardBtn);
            navButtons.put("Schüler", schuelerBtn);
            navButtons.put("Komitees", komiteeBtn);
            navButtons.put("Events", eventBtn);

            // 🔥 WICHTIG: Buttons zur Sidebar hinzufügen
            sidebar.add(dashboardBtn);
            sidebar.add(Box.createVerticalStrut(8));
            sidebar.add(schuelerBtn);
            sidebar.add(Box.createVerticalStrut(8));
            sidebar.add(komiteeBtn);
            sidebar.add(Box.createVerticalStrut(8));
            sidebar.add(eventBtn);

            return sidebar;
        }

        private JPanel createTopBar() {
            JPanel top = new JPanel(new BorderLayout());
            top.setBackground(Color.WHITE);
            top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIStyle.BORDER));
            top.setPreferredSize(new Dimension(0, 64));

            JPanel searchWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 14));
            searchWrap.setOpaque(false);
            JTextField searchField = UIStyle.createSearchField("Suchen...");
            searchField.setPreferredSize(new Dimension(240, 36));
            searchWrap.add(searchField);

            JPanel icons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 14));
            icons.setOpaque(false);
            JLabel bell = new JLabel("🔔");
            bell.setFont(new Font("SansSerif", Font.PLAIN, 18));
            bell.setForeground(UIStyle.MUTED);
            JLabel user = new JLabel("👤");
            user.setFont(new Font("SansSerif", Font.PLAIN, 18));
            user.setForeground(UIStyle.MUTED);
            icons.add(bell);
            icons.add(user);

            top.add(searchWrap, BorderLayout.WEST);
            top.add(icons, BorderLayout.EAST);
            return top;
        }

        private JButton createSidebarButton(String text) {
            JButton btn = new JButton(text);
            btn.setFocusPainted(false);
            btn.setBackground(Color.WHITE);
            btn.setForeground(UIStyle.TEXT);

            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);

            btn.setBorder(new EmptyBorder(12, 10, 12, 10)); // 🔥 wenig left padding
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

            return btn;
        }

        private void setActiveNav(String active) {
            for (Map.Entry<String, JButton> entry : navButtons.entrySet()) {
                JButton btn = entry.getValue();
                if (entry.getKey().equals(active)) {
                    btn.setBackground(UIStyle.SOFT_PURPLE);
                    btn.setForeground(UIStyle.PURPLE);
                    btn.setFont(new Font("SansSerif", Font.BOLD, 15));
                } else {
                    btn.setBackground(Color.WHITE);
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
            setActiveNav("Übersicht");
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

        private void switchView(JPanel newPanel) {
            contentPanel.removeAll();
            contentPanel.add(newPanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }