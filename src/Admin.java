import javax.swing.JPanel;

// INHERITANCE: an Admin is a User.
public class Admin extends User {

    public Admin(String id, String name, String password) throws LMSException {
        super(id, name, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public JPanel createDashboard(LMSApp app) {
        return new AdminPanel(app, this);
    }
}
