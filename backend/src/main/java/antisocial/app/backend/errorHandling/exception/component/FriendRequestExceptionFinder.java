package antisocial.app.backend.errorHandling.exception.component;

import antisocial.app.backend.errorHandling.exception.FriendRequestException;
import antisocial.app.backend.errorHandling.exception.IExceptionFinder;
import org.springframework.stereotype.Component;

@Component
public class FriendRequestExceptionFinder implements IExceptionFinder {

    @Override
    public boolean isNeeded(Exception exception) {
        return exception instanceof FriendRequestException;
    }

}
