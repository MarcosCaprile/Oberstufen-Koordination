// SchuelerVerwaltung.java
import java.sql.*;
import java.util.ArrayList;

public class SchuelerVerwaltung {

    public void fuegeSchuelerHinzu(Schueler s) {
        String sql = "INSERT INTO schueler (id, name, mailAdresse, handynummer) VALUES (?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, s.getId());
            ps.setString(2, s.getName());
            ps.setString(3, s.getMailAdresse());
            ps.setString(4, s.getHandynummer());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bearbeiteSchueler(String id, String neuerName, String neueMail, String neueNummer) {
        String sql = "UPDATE schueler SET name = ?, mailAdresse = ?, handynummer = ? WHERE id = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, neuerName);
            ps.setString(2, neueMail);
            ps.setString(3, neueNummer);
            ps.setString(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loescheSchueler(String id) {
        try (Connection conn = Database.connect()) {
            try (PreparedStatement ps1 = conn.prepareStatement(
                    "DELETE FROM teilnahme WHERE schuelerId = ?")) {
                ps1.setString(1, id);
                ps1.executeUpdate();
            }
            try (PreparedStatement ps2 = conn.prepareStatement(
                    "DELETE FROM komitee_mitglieder WHERE schuelerId = ?")) {
                ps2.setString(1, id);
                ps2.executeUpdate();
            }
            try (PreparedStatement ps3 = conn.prepareStatement(
                    "DELETE FROM schueler WHERE id = ?")) {
                ps3.setString(1, id);
                ps3.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Schueler findeSchueler(String id) {
        String sql = "SELECT * FROM schueler WHERE TRIM(id) = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Schueler(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("mailAdresse"),
                            rs.getString("handynummer")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Schueler> ladeAlleSchueler() {
        ArrayList<Schueler> liste = new ArrayList<>();
        String sql = "SELECT * FROM schueler ORDER BY id";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(new Schueler(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("mailAdresse"),
                        rs.getString("handynummer")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }

    public int getAnzahlSchueler() {
        String sql = "SELECT COUNT(*) AS anzahl FROM schueler";
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

    public ArrayList<Schueler> sucheSchueler(String suchbegriff) {
        ArrayList<Schueler> ergebnis = new ArrayList<>();
        String sql = "SELECT * FROM schueler WHERE LOWER(id) LIKE ? OR LOWER(name) LIKE ? OR LOWER(mailAdresse) LIKE ? ORDER BY id";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String pattern = "%" + suchbegriff.toLowerCase() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ergebnis.add(new Schueler(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("mailAdresse"),
                            rs.getString("handynummer")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ergebnis;
    }

    public int berechnePunkteFuerSchueler(String schuelerId) {
        String sql = "SELECT SUM(e.punktzahl) AS summe FROM teilnahme t JOIN `event` e ON t.eventId = e.id WHERE t.schuelerId = ?";
        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, schuelerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("summe");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}