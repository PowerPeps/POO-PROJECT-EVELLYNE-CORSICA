public class LecteurHistoire {
    private Scene currentScene;

    public void chargerHistoire(Scene sceneDepart) {
        this.currentScene = sceneDepart;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void lireScene() {
        if (currentScene != null) {
            System.out.println(currentScene.getDescription());
            for (Personnage personnage : currentScene.getPersonnagesPresent()) {
                System.out.println("Personnage : " + personnage.getNomComplet());
            }
        }
    }

    public void changerScene(Scene nextScene) {
        this.currentScene = nextScene;
        lireScene();
    }
}
