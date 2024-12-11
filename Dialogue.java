public class Dialogue {
    private String character;
    private String text;

    public Dialogue(String character, String text) {
        this.character = character;
        this.text = text;
    }

    public String getCharacter() { return character; }
    public void setCharacter(String character) { this.character = character; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}

