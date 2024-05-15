package antisocial.app.frontend.service.credential;

import android.content.Context;
import android.widget.Toast;

public class CheckUsername implements ICheckCredential{

    @Override
    public boolean isNeeded(String type) {
        return type.equals("Username");
    }

    @Override
    public boolean checkCredential(String username, Context context) {
        if(!username.matches("\\w+")){
            Toast.makeText(context, "Username can only have letters, numbers and underscore",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
