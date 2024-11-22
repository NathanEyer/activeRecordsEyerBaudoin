import java.sql.*;

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

    public int getIdReal(){
        return this.id_real ;
    }


    // TODO

    public static Film findById(int id){
        return ;
    }

    public Personne getRealisateur(){
        return ;
    }
}
