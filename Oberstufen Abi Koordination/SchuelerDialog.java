// SchuelerDialog.java
import javax.swing.*;
import javax.swing.border.EmptyBorder;
    import javax.swing.border.LineBorder;
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

            idFeld = createField(content, "Schüler-ID", "🆔");
            nameFeld = createField(content, "Name", "👤");
            mailFeld = createField(content, "E-Mail Adresse", "✉");
            handyFeld = createField(content, "Handynummer", "☎");

            if (schueler != null) {
                idFeld.setText(schueler.getId());
                nameFeld.setText(schueler.getName());
                mailFeld.setText(schueler.getMailAdresse());
                handyFeld.setText(schueler.getHandynummer());

                JTextField punkteFeld = createField(content, "Eventpunkte", "⭐");
                punkteFeld.setText(String.valueOf(ev.berechnePunkteFuerSchueler(schueler.getId())));
                punkteFeld.setEditable(false);
                punkteFeld.setBackground(UIStyle.SOFT_PURPLE);
                punkteFeld.setForeground(UIStyle.PURPLE);
            }

            idFeld.setEditable(idBearbeitbar);

            JPanel footer = new RoundedPanel(16);
            footer.setLayout(new GridLayout(1, 2, 12, 0));
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

            add(scrollPane, BorderLayout.CENTER);
        }

        private JTextField createField(JPanel parent, String labelText, String icon) {
            JLabel label = new JLabel(labelText);
            label.setForeground(UIStyle.TEXT);
            label.setFont(new Font("SansSerif", Font.BOLD, 14));
            parent.add(label);
            parent.add(Box.createVerticalStrut(8));

            JPanel wrapper = new RoundedPanel(14);
            wrapper.setLayout(new BorderLayout());
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

        public String getMailWert() {
            return mailFeld.getText().trim();
        }

        public String getHandyWert() {
            return handyFeld.getText().trim();
        }
    }