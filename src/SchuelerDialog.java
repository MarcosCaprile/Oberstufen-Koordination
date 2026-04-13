import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class SchuelerDialog extends JDialog {

    private boolean gespeichert;

    private JTextField idFeld;
    private JTextField vornameFeld;
    private JTextField nachnameFeld;
    private JTextField mailFeld;
    private JTextField handyFeld;

    private JCheckBox consentBox;

    public SchuelerDialog(Window owner, Schueler schueler, boolean idBearbeitbar, EventVerwaltung ev) {
        super(owner, schueler == null ? "Neuer Schüler" : "Schüler bearbeiten", ModalityType.APPLICATION_MODAL);
        this.gespeichert = false;

        setSize(450, 550);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setBorder(UIStyle.panelPadding(22, 22, 22, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        // Pflichtfelder mit *
        idFeld = createField(content, "Schüler-ID *", "🆔");
        vornameFeld = createField(content, "Vorname *", "👤");
        nachnameFeld = createField(content, "Nachname *", "👤");
        mailFeld = createField(content, "E-Mail", "✉");
        handyFeld = createField(content, "Handynummer", "☎");

        if (schueler != null) {
            idFeld.setText(schueler.getId());
            vornameFeld.setText(schueler.getVorname());
            nachnameFeld.setText(schueler.getNachname());
            mailFeld.setText(schueler.getMailAdresse());
            handyFeld.setText(schueler.getHandynummer());

            JTextField punkteFeld = createField(content, "Eventpunkte", "⭐");
            punkteFeld.setText(String.valueOf(ev.berechnePunkteFuerSchueler(schueler.getId())));
            punkteFeld.setEditable(false);
        }

        idFeld.setEditable(idBearbeitbar);

        // Datenschutz Checkbox
        consentBox = new JCheckBox("Ich stimme der Speicherung meiner Daten zu");
        consentBox.setFont(UIStyle.bodyFont());
        content.add(consentBox);

        // Datenschutz-Link
        JButton datenschutzBtn = new JButton("Datenschutz anzeigen");
        datenschutzBtn.setBorderPainted(false);
        datenschutzBtn.setForeground(Color.BLUE);
        datenschutzBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        datenschutzBtn.setOpaque(false);

        datenschutzBtn.addActionListener(e -> zeigeDatenschutz());

        content.add(datenschutzBtn);
        content.add(Box.createVerticalStrut(16));

        JPanel footer = new RoundedPanel(16);
        footer.setLayout(new GridLayout(1, 2, 12, 0));

        JButton cancel = UIStyle.createSecondaryButton("Abbrechen");
        JButton save = UIStyle.createPrimaryButton("Speichern", UIStyle.BLUE);

        cancel.addActionListener(e -> dispose());

        save.addActionListener(e -> {
            // Pflichtfelder prüfen
            if (idFeld.getText().isEmpty() ||
                vornameFeld.getText().isEmpty() ||
                nachnameFeld.getText().isEmpty()) {

                JOptionPane.showMessageDialog(this, "Bitte alle Pflichtfelder ausfüllen!");
                return;
            }

            // Datenschutz prüfen
            if (!consentBox.isSelected()) {
                JOptionPane.showMessageDialog(this, "Bitte Datenschutz akzeptieren!");
                return;
            }

            gespeichert = true;
            dispose();
        });

        footer.add(cancel);
        footer.add(save);
        content.add(footer);

        add(new JScrollPane(content), BorderLayout.CENTER);
    }

    private JTextField createField(JPanel parent, String labelText, String icon) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        parent.add(label);

        JPanel wrapper = new RoundedPanel(14);
        wrapper.setLayout(new BorderLayout());
        wrapper.setBorder(new LineBorder(UIStyle.BORDER, 1, true));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(new EmptyBorder(0, 10, 0, 10));
        wrapper.add(iconLabel, BorderLayout.WEST);

        JTextField field = new JTextField();
        field.setBorder(new EmptyBorder(10, 0, 10, 10));
        wrapper.add(field, BorderLayout.CENTER);

        parent.add(wrapper);
        parent.add(Box.createVerticalStrut(12));

        return field;
    }

    private void zeigeDatenschutz() {
        JOptionPane.showMessageDialog(this, """
                                            Datenschutz:
                                            
                                            - Daten werden nur f\u00fcr die Organisation verwendet
                                            - Keine Weitergabe an Dritte
                                            - Daten k\u00f6nnen jederzeit gel\u00f6scht werden""",
                "Datenschutz",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // Getter
    public boolean isGespeichert() { return gespeichert; }
    public String getIdWert() { return idFeld.getText(); }
    public String getVornameWert() { return vornameFeld.getText(); }
    public String getNachnameWert() { return nachnameFeld.getText(); }
    public String getMailWert() { return mailFeld.getText(); }
    public String getHandyWert() { return handyFeld.getText(); }
}