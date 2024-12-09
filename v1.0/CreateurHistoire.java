public class CreateurHistoire {
    public Personnage creerPersonnage(String nom, String prenom, int age, String roleDansHistoire) {
        return new Personnage(nom, prenom, age, roleDansHistoire);
    }

    public Scene creerScene(String description, String image) {
        return new Scene(description, image);
    }

    public Dialogue creerDialogue(String content) {
        return new Dialogue(content);
    }

    public Choix creerChoix(String description, Condition condition, Scene sceneSuivante) {
        return new Choix(description, condition, sceneSuivante);
    }

    public Condition creerCondition(String description) {
        return new Condition(description);
    }

    public void lierScenes(Scene scene1, Scene scene2, boolean lienReciproque) {
        scene1.setNextScene(scene2);
        if (lienReciproque) {
            scene2.setNextScene(scene1);
        }
    }

    public void ajouterDialogueArbre(ArbreDialogue arbre, Dialogue dialogue, int position) {
        if (position == -1) {
            arbre.addDialogue(dialogue);
        } else {
            arbre.getDialogues().add(position, dialogue);
        }
    }

    public void ajouterPersonnageScene(Scene scene, Personnage personnage, boolean remplacerSiPresent) {
        if (remplacerSiPresent) {
            scene.getPersonnagesPresent().removeIf(p -> p.getNomComplet().equals(personnage.getNomComplet()));
        }
        scene.addPersonnage(personnage);
    }
}
