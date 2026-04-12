// SchuelerPanel.java
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class SchuelerPanel extends JPanel {

    private final SchuelerVerwaltung sv;
    private final EventVerwaltung ev;

    private final JPanel rowsPanel;
    private final JTextField sucheFeld;

    public SchuelerPanel(SchuelerVerwaltung sv, EventVerwaltung ev) {
        this.sv = sv;
        this.ev = ev;

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

        JLabel title = new JLabel("Schülerverwaltung");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(UIStyle.TEXT);

        JLabel sub = new JLabel("Verwalten Sie alle Schüler und ihre Eventpunkte");
        sub.setFont(UIStyle.bodyFont());
        sub.setForeground(UIStyle.MUTED);

        titleWrap.add(title);
        titleWrap.add(Box.createVerticalStrut(6));
        titleWrap.add(sub);

        JPanel neuBtn = UIStyle.createBigActionButton("+ Neuer Schüler", UIStyle.BLUE, this::neuerSchueler);

        header.add(titleWrap, BorderLayout.WEST);
        header.add(neuBtn, BorderLayout.EAST);

        content.add(header);
        content.add(Box.createVerticalStrut(22));

        JPanel searchCard = UIStyle.createCard();
        searchCard.setLayout(new BorderLayout());

        sucheFeld = UIStyle.createSearchField("Schüler suchen");
        sucheFeld.addActionListener(e -> laden());

        searchCard.add(sucheFeld, BorderLayout.CENTER);
        content.add(searchCard);
        content.add(Box.createVerticalStrut(18));

        JPanel tableCard = UIStyle.createCard();
        tableCard.setLayout(new BorderLayout());

        JPanel head = new JPanel(new GridLayout(1, 5));
        head.setOpaque(false);
        head.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        head.add(createHeadLabel("ID"));
        head.add(createHeadLabel("NAME"));
        head.add(createHeadLabel("KONTAKT"));
        head.add(createHeadLabel("EVENTPUNKTE"));
        head.add(createHeadLabel("AKTIONEN"));

        tableCard.add(head, BorderLayout.NORTH);

        rowsPanel = new JPanel();
        rowsPanel.setOpaque(false);
        rowsPanel.setLayout(new BoxLayout(rowsPanel, BoxLayout.Y_AXIS));

        JScrollPane scroll = new JScrollPane(rowsPanel);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
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

        String suche = sucheFeld.getText().toLowerCase();

        for (Schueler s : sv.ladeAlleSchueler()) {
            if (suche.isEmpty()
                    || s.getName().toLowerCase().contains(suche)
                    || s.getId().toLowerCase().contains(suche)) {
                rowsPanel.add(createRow(s));
                rowsPanel.add(Box.createVerticalStrut(8));
            }
        }

        rowsPanel.revalidate();
        rowsPanel.repaint();
    }

    private JPanel createRow(Schueler s) {
        JPanel row = new RoundedPanel(16);
        row.setLayout(new GridLayout(1, 5));
        row.setBackground(Color.WHITE);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(243, 244, 246)),
                BorderFactory.createEmptyBorder(16, 0, 16, 0)
        ));

        JLabel id = new JLabel(s.getId());
        id.setFont(new Font("SansSerif", Font.BOLD, 14));
        id.setForeground(UIStyle.TEXT);

        JLabel name = new JLabel(s.getName());
        name.setFont(new Font("SansSerif", Font.BOLD, 14));
        name.setForeground(UIStyle.TEXT);

        JPanel kontakt = new JPanel();
        kontakt.setOpaque(false);
        kontakt.setLayout(new BoxLayout(kontakt, BoxLayout.Y_AXIS));
        kontakt.add(makeLine("✉  " + s.getMailAdresse()));
        kontakt.add(Box.createVerticalStrut(6));
        kontakt.add(makeLine("☎  " + s.getHandynummer()));

        JPanel punkteWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        punkteWrap.setOpaque(false);
        int punkte = ev.berechnePunkteFuerSchueler(s.getId());
        punkteWrap.add(UIStyle.createChip(punkte + " Punkte", UIStyle.SOFT_PURPLE, UIStyle.PURPLE));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);

        JButton edit = UIStyle.createIconButton("✎", UIStyle.BLUE);
        JButton delete = UIStyle.createIconButton("🗑", UIStyle.RED);

        edit.addActionListener(e -> bearbeiteSchueler(s));
        delete.addActionListener(e -> loescheSchueler(s));

        actions.add(edit);
        actions.add(delete);

        row.add(id);
        row.add(name);
        row.add(kontakt);
        row.add(punkteWrap);
        row.add(actions);

        return row;
    }

    private JLabel makeLine(String text) {
        JLabel label = new JLabel(text);
        label.setFont(UIStyle.bodyFont());
        label.setForeground(UIStyle.MUTED);
        return label;
    }

    private void neuerSchueler() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        SchuelerDialog dialog = new SchuelerDialog(owner, null, true, ev);
        dialog.setVisible(true);

        if (dialog.isGespeichert()) {
            Schueler s = new Schueler(
                    dialog.getIdWert(),
                    dialog.getNameWert(),
                    dialog.getMailWert(),
                    dialog.getHandyWert()
            );
            sv.fuegeSchuelerHinzu(s);
            laden();
        }
    }

    private void bearbeiteSchueler(Schueler s) {
        Window owner = SwingUtilities.getWindowAncestor(this);
        SchuelerDialog dialog = new SchuelerDialog(owner, s, false, ev);
        dialog.setVisible(true);

        if (dialog.isGespeichert()) {
            sv.bearbeiteSchueler(
                    s.getId(),
                    dialog.getNameWert(),
                    dialog.getMailWert(),
                    dialog.getHandyWert()
            );
            laden();
        }
    }

    private void loescheSchueler(Schueler s) {
        int ok = JOptionPane.showConfirmDialog(
                this,
                "Schüler wirklich löschen?",
                "Bestätigung",
                JOptionPane.YES_NO_OPTION
        );
        if (ok == JOptionPane.YES_OPTION) {
            sv.loescheSchueler(s.getId());
            laden();
        }
    }
}