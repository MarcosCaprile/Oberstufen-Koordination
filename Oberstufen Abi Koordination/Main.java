// Main.java
import java.sql.SQLException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            Database.connect();

            SchuelerVerwaltung sv = new SchuelerVerwaltung();
            KomiteeVerwaltung kv = new KomiteeVerwaltung();
            EventVerwaltung ev = new EventVerwaltung();

            new Hauptfenster(sv, kv, ev).setVisible(true);
        });
    }
}