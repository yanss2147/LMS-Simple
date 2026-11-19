import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// The main window. It shows one screen at a time:
// the login screen, or the dashboard of whoever logged in.
public class LMSApp extends JFrame {

    public LMSApp() {
        setTitle("Simple LMS");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Save everything when the window is closed
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveData();
            }
        });

        showLogin();
    }

    public void showLogin() {
        showScreen(new LoginPanel(this));
    }

    // POLYMORPHISM: we don't check if the user is a Student, Teacher or Admin.
    // Each one creates its own dashboard.
    public void showDashboard(User user) {
        showScreen(user.createDashboard(this));
    }

    public void logout() {
        saveData();
        showLogin();
    }

    private void showScreen(JPanel screen) {
        setContentPane(screen);
        revalidate();
        repaint();
    }

    public void saveData() {
        try {
            FileManager.getInstance().save();
        } catch (LMSException e) {
            showError(e.getMessage());
        }
    }

    // ---------- Helpers used by all screens ----------

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    // The bar at the top of every dashboard: "Welcome, name" and a Log Out button
    public JPanel createHeader(User user) {
        JLabel welcome = new JLabel("Welcome, " + user.getName() + " (" + user.getRole() + ")");
        welcome.setFont(welcome.getFont().deriveFont(Font.BOLD, 16f));

        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(e -> logout());

        JPanel header = new JPanel(new BorderLayout());
        header.add(welcome, BorderLayout.WEST);
        header.add(logoutButton, BorderLayout.EAST);
        return header;
    }

    public static void main(String[] args) {
        LMSApp app = new LMSApp();
        try {
            FileManager.getInstance().load();
        } catch (LMSException e) {
            app.showError(e.getMessage());
        }
        app.setVisible(true);
    }
}
