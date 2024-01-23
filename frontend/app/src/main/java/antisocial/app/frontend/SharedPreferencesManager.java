package antisocial.app.frontend;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String PREFERENCE_NAME = "Anti-Social-App";
    private static final String JWT_NAME = "jwt";
    private Context context;

    public SharedPreferencesManager(Context context) {
        this.context = context;
    }

    public void saveJwt(String jwt){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(JWT_NAME, jwt);
        editor.apply();
    }

    public String getJwt(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(JWT_NAME, null);
    }
}
