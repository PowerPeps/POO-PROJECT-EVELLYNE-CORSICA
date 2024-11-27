import java.util.List;

public class StoryEvent {
    private String description;
    private List<Choice> choices;
    private List<Character> charactersInvolved;

    public StoryEvent(String description, List<Choice> choices, List<Character> charactersInvolved) {
        this.description = description;
        this.choices = choices;
        this.charactersInvolved = charactersInvolved;
    }

    public void trigger() {
        System.out.println("Event: " + description);
    }

    public StoryEvent resolveChoice(Choice choice) {
        if (choice != null) {
            System.out.println("Resolving choice: " + choice.getText());
            return choice.getNextEvent();
        }
        return null;
    }
}
