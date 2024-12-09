public class Condition {
    private String description;

    public Condition(String description) {
        this.description = description;
    }

    public boolean verifier() {
        // Logique pour vérifier la condition
        return true;
    }
    public String getDescription() {
        return(this.description);
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
