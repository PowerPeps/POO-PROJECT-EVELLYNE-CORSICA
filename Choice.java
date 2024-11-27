public class Choice {
    private String text;
    private String consequence;
    private StoryEvent nextEvent;

    public Choice(String text, String consequence, StoryEvent nextEvent) {
        this.text = text;
        this.consequence = consequence;
        this.nextEvent = nextEvent;
    }

    public String getText() {
        return text;
    }

    public StoryEvent getNextEvent() {
        return nextEvent;
    }

    public void select() {
        System.out.println("You selected: " + text);
        System.out.println("Consequence: " + consequence);
    }
}
