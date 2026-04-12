public class SchuelerController {

    private SchuelerVerwaltung sv;

    public SchuelerController(SchuelerVerwaltung sv) {
        this.sv = sv;
    }

    public void erstelleSchueler(Schueler s) {
        sv.fuegeSchuelerHinzu(s);
    }

    public void loescheSchueler(String id) {
        sv.loescheSchueler(id);
    }

    public void bearbeiteSchueler(String id, String name,
                                  String mail, String handy) {
        sv.bearbeiteSchueler(id, name, mail, handy);
    }
}