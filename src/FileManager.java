import java.io.*;
import java.util.ArrayList;

// Keeps all users and assignments, and saves/loads them to a file.
//
// DESIGN PATTERN - SINGLETON: there is only ONE FileManager in the whole
// program. Every screen gets it with FileManager.getInstance(), so they all
// share the same data.
public class FileManager {

    private static FileManager instance;
    private static final String FILE_NAME = "lms_data.dat";

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Assignment> assignments = new ArrayList<>();

    // private: nobody else can write "new FileManager()"
    private FileManager() {
    }

    public static FileManager getInstance() {
        if (instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    // ---------- File handling ----------

    public void save() throws LMSException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(users);
            out.writeObject(assignments);
        } catch (IOException e) {
            throw new LMSException("Could not save the data.");
        }
    }

    @SuppressWarnings("unchecked")
    public void load() throws LMSException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            addSampleData();   // first time running the program
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            users = (ArrayList<User>) in.readObject();
            assignments = (ArrayList<Assignment>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new LMSException("Could not load the data. Delete " + FILE_NAME + " to start over.");
        }
    }

    // ---------- Users ----------

    public User login(String id, String password) throws LMSException {
        for (User user : users) {
            if (user.getId().equals(id) && user.checkPassword(password)) {
                return user;
            }
        }
        throw new LMSException("Wrong ID or password.");
    }

    public void addUser(User newUser) throws LMSException {
        for (User user : users) {
            if (user.getId().equals(newUser.getId())) {
                throw new LMSException("ID " + newUser.getId() + " is already used.");
            }
        }
        users.add(newUser);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    // ---------- Assignments ----------

    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    // ---------- Sample data ----------

    private void addSampleData() throws LMSException {
        users.add(new Admin("admin", "System Admin", "1234"));
        users.add(new Teacher("teacher", "Maria Santos", "1234"));
        users.add(new Student("student", "Juan Dela Cruz", "1234"));
        assignments.add(new Assignment("Class Diagram", "CSIT227",
                "Draw the UML class diagram of your project.", 100));
    }
}
