import javax.swing.JPanel;

// INHERITANCE: a Student is a User.
public class Student extends User {

    public Student(String id, String name, String password) throws LMSException {
        super(id, name, password);
    }

    @Override
    public String getRole() {
        return "Student";
    }

    @Override
    public JPanel createDashboard(LMSApp app) {
        return new StudentPanel(app, this);
    }
}
