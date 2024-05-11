package antisocial.app.frontend.service;

public class CredentialCheck {
    private boolean isValid;
    private String message;

    public CredentialCheck(boolean isValid, String message) {
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
