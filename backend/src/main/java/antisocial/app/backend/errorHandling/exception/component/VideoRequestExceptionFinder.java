package antisocial.app.backend.errorHandling.exception.component;

import antisocial.app.backend.errorHandling.exception.IExceptionFinder;
import antisocial.app.backend.errorHandling.exception.VideoRequestException;
import org.springframework.stereotype.Component;

@Component
public class VideoRequestExceptionFinder implements IExceptionFinder {

    @Override
    public boolean isNeeded(Exception exception) {
        return exception instanceof VideoRequestException;
    }
}
