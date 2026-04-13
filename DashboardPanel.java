// DashboardPanel.java
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    public DashboardPanel(SchuelerVerwaltung sv,
                          KomiteeVerwaltung kv,
                          EventVerwaltung ev,
                          Runnable onNeueSchueler,
                          Runnable onNeuesKomitee,
                          Runnable onNeuesEvent) {

        setLayout(new BorderLayout());
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("Übersicht");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(title);
        header.add(Box.createVerticalStrut(5));
        setBackground(UIStyle.BG);
        setBorder(UIStyle.panelPadding(24, 24, 24, 24));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Titel und Untertitel
        JLabel heading = new JLabel("Übersicht");
        heading.setFont(UIStyle.titleFont());
        heading.setForeground(UIStyle.TEXT);

        content.add(heading);
        content.add(Box.createVerticalStrut(6));
        content.add(Box.createVerticalStrut(24));

        // Statistik-Kacheln
        int gesamtPunkte = 0;
        for (Schueler s : sv.ladeAlleSchueler()) {
            gesamtPunkte += ev.berechnePunkteFuerSchueler(s.getId());
        }

        JPanel stats = new JPanel(new GridLayout(1, 4, 16, 16));
        stats.setOpaque(false);
        stats.add(createStatCard("👩‍🎓", "Schüler gesamt", String.valueOf(sv.getAnzahlSchueler()), UIStyle.BLUE));
        stats.add(createStatCard("👥", "Aktive Komitees", String.valueOf(kv.getAnzahlKomitees()), UIStyle.GREEN));
        stats.add(createStatCard("📅", "Geplante Events", String.valueOf(ev.getAnzahlEvents()), UIStyle.PURPLE));
        stats.add(createStatCard("🏅", "Vergebene Punkte", String.valueOf(gesamtPunkte), UIStyle.ORANGE));
        content.add(stats);
        content.add(Box.createVerticalStrut(24));

        // Schnellaktionen
        JPanel quickActions = new JPanel(new GridLayout(1, 3, 16, 16));
        quickActions.setOpaque(false);
        quickActions.add(createQuickCard("➕", "Neuer Schüler", UIStyle.SOFT_BLUE, UIStyle.BLUE, onNeueSchueler));
        quickActions.add(createQuickCard("➕", "Neues Komitee", UIStyle.SOFT_GREEN, UIStyle.GREEN, onNeuesKomitee));
        quickActions.add(createQuickCard("➕", "Neues Event", UIStyle.SOFT_PURPLE, UIStyle.PURPLE, onNeuesEvent));
        content.add(quickActions);
        content.add(Box.createVerticalStrut(24));

        // Sektionen: Letzte Aktivitäten und Nächste Events
        JPanel overview = new JPanel(new GridLayout(1, 2, 16, 16));
        overview.setOpaque(false);
        overview.add(createActivityCard(ev));
        overview.add(createNextEventsCard(ev));
        content.add(overview);

        add(content, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String emoji, String label, String value, Color accent) {
        JPanel card = new RoundedPanel(20);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel icon = new JLabel(emoji, SwingConstants.CENTER);
        icon.setOpaque(true);
        icon.setBackground(accent);
        icon.setForeground(Color.WHITE);
        icon.setFont(new Font("SansSerif", Font.PLAIN, 24));
        icon.setPreferredSize(new Dimension(48, 48));
        icon.setMaximumSize(new Dimension(48, 48));
        icon.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel lbl = new JLabel(label);
        lbl.setFont(UIStyle.bodyFont());
        lbl.setForeground(UIStyle.MUTED);

        JLabel val = new JLabel(value);
        val.setFont(new Font("SansSerif", Font.BOLD, 32));
        val.setForeground(UIStyle.TEXT);

        card.add(icon);
        card.add(Box.createVerticalStrut(12));
        card.add(lbl);
        card.add(Box.createVerticalStrut(4));
        card.add(val);
        return card;
    }

    private JPanel createQuickCard(String emoji, String label, Color tint, Color accent, Runnable action) {
        JPanel card = new RoundedPanel(20);
        card.setBackground(tint);
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel icon = new JLabel(emoji, SwingConstants.CENTER);
        icon.setOpaque(true);
        icon.setBackground(accent);
        icon.setForeground(Color.WHITE);
        icon.setFont(new Font("SansSerif", Font.BOLD, 24));
        icon.setPreferredSize(new Dimension(40, 40));
        icon.setMaximumSize(new Dimension(40, 40));
        icon.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel txt = new JLabel(label);
        txt.setAlignmentX(Component.CENTER_ALIGNMENT);
        txt.setFont(new Font("SansSerif", Font.BOLD, 14));
        txt.setForeground(UIStyle.TEXT);
        txt.setBorder(new EmptyBorder(8, 0, 0, 0));

        card.add(icon);
        card.add(txt);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                action.run();
            }
        });
        return card;
    }

    private JPanel createActivityCard(EventVerwaltung ev) {
        JPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        JLabel title = new JLabel("Letzte Aktivitäten");
        title.setFont(UIStyle.sectionFont());
        title.setForeground(UIStyle.TEXT);
        card.add(title, BorderLayout.NORTH);

        List<Event> events = new java.util.ArrayList<>(ev.ladeAlleEvents());
        java.util.Collections.sort(events, (a, b) -> {
            java.time.LocalDateTime da = parseDateTime(a.getDatum(), a.getUhrzeit());
            java.time.LocalDateTime db = parseDateTime(b.getDatum(), b.getUhrzeit());
            return db.compareTo(da);
        });

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        int count = 0;
        for (Event e : events) {
            if (count >= 4) break;
            JPanel item = new JPanel(new BorderLayout());
            item.setOpaque(false);
            JLabel name = new JLabel(e.getName());
            name.setFont(UIStyle.bodyFont());
            name.setForeground(UIStyle.TEXT);
            JLabel date = new JLabel(e.getDatum() + " · " + e.getUhrzeit());
            date.setFont(UIStyle.smallFont());
            date.setForeground(UIStyle.MUTED);
            item.add(name, BorderLayout.NORTH);
            item.add(date, BorderLayout.SOUTH);
            list.add(item);
            list.add(Box.createVerticalStrut(8));
            count++;
        }
        if (events.isEmpty()) {
            JLabel empty = new JLabel("Keine Aktivitäten verfügbar");
            empty.setFont(UIStyle.bodyFont());
            empty.setForeground(UIStyle.MUTED);
            list.add(empty);
        }
        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private JPanel createNextEventsCard(EventVerwaltung ev) {
        JPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());
        JLabel title = new JLabel("Nächste Events");
        title.setFont(UIStyle.sectionFont());
        title.setForeground(UIStyle.TEXT);
        card.add(title, BorderLayout.NORTH);

        java.util.List<Event> events = new java.util.ArrayList<>(ev.ladeAlleEvents());
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        events.removeIf(e -> parseDateTime(e.getDatum(), e.getUhrzeit()).isBefore(now));
        java.util.Collections.sort(events, (a, b) -> {
            java.time.LocalDateTime da = parseDateTime(a.getDatum(), a.getUhrzeit());
            java.time.LocalDateTime db = parseDateTime(b.getDatum(), b.getUhrzeit());
            return da.compareTo(db);
        });

        JPanel list = new JPanel();
        list.setOpaque(false);
        list.setLayout(new BoxLayout(list, BoxLayout.Y_AXIS));
        int count = 0;
        for (Event e : events) {
            if (count >= 4) break;
            JPanel row = new JPanel();
            row.setOpaque(false);
            row.setLayout(new BoxLayout(row, BoxLayout.Y_AXIS));

            JLabel name = new JLabel(e.getName());
            name.setFont(UIStyle.bodyFont());
            name.setForeground(UIStyle.TEXT);
            JLabel meta = new JLabel(e.getDatum() + " · " + e.getUhrzeit() + " · " + e.getOrt());
            meta.setFont(UIStyle.smallFont());
            meta.setForeground(UIStyle.MUTED);

            int teilnehmer = ev.getTeilnehmerAnzahl(e.getId());
            int max = e.getMaxTeilnehmer();
            JProgressBar bar = new JProgressBar(0, max > 0 ? max : 1);
            bar.setValue(teilnehmer);
            bar.setStringPainted(true);
            bar.setString(teilnehmer + "/" + max);
            bar.setForeground(UIStyle.PURPLE);
            bar.setBackground(UIStyle.SOFT_PURPLE);
            bar.setBorder(new javax.swing.border.LineBorder(UIStyle.SOFT_PURPLE, 1, true));
            bar.setPreferredSize(new Dimension(200, 8));

            JLabel status = e.isAbgeschlossen()
                    ? UIStyle.createChip("Abgeschlossen", UIStyle.SOFT_GREEN, UIStyle.GREEN)
                    : UIStyle.createChip("Geplant", UIStyle.SOFT_ORANGE, UIStyle.ORANGE);

            JPanel topLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            topLine.setOpaque(false);
            topLine.add(name);
            topLine.add(Box.createHorizontalStrut(8));
            topLine.add(status);

            row.add(topLine);
            row.add(meta);
            row.add(Box.createVerticalStrut(4));
            row.add(bar);
            row.setBorder(new EmptyBorder(0, 0, 12, 0));

            list.add(row);
            count++;
        }
        if (events.isEmpty()) {
            JLabel empty = new JLabel("Keine bevorstehenden Events verfügbar");
            empty.setFont(UIStyle.bodyFont());
            empty.setForeground(UIStyle.MUTED);
            list.add(empty);
        }

        JScrollPane scroll = new JScrollPane(list);
        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private static java.time.LocalDateTime parseDateTime(String date, String time) {
        try {
            java.time.LocalDate d = java.time.LocalDate.parse(date);
            java.time.LocalTime t = java.time.LocalTime.parse(time);
            return java.time.LocalDateTime.of(d, t);
        } catch (Exception ex) {
            return java.time.LocalDateTime.now();
        }
    }
}