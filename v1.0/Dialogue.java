import java.util.ArrayList;
import java.util.List;

public class Dialogue {
    private String content;
    private List<Choix> choixDisponibles;

    public Dialogue(String content) {
        this.content = content;
        this.choixDisponibles = new ArrayList<>();
    }

    public void addChoix(Choix choix) {
        choixDisponibles.add(choix);
    }

    public String getContent() {
        return content;
    }

    public List<Choix> getChoixDisponibles() {
        return choixDisponibles;
    }
}
