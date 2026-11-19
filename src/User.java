import javax.swing.JPanel;
import java.io.Serializable;

// ABSTRACTION: User is abstract. You can't create a plain "User",
// only a Student, Teacher or Admin.
public abstract class User implements Serializable {

    // ENCAPSULATION: fields are private. Other classes use the methods below.
    private String id;
    private String name;
    private String password;

    public User(String id, String name, String password) throws LMSException {
        if (id.isBlank() || name.isBlank() || password.isBlank()) {
            throw new LMSException("ID, name and password are all required.");
        }
        this.id = id.trim();
        this.name = name.trim();
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // The password is never given out. We only check if it matches.
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    // POLYMORPHISM: every kind of user answers these differently.
    public abstract String getRole();

    public abstract JPanel createDashboard(LMSApp app);

    @Override
    public String toString() {
        return id + " - " + name + " (" + getRole() + ")";
    }
}
