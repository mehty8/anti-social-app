package antisocial.app.frontend.service.credential;

import android.content.Context;

public interface ICheckCredential {

    boolean isNeeded(String type);

    boolean checkCredential(String credential, Context context);
}
