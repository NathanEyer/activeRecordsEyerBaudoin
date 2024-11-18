import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {
    private DBConnection dbConnection;

    @BeforeEach
    void setUp() {
        dbConnection = DBConnection.getInstance();
    }

    @Test
    public void testGetConnection(){
        Connection c1 = DBConnection.getInstance().getConnection();
        Connection c2 = DBConnection.getInstance().getConnection();

        assertEquals(c1, c2);
    }

    @Test
    void testChangeDatabase() {
        String db1 = "test_active_records";
        String db2 = "active_records";

        dbConnection.setNomDB(db1);
        Connection conn1 = dbConnection.getConnection();

        assertNotNull(conn1, "La connexion à la première base ne doit pas être nulle.");

        dbConnection.setNomDB(db2);
        Connection conn2 = dbConnection.getConnection();

        assertNotEquals(conn1, conn2, "Les connexions ne doivent pas être identiques après avoir changé la base.");
    }

    @Test
    void testConnectionType() {
        Connection conn = dbConnection.getConnection();

        assertTrue(conn instanceof java.sql.Connection, "La connexion doit être une instance de java.sql.Connection.");
    }

    @Test
    void testConnectionAfterDatabaseChange() {
        dbConnection.setNomDB("test_active_records");
        Connection conn1 = dbConnection.getConnection();

        dbConnection.setNomDB("active_records");
        Connection conn2 = dbConnection.getConnection();

        assertNotSame(conn1, conn2, "Les connexions doivent être différentes après avoir changé la base.");
    }
}