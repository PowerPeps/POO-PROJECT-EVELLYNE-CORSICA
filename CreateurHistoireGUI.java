import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class CreateurHistoireGUI extends JFrame {
    private JGraph graph;
    private List<Scene> scenes = new ArrayList<>();
    private Map<String, DefaultGraphCell> sceneNodes;
    private boolean isLinkToolActive = false; // Mode pour créer des transitions
    private DefaultGraphCell selectedSource = null; // Nœud source sélectionné pour le lien

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
        JButton btnLien = new JButton("Outil Lien"); // Outil pour créer des transitions
        JButton btnRefresh = new JButton("Rafraîchir"); // Bouton pour rafraîchir

        btnAjouterScene.addActionListener(e -> ajouterScene());
        btnLien.addActionListener(e -> toggleLinkTool());
        btnRefresh.addActionListener(e -> refreshGraph());

        panelOutils.add(btnAjouterScene);
        panelOutils.add(Box.createRigidArea(new Dimension(0, 10)));
        panelOutils.add(btnLien);
        panelOutils.add(Box.createRigidArea(new Dimension(0, 10)));
        panelOutils.add(btnRefresh);

        add(panelOutils, BorderLayout.EAST);
        add(new JScrollPane(graph), BorderLayout.CENTER);
    }

    private void initGraph() {
        graph = new JGraph();
        sceneNodes = new Hashtable<>();

        // Écouteur pour le menu contextuel
        graph.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    Object[] selectedCells = graph.getSelectionCells();
                    if (selectedCells.length > 0) {
                        showContextMenu(selectedCells, e.getPoint());
                    }
                } else if (isLinkToolActive && SwingUtilities.isLeftMouseButton(e)) {
                    Object cell = graph.getFirstCellForLocation(e.getX(), e.getY());
                    if (cell instanceof DefaultGraphCell) {
                        handleLinkTool((DefaultGraphCell) cell);
                    }
                }
            }
        });
    }

    private void refreshGraph() {
        // Supprime tous les éléments actuels
        graph.getGraphLayoutCache().remove(graph.getRoots(), true, true);

        // Ajoute les nouvelles scènes
        for (Scene scene : scenes) {
            DefaultGraphCell cell = new DefaultGraphCell(scene.getName());
            GraphConstants.setBounds(cell.getAttributes(), new Rectangle(100, 100 + (scenes.indexOf(scene) * 50), 120, 50));
            GraphConstants.setBorderColor(cell.getAttributes(), Color.BLACK);
            GraphConstants.setBackground(cell.getAttributes(), Color.LIGHT_GRAY);
            GraphConstants.setOpaque(cell.getAttributes(), true);

            sceneNodes.put(scene.getId(), cell);
            graph.getGraphLayoutCache().insert(cell);
        }

        // Rafraîchit l'affichage de toutes les cellules
        graph.getGraphLayoutCache().refresh(new org.jgraph.graph.CellView[0], true);
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
        selectedSource = null; // Réinitialise la sélection
    }

    private void handleLinkTool(DefaultGraphCell cell) {
        if (selectedSource == null) {
            // Première sélection : Définit la source
            selectedSource = cell;
        } else {
            // Deuxième sélection : Définit la cible et ajoute une transition
            DefaultGraphCell target = cell;
            String choiceText = JOptionPane.showInputDialog(this, "Texte du choix :", "Créer Transition");
            if (choiceText != null) {
                DefaultEdge edge = new DefaultEdge(choiceText);
                edge.setSource(selectedSource.getChildCount() > 0 ? selectedSource.getChildAt(0) : selectedSource);
                edge.setTarget(target.getChildCount() > 0 ? target.getChildAt(0) : target);
                graph.getGraphLayoutCache().insert(edge);
            }
            selectedSource = null; // Réinitialise après la création du lien
        }
    }

    private void showContextMenu(Object[] selectedCells, Point point) {
        JPopupMenu contextMenu = new JPopupMenu();

        if (selectedCells.length == 1) {
            Object cell = selectedCells[0];
            if (cell instanceof DefaultGraphCell || cell instanceof DefaultEdge) {
                JMenuItem modifyItem = new JMenuItem("Modifier");
                modifyItem.addActionListener(e -> modifierElement(cell));
                contextMenu.add(modifyItem);
            }
        }

        JMenuItem deleteItem = new JMenuItem("Supprimer");
        deleteItem.addActionListener(e -> supprimerElements(selectedCells));
        contextMenu.add(deleteItem);

        contextMenu.show(graph, point.x, point.y);
    }

    private void modifierElement(Object cell) {
        if (cell instanceof DefaultGraphCell) {
            String currentName = (String) ((DefaultGraphCell) cell).getUserObject();
            String newName = JOptionPane.showInputDialog(this, "Modifier le nom :", currentName);
            if (newName != null && !newName.trim().isEmpty()) {
                ((DefaultGraphCell) cell).setUserObject(newName);
                graph.getGraphLayoutCache().update();
            }
        } else if (cell instanceof DefaultEdge) {
            String currentText = (String) ((DefaultEdge) cell).getUserObject();
            String newText = JOptionPane.showInputDialog(this, "Modifier le texte du lien :", currentText);
            if (newText != null && !newText.trim().isEmpty()) {
                ((DefaultEdge) cell).setUserObject(newText);
                graph.getGraphLayoutCache().update();
            }
        }
    }

    private void supprimerElements(Object[] selectedCells) {
        for (Object cell : selectedCells) {
            if (cell instanceof DefaultGraphCell) {
                scenes.removeIf(scene -> scene.getName().equals(((DefaultGraphCell) cell).getUserObject()));
            }
        }
        graph.getGraphLayoutCache().remove(selectedCells, true, true);
    }

    private void nouveauProjet() {
        scenes.clear();
        refreshGraph();
    }

    private void ouvrirProjet() {
        JOptionPane.showMessageDialog(this, "Fonctionnalité en cours de développement !");
    }

    private void enregistrerProjet() {
        JOptionPane.showMessageDialog(this, "Fonctionnalité en cours de développement !");
    }

    private void exporterEnJson() {
        enregistrerProjet();
    }
}
