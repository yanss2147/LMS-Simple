import java.io.Serializable;
import java.util.ArrayList;

public class Assignment implements Serializable {

    private String title;
    private String course;
    private String instructions;
    private int maxScore;
    private ArrayList<Submission> submissions = new ArrayList<>();

    public Assignment(String title, String course, String instructions, int maxScore) throws LMSException {
        if (title.isBlank() || course.isBlank()) {
            throw new LMSException("Title and course are required.");
        }
        if (maxScore <= 0) {
            throw new LMSException("Max score must be more than 0.");
        }
        this.title = title.trim();
        this.course = course.trim();
        this.instructions = instructions.trim();
        this.maxScore = maxScore;
    }

    public void addSubmission(Submission submission) throws LMSException {
        if (getSubmissionOf(submission.getStudent()) != null) {
            throw new LMSException("You already submitted this assignment.");
        }
        submissions.add(submission);
    }

    // Returns this student's submission, or null if they haven't submitted.
    public Submission getSubmissionOf(Student student) {
        for (Submission s : submissions) {
            if (s.getStudent().getId().equals(student.getId())) {
                return s;
            }
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public String getCourse() {
        return course;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public ArrayList<Submission> getSubmissions() {
        return submissions;
    }

    @Override
    public String toString() {
        return course + " - " + title;
    }
}
