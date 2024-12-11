import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URL;
import java.awt.Toolkit;


public class LecteurHistoireGUI extends JFrame {
    private List<Scene> scenes;
    private Scene currentScene;
    private Map<String, Scene> sceneMap;
    private JTextPane textScene;
    private JPanel panelChoix;
    private JLabel backgroundLabel;
    private List<Dialogue> currentDialogues;
    private int dialogueIndex;

    public LecteurHistoireGUI(List<Scene> scenes) {
        this.scenes = scenes;
        this.sceneMap = new HashMap<>();
        for (Scene scene : scenes) {
            sceneMap.put(scene.getId(), scene);
        }

        setTitle("Lecteur d'Histoire");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        // Label pour afficher l'image de fond
        backgroundLabel = new JLabel();
        backgroundLabel.setLayout(new BorderLayout());
        add(backgroundLabel, BorderLayout.CENTER);

        // Panneau pour afficher les dialogues
        textScene = new JTextPane();
        textScene.setEditable(false);
        textScene.setForeground(Color.WHITE);
        textScene.setBackground(new Color(0, 0, 0)); // Fond semi-transparent noir
        textScene.setFont(new Font("Serif", Font.BOLD, 20));
        textScene.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textScene);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(scrollPane, BorderLayout.CENTER);

        // Panneau pour afficher les choix
        panelChoix = new JPanel();
        panelChoix.setLayout(new GridLayout(0, 1));
        panelChoix.setOpaque(false);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(textPanel, BorderLayout.CENTER);
        bottomPanel.add(panelChoix, BorderLayout.SOUTH);

        backgroundLabel.add(bottomPanel, BorderLayout.SOUTH);

        if (!scenes.isEmpty()) {
            loadScene(scenes.get(0));
        }

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    advanceDialogue();
                }
            }
        });

        setFocusable(true);
        requestFocusInWindow();
    }

    private void loadScene(Scene scene) {
        if (scene == null) {
            JOptionPane.showMessageDialog(this, 
                "Scène introuvable", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        currentScene = scene;
        currentDialogues = scene.getDialogues();
        dialogueIndex = 0;
        textScene.setText("");
        updateBackground(scene.getBackgroundImage());
        updateChoices(scene.getChoices());
        panelChoix.setVisible(false);
        if (!currentDialogues.isEmpty()) {
            showDialogue(currentDialogues.get(dialogueIndex));
        }
    }

    private void showDialogue(Dialogue dialogue) {
        textScene.setText(dialogue.getCharacter() + ": " + dialogue.getText());
    }

    private void advanceDialogue() {
        if (currentDialogues == null || dialogueIndex >= currentDialogues.size() - 1) {
            // Tous les dialogues sont affichés
            panelChoix.setVisible(true);
            return;
        }
        dialogueIndex++;
        showDialogue(currentDialogues.get(dialogueIndex));
    }

    private void updateBackground(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                BufferedImage img = null;
                File file = new File(imagePath);
                img = ImageIO.read(file);

                if (img != null) {
                    Image scaledImg = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                    backgroundLabel.setIcon(new ImageIcon(scaledImg));
                } else {
                    throw new IOException("Impossible de charger l'image après plusieurs tentatives.");
                }
            } catch (IOException e) {
                e.printStackTrace(); // Pour avoir plus de détails sur l'erreur
                System.out.println("Répertoire de travail: " + System.getProperty("user.dir"));
                JOptionPane.showMessageDialog(this, 
                    "Impossible de charger l'image : " + imagePath + "\n" + 
                    "Erreur détaillée : " + e.getMessage() + "\n" +
                    "Répertoire de travail : " + System.getProperty("user.dir"), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                backgroundLabel.setIcon(null);
                backgroundLabel.setBackground(Color.BLACK);
                backgroundLabel.setOpaque(true);
            }
        } else {
            backgroundLabel.setIcon(null);
            backgroundLabel.setBackground(Color.BLACK);
            backgroundLabel.setOpaque(true);
        }
    }

    private void updateChoices(List<Choice> choices) {
        panelChoix.removeAll();
        for (Choice choice : choices) {
            JButton btnChoice = new JButton(choice.getText());
            btnChoice.addActionListener(e -> {
                Scene nextScene = sceneMap.get(choice.getTargetSceneId());
                if (nextScene != null) {
                    loadScene(nextScene);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "ID de scène invalide : " + choice.getTargetSceneId(), 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            panelChoix.add(btnChoice);
        }
        panelChoix.revalidate();
        panelChoix.repaint();
    }
}
