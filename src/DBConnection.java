import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private String username = "root";
    private String password = "corroy";
    private String serverName = "localhost";
    private String portNumber = "3306";
    private String dbName = "active_records";
    private Connection connection;
    private static DBConnection instance;

    private DBConnection() {
        try {
            // chargement du driver jdbc
            Class.forName("com.mysql.cj.jdbc.Driver");

            // creation de la connection
            Properties connectionProps = new Properties();
            connectionProps.put("user", username);
            connectionProps.put("password", password);
            String urlDB = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName;
            this.connection = DriverManager.getConnection(urlDB, connectionProps);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    // Méthode pour récupérer la connexion
    public Connection getConnection() {
        return connection;
    }
}
