package activeRecord;

public class RealisateurAbsentException extends Exception {
    public RealisateurAbsentException(String message) {
        super(message);
    }

    public RealisateurAbsentException() {
        super("Le réalisateur est absent : l'id_real est égal à -1.");
    }
}
