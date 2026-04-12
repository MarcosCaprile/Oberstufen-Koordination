// KomiteePanel.java
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.util.ArrayList;

public class KomiteePanel extends JPanel {

    private final KomiteeVerwaltung kv;
    private final SchuelerVerwaltung sv;

    private final JPanel cardsPanel;
    private final JTextField sucheFeld;

    private final KomiteeController controller;

    public KomiteePanel(KomiteeVerwaltung kv, SchuelerVerwaltung sv, KomiteeController controller) {
        this.kv = kv;
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

        JLabel title = new JLabel("Komitee-Verwaltung");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(UIStyle.TEXT);

        JLabel sub = new JLabel("Verwalten Sie alle Komitees und ihre Aktivitäten");
        sub.setFont(UIStyle.bodyFont());
        sub.setForeground(UIStyle.MUTED);

        titleWrap.add(title);
        titleWrap.add(Box.createVerticalStrut(6));
        titleWrap.add(sub);

        JPanel neuBtn = UIStyle.createBigActionButton("+ Neues Komitee", UIStyle.GREEN, this::neuesKomitee);

        header.add(titleWrap, BorderLayout.WEST);
        header.add(neuBtn, BorderLayout.EAST);

        content.add(header);
        content.add(Box.createVerticalStrut(22));

        JPanel searchCard = UIStyle.createCard();
        searchCard.setLayout(new BorderLayout());
        sucheFeld = UIStyle.createSearchField("Komitee suchen");
        sucheFeld.addActionListener(e -> laden());
        searchCard.add(sucheFeld, BorderLayout.CENTER);
        content.add(searchCard);
        content.add(Box.createVerticalStrut(18));

        cardsPanel = new JPanel(new GridLayout(0, 2, 18, 18));
        cardsPanel.setOpaque(false);

        JScrollPane scroll = new JScrollPane(cardsPanel);
        scroll.getViewport().setOpaque(false);
        scroll.setOpaque(false);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(UIStyle.BG);

        content.add(scroll);
        add(content, BorderLayout.CENTER);

        laden();
    }

