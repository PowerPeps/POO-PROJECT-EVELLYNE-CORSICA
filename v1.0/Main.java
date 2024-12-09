import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphLayoutCache;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createMainMenu);
    }

    private static void createMainMenu() {
        JFrame frame = new JFrame("Menu Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new GridLayout(3, 1, 10, 10));
        frame.setLocationRelativeTo(null);

        JLabel titleLabel = new JLabel("Bienvenue dans le Créateur et Lecteur d'Histoire", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        frame.add(titleLabel);

        JButton createurButton = new JButton("Créateur d'Histoire");
        createurButton.setFont(new Font("Arial", Font.BOLD, 16));
        createurButton.addActionListener(e -> {
            frame.dispose();
            SwingUtilities.invokeLater(CreateurHistoireGUI::new);
        });

        JButton lecteurButton = new JButton("Lecteur d'Histoire");
        lecteurButton.setFont(new Font("Arial", Font.BOLD, 16));
        lecteurButton.addActionListener(e -> {
            frame.dispose();
            SwingUtilities.invokeLater(LecteurHistoireGUI::new);
        });

        frame.add(createurButton);
        frame.add(lecteurButton);
        frame.setVisible(true);
    }
}

class CreateurHistoireGUI {
    private JFrame frame;
    private JGraph graph;
    private DefaultGraphCell rootCell;
    private CreateurHistoire createurHistoire;

    public CreateurHistoireGUI() {
        frame = new JFrame("Créateur d'Histoire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        createurHistoire = new CreateurHistoire();

        JMenuBar menuBar = new JMenuBar();

        // Menu Fichier
        JMenu menuFichier = new JMenu("Fichier");
        JMenuItem nouveauProjet = new JMenuItem("Nouveau Projet");
        nouveauProjet.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        nouveauProjet.addActionListener(e -> nouveauProjet());
        menuFichier.add(nouveauProjet);

        JMenuItem ouvrir = new JMenuItem("Ouvrir");
        ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        ouvrir.addActionListener(this::loadHistoire);
        menuFichier.add(ouvrir);

        JMenuItem enregistrer = new JMenuItem("Enregistrer");
        enregistrer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        enregistrer.addActionListener(this::saveHistoire);
        menuFichier.add(enregistrer);

        JMenuItem exporter = new JMenuItem("Exporter");
        exporter.addActionListener(e -> exporterHistoire());
        menuFichier.add(exporter);

        JMenuItem cloud = new JMenuItem("Cloud");
        cloud.addActionListener(e -> cloudOptions());
        menuFichier.add(cloud);

        JMenuItem fermer = new JMenuItem("Fermer");
        fermer.addActionListener(e -> frame.dispose());
        menuFichier.add(fermer);

        menuBar.add(menuFichier);

        // Menu Éditer
        JMenu menuEditer = new JMenu("Éditer");
        JMenuItem annuler = new JMenuItem("Annuler");
        annuler.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.CTRL_MASK));
        menuEditer.add(annuler);

        JMenuItem retablir = new JMenuItem("Rétablir");
        retablir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, ActionEvent.CTRL_MASK));
        menuEditer.add(retablir);

        JMenuItem copier = new JMenuItem("Copier");
        copier.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuEditer.add(copier);

        JMenuItem coller = new JMenuItem("Coller");
        coller.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuEditer.add(coller);

        JMenuItem supprimer = new JMenuItem("Supprimer");
        supprimer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        supprimer.addActionListener(e -> deleteSelectedNode());
        menuEditer.add(supprimer);

        JMenuItem fusionnerNoeuds = new JMenuItem("Fusionner Nœuds");
        fusionnerNoeuds.addActionListener(e -> fusionnerNoeuds());
        menuEditer.add(fusionnerNoeuds);

        JMenuItem modifierProprietes = new JMenuItem("Modifier les propriétés");
        modifierProprietes.addActionListener(e -> modifySelectedNode());
        menuEditer.add(modifierProprietes);

        menuBar.add(menuEditer);

        // Menu Vue
        JMenu menuVue = new JMenu("Vue");
        JMenuItem zoomAvant = new JMenuItem("Zoom Avant");
        zoomAvant.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, ActionEvent.CTRL_MASK));
        menuVue.add(zoomAvant);

        JMenuItem zoomArriere = new JMenuItem("Zoom Arrière");
        zoomArriere.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, ActionEvent.CTRL_MASK));
        menuVue.add(zoomArriere);

        JMenuItem recentrer = new JMenuItem("Recentrer");
        recentrer.addActionListener(e -> recentrerVue());
        menuVue.add(recentrer);

        JMenuItem modePresentation = new JMenuItem("Mode Présentation");
        modePresentation.addActionListener(e -> modePresentation());
        menuVue.add(modePresentation);

        menuBar.add(menuVue);

        // Menu Insérer
        JMenu menuInserer = new JMenu("Insérer");
        JMenuItem ajouterScene = new JMenuItem("Ajouter une Scène");
        ajouterScene.addActionListener(e -> addNewScene());
        menuInserer.add(ajouterScene);

        JMenuItem ajouterPersonnage = new JMenuItem("Ajouter un Personnage");
        ajouterPersonnage.addActionListener(e -> ajouterPersonnage());
        menuInserer.add(ajouterPersonnage);

        JMenuItem ajouterDialogue = new JMenuItem("Ajouter un Dialogue");
        ajouterDialogue.addActionListener(e -> ajouterDialogue());
        menuInserer.add(ajouterDialogue);

        JMenuItem ajouterCondition = new JMenuItem("Ajouter une Condition");
        ajouterCondition.addActionListener(e -> ajouterCondition());
        menuInserer.add(ajouterCondition);

        JMenuItem ajouterFichier = new JMenuItem("Ajouter un Fichier");
        ajouterFichier.addActionListener(e -> ajouterFichier());
        menuInserer.add(ajouterFichier);

        menuBar.add(menuInserer);

        // Menu Aide
        JMenu menuAide = new JMenu("Aide");
        JMenuItem tutoriels = new JMenuItem("Tutoriels Interactifs");
        tutoriels.addActionListener(e -> lancerTutoriels());
        menuAide.add(tutoriels);

        JMenuItem documentation = new JMenuItem("Documentation");
        documentation.addActionListener(e -> ouvrirDocumentation());
        menuAide.add(documentation);

        JMenuItem raccourcis = new JMenuItem("Raccourcis Clavier");
        raccourcis.addActionListener(e -> afficherRaccourcis());
        menuAide.add(raccourcis);

        menuBar.add(menuAide);

        frame.setJMenuBar(menuBar);

        GraphModel model = new org.jgraph.graph.DefaultGraphModel();
        GraphLayoutCache view = new GraphLayoutCache(model, new org.jgraph.graph.DefaultCellViewFactory());
        graph = new JGraph(model, view);

        rootCell = new DefaultGraphCell("Histoire");
        graph.getGraphLayoutCache().insert(rootCell);

        graph.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(graph), BorderLayout.CENTER);

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setVisible(true);
    }

    private void nouveauProjet() {
        graph.getGraphLayoutCache().remove(graph.getRoots());
        rootCell = new DefaultGraphCell("Histoire");
        graph.getGraphLayoutCache().insert(rootCell);
    }

    private void saveHistoire(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(graphToString());
                JOptionPane.showMessageDialog(frame, "Histoire enregistrée avec succès.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'enregistrement de l'histoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadHistoire(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                graph.getGraphLayoutCache().remove(graph.getRoots());
                String line;
                while ((line = reader.readLine()) != null) {
                    addNodeFromString(line);
                }
                JOptionPane.showMessageDialog(frame, "Histoire chargée avec succès.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors du chargement de l'histoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exporterHistoire() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showSaveDialog(frame);
        if (option == fileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(file)) {
                String json = "{\n\"histoire\": \"" + graphToString().replace("\n", "\\n") + "\"\n}";
                writer.write(json);
                JOptionPane.showMessageDialog(frame, "Histoire exportée avec succès.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors de l'exportation de l'histoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cloudOptions() {
        JOptionPane.showMessageDialog(frame, "Fonctionnalité cloud en cours de développement.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void recentrerVue() {
        graph.setScale(1.0);
    }

    private void modePresentation() {
        JOptionPane.showMessageDialog(frame, "Mode présentation en cours de développement.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void ajouterPersonnage() {
        String nom = JOptionPane.showInputDialog(frame, "Entrez le nom du personnage :");
        String prenom = JOptionPane.showInputDialog(frame, "Entrez le prénom du personnage :");
        int age = Integer.parseInt(JOptionPane.showInputDialog(frame, "Entrez l'âge du personnage :"));
        String role = JOptionPane.showInputDialog(frame, "Entrez le rôle du personnage dans l'histoire :");

        Personnage personnage = createurHistoire.creerPersonnage(nom, prenom, age, role);
        DefaultGraphCell personnageNode = new DefaultGraphCell(personnage.getNomComplet());
        graph.getGraphLayoutCache().insert(personnageNode);
    }

    private void ajouterDialogue() {
        String content = JOptionPane.showInputDialog(frame, "Entrez le contenu du dialogue :");
        Dialogue dialogue = createurHistoire.creerDialogue(content);
        DefaultGraphCell dialogueNode = new DefaultGraphCell("Dialogue: " + content);
        graph.getGraphLayoutCache().insert(dialogueNode);
    }

    private void ajouterCondition() {
        String description = JOptionPane.showInputDialog(frame, "Entrez la description de la condition :");
        Condition condition = createurHistoire.creerCondition(description);
        DefaultGraphCell conditionNode = new DefaultGraphCell("Condition: " + condition.getDescription());
        graph.getGraphLayoutCache().insert(conditionNode);
    }

    private void ajouterFichier() {
        JOptionPane.showMessageDialog(frame, "Fonctionnalité d'ajout de fichier en cours de développement.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void lancerTutoriels() {
        JOptionPane.showMessageDialog(frame, "Tutoriels interactifs en cours de développement.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void ouvrirDocumentation() {
        JOptionPane.showMessageDialog(frame, "Documentation en cours de développement.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void afficherRaccourcis() {
        JOptionPane.showMessageDialog(frame, "Liste des raccourcis clavier en cours de développement.", "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addNewScene() {
        String description = JOptionPane.showInputDialog(frame, "Entrez la description de la nouvelle scène :");
        String image = JOptionPane.showInputDialog(frame, "Entrez le nom de l'image associée :");
        Scene newScene = createurHistoire.creerScene(description, image);
        DefaultGraphCell newSceneNode = new DefaultGraphCell(description);
        graph.getGraphLayoutCache().insert(newSceneNode);
    }

    private void deleteSelectedNode() {
        Object[] selectedCells = graph.getSelectionCells();
        if (selectedCells != null && selectedCells.length > 0) {
            graph.getGraphLayoutCache().remove(selectedCells);
        } else {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un noeud à supprimer.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void modifySelectedNode() {
        Object[] selectedCells = graph.getSelectionCells();
        if (selectedCells != null && selectedCells.length == 1) {
            DefaultGraphCell selectedCell = (DefaultGraphCell) selectedCells[0];
            String newName = JOptionPane.showInputDialog(frame, "Entrez le nouveau nom :", selectedCell.getUserObject().toString());
            if (newName != null && !newName.trim().isEmpty()) {
                selectedCell.setUserObject(newName);
                graph.getGraphLayoutCache().reload();
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner un seul noeud à modifier.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void fusionnerNoeuds() {
        Object[] selectedCells = graph.getSelectionCells();
        if (selectedCells != null && selectedCells.length == 2) {
            DefaultGraphCell cell1 = (DefaultGraphCell) selectedCells[0];
            DefaultGraphCell cell2 = (DefaultGraphCell) selectedCells[1];
            DefaultEdge edge = new DefaultEdge();
            edge.setSource(cell1.getChildAt(0));
            edge.setTarget(cell2.getChildAt(0));
            graph.getGraphLayoutCache().insert(edge);
        } else {
            JOptionPane.showMessageDialog(frame, "Veuillez sélectionner exactement deux noeuds à fusionner.", "Erreur", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String graphToString() {
        StringBuilder builder = new StringBuilder();
        Object[] cells = graph.getRoots();
        for (Object cell : cells) {
            if (cell instanceof DefaultGraphCell) {
                builder.append(((DefaultGraphCell) cell).getUserObject().toString()).append("\n");
            }
        }
        return builder.toString();
    }

    private void addNodeFromString(String line) {
        DefaultGraphCell newNode = new DefaultGraphCell(line.trim());
        graph.getGraphLayoutCache().insert(newNode);
    }
}

class LecteurHistoireGUI {
    private JFrame frame;
    private JGraph graph;
    private DefaultGraphCell rootCell;

    public LecteurHistoireGUI() {
        frame = new JFrame("Lecteur d'Histoire");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        GraphModel model = new org.jgraph.graph.DefaultGraphModel();
        GraphLayoutCache view = new GraphLayoutCache(model, new org.jgraph.graph.DefaultCellViewFactory());
        graph = new JGraph(model, view);

        rootCell = new DefaultGraphCell("Histoire");
        graph.getGraphLayoutCache().insert(rootCell);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(graph), BorderLayout.CENTER);

        frame.getContentPane().add(panel);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuFichier = new JMenu("Fichier");

        JMenuItem ouvrir = new JMenuItem("Ouvrir");
        ouvrir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        ouvrir.addActionListener(this::loadHistoire);
        menuFichier.add(ouvrir);

        JMenuItem fermer = new JMenuItem("Fermer");
        fermer.addActionListener(e -> frame.dispose());
        menuFichier.add(fermer);

        menuBar.add(menuFichier);

        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }

    private void loadHistoire(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(frame);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                graph.getGraphLayoutCache().remove(graph.getRoots());
                String line;
                while ((line = reader.readLine()) != null) {
                    addNodeFromString(line);
                }
                JOptionPane.showMessageDialog(frame, "Histoire chargée avec succès.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Erreur lors du chargement de l'histoire.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addNodeFromString(String line) {
        DefaultGraphCell newNode = new DefaultGraphCell(line.trim());
        graph.getGraphLayoutCache().insert(newNode);
    }
}
