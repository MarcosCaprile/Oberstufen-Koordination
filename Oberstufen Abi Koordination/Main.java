public class Main {
    public static void main(String[] args) {
        
        Database.connect();
        SchuelerVerwaltung sv = new SchuelerVerwaltung();
        KomiteeVerwaltung kv = new KomiteeVerwaltung();
        EventVerwaltung ev = new EventVerwaltung();

        new Hauptfenster(sv, kv, ev).setVisible(true);
    }
}