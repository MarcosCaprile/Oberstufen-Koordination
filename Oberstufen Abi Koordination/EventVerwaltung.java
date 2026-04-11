import java.sql.*;
import java.util.ArrayList;

public class EventVerwaltung {

    public void fuegeEventHinzu(Event e) {
        String sql = "INSERT INTO `event` (id, name, datum, uhrzeit, ort, punktzahl, maxTeilnehmer, abgeschlossen) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getId());
            ps.setString(2, e.getName());
            ps.setString(3, e.getDatum());
            ps.setString(4, e.getUhrzeit());
            ps.setString(5, e.getOrt());
            ps.setInt(6, e.getPunktzahl());
            ps.setInt(7, e.getMaxTeilnehmer());
            ps.setBoolean(8, false);

            int rows = ps.executeUpdate();
            System.out.println("EVENT SAVED rows=" + rows);

        } catch (Exception ex) {
            System.out.println("EVENT ERROR:");
            ex.printStackTrace();
        }
    }

    public void loescheEvent(String id) {
        try (Connection conn = Database.connect()) {

            PreparedStatement ps1 = conn.prepareStatement("DELETE FROM teilnahme WHERE eventId = ?");
            ps1.setString(1, id);
            ps1.executeUpdate();

            PreparedStatement ps2 = conn.prepareStatement("DELETE FROM `event` WHERE id = ?");
            ps2.setString(1, id);

            int rows = ps2.executeUpdate();
            System.out.println("DELETE EVENT rows=" + rows);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Event> getAlleEvents() {
        return ladeAlleEvents();
    }

    public ArrayList<Event> ladeAlleEvents() {
        ArrayList<Event> list = new ArrayList<>();

        String sql = "SELECT * FROM `event` ORDER BY datum, uhrzeit";

        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Event e = new Event(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("datum"),
                        rs.getString("uhrzeit"),
                        rs.getString("ort"),
                        rs.getInt("punktzahl"),
                        rs.getInt("maxTeilnehmer")
                );
                e.setAbgeschlossen(rs.getBoolean("abgeschlossen"));
                list.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int getAnzahlEvents() {
        String sql = "SELECT COUNT(*) AS anzahl FROM `event`";

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

    public void meldeSchuelerAn(String schuelerId, String eventId) {
        String checkSql = "SELECT id FROM schueler WHERE TRIM(id) = ?";
        String existsSql = "SELECT * FROM teilnahme WHERE schuelerId = ? AND eventId = ?";
        String insertSql = "INSERT INTO teilnahme (schuelerId, eventId, bestaetigt) VALUES (?, ?, false)";
        String checkEvent = "SELECT abgeschlossen FROM `event` WHERE id = ?";

        try (Connection conn = Database.connect()) {

            PreparedStatement psEvent = conn.prepareStatement(checkEvent);
            psEvent.setString(1, eventId);
            ResultSet rsEvent = psEvent.executeQuery();

            if (rsEvent.next() && rsEvent.getBoolean("abgeschlossen")) {
                System.out.println("Event bereits abgeschlossen → Anmeldung blockiert");
                return;
            }

            try (PreparedStatement psCheck = conn.prepareStatement(checkSql)) {
                psCheck.setString(1, schuelerId.trim());
                ResultSet rs = psCheck.executeQuery();

                if (!rs.next()) {
                    System.out.println("Schüler nicht gefunden!");
                    return;
                }
            }

            try (PreparedStatement exists = conn.prepareStatement(existsSql)) {
                exists.setString(1, schuelerId.trim());
                exists.setString(2, eventId);
                ResultSet rs = exists.executeQuery();

                if (rs.next()) {
                    System.out.println("Schüler bereits angemeldet");
                    return;
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setString(1, schuelerId.trim());
                ps.setString(2, eventId);

                int rows = ps.executeUpdate();
                System.out.println("Event Anmeldung rows=" + rows);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }

    public void verlasseEvent(String schuelerId, String eventId) {
        String sql = "DELETE FROM teilnahme WHERE schuelerId = ? AND eventId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, schuelerId.trim());
            ps.setString(2, eventId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void bestaetigeTeilnahme(String schuelerId, String eventId) {
        String sql = "UPDATE teilnahme SET bestaetigt = TRUE WHERE schuelerId = ? AND eventId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, schuelerId.trim());
            ps.setString(2, eventId);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eventAbschliessen(String eventId) {
        String check = "SELECT abgeschlossen FROM `event` WHERE id=?";

        try (Connection conn = Database.connect()) {

            PreparedStatement psCheck = conn.prepareStatement(check);
            psCheck.setString(1, eventId);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next() && rs.getBoolean("abgeschlossen")) {
                System.out.println("Event bereits abgeschlossen");
                return;
            }

            try (PreparedStatement ps1 = conn.prepareStatement("UPDATE `event` SET abgeschlossen = TRUE WHERE id = ?")) {
                ps1.setString(1, eventId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement("UPDATE teilnahme SET bestaetigt = TRUE WHERE eventId = ?")) {
                ps2.setString(1, eventId);
                ps2.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        
    }

    public int berechnePunkteFuerSchueler(String schuelerId) {
        String sql = """
                SELECT COALESCE(SUM(e.punktzahl), 0) AS gesamt
                FROM teilnahme t
                JOIN `event` e ON t.eventId = e.id
                WHERE t.schuelerId = ? AND t.bestaetigt = TRUE
                """;

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, schuelerId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("gesamt");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int getTeilnehmerAnzahl(String eventId) {
        String sql = "SELECT COUNT(*) AS anzahl FROM teilnahme WHERE eventId = ?";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, eventId);

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

    public ArrayList<String> getTeilnehmerIds(String eventId) {
        ArrayList<String> ids = new ArrayList<>();
        String sql = "SELECT schuelerId FROM teilnahme WHERE eventId = ? ORDER BY schuelerId";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, eventId);

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

    public ArrayList<Event> sucheEvent(String text) {
        ArrayList<Event> result = new ArrayList<>();
        String sql = "SELECT * FROM `event` WHERE LOWER(id) LIKE ? OR LOWER(name) LIKE ? ORDER BY datum, uhrzeit";

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String pattern = "%" + text.toLowerCase() + "%";
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Event e = new Event(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("datum"),
                            rs.getString("uhrzeit"),
                            rs.getString("ort"),
                            rs.getInt("punktzahl"),
                            rs.getInt("maxTeilnehmer")
                    );
                    e.setAbgeschlossen(rs.getBoolean("abgeschlossen"));
                    result.add(e);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void bearbeiteEvent(String id, String name, String datum, String uhrzeit, String ort, int punktzahl, int maxTeilnehmer) {
        String sql = "UPDATE `event` SET name=?, datum=?, uhrzeit=?, ort=?, punktzahl=?, maxTeilnehmer=? WHERE id=?";
        String check = "SELECT abgeschlossen FROM `event` WHERE id=?";

        

        try (Connection conn = Database.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, datum);
            ps.setString(3, uhrzeit);
            ps.setString(4, ort);
            ps.setInt(5, punktzahl);
            ps.setInt(6, maxTeilnehmer);
            ps.setString(7, id);

            ps.executeUpdate();

            PreparedStatement psCheck = conn.prepareStatement(check);
            psCheck.setString(1, id);
            ResultSet rs = psCheck.executeQuery();

            if (rs.next() && rs.getBoolean("abgeschlossen")) {
                System.out.println("Event ist abgeschlossen → Bearbeitung blockiert");
                return;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}