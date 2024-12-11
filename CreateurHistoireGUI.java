import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.swing_viewer.*;
import org.graphstream.ui.swing.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateurHistoireGUI extends JFrame {
    private Graph graph;
    private Viewer viewer;
    private ViewPanel view;
    private java.util.List<Scene> scenes = new ArrayList<>();
    private Map<String, Node> sceneNodes;
    private boolean isLinkToolActive = false;
    private Node selectedSource = null;
    private JButton btnLien;

    public CreateurHistoireGUI() {
        setTitle("Créateur d'Histoire");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        initGraph();

        // Barre de menus
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem itemNouveau = new JMenuItem("Nouveau Projet");
        JMenuItem itemOuvrir = new JMenuItem("Ouvrir Projet");
        JMenuItem itemEnregistrer = new JMenuItem("Enregistrer Projet");
        JMenuItem itemExporter = new JMenuItem("Exporter en JSON");

        itemNouveau.addActionListener(e -> nouveauProjet());
        itemOuvrir.addActionListener(e -> ouvrirProjet());
        itemEnregistrer.addActionListener(e -> enregistrerProjet());
        itemExporter.addActionListener(e -> exporterEnJson());

        menuFichier.add(itemNouveau);
        menuFichier.add(itemOuvrir);
        menuFichier.addSeparator();
        menuFichier.add(itemEnregistrer);
        menuFichier.add(itemExporter);
        menuBar.add(menuFichier);

        setJMenuBar(menuBar);

        // Barre d'outils
        JPanel panelOutils = new JPanel();
        panelOutils.setLayout(new BoxLayout(panelOutils, BoxLayout.Y_AXIS));
        panelOutils.setBorder(BorderFactory.createTitledBorder("Outils"));

        JButton btnAjouterScene = new JButton("Ajouter Scène");
        btnLien = new JButton("Outil Lien");
        JButton btnRefresh = new JButton("Rafraîchir");

        btnAjouterScene.addActionListener(e -> ajouterScene());
        btnLien.addActionListener(e -> toggleLinkTool());
        btnRefresh.addActionListener(e -> refreshGraph());

        panelOutils.add(btnAjouterScene);
        panelOutils.add(Box.createRigidArea(new Dimension(0, 10)));
        panelOutils.add(btnLien);
        panelOutils.add(Box.createRigidArea(new Dimension(0, 10)));
        panelOutils.add(btnRefresh);

        add(panelOutils, BorderLayout.EAST);

        add(view, BorderLayout.CENTER);
    }

    private void initGraph() {
        // Initialisation du graphe
        graph = new SingleGraph("Histoire");
        graph.setAttribute("ui.stylesheet", "node { fill-color: white; stroke-color: black; stroke-width: 2px; text-size: 14; text-color: black; } edge { shape: line; stroke-color: black; stroke-width: 2px; text-color: black; }");

        // Configuration explicite pour utiliser Swing
        System.setProperty("org.graphstream.ui", "swing");

        try {
            // Affichage du graphe et récupération du Viewer
            viewer = graph.display(false); // Le paramètre "false" empêche l'ouverture automatique d'une nouvelle fenêtre
            view = (ViewPanel) viewer.addDefaultView(false); // Désactivation de l'ouverture automatique de la vue
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'initialisation du graphe", e);
        }

        // Initialisation de la carte des nœuds de la scène
        sceneNodes = new HashMap<>();
    }


    private String getSceneIdFromNode(Node node) {
        return node != null ? node.getId() : null;
    }

    private void refreshGraph() {
        graph.clear(); // Efface tous les nœuds et arêtes existants

        sceneNodes.clear();

        // Ajouter les scènes comme nœuds
        for (Scene scene : scenes) {
            Node node = graph.addNode(scene.getId());
            node.setAttribute("ui.label", scene.getName());
            node.setAttribute("xyz", 0, scenes.indexOf(scene) * 2, 0); // Positionnement simplifié
            sceneNodes.put(scene.getId(), node);
        }

        // Ajouter les choix comme arêtes
        for (Scene scene : scenes) {
            Node sourceNode = sceneNodes.get(scene.getId());
            if (sourceNode != null && scene.getChoices() != null) {
                for (Choice choice : scene.getChoices()) {
                    Node targetNode = sceneNodes.get(choice.getTargetSceneId());
                    if (targetNode != null) {
                        Edge edge = graph.addEdge(
                            sourceNode.getId() + "-" + targetNode.getId(),
                            sourceNode,
                            targetNode,
                            true
                        );
                        edge.setAttribute("ui.label", choice.getText());
                    }
                }
            }
        }

        // Ajuster la disposition automatique
        viewer.enableAutoLayout();
    }

    private void ajouterScene() {
        String sceneName = JOptionPane.showInputDialog(this, "Nom de la nouvelle scène :", "Nouvelle Scène");
        if (sceneName != null && !sceneName.trim().isEmpty()) {
            Scene newScene = new Scene();
            newScene.setId(String.valueOf(scenes.size() + 1));
            newScene.setName(sceneName);
            newScene.setDialogues(new ArrayList<>());
            newScene.setChoices(new ArrayList<>());
            scenes.add(newScene);
            refreshGraph();
        }
    }

    private void toggleLinkTool() {
        isLinkToolActive = !isLinkToolActive;
        selectedSource = null;

        btnLien.setBackground(isLinkToolActive ? Color.GREEN : null);
        btnLien.setForeground(isLinkToolActive ? Color.BLACK : null);
    }

    private void handleLinkTool(Node node) {
        if (selectedSource == null) {
            selectedSource = node;
            selectedSource.setAttribute("ui.style", "fill-color: yellow;");
        } else {
            Node target = node;
            String choiceText = JOptionPane.showInputDialog(this, "Texte du choix :", "Créer Transition");
            if (choiceText != null && !choiceText.trim().isEmpty()) {
                Edge edge = graph.addEdge(
                    selectedSource.getId() + "-" + target.getId(),
                    selectedSource,
                    target,
                    true
                );
                edge.setAttribute("ui.label", choiceText);

                // Restaurer le style original du nœud source
                selectedSource.setAttribute("ui.style", "fill-color: white;");

                // Ajouter le choix à la scène source
                Scene sourceScene = scenes.stream()
                    .filter(s -> s.getId().equals(selectedSource.getId()))
                    .findFirst()
                    .orElse(null);

                if (sourceScene != null) {
                    sourceScene.addChoice(new Choice(choiceText, target.getId(), null));
                }
            }
            selectedSource = null;
        }
    }

    private void nouveauProjet() {
        scenes.clear();
        graph.clear();
        sceneNodes.clear();
    }

    private void ouvrirProjet() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Ouvrir un projet JSON");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers JSON", "json"));

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            try {
                java.util.List<Scene> loadedScenes = HistoireManager.loadFromJson(filePath);
                scenes.clear();
                scenes.addAll(loadedScenes);
                refreshGraph();
                JOptionPane.showMessageDialog(this, "Projet chargé avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void enregistrerProjet() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer le projet");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers JSON", "json"));

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.endsWith(".json")) {
                filePath += ".json";
            }
            try {
                HistoireManager.saveToJson(scenes, filePath);
                JOptionPane.showMessageDialog(this, "Projet enregistré avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exporterEnJson() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exporter en JSON");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Fichiers JSON", "json"));
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            if (!filePath.endsWith(".json")) {
                filePath += ".json";
            }
            try {
                HistoireManager.saveToJson(scenes, filePath);
                JOptionPane.showMessageDialog(this, "Exporté avec succès : " + filePath, "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'exportation : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void editerDialogues(Node node) {
        String sceneId = getSceneIdFromNode(node);
        Scene scene = scenes.stream()
                            .filter(s -> s.getId().equals(sceneId))
                            .findFirst()
                            .orElse(null);

        if (scene != null) {
            JPanel dialoguePanel = new JPanel(new BorderLayout());
            DefaultListModel<Dialogue> dialogueListModel = new DefaultListModel<>();

            if (scene.getDialogues() != null) {
                for (Dialogue dialogue : scene.getDialogues()) {
                    dialogueListModel.addElement(dialogue);
                }
            }

            JList<Dialogue> dialogueList = new JList<>(dialogueListModel);
            dialogueList.setCellRenderer(new DialogueListCellRenderer());
            JScrollPane listScrollPane = new JScrollPane(dialogueList);
            dialoguePanel.add(listScrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            JButton addButton = new JButton("Ajouter");
            JButton editButton = new JButton("Modifier");
            JButton deleteButton = new JButton("Supprimer");

            addButton.addActionListener(e -> ajouterDialogue(dialogueListModel));
            editButton.addActionListener(e -> modifierDialogue(dialogueList, dialogueListModel));
            deleteButton.addActionListener(e -> supprimerDialogue(dialogueList, dialogueListModel));

            buttonPanel.add(addButton);
            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            dialoguePanel.add(buttonPanel, BorderLayout.SOUTH);

            int result = JOptionPane.showConfirmDialog(this, dialoguePanel, "Éditer les dialogues", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                java.util.List<Dialogue> newDialogues = new ArrayList<>();
                for (int i = 0; i < dialogueListModel.size(); i++) {
                    newDialogues.add(dialogueListModel.getElementAt(i));
                }
                scene.setDialogues(newDialogues);
            }
        } else {
            JOptionPane.showMessageDialog(this, "La scène spécifiée est introuvable.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void ajouterDialogue(DefaultListModel<Dialogue> model) {
        JTextField characterField = new JTextField(10);
        JTextArea textArea = new JTextArea(5, 20);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Personnage:"), BorderLayout.NORTH);
        panel.add(characterField, BorderLayout.CENTER);
        panel.add(new JLabel("Dialogue:"), BorderLayout.SOUTH);
        panel.add(new JScrollPane(textArea), BorderLayout.SOUTH);

        int result = JOptionPane.showConfirmDialog(this, panel, "Ajouter un dialogue", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String character = characterField.getText().trim();
            String text = textArea.getText().trim();
            if (!character.isEmpty() && !text.isEmpty()) {
                model.addElement(new Dialogue(character, text));
            }
        }
    }

    private void modifierDialogue(JList<Dialogue> list, DefaultListModel<Dialogue> model) {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex != -1) {
            Dialogue selectedDialogue = model.getElementAt(selectedIndex);
            JTextField characterField = new JTextField(selectedDialogue.getCharacter(), 10);
            JTextArea textArea = new JTextArea(selectedDialogue.getText(), 5, 20);
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JLabel("Personnage:"), BorderLayout.NORTH);
            panel.add(characterField, BorderLayout.CENTER);
            panel.add(new JLabel("Dialogue:"), BorderLayout.SOUTH);
            panel.add(new JScrollPane(textArea), BorderLayout.SOUTH);

            int result = JOptionPane.showConfirmDialog(this, panel, "Modifier le dialogue", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String character = characterField.getText().trim();
                String text = textArea.getText().trim();
                if (!character.isEmpty() && !text.isEmpty()) {
                    model.setElementAt(new Dialogue(character, text), selectedIndex);
                }
            }
        }
    }

    private void supprimerDialogue(JList<Dialogue> list, DefaultListModel<Dialogue> model) {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex != -1) {
            model.remove(selectedIndex);
        }
    }

    private void definirImageFond(Node node) {
        String sceneId = getSceneIdFromNode(node);
        Scene scene = scenes.stream()
                            .filter(s -> s.getId().equals(sceneId))
                            .findFirst()
                            .orElse(null);

        if (scene != null) {
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "jpeg");
            fileChooser.setFileFilter(filter);

            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                String imagePath = fileChooser.getSelectedFile().getPath();
                scene.setBackgroundImage(imagePath);
                JOptionPane.showMessageDialog(this, "Image de fond définie avec succès.");
            }
        }
    }


    private class DialogueListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Dialogue) {
                Dialogue dialogue = (Dialogue) value;
                setText(dialogue.getCharacter() + ": " + dialogue.getText());
            }
            return c;
        }
    }

    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");
        SwingUtilities.invokeLater(() -> {
            CreateurHistoireGUI gui = new CreateurHistoireGUI();
            gui.setVisible(true);
        });
    }

}