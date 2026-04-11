import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class EventPanel extends JPanel {

    private final EventVerwaltung ev;
    private final SchuelerVerwaltung sv;

    private final JPanel rowsPanel;
    private final JTextField sucheFeld;

    public EventPanel(EventVerwaltung ev, SchuelerVerwaltung sv) {
        this.ev = ev;
        this.sv = sv;

        setLayout(new BorderLayout());
        setBackground(UIStyle.BG);
        setBorder(UIStyle.panelPadding(28, 28, 28, 28));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleWrap = new JPanel();
        titleWrap.setOpaque(false);
        titleWrap.setLayout(new BoxLayout(titleWrap, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Event-Verwaltung");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(UIStyle.TEXT);

        JLabel sub = new JLabel("Verwalten Sie alle Events und Veranstaltungen");
        sub.setFont(UIStyle.bodyFont());
        sub.setForeground(UIStyle.MUTED);

        titleWrap.add(title);
        titleWrap.add(Box.createVerticalStrut(6));
        titleWrap.add(sub);

        JButton neuBtn = UIStyle.createPrimaryButton("+  Neues Event", UIStyle.PURPLE);
        neuBtn.addActionListener(e -> neuesEvent());

        header.add(titleWrap, BorderLayout.WEST);
        header.add(neuBtn, BorderLayout.EAST);

        content.add(header);
        content.add(Box.createVerticalStrut(22));

        JPanel topCard = UIStyle.createCard();
        topCard.setLayout(new BoxLayout(topCard, BoxLayout.Y_AXIS));

        sucheFeld = UIStyle.createSearchField("Event suchen");
        sucheFeld.addActionListener(e -> laden());

        topCard.add(sucheFeld);

        content.add(topCard);
        content.add(Box.createVerticalStrut(18));

        JPanel tableCard = UIStyle.createCard();
        tableCard.setLayout(new BorderLayout());

        JPanel head = new JPanel(new GridLayout(1, 6));
        head.setOpaque(false);
        head.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        head.add(createHeadLabel("ID"));
        head.add(createHeadLabel("EVENT-NAME"));
        head.add(createHeadLabel("DATUM & UHRZEIT"));
        head.add(createHeadLabel("PUNKTZAHL"));
        head.add(createHeadLabel("TEILNEHMER"));
        head.add(createHeadLabel("AKTIONEN"));
        head.add(createHeadLabel("STATUS"));

        tableCard.add(head, BorderLayout.NORTH);

        rowsPanel = new JPanel();
        rowsPanel.setOpaque(false);
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(rowsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Color.WHITE);
        tableCard.add(scroll, BorderLayout.CENTER);

        content.add(tableCard);
        add(content, BorderLayout.CENTER);

        laden();
    }

    private JLabel createHeadLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(UIStyle.MUTED);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }

    private void laden() {
        rowsPanel.removeAll();

        String query = sucheFeld.getText().trim().toLowerCase();

        for (Event e : ev.ladeAlleEvents()) {
            if (query.isEmpty()
                    || e.getName().toLowerCase().contains(query)
                    || e.getId().toLowerCase().contains(query)) {
                rowsPanel.add(createRow(e));
                rowsPanel.add(Box.createVerticalStrut(8));
            }
        }

        rowsPanel.revalidate();
        rowsPanel.repaint();
    }


    private JPanel createRow(Event e) {
        JPanel row = new JPanel(new GridLayout(1, 7));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(243, 244, 246)),
                BorderFactory.createEmptyBorder(16, 0, 16, 0)
        ));

        JLabel id = new JLabel(e.getId());
        id.setFont(new Font("SansSerif", Font.BOLD, 14));
        id.setForeground(UIStyle.TEXT);

        JLabel name = new JLabel(e.getName());
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        name.setForeground(UIStyle.TEXT);

        JPanel dateBox = new JPanel();
        dateBox.setOpaque(false);
        dateBox.setLayout(new BoxLayout(dateBox, BoxLayout.Y_AXIS));
        dateBox.add(makeLine("📅  " + e.getDatum()));
        dateBox.add(Box.createVerticalStrut(6));
        dateBox.add(makeLine("🕒  " + e.getUhrzeit()));

        JPanel punkte = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        punkte.setOpaque(false);
        punkte.add(UIStyle.createChip(e.getPunktzahl() + " Punkte", new Color(243, 232, 255), UIStyle.PURPLE));

        JPanel status = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        status.setOpaque(false);

        if (e.isAbgeschlossen()) {
            status.add(UIStyle.createChip(
                "Abgeschlossen",
                new Color(220, 252, 231),
                new Color(22, 163, 74)
            ));
        }

        JPanel teilnehmer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        teilnehmer.setOpaque(false);
        teilnehmer.add(UIStyle.createChip(
                ev.getTeilnehmerAnzahl(e.getId()) + "/" + e.getMaxTeilnehmer(),
                new Color(243, 232, 255),
                UIStyle.PURPLE
        ));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);

        JButton anmelden = UIStyle.createIconButton("+", UIStyle.BLUE);
        JButton info = UIStyle.createIconButton("ⓘ", UIStyle.MUTED);
        JButton edit = UIStyle.createIconButton("✎", UIStyle.PURPLE);
        JButton delete = UIStyle.createIconButton("🗑", UIStyle.RED);

        // 🔥 EVENT ABGESCHLOSSEN → ALLES SPERREN
        if (e.isAbgeschlossen()) {

            anmelden.setEnabled(false);
            edit.setEnabled(false);
            delete.setEnabled(false);

            anmelden.setForeground(Color.GRAY);
            edit.setForeground(Color.GRAY);
            delete.setForeground(Color.GRAY);
        }

        // Aktionen
        anmelden.addActionListener(a -> {
            if (!e.isAbgeschlossen()) {
                String schuelerId = JOptionPane.showInputDialog(this, "Schüler-ID zum Anmelden:");
                if (schuelerId != null && !schuelerId.trim().isEmpty()) {
                    ev.meldeSchuelerAn(schuelerId.trim(), e.getId());
                    laden();
                }
            }
        });

        info.addActionListener(a -> zeigeInfos(e));
        edit.addActionListener(a -> {
            if (!e.isAbgeschlossen()) {
                bearbeiteEvent(e);
            }
        });
        delete.addActionListener(a -> {
            if (!e.isAbgeschlossen()) {
                loescheEvent(e);
            }
        });

        actions.add(anmelden);
        actions.add(info);
        actions.add(edit);
        actions.add(delete);

        row.add(id);
        row.add(name);
        row.add(dateBox);
        row.add(punkte);
        row.add(teilnehmer);
        row.add(actions);
        row.add(status);
        row.add(actions);

        return row;
    }

    private JLabel makeLine(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIStyle.bodyFont());
        label.setForeground(UIStyle.MUTED);
        return label;
    }

    private void neuesEvent() {
        System.out.println("CLICK EVENT BUTTON");

        EventDialog dialog = new EventDialog(
                SwingUtilities.getWindowAncestor(this),
                null,
                true
        );

        dialog.setVisible(true);

        if (dialog.isGespeichert()) {

            System.out.println("DIALOG BESTÄTIGT");

            Event e = new Event(
                    dialog.getIdWert(),
                    dialog.getNameWert(),
                    dialog.getDatumWert(),
                    dialog.getUhrzeitWert(),
                    dialog.getOrtWert(),
                    dialog.getPunkteWert(),
                    dialog.getMaxWert()
            );

            System.out.println("ERSTELLT: " + e.getName());

            ev.fuegeEventHinzu(e);

            laden();
        }
    }

    private void bearbeiteEvent(Event e) {
        System.out.println("Bearbeite Event: " + e.getId());

        EventDialog dialog = new EventDialog(
                SwingUtilities.getWindowAncestor(this),
                e,
                false
        );

        dialog.setVisible(true);

        if (dialog.isGespeichert()) {

            ev.bearbeiteEvent(
                    e.getId(),
                    dialog.getNameWert(),
                    dialog.getDatumWert(),
                    dialog.getUhrzeitWert(),
                    dialog.getOrtWert(),
                    dialog.getPunkteWert(),
                    dialog.getMaxWert()
            );

            laden();
        }
    }

    private void loescheEvent(Event e) {
        System.out.println("Lösche Event: " + e.getId());

        int ok = JOptionPane.showConfirmDialog(this, "Event löschen?");

        if (ok == JOptionPane.YES_OPTION) {
            ev.loescheEvent(e.getId());
            laden();
        }
    }

    private void zeigeInfos(Event e) {
        ArrayList<String> ids = ev.getTeilnehmerIds(e.getId());
        StringBuilder text = new StringBuilder();

        if (ids.isEmpty()) {
            text.append("Keine Teilnehmer");
        } else {
            for (String id : ids) {
                Schueler s = sv.findeSchueler(id);
                if (s != null) {
                    text.append("• ").append(s.getName()).append(" (").append(s.getId()).append(")\n");
                } else {
                    text.append("• ").append(id).append("\n");
                }
            }
        }

        Object[] options = {"Schüler anmelden", "Schüler entfernen", "Event abschließen", "Schließen"};

        int result = JOptionPane.showOptionDialog(this,
                "Event: " + e.getName() +
                        "\nOrt: " + e.getOrt() +
                        "\nDatum: " + e.getDatum() +
                        "\nUhrzeit: " + e.getUhrzeit() +
                        "\nPunktzahl: " + e.getPunktzahl() +
                        "\nTeilnehmer: " + ev.getTeilnehmerAnzahl(e.getId()) + "/" + e.getMaxTeilnehmer() +
                        "\n\nAngemeldete Schüler:\n" + text,
                "Event Infos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (result == 0) {
            String schuelerId = JOptionPane.showInputDialog(this, "Schüler-ID zum Anmelden:");
            if (schuelerId != null && !schuelerId.trim().isEmpty()) {
                ev.meldeSchuelerAn(schuelerId.trim(), e.getId());
                laden();
            }
        } else if (result == 1) {
            String schuelerId = JOptionPane.showInputDialog(this, "Schüler-ID zum Entfernen:");
            if (schuelerId != null && !schuelerId.trim().isEmpty()) {
                ev.verlasseEvent(schuelerId.trim(), e.getId());
                laden();
            }
        } else if (result == 2) {
            ev.eventAbschliessen(e.getId());
            laden();
        }
    }
}