import java.util.List;

public class StoryEvent {
    private String description;
    private List<Choice> choices;
    private List<Character> charactersInvolved;

    public void trigger() {}
    public StoryEvent resolveChoice(Choice choice) {
        return null;
    }
}