import javax.swing.*;
import java.awt.*;

public class SchuelerDialog extends JDialog {

    private boolean gespeichert;
    private JTextField idFeld;
    private JTextField nameFeld;
    private JTextField mailFeld;
    private JTextField handyFeld;

    public SchuelerDialog(Window owner, Schueler schueler, boolean idBearbeitbar, EventVerwaltung ev) {
        super(owner, schueler == null ? "Neuer Schüler" : "Schüler bearbeiten", ModalityType.APPLICATION_MODAL);
        this.gespeichert = false;

        setSize(430, 430);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setBorder(UIStyle.panelPadding(22, 22, 22, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        idFeld = createField(content, "ID");
        nameFeld = createField(content, "Name");
        mailFeld = createField(content, "E-Mail Adresse");
        handyFeld = createField(content, "Handynummer");

        if (schueler != null) {
            idFeld.setText(schueler.getId());
            nameFeld.setText(schueler.getName());
            mailFeld.setText(schueler.getMailAdresse());
            handyFeld.setText(schueler.getHandynummer());

            JTextField punkteFeld = createField(content, "Eventpunkte");
            punkteFeld.setText(String.valueOf(ev.berechnePunkteFuerSchueler(schueler.getId())));
            punkteFeld.setEditable(false);
            punkteFeld.setBackground(new Color(249, 250, 251));
        }

        idFeld.setEditable(idBearbeitbar);

        JPanel footer = new JPanel(new GridLayout(1, 2, 12, 0));
        footer.setOpaque(false);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton cancel = UIStyle.createSecondaryButton("Abbrechen");
        JButton save = UIStyle.createPrimaryButton("Speichern", UIStyle.BLUE);

        cancel.addActionListener(e -> dispose());
        save.addActionListener(e -> {
            gespeichert = true;
            dispose();
        });

        footer.add(cancel);
        footer.add(save);
        content.add(footer);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JTextField createField(JPanel parent, String labelText) {
        JLabel label = new JLabel(labelText);
        label.setForeground(UIStyle.TEXT);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));

        JTextField field = new JTextField();
        field.setFont(UIStyle.bodyFont());
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.BORDER, 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));

        parent.add(label);
        parent.add(Box.createVerticalStrut(8));
        parent.add(field);
        parent.add(Box.createVerticalStrut(16));
        return field;
    }

    public boolean isGespeichert() {
        return gespeichert;
    }

    public String getIdWert() {
        return idFeld.getText().trim();
    }

    public String getNameWert() {
        return nameFeld.getText().trim();
    }

    public String getMailWert() {
        return mailFeld.getText().trim();
    }

    public String getHandyWert() {
        return handyFeld.getText().trim();
    }
}