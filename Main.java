import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Example characters
        Character char1 = new Character("Alex", "Kind", Arrays.asList("Books"), Arrays.asList("Loud noises"));
        Character char2 = new Character("Sam", "Sarcastic", Arrays.asList("Music"), Arrays.asList("Rules"));

        // Relationships
        Relationship rel1 = new Relationship(char1);
        Relationship rel2 = new Relationship(char2);

        // Player
        Player player = new Player("Player1");

        // Choices
        Choice choice1 = new Choice("Talk to Alex", "Alex liked your conversation.", null);
        Choice choice2 = new Choice("Ignore Alex", "Alex felt ignored.", null);

        // Event
        StoryEvent event = new StoryEvent("A chance encounter", Arrays.asList(choice1, choice2), Arrays.asList(char1, char2));

        // Game
        Game game = new Game("Otome Adventure");
        game.startGame();
        event.trigger();
        player.chooseOption(choice1);
    }
}
