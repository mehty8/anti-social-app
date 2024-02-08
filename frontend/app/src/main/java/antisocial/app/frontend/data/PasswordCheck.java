package antisocial.app.frontend.data;

public class PasswordCheck {
    private boolean isValid;
    private String message;

    public PasswordCheck(boolean isValid, String message) {
        this.isValid = isValid;
        this.message = message;
    }

    public boolean isValid() {
        return isValid;
    }

    public String getMessage() {
        return message;
    }
}
