package antisocial.app.frontend.service;

import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

public class CheckJwtExpiration {

    public static boolean expired(String jwt, int additionalTime) throws JSONException {
        String jwtExpStringCoded = jwt.split("\\.")[1];
        String body = new String(Base64.decode(jwtExpStringCoded, Base64.URL_SAFE), StandardCharsets.UTF_8);
        JSONObject object = new JSONObject(body);
        long expirationTime = object.getLong("exp");
        long currentTime = System.currentTimeMillis() / 1000;

        return (expirationTime + additionalTime) <= currentTime;
    }
}
