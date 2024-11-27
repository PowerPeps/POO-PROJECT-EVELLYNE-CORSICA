import java.util.ArrayList;
import java.util.List;

public class Relationship {
    private Character character;
    private int level;
    private List<String> flags;

    public Relationship(Character character) {
        this.character = character;
        this.level = 0;
        this.flags = new ArrayList<>();
    }

    public void increaseLevel(int value) {
        level += value;
        System.out.println("Relationship with " + character.getName() + " increased to " + level);
    }

    public void addFlag(String flag) {
        flags.add(flag);
        System.out.println("Flag added: " + flag);
    }
}
