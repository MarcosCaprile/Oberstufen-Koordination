// EventDialog.java
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

public class EventDialog extends JDialog {

    private boolean gespeichert;
    private JTextField idFeld;
    private JTextField nameFeld;
    private JTextField datumFeld;
    private JTextField uhrzeitFeld;
    private JTextField ortFeld;
    private JTextField punkteFeld;
    private JTextField maxFeld;

    public EventDialog(Window owner, Event event, boolean idBearbeitbar) {
        super(owner, event == null ? "Neues Event" : "Event bearbeiten", ModalityType.APPLICATION_MODAL);
        gespeichert = false;

        setSize(480, 520);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel content = new JPanel();
        content.setBackground(Color.WHITE);
        content.setBorder(UIStyle.panelPadding(22, 22, 22, 22));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        idFeld = createField(content, "Event-ID", "🆔");
        nameFeld = createField(content, "Event-Name", "🏷");
        datumFeld = createField(content, "Datum", "📅");
        uhrzeitFeld = createField(content, "Uhrzeit", "🕒");
        ortFeld = createField(content, "Ort", "📍");
        punkteFeld = createField(content, "Punktzahl", "⭐");
        maxFeld = createField(content, "Max Teilnehmer", "👥");

        if (event != null) {
            idFeld.setText(event.getId());
            nameFeld.setText(event.getName());
            datumFeld.setText(event.getDatum());
            uhrzeitFeld.setText(event.getUhrzeit());
            ortFeld.setText(event.getOrt());
            punkteFeld.setText(String.valueOf(event.getPunktzahl()));
            maxFeld.setText(String.valueOf(event.getMaxTeilnehmer()));
        }

        idFeld.setEditable(idBearbeitbar);

        JPanel footer = new JPanel(new GridLayout(1, 2, 12, 0));
        footer.setOpaque(false);

        JButton cancel = UIStyle.createSecondaryButton("Abbrechen");
        JButton save = UIStyle.createPrimaryButton("Speichern", UIStyle.PURPLE);

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

    private JTextField createField(JPanel parent, String labelText, String icon) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(UIStyle.TEXT);
        parent.add(label);
        parent.add(Box.createVerticalStrut(8));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIStyle.BORDER, 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(UIStyle.MUTED);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        iconLabel.setBorder(new EmptyBorder(0, 12, 0, 8));
        wrapper.add(iconLabel, BorderLayout.WEST);

        JTextField field = new JTextField();
        field.setBorder(new EmptyBorder(12, 0, 12, 12));
        field.setFont(UIStyle.bodyFont());
        field.setBackground(Color.WHITE);
        wrapper.add(field, BorderLayout.CENTER);

        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        parent.add(wrapper);
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

    public String getDatumWert() {
        return datumFeld.getText().trim();
    }

    public String getUhrzeitWert() {
        return uhrzeitFeld.getText().trim();
    }

    public String getOrtWert() {
        return ortFeld.getText().trim();
    }

    public int getPunkteWert() {
        return Integer.parseInt(punkteFeld.getText().trim());
    }

    public int getMaxWert() {
        return Integer.parseInt(maxFeld.getText().trim());
    }
}