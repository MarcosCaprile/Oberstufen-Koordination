import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    public DashboardPanel(SchuelerVerwaltung sv,
                          KomiteeVerwaltung kv,
                          EventVerwaltung ev,
                          Runnable onNeueSchueler,
                          Runnable onNeuesKomitee,
                          Runnable onNeuesEvent) {

        setLayout(new BorderLayout());
        setBackground(UIStyle.BG);
        setBorder(UIStyle.panelPadding(28, 28, 28, 28));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Übersicht");
        heading.setFont(new Font("SansSerif", Font.BOLD, 34));
        heading.setForeground(UIStyle.TEXT);

        JLabel sub = new JLabel("Willkommen im Verwaltungssystem für Schüler, Komitees und Events");
        sub.setFont(UIStyle.bodyFont());
        sub.setForeground(UIStyle.MUTED);

        content.add(heading);
        content.add(Box.createVerticalStrut(8));
        content.add(sub);
        content.add(Box.createVerticalStrut(26));

        JPanel stats = new JPanel(new GridLayout(1, 4, 18, 18));
        stats.setOpaque(false);

        int gesamtPunkte = 0;
        for (Schueler s : sv.getAlleSchueler()) {
            gesamtPunkte += ev.berechnePunkteFuerSchueler(s.getId());
        }

        stats.add(createStatCard("👥", "Schüler gesamt", String.valueOf(sv.getAnzahlSchueler()), UIStyle.BLUE));
        stats.add(createStatCard("🧑‍🤝‍🧑", "Aktive Komitees", String.valueOf(kv.getAnzahlKomitees()), UIStyle.GREEN));
        stats.add(createStatCard("📅", "Geplante Events", String.valueOf(ev.getAnzahlEvents()), UIStyle.PURPLE));
        stats.add(createStatCard("🏅", "Vergebene Punkte", String.valueOf(gesamtPunkte), UIStyle.ORANGE));

        content.add(stats);
        content.add(Box.createVerticalStrut(28));

        JPanel quick = UIStyle.createCard();
        quick.setLayout(new BorderLayout());
        quick.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JLabel quickTitle = new JLabel("Schnellaktionen");
        quickTitle.setFont(UIStyle.sectionFont());
        quickTitle.setForeground(UIStyle.TEXT);
        quick.add(quickTitle, BorderLayout.NORTH);

        JPanel actions = new JPanel(new GridLayout(1, 3, 14, 14));
        actions.setOpaque(false);
        actions.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));

        actions.add(createQuickAction("👥", "Neuer Schüler", onNeueSchueler));
        actions.add(createQuickAction("🧑‍🤝‍🧑", "Neues Komitee", onNeuesKomitee));
        actions.add(createQuickAction("📅", "Neues Event", onNeuesEvent));

        quick.add(actions, BorderLayout.CENTER);
        content.add(quick);

        add(content, BorderLayout.NORTH);
    }

    private JPanel createStatCard(String icon, String label, String value, Color accent) {
        JPanel card = UIStyle.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(accent);
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 20));
        iconLabel.setPreferredSize(new Dimension(42, 42));
        iconLabel.setMaximumSize(new Dimension(42, 42));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JLabel text = new JLabel(label);
        text.setForeground(UIStyle.MUTED);
        text.setFont(UIStyle.bodyFont());

        JLabel val = new JLabel(value);
        val.setForeground(UIStyle.TEXT);
        val.setFont(new Font("SansSerif", Font.BOLD, 30));

        card.add(iconLabel);
        card.add(Box.createVerticalStrut(18));
        card.add(text);
        card.add(Box.createVerticalStrut(8));
        card.add(val);

        return card;
    }

    private JButton createQuickAction(String icon, String text, Runnable action) {
        JButton button = new JButton("<html><div style='text-align:center;'><div style='font-size:24px;'>" +
                icon + "</div><div style='margin-top:8px; font-weight:bold; color:#6B7280;'>" + text + "</div></div></html>");
        button.setFocusPainted(false);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createDashedBorder(UIStyle.BORDER, 5, 5));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> action.run());
        return button;
    }
}