package activeRecord;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    //Attributs de connexions
    private String serverName = "localhost";
    private String portNumber = "3306";
    private static String dbName = "active_records";
    private static Connection connection;
    private static DBConnection instance;
    private String username = "root";
    private String password = "corroy";

    /**
     * Création de la connexion
     */
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

    /**
     * Récupération de l'instance
     * @return instance créé
     */
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    /**
     * récupération de la connexion
     * @return Connection
     */
    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                createConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de la connexion", e);
        }
        return connection;
    }

    /**
     * Change la base de données utilisées
     * @param nomDB bd
     */
    public static synchronized void setNomDB(String nomDB) {
        // Vérifie si le nom de la base de données a changé
        if (dbName == null || !dbName.equals(nomDB)) {
            dbName = nomDB;

            try {
                // Ferme l'ancienne connexion si elle existe
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Recrée la connexion avec la nouvelle base de données
            if (instance != null) {
                instance.createConnection();
            }
        }
    }
}
