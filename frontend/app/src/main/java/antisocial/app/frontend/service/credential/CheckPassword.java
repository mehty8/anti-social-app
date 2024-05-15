package antisocial.app.frontend.service.credential;

import android.content.Context;
import android.widget.Toast;

public class CheckPassword implements ICheckCredential{

    @Override
    public boolean isNeeded(String type) {
        return type.equals("Password");
    }

    @Override
    public boolean checkCredential(String password, Context context) {
        if(!password.matches("^(?=.*[a-z])(?=.*[A-Z]).{1,}$")){
            Toast.makeText(context, "Password must contain at least 1 upper case and 1 lower case",
            Toast.LENGTH_LONG).show();
            return false;

        } else if(!password.matches("^(?=.*\\d).{1,}")){
            Toast.makeText(context, "Password must contain at least one digit", Toast.LENGTH_LONG).show();
            return false;

        } else if(!password.matches("^.{8,}$")){
            Toast.makeText(context, "Password must be at least 8 characters long", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }
}
