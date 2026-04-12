// Teilnahme.java
public class Teilnahme {

    private String schuelerId;
    private String eventId;
    private boolean bestaetigt;

    public Teilnahme(String schuelerId, String eventId) {
        this.schuelerId = schuelerId;
        this.eventId = eventId;
        this.bestaetigt = false;
    }

    public String getSchuelerId() {
        return schuelerId;
    }

    public String getEventId() {
        return eventId;
    }

    public boolean isBestaetigt() {
        return bestaetigt;
    }

    public void setBestaetigt(boolean bestaetigt) {
        this.bestaetigt = bestaetigt;
    }
}