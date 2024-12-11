import java.util.List;

public class Scene {
    private String id;
    private String name;
    private List<String> dialogues;
    private List<Choice> choices;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getDialogues() { return dialogues; }
    public void setDialogues(List<String> dialogues) { this.dialogues = dialogues; }

    public List<Choice> getChoices() { return choices; }
    public void setChoices(List<Choice> choices) { this.choices = choices; }
}
