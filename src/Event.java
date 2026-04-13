public class Event {

    private final String id;
    private String name;
    private String datum;
    private String uhrzeit;
    private String ort;
    private int punktzahl;
    private int maxTeilnehmer;
    private boolean abgeschlossen;

    public Event(String id, String name, String datum, String uhrzeit,
                 String ort, int punktzahl, int maxTeilnehmer) {
        this.id = id;
        this.name = name;
        this.datum = datum;
        this.uhrzeit = uhrzeit;
        this.ort = ort;
        this.punktzahl = punktzahl;
        this.maxTeilnehmer = maxTeilnehmer;
        this.abgeschlossen = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDatum() {
        return datum;
    }

    public String getUhrzeit() {
        return uhrzeit;
    }

    public String getOrt() {
        return ort;
    }

    public int getPunktzahl() {
        return punktzahl;
    }

    public int getMaxTeilnehmer() {
        return maxTeilnehmer;
    }

    public boolean isAbgeschlossen() {
        return abgeschlossen;
    }

    // Alias, falls irgendwo im UI "istAbgeschlossen()" verwendet wird
    public boolean istAbgeschlossen() {
        return abgeschlossen;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public void setUhrzeit(String uhrzeit) {
        this.uhrzeit = uhrzeit;
    }

    public void setOrt(String ort) {
        this.ort = ort;
    }

    public void setPunktzahl(int punktzahl) {
        this.punktzahl = punktzahl;
    }

    public void setMaxTeilnehmer(int maxTeilnehmer) {
        this.maxTeilnehmer = maxTeilnehmer;
    }

    public void setAbgeschlossen(boolean abgeschlossen) {
        this.abgeschlossen = abgeschlossen;
    }
}