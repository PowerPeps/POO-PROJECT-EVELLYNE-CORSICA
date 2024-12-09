import java.util.ArrayList;
import java.util.List;

public class Scene {
    private String description;
    private String image;
    private List<VariableHistorique> variablesHistorique;
    private Scene nextScene;
    private List<Personnage> personnagesPresent;

    public Scene(String description, String image) {
        this.description = description;
        this.image = image;
        this.variablesHistorique = new ArrayList<>();
        this.personnagesPresent = new ArrayList<>();
    }

    public void setNextScene(Scene nextScene) {
        this.nextScene = nextScene;
    }

    public Scene getNextScene() {
        return nextScene;
    }

    public void addPersonnage(Personnage personnage) {
        personnagesPresent.add(personnage);
    }

    public String getDescription() {
        return description;
    }

    public List<Personnage> getPersonnagesPresent() {
        return personnagesPresent;
    }

    public void addVariableHistorique(VariableHistorique variable) {
        variablesHistorique.add(variable);
    }

    public List<VariableHistorique> getVariablesHistorique() {
        return variablesHistorique;
    }
}
