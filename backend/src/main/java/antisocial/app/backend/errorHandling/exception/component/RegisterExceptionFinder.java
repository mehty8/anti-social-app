package antisocial.app.backend.errorHandling.exception.component;

import antisocial.app.backend.errorHandling.exception.IExceptionFinder;
import antisocial.app.backend.errorHandling.exception.RegisterException;
import org.springframework.stereotype.Component;

@Component
public class RegisterExceptionFinder implements IExceptionFinder {
    @Override
    public boolean isNeeded(Exception exception) {
        return exception instanceof RegisterException;
    }
}
