public class SchuelerController {

    private final SchuelerVerwaltung sv;

    public SchuelerController(SchuelerVerwaltung sv) {
        this.sv = sv;
    }

    public void erstelleSchueler(Schueler s) {
        sv.fuegeSchuelerHinzu(s);
    }

    public void loescheSchueler(String id) {
        sv.loescheSchueler(id);
    }

    public void bearbeiteSchueler(String id, String vorname, String nachname, String mail, String handy) {
        sv.bearbeiteSchueler(id, vorname, nachname, mail, handy);
    }
}