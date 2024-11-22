import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonneTest {

    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        // Configure DBConnection pour pointer vers la base de test
        DBConnection.setNomDB("test_active_records");
        connection = DBConnection.getInstance().getConnection();

        // Crée la table Personne
        String createTableSQL = """
                CREATE TABLE IF NOT EXISTS Personne (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(50) NOT NULL,
                    prenom VARCHAR(50) NOT NULL
                );
                """;
        PreparedStatement createTable = connection.prepareStatement(createTableSQL);
        createTable.execute();

        // Insère les données de test
        String insertDataSQL = """
                INSERT INTO Personne (nom, prenom) VALUES
                ('Spielberg', 'Steven'),
                ('Scott', 'Ridley'),
                ('Kubrick', 'Stanley'),
                ('Fincher', 'David');
                """;
        PreparedStatement insertData = connection.prepareStatement(insertDataSQL);
        insertData.execute();
    }

    @Test
    void findAll() {
        ArrayList<Personne> personnes = Personne.findAll();
        assertNotNull(personnes);
        assertEquals(4, personnes.size());
        assertEquals("Spielberg", personnes.get(0).getNom());
        assertEquals("Steven", personnes.get(0).getPrenom());
    }

    @Test
    void findById() {
        Personne personne = Personne.findById(1);
        assertNotNull(personne);
        assertEquals("Spielberg", personne.getNom());
        assertEquals("Steven", personne.getPrenom());
    }

    @Test
    void findByName() {
        ArrayList<Personne> personnes = Personne.findByName("Fincher");
        assertNotNull(personnes);
        assertEquals(1, personnes.size());
        assertEquals("David", personnes.get(0).getPrenom());
    }

    @AfterEach
    void tearDown() throws SQLException {
        // Supprime les données de la table Personne
        String deleteDataSQL = "DELETE FROM Personne;";
        PreparedStatement deleteData = connection.prepareStatement(deleteDataSQL);
        deleteData.execute();

        String dropTableSQL = "DROP TABLE IF EXISTS Personne;";
        PreparedStatement dropTable = connection.prepareStatement(dropTableSQL);
        dropTable.execute();
    }
}
