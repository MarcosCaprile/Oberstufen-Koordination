import java.sql.*;
import java.util.ArrayList;

public class KomiteeVerwaltung {

    public void fuegeKomiteeHinzu(Komitee k) {
        String sql = "INSERT INTO komitee (id, name, treffzeiten, aufgabe, leiterId) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, k.getId());
            ps.setString(2, k.getName());
            ps.setString(3, k.getTreffzeiten());
            ps.setString(4, k.getAufgabe());
            ps.setString(5, k.getLeiterId());

            int rows = ps.executeUpdate();
            System.out.println("KOMITEE gespeichert, rows=" + rows);

        } catch (SQLException ex) {
            System.out.println("Fehler beim Speichern von Komitee:");
            ex.printStackTrace();
        }
    }

    public ArrayList<Komitee> getAlleKomitees() {
        return ladeAlleKomitees();
    }

    public ArrayList<Komitee> ladeAlleKomitees() {
        ArrayList<Komitee> list = new ArrayList<>();

        String sql = "SELECT * FROM komitee ORDER BY id";

        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Komitee(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("treffzeiten"),
                        rs.getString("aufgabe"),
                        rs.getString("leiterId")
                ));
            }

        } catch (SQLException e) {
            System.out.println("FEHLER LADEN KOMITEE:");
            e.printStackTrace();
        }

        return list;
    }

    public int getAnzahlKomitees() {
        String sql = "SELECT COUNT(*) AS anzahl FROM komitee";

        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("anzahl");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public Komitee findeKomitee(String komiteeId) {
        String sql = "SELECT * FROM komitee WHERE id = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, komiteeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Komitee(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("treffzeiten"),
                            rs.getString("aufgabe"),
                            rs.getString("leiterId")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void loescheKomitee(String id) {
        try (Connection conn = Database.connect()) {

            try (PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM komitee_mitglieder WHERE komiteeId = ?")) {
                ps1.setString(1, id);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM komitee WHERE id = ?")) {
                ps2.setString(1, id);
                int rows = ps2.executeUpdate();
                System.out.println("Komitee DELETE rows=" + rows);
            }

        } catch (Exception e) {
            System.out.println("DELETE ERROR");
            e.printStackTrace();
        }
    }

    public void bearbeiteKomitee(String id, String name, String treffzeiten, String aufgabe, String leiterId) {
        String sql = "UPDATE komitee SET name=?, treffzeiten=?, aufgabe=?, leiterId=? WHERE id=?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, treffzeiten);
            ps.setString(3, aufgabe);
            ps.setString(4, leiterId);
            ps.setString(5, id);

            int rows = ps.executeUpdate();
            System.out.println("Komitee UPDATE rows=" + rows);

        } catch (Exception e) {
            System.out.println("UPDATE ERROR");
            e.printStackTrace();
        }
    }

    public ArrayList<Komitee> sucheKomitee(String suchbegriff) {
        ArrayList<Komitee> ergebnis = new ArrayList<>();
        String sql = "SELECT * FROM komitee WHERE LOWER(id) LIKE ? OR LOWER(name) LIKE ? OR LOWER(aufgabe) LIKE ? ORDER BY id";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + suchbegriff.toLowerCase() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ergebnis.add(new Komitee(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("treffzeiten"),
                            rs.getString("aufgabe"),
                            rs.getString("leiterId")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ergebnis;
    }

    public void trittBei(String schuelerId, String komiteeId) {
        String checkSql = "SELECT id FROM schueler WHERE TRIM(id) = ?";
        String existsSql = "SELECT * FROM komitee_mitglieder WHERE schuelerId = ? AND komiteeId = ?";
        String insertSql = "INSERT INTO komitee_mitglieder (schuelerId, komiteeId) VALUES (?, ?)";

        try (Connection conn = Database.connect()) {

            try (PreparedStatement checkSchueler = conn.prepareStatement(checkSql)) {
                checkSchueler.setString(1, schuelerId.trim());
                ResultSet rs = checkSchueler.executeQuery();

                if (!rs.next()) {
                    System.out.println("Schüler-ID nicht gefunden: " + schuelerId);
                    return;
                }
            }

            try (PreparedStatement exists = conn.prepareStatement(existsSql)) {
                exists.setString(1, schuelerId.trim());
                exists.setString(2, komiteeId);
                ResultSet rs = exists.executeQuery();

                if (rs.next()) {
                    System.out.println("Schüler bereits im Komitee");
                    return;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, schuelerId.trim());
                ps.setString(2, komiteeId);

                int rows = ps.executeUpdate();
                System.out.println("Komitee-Beitritt rows=" + rows);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void trittAus(String schuelerId, String komiteeId) {
        String sql = "DELETE FROM komitee_mitglieder WHERE schuelerId = ? AND komiteeId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, schuelerId.trim());
            ps.setString(2, komiteeId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getMitgliederAnzahl(String komiteeId) {
        String sql = "SELECT COUNT(*) AS anzahl FROM komitee_mitglieder WHERE komiteeId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, komiteeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("anzahl");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public ArrayList<String> getMitgliederIds(String komiteeId) {
        ArrayList<String> ids = new ArrayList<>();
        String sql = "SELECT schuelerId FROM komitee_mitglieder WHERE komiteeId = ? ORDER BY schuelerId";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, komiteeId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getString("schuelerId"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ids;
    }

    public ArrayList<Schueler> getMitgliederAlsSchueler(String komiteeId, SchuelerVerwaltung sv) {
        ArrayList<Schueler> result = new ArrayList<>();

        for (String id : getMitgliederIds(komiteeId)) {
            Schueler s = sv.findeSchueler(id);
            if (s != null) {
                result.add(s);
            }
        }

        return result;
    }
}