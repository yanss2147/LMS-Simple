import javax.swing.*;
import java.awt.*;

// What a teacher sees: a form to create assignments (left), and
// a place to read and grade students' submissions (right).
public class TeacherPanel extends JPanel {

    private LMSApp app;

    // Create assignment form
    private JTextField titleField = new JTextField();
    private JTextField courseField = new JTextField();
    private JTextArea instructionsArea = new JTextArea(4, 15);
    private JTextField maxScoreField = new JTextField("100");

    // Grading
    private JComboBox<Assignment> assignmentBox = new JComboBox<>();
    private JList<Submission> submissionList = new JList<>();
    private JTextArea detailsArea = new JTextArea(4, 20);
    private JTextArea answerArea = new JTextArea();
    private JTextField scoreField = new JTextField(5);
    private JLabel maxScoreLabel = new JLabel("/ -");
    private JTextArea feedbackArea = new JTextArea(2, 20);

    public TeacherPanel(LMSApp app, Teacher teacher) {
        this.app = app;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(app.createHeader(teacher), BorderLayout.NORTH);
        add(createAssignmentForm(), BorderLayout.WEST);
        add(createGradingPanel(), BorderLayout.CENTER);

        refreshAssignments();
    }

    // ---------- Left side: create an assignment ----------

    private JPanel createAssignmentForm() {
        instructionsArea.setLineWrap(true);
        JButton createButton = new JButton("Create Assignment");
        createButton.addActionListener(e -> createAssignment());

        JPanel form = new JPanel(new GridLayout(0, 1, 3, 3));
        form.add(new JLabel("Title:"));
        form.add(titleField);
        form.add(new JLabel("Course:"));
        form.add(courseField);
        form.add(new JLabel("Max score:"));
        form.add(maxScoreField);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("New Assignment"));
        panel.setPreferredSize(new Dimension(260, 0));
        panel.add(form, BorderLayout.NORTH);
        JPanel instructions = new JPanel(new BorderLayout());
        instructions.add(new JLabel("Instructions:"), BorderLayout.NORTH);
        instructions.add(new JScrollPane(instructionsArea), BorderLayout.CENTER);
        panel.add(instructions, BorderLayout.CENTER);
        panel.add(createButton, BorderLayout.SOUTH);
        return panel;
    }

    private void createAssignment() {
        try {
            int maxScore;
            try {
                maxScore = Integer.parseInt(maxScoreField.getText().trim());
            } catch (NumberFormatException e) {
                throw new LMSException("Max score must be a whole number.");
            }
            Assignment assignment = new Assignment(titleField.getText(), courseField.getText(),
                    instructionsArea.getText(), maxScore);
            FileManager.getInstance().addAssignment(assignment);
            app.saveData();

            titleField.setText("");
            courseField.setText("");
            instructionsArea.setText("");
            refreshAssignments();
            assignmentBox.setSelectedItem(assignment);
            app.showMessage("Assignment created!");
        } catch (LMSException e) {
            app.showError(e.getMessage());
        }
    }

    // ---------- Right side: grade submissions ----------

    private JPanel createGradingPanel() {
        assignmentBox.addActionListener(e -> showSubmissions());
        submissionList.addListSelectionListener(e -> showAnswer());
        answerArea.setEditable(false);
        answerArea.setLineWrap(true);
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        JButton gradeButton = new JButton("Save Grade");
        gradeButton.addActionListener(e -> grade());

        JPanel chooseRow = new JPanel(new BorderLayout(5, 5));
        chooseRow.add(new JLabel("Assignment:"), BorderLayout.WEST);
        chooseRow.add(assignmentBox, BorderLayout.CENTER);

        JScrollPane detailsScroll = new JScrollPane(detailsArea);
        detailsScroll.setBorder(BorderFactory.createTitledBorder("Assignment Details"));

        JPanel top = new JPanel(new BorderLayout(5, 5));
        top.add(chooseRow, BorderLayout.NORTH);
        top.add(detailsScroll, BorderLayout.CENTER);

        JPanel lists = new JPanel(new GridLayout(1, 2, 5, 5));
        JScrollPane submissionsScroll = new JScrollPane(submissionList);
        submissionsScroll.setBorder(BorderFactory.createTitledBorder("Submissions"));
        JScrollPane answerScroll = new JScrollPane(answerArea);
        answerScroll.setBorder(BorderFactory.createTitledBorder("Student's answer"));
        lists.add(submissionsScroll);
        lists.add(answerScroll);

        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackScroll.setBorder(BorderFactory.createTitledBorder("Feedback (optional)"));

        JPanel scoreRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        scoreRow.add(new JLabel("Score:"));
        scoreRow.add(scoreField);
        scoreRow.add(maxScoreLabel);
        scoreRow.add(gradeButton);

        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(feedbackScroll, BorderLayout.CENTER);
        bottom.add(scoreRow, BorderLayout.EAST);

        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Grade Submissions"));
        panel.add(top, BorderLayout.NORTH);
        panel.add(lists, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshAssignments() {
        assignmentBox.removeAllItems();
        for (Assignment a : FileManager.getInstance().getAssignments()) {
            assignmentBox.addItem(a);
        }
    }

    private void showSubmissions() {
        Assignment assignment = (Assignment) assignmentBox.getSelectedItem();
        if (assignment == null) {
            submissionList.setListData(new Submission[0]);
            detailsArea.setText("No assignments yet. Create one on the left.");
            maxScoreLabel.setText("/ -");
        } else {
            submissionList.setListData(assignment.getSubmissions().toArray(new Submission[0]));
            showDetails(assignment);
            maxScoreLabel.setText("/ " + assignment.getMaxScore());
        }
        showAnswer();
    }

    private void showDetails(Assignment assignment) {
        int graded = 0;
        for (Submission s : assignment.getSubmissions()) {
            if (s.isGraded()) {
                graded++;
            }
        }
        detailsArea.setText("Title: " + assignment.getTitle()
                + "     Course: " + assignment.getCourse()
                + "     Max score: " + assignment.getMaxScore() + "\n"
                + "Submissions: " + assignment.getSubmissions().size()
                + " (" + graded + " graded)\n\n"
                + "Instructions: " + assignment.getInstructions());
        detailsArea.setCaretPosition(0);
    }

    private void showAnswer() {
        Submission submission = submissionList.getSelectedValue();
        if (submission == null) {
            answerArea.setText("");
            scoreField.setText("");
            feedbackArea.setText("");
        } else {
            answerArea.setText(submission.getAnswer());
            scoreField.setText(submission.isGraded() ? String.valueOf(submission.getScore()) : "");
            feedbackArea.setText(submission.getFeedback());
        }
    }

    private void grade() {
        Assignment assignment = (Assignment) assignmentBox.getSelectedItem();
        Submission submission = submissionList.getSelectedValue();
        try {
            if (submission == null) {
                throw new LMSException("Select a submission first.");
            }
            int score;
            try {
                score = Integer.parseInt(scoreField.getText().trim());
            } catch (NumberFormatException e) {
                throw new LMSException("Score must be a whole number.");
            }
            submission.grade(score, assignment.getMaxScore(), feedbackArea.getText());
            app.saveData();
            submissionList.repaint();   // so the list shows the new score
            showDetails(assignment);    // updates the "graded" count
            app.showMessage("Grade saved!");
        } catch (LMSException e) {
            app.showError(e.getMessage());
        }
    }
}