    private void laden() {
        cardsPanel.removeAll();

        String query = sucheFeld.getText().trim().toLowerCase();

        for (Komitee k : kv.ladeAlleKomitees()) {
            if (query.isEmpty()
                    || k.getName().toLowerCase().contains(query)
                    || k.getId().toLowerCase().contains(query)) {
                cardsPanel.add(createCard(k));
            }
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private JPanel createCard(Komitee k) {
        JPanel card = new RoundedPanel(20);
        card.setLayout(new BorderLayout());

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JPanel titleWrap = new JPanel();
        titleWrap.setOpaque(false);
        titleWrap.setLayout(new BoxLayout(titleWrap, BoxLayout.Y_AXIS));

        JPanel titleLine = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        titleLine.setOpaque(false);

        JLabel title = new JLabel(k.getName());
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(UIStyle.TEXT);

        JLabel chip = UIStyle.createChip(k.getId(), new Color(243, 244, 246), UIStyle.MUTED);

        titleLine.add(title);
        titleLine.add(chip);

        JLabel time = UIStyle.createMutedLabel("🕒  " + k.getTreffzeiten());

        titleWrap.add(titleLine);
        titleWrap.add(Box.createVerticalStrut(8));
        titleWrap.add(time);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        actions.setOpaque(false);

        JButton info = UIStyle.createIconButton("ⓘ", UIStyle.BLUE);
        JButton edit = UIStyle.createIconButton("✎", UIStyle.GREEN);
        JButton delete = UIStyle.createIconButton("🗑", UIStyle.RED);

        info.addActionListener(e -> zeigeInfos(k));
        edit.addActionListener(e -> bearbeiteKomitee(k));
        delete.addActionListener(e -> loescheKomitee(k));

        actions.add(info);
        actions.add(edit);
        actions.add(delete);

        top.add(titleWrap, BorderLayout.WEST);
        top.add(actions, BorderLayout.EAST);

        JPanel middle = new JPanel();
        middle.setOpaque(false);
        middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
        middle.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));

        JLabel aufgabeHead = new JLabel("☷  Aufgaben");
        aufgabeHead.setFont(new Font("SansSerif", Font.BOLD, 14));
        aufgabeHead.setForeground(UIStyle.TEXT);

        JLabel aufgabe = UIStyle.createMutedLabel("<html><div style='width:360px;'>" + k.getAufgabe() + "</div></html>");

        middle.add(aufgabeHead);
        middle.add(Box.createVerticalStrut(8));
        middle.add(aufgabe);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(243, 244, 246)));

        JLabel mitglieder = UIStyle.createMutedLabel("Mitglieder:");
        JLabel count = new JLabel(kv.getMitgliederAnzahl(k.getId()) + " Schüler");
        count.setFont(new Font("SansSerif", Font.BOLD, 14));
        count.setForeground(UIStyle.TEXT);

        bottom.add(mitglieder, BorderLayout.WEST);
        bottom.add(count, BorderLayout.EAST);

        card.add(top, BorderLayout.NORTH);
        card.add(middle, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private void neuesKomitee() {
        KomiteeDialog dialog = new KomiteeDialog(
                SwingUtilities.getWindowAncestor(this),
                null,
                true
        );
        dialog.setVisible(true);

        if (dialog.isGespeichert()) {
            String id = dialog.getIdWert();
            if (id == null || id.isEmpty()) {
                id = "K" + System.currentTimeMillis();
            }
            Komitee k = new Komitee(
                    dialog.getIdWert(),
                    dialog.getNameWert(),
                    dialog.getTreffWert(),
                    dialog.getAufgabeWert(),
                    dialog.getLeiterWert()
            );
            controller.erstelleKomitee(k);
            laden();
        }
    }

    private void bearbeiteKomitee(Komitee k) {
        KomiteeDialog dialog = new KomiteeDialog(
                SwingUtilities.getWindowAncestor(this),
                k,
                false
        );
        dialog.setVisible(true);

        if (dialog.isGespeichert()) {
            controller.bearbeiteKomitee(
                    k.getId(),
                    dialog.getNameWert(),
                    dialog.getTreffWert(),
                    dialog.getAufgabeWert(),
                    dialog.getLeiterWert()
            );
            laden();
        }
    }

    private void loescheKomitee(Komitee k) {
        int ok = JOptionPane.showConfirmDialog(this, "Komitee löschen?");
        if (ok == JOptionPane.YES_OPTION) {
            controller.loescheKomitee(k.getId());
            laden();
        }
    }

    private void zeigeInfos(Komitee k) {
        ArrayList<Schueler> mitglieder = kv.getMitgliederAlsSchueler(k.getId(), sv);

        StringBuilder sb = new StringBuilder();
        if (mitglieder.isEmpty()) {
            sb.append("Keine Mitglieder");
        } else {
            for (Schueler s : mitglieder) {
                sb.append("• ").append(s.getName()).append(" (").append(s.getId()).append(")\n");
            }
        }

        Object[] options = {"Schüler hinzufügen", "Schüler entfernen", "Schließen"};
        int result = JOptionPane.showOptionDialog(this,
                "Komitee: " + k.getName() +
                        "\nID: " + k.getId() +
                        "\nTreffzeiten: " + k.getTreffzeiten() +
                        "\nLeiter-ID: " + k.getLeiterId() +
                        "\n\nAufgaben:\n" + k.getAufgabe() +
                        "\n\nMitglieder:\n" + sb,
                "Komitee Infos",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]);

        if (result == 0) {
            String schuelerId = JOptionPane.showInputDialog(this, "Schüler-ID zum Hinzufügen:");
            if (schuelerId != null && !schuelerId.trim().isEmpty()) {
                controller.trittBei(schuelerId.trim(), k.getId());
                laden();
            }
        } else if (result == 1) {
            String schuelerId = JOptionPane.showInputDialog(this, "Schüler-ID zum Entfernen:");
            if (schuelerId != null && !schuelerId.trim().isEmpty()) {
                controller.trittAus(schuelerId.trim(), k.getId());
                laden();
            }
        }
    }
}