public class Schueler {

    private String id;
    private String vorname;
    private String nachname;
    private String mailAdresse;
    private String handynummer;

    public Schueler(String id, String vorname, String nachname, String mailAdresse, String handynummer) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.mailAdresse = mailAdresse;
        this.handynummer = handynummer;
    }

    public String getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public String getName() {
        return (vorname + " " + nachname).trim();
    }

    public String getMailAdresse() {
        return mailAdresse;
    }

    public String getHandynummer() {
        return handynummer;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setMailAdresse(String mailAdresse) {
        this.mailAdresse = mailAdresse;
    }

    public void setHandynummer(String handynummer) {
        this.handynummer = handynummer;
    }
}