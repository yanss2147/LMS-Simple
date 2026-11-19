import javax.swing.*;
import java.awt.*;

// What a student sees: the list of assignments, the details of the
// selected one, and a box to type and submit an answer.
public class StudentPanel extends JPanel {

    private LMSApp app;
    private Student student;

    private JList<Assignment> assignmentList = new JList<>();
    private JTextArea detailsArea = new JTextArea();
    private JTextArea answerArea = new JTextArea();
    private JButton submitButton = new JButton("Submit Answer");

    public StudentPanel(LMSApp app, Student student) {
        this.app = app;
        this.student = student;

        assignmentList.setListData(FileManager.getInstance().getAssignments().toArray(new Assignment[0]));
        assignmentList.addListSelectionListener(e -> showSelected());

        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);
        answerArea.setLineWrap(true);
        submitButton.addActionListener(e -> submit());

        JPanel left = new JPanel(new BorderLayout());
        left.add(new JLabel("Assignments:"), BorderLayout.NORTH);
        left.add(new JScrollPane(assignmentList), BorderLayout.CENTER);
        left.setPreferredSize(new Dimension(280, 0));

        JPanel right = new JPanel(new GridLayout(2, 1, 5, 5));
        right.add(withLabel("Details:", new JScrollPane(detailsArea)));
        JPanel answerPanel = withLabel("Your answer:", new JScrollPane(answerArea));
        answerPanel.add(submitButton, BorderLayout.SOUTH);
        right.add(answerPanel);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(app.createHeader(student), BorderLayout.NORTH);
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);

        showSelected();
    }

    private void showSelected() {
        Assignment assignment = assignmentList.getSelectedValue();
        if (assignment == null) {
            detailsArea.setText("Select an assignment on the left.");
            answerArea.setText("");
            answerArea.setEditable(false);
            submitButton.setEnabled(false);
            return;
        }

        String details = "Title: " + assignment.getTitle() + "\n"
                + "Course: " + assignment.getCourse() + "\n"
                + "Max score: " + assignment.getMaxScore() + "\n\n"
                + assignment.getInstructions() + "\n\n";

        Submission submission = assignment.getSubmissionOf(student);
        if (submission == null) {
            details += "Status: NOT SUBMITTED";
            answerArea.setText("");
            answerArea.setEditable(true);
            submitButton.setEnabled(true);
        } else {
            if (submission.isGraded()) {
                details += "Status: GRADED - " + submission.getScore() + " / " + assignment.getMaxScore();
                if (!submission.getFeedback().isEmpty()) {
                    details += "\nTeacher's feedback: " + submission.getFeedback();
                }
            } else {
                details += "Status: SUBMITTED - waiting for grade";
            }
            answerArea.setText(submission.getAnswer());
            answerArea.setEditable(false);
            submitButton.setEnabled(false);
        }
        detailsArea.setText(details);
    }

    private void submit() {
        Assignment assignment = assignmentList.getSelectedValue();
        try {
            Submission submission = new Submission(student, answerArea.getText());
            assignment.addSubmission(submission);
            app.saveData();
            app.showMessage("Answer submitted!");
            showSelected();
        } catch (LMSException e) {
            app.showError(e.getMessage());
        }
    }

    private JPanel withLabel(String label, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
}
