import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class MenuPrincipalGUI extends JFrame {
    public MenuPrincipalGUI() {
        setTitle("Menu Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel label = new JLabel("Menu Principal");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton btnCreateur = new JButton("Créateur d'Histoire");
        btnCreateur.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCreateur.addActionListener(e -> openCreateurHistoire());
        panel.add(btnCreateur);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnLecteur = new JButton("Lecteur d'Histoire");
        btnLecteur.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLecteur.addActionListener(e -> openLecteurHistoire());
        panel.add(btnLecteur);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnTestHistoire = new JButton("Lancer l'histoire de test");
        btnTestHistoire.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnTestHistoire.addActionListener(e -> lancerHistoireTest());
        panel.add(btnTestHistoire);

        add(panel);
    }

    private void openCreateurHistoire() {
        new CreateurHistoireGUI().setVisible(true);
    }

    private void openLecteurHistoire() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Fichiers JSON", "json"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            try {
                List<Scene> scenes = loadScenesFromJson(filePath);
                new LecteurHistoireGUI(scenes).setVisible(true);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement du fichier: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void lancerHistoireTest() {
        try {
            List<Scene> testStory = loadScenesFromJson("story.json");
            new LecteurHistoireGUI(testStory).setVisible(true);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement de l'histoire de test: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Scene> loadScenesFromJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            java.lang.reflect.Type storyType = new TypeToken<StoryData>() {}.getType();
            StoryData storyData = gson.fromJson(reader, storyType);
            return storyData.getScenes();
        }
    }

}