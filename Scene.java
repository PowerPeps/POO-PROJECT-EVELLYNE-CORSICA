import java.util.ArrayList;
import java.util.List;

public class Scene {
    private String id;
    private String name;
    private List<Dialogue> dialogues;
    private List<Choice> choices;
    private String backgroundImage;

    // Constructeur vide
    public Scene() {
        this.dialogues = new ArrayList<>();
        this.choices = new ArrayList<>();
    }

    // Constructeur complet
    public Scene(String id, String name, List<Dialogue> dialogues, List<Choice> choices, String backgroundImage) {
        setId(id);
        setName(name);
        this.dialogues = dialogues != null ? dialogues : new ArrayList<>();
        this.choices = choices != null ? choices : new ArrayList<>();
        this.backgroundImage = backgroundImage;
    }

    // Getters et Setters avec validation
    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ID de la scène ne peut pas être nul ou vide.");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la scène ne peut pas être nul ou vide.");
        }
        this.name = name;
    }

    public List<Dialogue> getDialogues() {
        return dialogues;
    }

    public void setDialogues(List<Dialogue> dialogues) {
        this.dialogues = dialogues != null ? dialogues : new ArrayList<>();
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices != null ? choices : new ArrayList<>();
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    // Méthodes utilitaires
    public void addDialogue(Dialogue dialogue) {
        if (dialogue != null) {
            dialogues.add(dialogue);
        }
    }

    public void addChoice(Choice choice) {
        if (choice != null) {
            choices.add(choice);
        }
    }

    @Override
    public String toString() {
        return "Scene{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dialogues=" + dialogues +
                ", choices=" + choices +
                ", backgroundImage='" + backgroundImage + '\'' +
                '}';
    }
}
