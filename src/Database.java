import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;

public class Database {

    private static Connection connection;

    public static Connection connect() {
        try {
            System.out.println("Pfad: " + new java.io.File(".").getAbsolutePath());

            File file = new File("db.properties");
            System.out.println("Existiert: " + file.exists());

            FileInputStream input = new FileInputStream(file);

            Properties props = new Properties();
            props.load(input);

            System.out.println("URL: " + props.getProperty("db.url"));

            return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}