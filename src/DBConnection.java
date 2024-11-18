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
    private String username = "root";
    private String password = "corroy";

    private DBConnection() {
        createConnection();
    }

    private void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Properties connectionProps = new Properties();
            connectionProps.put("user", username);
            connectionProps.put("password", password);
            String urlDB = "jdbc:mysql://" + serverName + ":" + portNumber + "/" + dbName;
            this.connection = DriverManager.getConnection(urlDB, connectionProps);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setNomDB(String nomDB) {
        if (this.dbName == null || !this.dbName.equals(nomDB)) {
            this.dbName = nomDB;
            try {
                if (connection != null) {
                    connection.close(); // Fermer l'ancienne connexion
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            createConnection();
        }
    }
}
