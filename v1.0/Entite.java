public abstract class Entite {
    protected String nom;
    protected String prenom;

    public Entite(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getNomComplet() {
        return nom + " " + prenom;
    }
}
