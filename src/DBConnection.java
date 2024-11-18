import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private String serverName = "localhost";
    private String portNumber = "3306";
    private String dbName = "active_records";
    private Connection connection;
    private static DBConnection instance;

    private DBConnection(String username, String password) {
        try {
            // chargement du driver jdbc
            Class.forName("com.mysql.cj.jdbc.Driver");

            // creation de la connection
            Properties connectionProps = new Properties();
            connectionProps.put("user", username);
            connectionProps.put("password", password);
            String urlDB = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName;
            this.connection = DriverManager.getConnection(urlDB, connectionProps);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized DBConnection getInstance(String username, String password) {
        if (instance == null) {
            instance = new DBConnection(username, password);
        }
        return instance;
    }

    // Méthode pour récupérer la connexion
    public Connection getConnection() {
        
        return connection;
    }

    public void setNomDB(String nomDB) {
        this.dbName = nomDB;
    }
}
