import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private LMSApp app;
    private JTextField idField = new JTextField(15);
    private JPasswordField passwordField = new JPasswordField(15);

    public LoginPanel(LMSApp app) {
        this.app = app;

        JLabel title = new JLabel("Simple LMS", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));

        JButton loginButton = new JButton("Log In");
        loginButton.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());   // pressing Enter also logs in

        JPanel form = new JPanel(new GridLayout(5, 1, 5, 5));
        form.add(title);
        form.add(labeled("User ID:", idField));
        form.add(labeled("Password:", passwordField));
        form.add(loginButton);
        form.add(new JLabel("Sample: admin / teacher / student, password 1234", SwingConstants.CENTER));

        setLayout(new GridBagLayout());   // keeps the form in the middle
        add(form);
    }

    private void login() {
        String id = idField.getText().trim();
        String password = new String(passwordField.getPassword());
        try {
            User user = FileManager.getInstance().login(id, password);
            app.showDashboard(user);
        } catch (LMSException e) {
            app.showError(e.getMessage());
        }
    }

    private JPanel labeled(String label, JComponent field) {
        JPanel row = new JPanel(new BorderLayout(5, 5));
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(80, 20));
        row.add(jLabel, BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }
}
