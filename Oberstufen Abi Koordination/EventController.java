public class EventController {

    private EventVerwaltung ev;

    public EventController(EventVerwaltung ev) {
        this.ev = ev;
    }

    public void erstelleEvent(Event e) {
        ev.fuegeEventHinzu(e);
    }

    public void loescheEvent(String id) {
        ev.loescheEvent(id);
    }

    public void bearbeiteEvent(String id, String name, String datum,
                               String uhrzeit, String ort,
                               int punkte, int max) {
        ev.bearbeiteEvent(id, name, datum, uhrzeit, ort, punkte, max);
    }

    public void schuelerAnmelden(String schuelerId, String eventId) {
        ev.meldeSchuelerAn(schuelerId, eventId);
    }

    public void schuelerEntfernen(String schuelerId, String eventId) {
        ev.verlasseEvent(schuelerId, eventId);
    }

    public void eventAbschliessen(String eventId) {
        ev.eventAbschliessen(eventId);
    }

}