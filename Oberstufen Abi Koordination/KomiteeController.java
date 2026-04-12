public class KomiteeController {

    private KomiteeVerwaltung kv;

    public KomiteeController(KomiteeVerwaltung kv) {
        this.kv = kv;
    }

    public void erstelleKomitee(Komitee k) {
        kv.fuegeKomiteeHinzu(k);
    }

    public void loescheKomitee(String id) {
        kv.loescheKomitee(id);
    }

    public void bearbeiteKomitee(String id, String name,
                                 String treff, String aufgabe, String leiter) {
        kv.bearbeiteKomitee(id, name, treff, aufgabe, leiter);
    }

    public void trittBei(String schuelerId, String komiteeId) {
        kv.trittBei(schuelerId, komiteeId);
    }

    public void trittAus(String schuelerId, String komiteeId) {
        kv.trittAus(schuelerId, komiteeId);
    }
}