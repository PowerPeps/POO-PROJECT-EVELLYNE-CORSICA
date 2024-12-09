public class Choix {
    private String description;
    private Condition condition;
    private Scene sceneSuivante;

    public Choix(String description, Condition condition, Scene sceneSuivante) {
        this.description = description;
        this.condition = condition;
        this.sceneSuivante = sceneSuivante;
    }

    public String getDescription() {
        return description;
    }

    public Scene getSceneSuivante() {
        return sceneSuivante;
    }

    public Condition getCondition() {
        return condition;
    }
}
