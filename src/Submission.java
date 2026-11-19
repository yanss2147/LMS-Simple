import java.io.Serializable;

public class Submission implements Serializable {

    private Student student;
    private String answer;
    private int score;
    private String feedback = "";
    private boolean graded;

    public Submission(Student student, String answer) throws LMSException {
        if (answer.isBlank()) {
            throw new LMSException("Your answer cannot be empty.");
        }
        this.student = student;
        this.answer = answer.trim();
    }

    public void grade(int score, int maxScore, String feedback) throws LMSException {
        if (score < 0 || score > maxScore) {
            throw new LMSException("Score must be from 0 to " + maxScore + ".");
        }
        this.score = score;
        this.feedback = feedback.trim();
        this.graded = true;
    }

    public Student getStudent() {
        return student;
    }

    public String getAnswer() {
        return answer;
    }

    public int getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }

    public boolean isGraded() {
        return graded;
    }

    @Override
    public String toString() {
        return student.getName() + (graded ? " - " + score : " - not graded");
    }
}
