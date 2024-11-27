import java.util.ArrayList;
import java.util.List;

public class Game {
    private String title;
    private StoryEvent currentEvent;

    public Game(String title) {
        this.title = title;
        this.currentEvent = null;
    }

    public void startGame() {
        System.out.println("Game started: " + title);
        currentEvent.trigger();
    }

    public void loadGame(String saveFile) {
        System.out.println("Game loaded from " + saveFile);
    }

    public void saveGame(String saveFile) {
        System.out.println("Game saved to " + saveFile);
    }

    public void progressEvent() {
        if (currentEvent != null) {
            System.out.println("Progressing to the next event...");
            currentEvent = currentEvent.resolveChoice(null); // Simplification
        }
    }
}
