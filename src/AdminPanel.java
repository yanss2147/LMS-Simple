import javax.swing.*;
import java.awt.*;

// What an admin sees: a form to add users (left) and the list of all users (right).
public class AdminPanel extends JPanel {

    private LMSApp app;
    private Admin admin;

    private JComboBox<String> roleBox = new JComboBox<>(new String[]{"Student", "Teacher", "Admin"});
    private JTextField idField = new JTextField();
    private JTextField nameField = new JTextField();
    private JTextField passwordField = new JTextField();
    private JList<User> userList = new JList<>();

    public AdminPanel(LMSApp app, Admin admin) {
        this.app = app;
        this.admin = admin;

        JButton addButton = new JButton("Add User");
        addButton.addActionListener(e -> addUser());

        JPanel form = new JPanel(new GridLayout(0, 1, 3, 3));
        form.add(new JLabel("Role:"));
        form.add(roleBox);
        form.add(new JLabel("User ID:"));
        form.add(idField);
        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Password:"));
        form.add(passwordField);
        form.add(addButton);

        JPanel formHolder = new JPanel(new BorderLayout());   // keeps the form at the top
        formHolder.setBorder(BorderFactory.createTitledBorder("New User"));
        formHolder.setPreferredSize(new Dimension(240, 0));
        formHolder.add(form, BorderLayout.NORTH);

        JButton removeButton = new JButton("Remove Selected User");
        removeButton.addActionListener(e -> removeUser());

        JPanel listPanel = new JPanel(new BorderLayout(5, 5));
        JScrollPane scroll = new JScrollPane(userList);
        scroll.setBorder(BorderFactory.createTitledBorder("All Users"));
        listPanel.add(scroll, BorderLayout.CENTER);
        listPanel.add(removeButton, BorderLayout.SOUTH);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(app.createHeader(admin), BorderLayout.NORTH);
        add(formHolder, BorderLayout.WEST);
        add(listPanel, BorderLayout.CENTER);

        refreshUsers();
    }

    private void addUser() {
        String role = (String) roleBox.getSelectedItem();
        String id = idField.getText();
        String name = nameField.getText();
        String password = passwordField.getText();

        try {
            User user;
            if (role.equals("Student")) {
                user = new Student(id, name, password);
            } else if (role.equals("Teacher")) {
                user = new Teacher(id, name, password);
            } else {
                user = new Admin(id, name, password);
            }
            FileManager.getInstance().addUser(user);
            app.saveData();

            idField.setText("");
            nameField.setText("");
            passwordField.setText("");
            refreshUsers();
            app.showMessage(name + " was added.");
        } catch (LMSException e) {
            app.showError(e.getMessage());
        }
    }

    private void removeUser() {
        User selected = userList.getSelectedValue();
        if (selected == null) {
            app.showError("Select a user first.");
        } else if (selected == admin) {
            app.showError("You cannot remove yourself.");
        } else {
            FileManager.getInstance().removeUser(selected);
            app.saveData();
            refreshUsers();
        }
    }

    private void refreshUsers() {
        userList.setListData(FileManager.getInstance().getUsers().toArray(new User[0]));
    }
}
