import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private String name;
    private Map<String, Integer> stats;
    private List<Relationship> relationships;

    public Player(String name) {
        this.name = name;
        this.stats = new HashMap<>();
    }

    public void chooseOption(Choice choice) {
        System.out.println(name + " chose: " + choice.getText());
        // Process the choice
    }

    public void updateStats(String stat, int value) {
        stats.put(stat, stats.getOrDefault(stat, 0) + value);
        System.out.println("Updated stat: " + stat + " -> " + stats.get(stat));
    }
}
