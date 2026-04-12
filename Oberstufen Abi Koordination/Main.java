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
            EventController ec = new EventController(ev);
            SchuelerController sc = new SchuelerController(sv);
            KomiteeController kc = new KomiteeController(kv);

            new Hauptfenster(sv, kv, ev, sc, kc, ec).setVisible(true);
        });
    }
}