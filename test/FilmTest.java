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
        String createTableSQL = """
                DROP TABLE IF EXISTS Film
                CREATE TABLE Film (
                      id int(11) NOT NULL,
                      titre varchar(40) NOT NULL,
                      id_rea int(11) NOT NULL,
                      PRIMARY KEY (id,id_rea)
                ) ENGINE=InnoDB DEFAULT CHARSET=latin1;
                """;
        PreparedStatement createTable = connection.prepareStatement(createTableSQL);
        createTable.execute();

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
        ArrayList<Film> personnes = Film.findAll();
        assertNotNull(personnes);
        assertEquals(4, personnes.size());
        assertEquals("Spielberg", personnes.get(0).getNom());
        assertEquals("Steven", personnes.get(0).getPrenom());
    }

    @Test
    void findById() {
        Film personne = Film.findById(1);
        assertNotNull(personne);
        assertEquals("Spielberg", personne.getNom());
        assertEquals("Steven", personne.getPrenom());
    }

    @Test
    void findByName() {
        ArrayList<Film> personnes = Film.findByName("Fincher");
        assertNotNull(personnes);
        assertEquals(1, personnes.size());
        assertEquals("David", personnes.get(0).getPrenom());
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
