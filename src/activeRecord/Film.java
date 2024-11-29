package activeRecord;

import java.sql.*;
import java.util.ArrayList;

public class Film {

    private int id;
    private String titre ;
    private Personne realisateur ;

    public Film(String titre, Personne rea) {
        this.id = -1 ;
        this.titre = titre;
        this.realisateur = rea;
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


    public Personne getRealisateur(){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Personne where id = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, realisateur.getId());
            ResultSet rs = statement.executeQuery();

            if(rs.next()){
                Personne p = new Personne(rs.getString("nom"), rs.getString("prenom"));
                p.setId(realisateur.getId());
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
                Personne p = Personne.findById(rs.getInt("id_rea"));
                Film f = new Film(rs.getString("titre"), p);
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

            return getArrayFilm(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Film> getArrayFilm(PreparedStatement statement) throws SQLException {
        ResultSet rs = statement.getResultSet();
        ArrayList<Film> tabFilms = new ArrayList<>();
        int i = 0;
        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");

            Personne p = Personne.findById(rs.getInt("id_rea"));
            Film f = new Film(titre, p);
            f.setId(id);
            tabFilms.add(f);
        }
        return tabFilms;
    }

    public static void createTable(){
        try {
            DBConnection.setNomDB("test_active_records");
            Connection connection = DBConnection.getInstance().getConnection();
            String createTableSQL = """
                CREATE TABLE if not exists Film (
                      id int(11) NOT NULL AUTO_INCREMENT,
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
            statement.setInt(2, this.realisateur.getId());
            statement.execute();

            sql = "Select * from Film where titre = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, this.titre);
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
            statement.setInt(2, this.realisateur.getId());
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

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", realisateur=" + realisateur +
                '}';
    }
}
