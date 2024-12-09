import java.util.ArrayList;
import java.util.List;

public class ArbreDialogue {
    private List<Dialogue> dialogues;

    public ArbreDialogue() {
        this.dialogues = new ArrayList<>();
    }

    public void addDialogue(Dialogue dialogue) {
        dialogues.add(dialogue);
    }

    public List<Dialogue> getDialogues() {
        return dialogues;
    }
}
