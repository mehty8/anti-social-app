package antisocial.app.backend.errorHandling.exception.component;

import antisocial.app.backend.errorHandling.exception.IExceptionFinder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

@Component
public class BadCredentialExceptionFinder implements IExceptionFinder {

    @Override
    public boolean isNeeded(Exception exception) {
        return exception instanceof BadCredentialsException;
    }

}
