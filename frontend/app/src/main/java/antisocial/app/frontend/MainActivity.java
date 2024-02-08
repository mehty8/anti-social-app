package antisocial.app.frontend;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Base64;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;

import antisocial.app.frontend.data.dto.FriendsNamesAndRequestsDTo;
import antisocial.app.frontend.page.MainPageActivity;
import antisocial.app.frontend.page.RegisterLoginActivity;
import antisocial.app.frontend.service.ApiClient;
import antisocial.app.frontend.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> manageFilesPermissionLauncher;
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        registerManageFilesPermissionLauncher();
        getPermission();
    }

    private void registerManageFilesPermissionLauncher() {
        manageFilesPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            try {
                                validateJwt();
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            Toast.makeText(this, "Permission required to proceed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    private void getPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            if(!Environment.isExternalStorageManager()){
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                manageFilesPermissionLauncher.launch(intent);
            } else {
                try {
                    validateJwt();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            try {
                validateJwt();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void validateJwt() throws JSONException {
        String jwt = sharedPreferencesManager.getJwt();
        if(jwt == null || expired(jwt)){
            Intent intent = new Intent(MainActivity.this, RegisterLoginActivity.class);
            startActivity(intent);
        } else {
            ApiService apiService = ApiClient.getApiServiceDynamic();
            Call<FriendsNamesAndRequestsDTo> call = apiService.getFriendsNamesAndRequests("Bearer " + jwt);
            call.enqueue(new Callback<FriendsNamesAndRequestsDTo>() {
                @Override
                public void onResponse(Call<FriendsNamesAndRequestsDTo> call, Response<FriendsNamesAndRequestsDTo> response) {
                    FriendsNamesAndRequestsDTo friendsAndRequests = response.body();
                    Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
                    intent.putExtra("friends", new HashSet<>(friendsAndRequests.getFriendsNames()));
                    intent.putExtra("requests", new HashSet<>(friendsAndRequests.getRequestsNames()));
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<FriendsNamesAndRequestsDTo> call, Throwable t) {
                    Intent intent = new Intent(MainActivity.this, RegisterLoginActivity.class);
                    startActivity(intent);
                }
            });

        }
    }

    private boolean expired(String jwt) throws JSONException {
        String jwtExpStringCoded = jwt.split("\\.")[1];
        String body = new String(Base64.decode(jwtExpStringCoded, Base64.URL_SAFE), StandardCharsets.UTF_8);
        JSONObject object = new JSONObject(body);
        long exp = object.getLong("exp");
        long currentTime = System.currentTimeMillis() / 1000;

        return exp <= currentTime;
    }
}