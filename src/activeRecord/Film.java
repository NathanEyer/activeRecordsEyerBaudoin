package activeRecord;

import java.sql.*;
import java.util.ArrayList;

public class Film {

    private int id;
    private String titre ;
    private int id_rea ;

    public Film(String titre, Personne rea) {
        this.id = -1 ;
        this.titre = titre;
        this.id_rea = rea.getId();
    }

    //   GETTER AND SETTER 

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getIdRea() {
        return this.id_rea;
    }

    public void setIdRea(String id_rea) {
        this.id_rea = id_rea;
    }


    // Methode active record

    public static ArrayList<Film> findAll(){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Film";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.execute();

            return getArrayFilm(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static Personne getRealisateur(){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Personne where id = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, id_rea);
            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                Personne p = new Personne(rs.getString("nom"), rs.getString("prenom"));
                p.setId(id_rea);
                return p;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Film findById(int id){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Film where id = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Film f = new Film(rs.getString("titre"), rs.getInt("id_rea"));
                f.setId(id);
                return f;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * On suppose que l'on peut avoir plusieurs film du meme nom
     */
    public static ArrayList<Film> findByName(String name){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Film where titre = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, name);
            statement.executeQuery();

            return getArrayPersonne(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Film> getArrayPersonne(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.getResultSet();
        ArrayList<Film> tabPersonnes = new ArrayList<>();
        int i = 0;
        while (rs.next()) {
            int id = rs.getInt("id");
            String prenom = rs.getString("titre");
            int id_rea = rs.getInt("id_rea");

            Film p = new Film(titre, id_rea);
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
                CREATE TABLE Film (
                      id int(11) NOT NULL,
                      titre varchar(40) NOT NULL,
                      id_rea int(11) NOT NULL,
                      PRIMARY KEY (id,id_rea)
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
            String dropTableSQL = "DROP TABLE IF EXISTS Film;";
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

            String sql = "INSERT INTO Film(titre, id_rea) VALUES (?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.titre);
            statement.setString(2, this.id_rea);
            statement.execute();

            sql = "Select * from Film where nom = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, this.nom);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                this.id = rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void update(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "UPDATE Film set titre = ?, id_rea = ? where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, this.titre);
            statement.setString(2, this.id_rea);
            statement.setInt(3, this.id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(){
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String sql = "Delete from Film where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, this.id);
            statement.execute();
            this.id = -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
