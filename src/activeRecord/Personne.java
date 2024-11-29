package activeRecord;

import java.sql.*;
import java.util.ArrayList;

public class Personne {
    //Attributs
    private String nom, prenom;
    private int id;

    /**
     * Construit une Personne avec un nom et un prénom
     * id initialisé à -1 tant qu'il n'est pas add dans la base
     * @param nom de la Personne
     * @param prenom de la Personne
     */
    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = -1;
    }

    /**
     * Retourne toutes les personnes de la base
     * @return Liste
     */
    public static ArrayList<Personne> findAll(){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Personne";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.execute();

            return getArrayPersonne(statement.getResultSet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retourne une personne en fonction de l'id
     * @param id de la personne recherché
     * @return Personne
     */
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
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retourne la ou les personne à partir d'un nom
     * @param name de la personne recherchée
     * @return Liste
     */
    public static ArrayList<Personne> findByName(String name){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Personne where nom = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();

            return getArrayPersonne(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Crée les listes selon le statement
     * @param rs sur lequel travailler
     * @return Liste
     * @throws SQLException erreurs sql
     */
    private static ArrayList<Personne> getArrayPersonne(ResultSet rs) throws SQLException {
        ArrayList<Personne> tabPersonnes = new ArrayList<>();
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

    /**
     * Crée la table de test
     */
    public static void createTable(){
        try {
            DBConnection.setNomDB("test_active_records");
            Connection connection = DBConnection.getInstance().getConnection();
            String createTableSQL = """
                CREATE TABLE if not exists Personne (
                  id int(11) NOT NULL AUTO_INCREMENT,
                  nom varchar(40) NOT NULL,
                  prenom varchar(40) NOT NULL,
                    PRIMARY KEY (id)
                ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
                """;
            PreparedStatement createTable = connection.prepareStatement(createTableSQL);
            createTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Supprime la table de test
     */
    public static void deleteTable(){
        try {
            DBConnection.setNomDB("test_active_records");
            Connection connection = DBConnection.getInstance().getConnection();
            String dropTableSQL = "DROP TABLE IF EXISTS Personne;";
            PreparedStatement dropTable = connection.prepareStatement(dropTableSQL);
            dropTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mets à jour la table
     */
    public void save(){
        if(this.id == -1) saveNew();
        else update();
    }

    /**
     * Insère une nouvelle personne dans la table
     */
    private void saveNew(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            String sql = "INSERT INTO Personne(nom, prenom) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, this.nom);
            statement.setString(2, this.prenom);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id = generatedKeys.getInt(1); // Récupérer l'ID auto-généré
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Mets à jour une personne dans la table
     */
    private void update(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "UPDATE Personne set nom = ?, prenom = ? where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.nom);
            statement.setString(2, this.prenom);
            statement.setInt(3, this.id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Supprime la personne courante de la table
     */
    public void delete(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "Delete from Personne where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            statement.execute();
            this.id = -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param id de la personne
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return nom de la personne
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return prénom de la personne
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param nom de la personne
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @param prenom de la personne
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return id de la personne
     */
    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Personne{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", id=" + id +
                '}';
    }
}
