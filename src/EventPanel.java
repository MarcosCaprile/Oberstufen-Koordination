// EventPanel.java
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;

public class EventPanel extends JPanel {

    private final EventVerwaltung ev;
    private final SchuelerVerwaltung sv;
    private final EventController controller;

    private final JPanel rowsPanel;
    private final JTextField sucheFeld;

    public EventPanel(EventVerwaltung ev, SchuelerVerwaltung sv, EventController controller) {
        this.ev = ev;
        this.sv = sv;
        this.controller = controller;

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

        JPanel neuBtn = UIStyle.createBigActionButton("+ Neues Event", UIStyle.PURPLE, this::neuesEvent);

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

        JPanel head = new JPanel(new GridLayout(1, 7));
        head.setOpaque(false);
        head.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        head.add(createHeadLabel("ID"));
        head.add(createHeadLabel("EVENT"));
        head.add(createHeadLabel("DATUM & ZEIT"));
        head.add(createHeadLabel("PUNKTZAHL"));
        head.add(createHeadLabel("TEILNEHMER"));
        head.add(createHeadLabel("STATUS"));
        head.add(createHeadLabel("AKTIONEN"));

        tableCard.add(head, BorderLayout.NORTH);

        rowsPanel = new JPanel();
        rowsPanel.setOpaque(false);
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(rowsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
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
        JPanel row = new RoundedPanel(16);
        row.setLayout(new GridLayout(1, 7));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(243, 244, 246)),
                BorderFactory.createEmptyBorder(16, 0, 16, 0)
        ));

        JLabel idLabel = new JLabel(e.getId());
        idLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        idLabel.setForeground(UIStyle.TEXT);

        JPanel nameCol = new JPanel();
        nameCol.setOpaque(false);
        nameCol.setLayout(new BoxLayout(nameCol, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(e.getName());
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nameLabel.setForeground(UIStyle.TEXT);
        JLabel ortLabel = new JLabel(e.getOrt());
        ortLabel.setFont(UIStyle.smallFont());
        ortLabel.setForeground(UIStyle.MUTED);
        nameCol.add(nameLabel);
        nameCol.add(ortLabel);

        JPanel datetime = new JPanel();
        datetime.setOpaque(false);
        datetime.setLayout(new BoxLayout(datetime, BoxLayout.Y_AXIS));
        datetime.add(makeLine("📅  " + e.getDatum()));
        datetime.add(Box.createVerticalStrut(6));
        datetime.add(makeLine("🕒  " + e.getUhrzeit()));

        JPanel punkteCol = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        punkteCol.setOpaque(false);
        punkteCol.add(UIStyle.createChip(e.getPunktzahl() + " Punkte", UIStyle.SOFT_PURPLE, UIStyle.PURPLE));

        JPanel teilnehmerCol = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        teilnehmerCol.setOpaque(false);
        int teilnehmer = ev.getTeilnehmerAnzahl(e.getId());
        int maxTeil = e.getMaxTeilnehmer();
        int maxVal = maxTeil > 0 ? maxTeil : 1;
        JProgressBar progress = new JProgressBar(0, maxVal);
        progress.setValue(teilnehmer);
        progress.setStringPainted(true);
        progress.setString(teilnehmer + "/" + maxTeil);
        progress.setForeground(UIStyle.PURPLE);
        progress.setBackground(UIStyle.SOFT_PURPLE);
        progress.setBorder(new LineBorder(UIStyle.SOFT_PURPLE, 1, true));
        progress.setPreferredSize(new Dimension(100, 12));
        teilnehmerCol.add(progress);

        JPanel statusCol = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusCol.setOpaque(false);
        if (e.isAbgeschlossen()) {
            statusCol.add(UIStyle.createChip("Abgeschlossen", UIStyle.SOFT_GREEN, UIStyle.GREEN));
        } else {
            statusCol.add(UIStyle.createChip("Geplant", UIStyle.SOFT_ORANGE, UIStyle.ORANGE));
        }

        JPanel actionsCol = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        actionsCol.setOpaque(false);
        JButton anmeldenBtn = UIStyle.createIconButton("＋", UIStyle.BLUE);
        JButton infoBtn = UIStyle.createIconButton("ⓘ", UIStyle.MUTED);
        JButton editBtn = UIStyle.createIconButton("✎", UIStyle.PURPLE);
        JButton deleteBtn = UIStyle.createIconButton("🗑", UIStyle.RED);

        if (e.isAbgeschlossen()) {
            anmeldenBtn.setEnabled(false);
            editBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
            anmeldenBtn.setForeground(UIStyle.MUTED);
            editBtn.setForeground(UIStyle.MUTED);
            deleteBtn.setForeground(UIStyle.MUTED);
        }

        anmeldenBtn.addActionListener(a -> {
            if (!e.isAbgeschlossen()) {
                String schuelerId = JOptionPane.showInputDialog(this, "Schüler-ID zum Anmelden:");
                if (schuelerId != null && !schuelerId.trim().isEmpty()) {
                    controller.schuelerAnmelden(schuelerId.trim(), e.getId());
                    laden();
                }
            }
        });
        infoBtn.addActionListener(a -> zeigeInfos(e));
        editBtn.addActionListener(a -> {
            if (!e.isAbgeschlossen()) {
                bearbeiteEvent(e);
            }
        });
        deleteBtn.addActionListener(a -> {
            if (!e.isAbgeschlossen()) {
                loescheEvent(e);
            }
        });

        actionsCol.add(anmeldenBtn);
        actionsCol.add(infoBtn);
        actionsCol.add(editBtn);
        actionsCol.add(deleteBtn);

        row.add(idLabel);
        row.add(nameCol);
        row.add(datetime);
        row.add(punkteCol);
        row.add(teilnehmerCol);
        row.add(statusCol);
        row.add(actionsCol);

        return row;
    }

    private JLabel makeLine(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIStyle.bodyFont());
        label.setForeground(UIStyle.MUTED);
        return label;
    }

    private void neuesEvent() {
        EventDialog dialog = new EventDialog(
                SwingUtilities.getWindowAncestor(this),
                null,
                true
        );
        dialog.setVisible(true);

        if (dialog.isGespeichert()) {
            Event e = new Event(
                    dialog.getIdWert(),
                    dialog.getNameWert(),
                    dialog.getDatumWert(),
                    dialog.getUhrzeitWert(),
                    dialog.getOrtWert(),
                    dialog.getPunkteWert(),
                    dialog.getMaxWert()
            );
            controller.erstelleEvent(e);
            laden();
        }
    }

    private void bearbeiteEvent(Event e) {
        EventDialog dialog = new EventDialog(
                SwingUtilities.getWindowAncestor(this),
                e,
                false
        );
        dialog.setVisible(true);

        if (dialog.isGespeichert()) {
            controller.bearbeiteEvent(
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
        int ok = JOptionPane.showConfirmDialog(this, "Event löschen?");
        if (ok == JOptionPane.YES_OPTION) {
            controller.loescheEvent(e.getId());
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
                controller.schuelerAnmelden(schuelerId.trim(), e.getId());
                laden();
            }
        } else if (result == 1) {
            String schuelerId = JOptionPane.showInputDialog(this, "Schüler-ID zum Entfernen:");
            if (schuelerId != null && !schuelerId.trim().isEmpty()) {
                controller.schuelerEntfernen(schuelerId.trim(), e.getId());
                laden();
            }
        } else if (result == 2) {
            controller.eventAbschliessen(e.getId());
            laden();
        }
    }
}