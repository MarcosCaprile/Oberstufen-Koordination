public class Teilnahme {
    private String schuelerId;
    private String eventId;
    private boolean hatTeilgenommen;

    public Teilnahme(String schuelerId, String eventId) {
        this.schuelerId = schuelerId;
        this.eventId = eventId;
        this.hatTeilgenommen = false;
    }

    public String getSchuelerId() {
        return schuelerId;
    }

    public String getEventId() {
        return eventId;
    }

    public boolean hatTeilgenommen() {
        return hatTeilgenommen;
    }

    public void setHatTeilgenommen(boolean hatTeilgenommen) {
        this.hatTeilgenommen = hatTeilgenommen;
    }
}