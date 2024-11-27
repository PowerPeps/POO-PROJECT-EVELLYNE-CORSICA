import java.util.List;

public class Character {
    private String name;
    private String personality;
    private List<String> likes;
    private List<String> dislikes;
    private int relationshipLevel;

    public Character(String name, String personality, List<String> likes, List<String> dislikes) {
        this.name = name;
        this.personality = personality;
        this.likes = likes;
        this.dislikes = dislikes;
        this.relationshipLevel = 0;
    }

    public void interact() {
        System.out.println(name + " is interacting with you.");
    }

    public void updateRelationship(int level) {
        relationshipLevel += level;
        System.out.println("Relationship level with " + name + ": " + relationshipLevel);
    }

    public String getName(){
        return(this.name);
    }
}
