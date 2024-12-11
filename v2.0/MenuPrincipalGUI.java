import javax.swing.*;
import java.awt.*;

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

        add(panel);
    }

    private void openCreateurHistoire() {
        new CreateurHistoireGUI().setVisible(true);
    }

    private void openLecteurHistoire() {
        new LecteurHistoireGUI().setVisible(true);
    }
}
