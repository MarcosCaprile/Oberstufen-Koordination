// Schueler.java
public class Schueler {

    private String id;
    private String name;
    private String mailAdresse;
    private String handynummer;

    public Schueler(String id, String name, String mailAdresse, String handynummer) {
        this.id = id;
        this.name = name;
        this.mailAdresse = mailAdresse;
        this.handynummer = handynummer;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMailAdresse() {
        return mailAdresse;
    }

    public String getHandynummer() {
        return handynummer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMailAdresse(String mailAdresse) {
        this.mailAdresse = mailAdresse;
    }

    public void setHandynummer(String handynummer) {
        this.handynummer = handynummer;
    }
}