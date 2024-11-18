import java.sql.*;

public class Personne {
    private String nom, prenom;
    private int id;

    public Personne(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = -1;
    }

    public static Personne findAll(){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from personne";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.executeQuery(sql);
            ResultSet rs = statement.getResultSet();
            // s'il y a un resultat
            while (rs.next()) {
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");
                System.out.println("-> (" + id + ") " + nom + ", " + prenom);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
