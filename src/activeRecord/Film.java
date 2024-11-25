import activeRecord.Personne;

public class Film {

    private String titre ;

    private int id ;

    private int id_real ;

    public Film(String titre, Personne real) {
        this.titre = titre;
        this.id = real.getId();
        this.id_real = -1 ;
    }

    private Film(String titre, int id, int id_real){
        this.titre = titre ;
        this.id = id ;
        this.id_real = id_real ;
    }

    public void setTitre(String titre){
        this.titre = titre ;
    }

    public String getTitre(){
        return this.titre ;
    }

    public int getId(){
        return this.id ;
    }

    public void setId(int id){
        this.id = id ;
    }

    public int getIdReal(){
        return this.id_real ;
    }


    // TODO

    public static Film findById(int id){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Film where id = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Film f = new Film(rs.getString("titre"), rs.getString("id"), rs.getString("id_real"));
                f.setId(id);
                return f;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Personne getRealisateur(){
        try {
            Connection connect = DBConnection.getInstance().getConnection();
            String sql = "Select * from Personne where id = ?";
            PreparedStatement statement = connect.prepareStatement(sql);
            statement.setInt(1, this.id_real);
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                Personne p = new Personne(rs.getString("nom"), rs.getString("prenom"));
                p.setId(this.id_real);
                return p;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
