import activeRecord.DBConnection;
import activeRecord.Film;
import activeRecord.Personne;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // Configure DBConnection pour pointer vers la base de test
        DBConnection.setNomDB("test_active_records");
        connection = DBConnection.getInstance().getConnection();

        // Crée la table Personne
        String dropSql = "DROP TABLE IF EXISTS Film";
        String createTableSQL = """
                CREATE TABLE Film (
                      id INT(11) NOT NULL,
                      titre VARCHAR(40) NOT NULL,
                      id_rea INT(11) NOT NULL,
                      PRIMARY KEY (id, id_rea)
                );
                """;
        PreparedStatement dropTable = connection.prepareStatement(dropSql);
        dropTable.execute();
        PreparedStatement createTable = connection.prepareStatement(createTableSQL);
        createTable.execute();
        Personne.createTable();

        // Insère les données de test
        String insertDataSQL = """
                INSERT INTO Film (id, titre, id_rea) VALUES
                 (1, 'Arche perdue', 1),
                 (2, 'Alien', 2),
                 (3, 'Temple Maudit', 1),
                 (4, 'Blade Runner', 2),
                 (5, 'Alien3', 4),
                 (6, 'Fight Club', 4),
                 (7, 'Orange Mecanique', 3);
                """;
        PreparedStatement insertData = connection.prepareStatement(insertDataSQL);
        insertData.execute();
    }

    @Test
    void findAll() {
        ArrayList<Film> films = Film.findAll();
        assertNotNull(films);
        assertEquals(7, films.size());
        assertEquals("Arche perdue", films.getFirst().getTitre());
    }

    @Test
    void findById() {
        Film film = Film.findById(1);
        assertNotNull(film);
        assertEquals("Arche perdue", film.getTitre());
    }

    @Test
    void findByTitle() {
        ArrayList<Film> film = Film.findByTitle("Blade Runner");
        assertNotNull(film);
        assertEquals(1, film.size());
        assertEquals("Blade Runner", film.get(0).getTitre());
    }

    @Test
    void findByRealisateur() {
        return ;
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Supprime les données de la table Film
        String deleteDataSQL = "DELETE FROM Film;";
        PreparedStatement deleteData = connection.prepareStatement(deleteDataSQL);
        deleteData.execute();

        String dropTableSQL = "DROP TABLE IF EXISTS Film;";
        PreparedStatement dropTable = connection.prepareStatement(dropTableSQL);
        dropTable.execute();
    }
}
