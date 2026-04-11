import javax.swing.*;
import java.awt.*;

public class KomiteeDialog extends JDialog {

    private boolean gespeichert;
    private JTextField idFeld;
    private JTextField nameFeld;
    private JTextField treffFeld;
    private JTextField leiterFeld;
    private JTextArea aufgabeArea;

    public KomiteeDialog(Window owner, Komitee komitee, boolean idBearbeitbar) {
        super(owner, komitee == null ? "Neues Komitee" : "Komitee bearbeiten", ModalityType.APPLICATION_MODAL);
        gespeichert = false;

        setSize(480, 460);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setBorder(UIStyle.panelPadding(22, 22, 22, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        idFeld = createField(content, "Komitee-ID");
        nameFeld = createField(content, "Komitee-Name");
        treffFeld = createField(content, "Treffzeiten");
        leiterFeld = createField(content, "Leiter-ID");

        JLabel label = new JLabel("Aufgaben");
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(UIStyle.TEXT);
        content.add(label);
        content.add(Box.createVerticalStrut(8));

        aufgabeArea = new JTextArea(6, 20);
        aufgabeArea.setLineWrap(true);
        aufgabeArea.setWrapStyleWord(true);
        aufgabeArea.setFont(UIStyle.bodyFont());
        aufgabeArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyle.BORDER, 1, true),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        JScrollPane areaScroll = new JScrollPane(aufgabeArea);
        areaScroll.setBorder(null);
        areaScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        content.add(areaScroll);
        content.add(Box.createVerticalStrut(18));

        if (komitee != null) {
            idFeld.setText(komitee.getId());
            nameFeld.setText(komitee.getName());
            treffFeld.setText(komitee.getTreffzeiten());
            leiterFeld.setText(komitee.getLeiterId());
            aufgabeArea.setText(komitee.getAufgabe());
        }

        idFeld.setEditable(idBearbeitbar);

        JPanel footer = new JPanel(new GridLayout(1, 2, 12, 0));
        footer.setOpaque(false);

        JButton cancel = UIStyle.createSecondaryButton("Abbrechen");
        JButton save = UIStyle.createPrimaryButton("Speichern", UIStyle.GREEN);

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
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(UIStyle.TEXT);

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

    public String getTreffWert() {
        return treffFeld.getText().trim();
    }

    public String getLeiterWert() {
        return leiterFeld.getText().trim();
    }

    public String getAufgabeWert() {
        return aufgabeArea.getText().trim();
    }
}