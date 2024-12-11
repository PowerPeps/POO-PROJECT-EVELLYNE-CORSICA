import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LecteurHistoireGUI extends JFrame {
    private List<Scene> scenes;
    private Scene currentScene;
    private Map<String, Scene> sceneMap;
    private JTextArea textScene;
    private JPanel panelChoix;

    public LecteurHistoireGUI() {
        setTitle("Lecteur d'Histoire");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel panelScene = new JPanel();
        panelScene.setLayout(new BorderLayout());
        panelScene.setBorder(BorderFactory.createTitledBorder("Scène Actuelle"));
        textScene = new JTextArea();
        textScene.setEditable(false);
        panelScene.add(new JScrollPane(textScene), BorderLayout.CENTER);
        add(panelScene, BorderLayout.CENTER);

        panelChoix = new JPanel();
        panelChoix.setLayout(new GridLayout(0, 1));
        add(panelChoix, BorderLayout.SOUTH);

        textScene.setText("Bienvenue dans l'histoire. \n Voici le texte de la scène initiale...");
        JButton btnChoix = new JButton("Continuer");
        btnChoix.addActionListener(e -> JOptionPane.showMessageDialog(this, "Choix sélectionné."));
        panelChoix.add(btnChoix);
    }
}
