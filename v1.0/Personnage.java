public class Personnage extends Entite {
    private int age;
    private String roleDansHistoire;
    private Scene positionActuelle;

    public Personnage(String nom, String prenom, int age, String roleDansHistoire) {
        super(nom, prenom);
        this.age = age;
        this.roleDansHistoire = roleDansHistoire;
    }

    public void setPositionActuelle(Scene positionActuelle) {
        this.positionActuelle = positionActuelle;
    }

    public Scene getPositionActuelle() {
        return positionActuelle;
    }

    public String getRoleDansHistoire() {
        return roleDansHistoire;
    }
}
