package activeRecord;

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

    public static void createTable(){
        try {
            DBConnection.setNomDB("test_active_records");
            Connection connection = DBConnection.getInstance().getConnection();
            String createTableSQL = """
                CREATE TABLE Personne (
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

    public static void deleteTable(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String dropTableSQL = "DROP TABLE IF EXISTS Personne;";
            PreparedStatement dropTable = connection.prepareStatement(dropTableSQL);
            dropTable.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(){
        if(this.id == -1) saveNew();
        else update();
    }

    private void saveNew(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();

            String sql = "INSERT INTO Personne(nom, prenom) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.nom);
            statement.setString(2, this.prenom);
            statement.execute();

            sql = "Select * from Personne where nom = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, this.nom);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                this.id = Integer.parseInt(rs.getString("id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getId() {
        return id;
    }
}
