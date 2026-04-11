import java.awt.Component;

public class Komitee {

    private String id;
    private String name;
    private String treffzeiten;
    private String aufgabe;
    private String leiterId;

    public Komitee(String id, String name, String treffzeiten, String aufgabe, String leiterId) {
        this.id = id;
        this.name = name;
        this.treffzeiten = treffzeiten;
        this.aufgabe = aufgabe;
        this.leiterId = leiterId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTreffzeiten() {
        return treffzeiten;
    }

    public String getAufgabe() {
        return aufgabe;
    }

    public String getLeiterId() {
        return leiterId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTreffzeiten(String treffzeiten) {
        this.treffzeiten = treffzeiten;
    }

    public void setAufgabe(String aufgabe) {
        this.aufgabe = aufgabe;
    }

    public void setLeiterId(String leiterId) {
        this.leiterId = leiterId;
    }

    public Component getMitglieder() {
        throw new UnsupportedOperationException("Unimplemented method 'getMitglieder'");
    }
}