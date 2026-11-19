import javax.swing.JPanel;

// INHERITANCE: a Teacher is a User.
public class Teacher extends User {

    public Teacher(String id, String name, String password) throws LMSException {
        super(id, name, password);
    }

    @Override
    public String getRole() {
        return "Teacher";
    }

    @Override
    public JPanel createDashboard(LMSApp app) {
        return new TeacherPanel(app, this);
    }
}
