package antisocial.app.frontend.service;

import android.content.Context;
import android.content.Intent;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.SharedPreferencesManager;

public class CheckJwtExpiration {

    public static boolean jwtExpired(String jwt, int additionalTime) throws JSONException {
        String jwtExpStringCoded = jwt.split("\\.")[1];
        String body = new String(Base64.decode(jwtExpStringCoded, Base64.URL_SAFE), StandardCharsets.UTF_8);
        JSONObject object = new JSONObject(body);
        long expirationTime = object.getLong("exp");
        long currentTime = System.currentTimeMillis() / 1000;

        return (expirationTime + additionalTime) <= currentTime;
    }

    public static void logoutJwtExpired(Context context){
        Toast.makeText(context, "Session expired, login again", Toast.LENGTH_LONG).show();
        new SharedPreferencesManager(context).deleteJwt();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
