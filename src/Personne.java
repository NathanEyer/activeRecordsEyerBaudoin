import java.sql.*;
import java.util.ArrayList;

public class Personne {
    private String nom, prenom;
    private int id;

    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = -1;
    }

    public static ArrayList<Personne> findAll(){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Personne";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.execute();

            return getArrayPersonne(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Personne findById(int id){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Personne where id = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Personne p = new Personne(rs.getString("nom"), rs.getString("prenom"));
                p.setId(id);
                return p;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Personne> findByName(String name){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Personne where nom = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, name);
            statement.executeQuery();

            return getArrayPersonne(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Personne> getArrayPersonne(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.getResultSet();
        ArrayList<Personne> tabPersonnes = new ArrayList<>();
        int i = 0;
        while (rs.next()) {
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            int id = rs.getInt("id");
            Personne p = new Personne(nom, prenom);
            p.setId(id);
            tabPersonnes.add(p);
        }
        return tabPersonnes;
    }

    public static void createTable(Connection connection){
        // Crée la table Personne
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS Personne (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(50) NOT NULL,
                    prenom VARCHAR(50) NOT NULL
                );
                """;
        try {
            PreparedStatement createTable = connection.prepareStatement(createTableSQL);
            createTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void dropTable(Connection connection){
        String dropTableSQL = "DROP TABLE IF EXISTS Personne;";
        PreparedStatement dropTable = null;
        try {
            dropTable = connection.prepareStatement(dropTableSQL);
            dropTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }
}
