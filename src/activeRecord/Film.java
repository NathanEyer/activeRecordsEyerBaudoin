package activeRecord;

import java.sql.*;
import java.util.ArrayList;

public class Film {
    //Attributs
    private int id;
    private String titre ;
    private Personne realisateur ;

    /**
     * Construit un film
     * @param titre du film
     * @param rea realisateur du film
     */
    public Film(String titre, Personne rea) {
        this.id = -1 ;
        this.titre = titre;
        this.realisateur = rea;
    }

    /**
     * Renvoie tous les films de la base
     * @return liste de films
     */
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

    /**
     * Retourne le réalisateur du film courant
     * @return réalisateur
     */
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

    /**
     * Retourne un film en fonction d'un id
     * @param id donné
     * @return Film correspondant
     */
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
     * Renvoie le film en fonction du titre
     * @param titre
     * @return liste de films
     */
    public static ArrayList<Film> findByTitle(String titre){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Film where titre = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setString(1, titre);
            statement.executeQuery();

            return getArrayFilm(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retourne un tableau de films
     * @param statement donné
     * @return liste de films
     * @throws SQLException exception
     */
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

    /**
     * Création de la table de test
     */
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

    /**
     * Suppression de la table de test
     */
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

    /**
     * Enregistre un nouveau film
     */
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

    /**
     * Mets à jour la table
     */
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

    /**
     * Supprime une table
     */
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

    /**
     * Affiche proprement un film
     * @return chaîne de film
     */
    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", realisateur=" + realisateur +
                '}';
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
}
