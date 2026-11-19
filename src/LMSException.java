// Our own exception. We THROW it when something is wrong (empty field,
// wrong password, score too high...) and CATCH it in the screens to show
// the message in a pop-up.
public class LMSException extends Exception {

    public LMSException(String message) {
        super(message);
    }
}
