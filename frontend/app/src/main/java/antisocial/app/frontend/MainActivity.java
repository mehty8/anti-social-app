package antisocial.app.frontend;

import static antisocial.app.frontend.service.CheckJwtExpiration.jwtExpired;
import static antisocial.app.frontend.service.HandleResponseFailure.responseError;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.HashSet;

import antisocial.app.frontend.data.dto.FriendsNamesAndRequestsDTo;
import antisocial.app.frontend.page.MainPageActivity;
import antisocial.app.frontend.page.RegisterLoginActivity;
import antisocial.app.frontend.service.api.ApiClient;
import antisocial.app.frontend.service.api.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private final ActivityResultLauncher<Intent> manageFilesPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                    try {
                        validateJwt();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);
                    }
            });
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());
        getPermission();
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
            setContentView(R.layout.activity_main_android10_or_lower);
            TextView text = findViewById(R.id.android10OrLower);
            String message = "You are using android "  + Build.VERSION.RELEASE + ". \n" +
                    "Sorry this app only works on android 11 or above!";
            text.setText(message);
        }
    }

    private void validateJwt() throws JSONException {
        String jwt = sharedPreferencesManager.getJwt();
        if(jwt == null || jwtExpired(jwt, 0)){
            Intent intent = new Intent(MainActivity.this, RegisterLoginActivity.class);
            startActivity(intent);
        } else {
            ApiService apiService = ApiClient.getApiServiceDynamic();
            Call<FriendsNamesAndRequestsDTo> call = apiService.getFriendsNamesAndRequests("Bearer " + jwt);
            call.enqueue(new Callback<FriendsNamesAndRequestsDTo>() {
                @Override
                public void onResponse(Call<FriendsNamesAndRequestsDTo> call, Response<FriendsNamesAndRequestsDTo> response) {
                    if(response.isSuccessful()){
                        FriendsNamesAndRequestsDTo friendsAndRequests = response.body();
                        Intent intent = new Intent(MainActivity.this, MainPageActivity.class);
                        intent.putExtra("friends", new HashSet<>(friendsAndRequests.getFriendsNames()));
                        intent.putExtra("requests", new HashSet<>(friendsAndRequests.getRequestsNames()));
                        startActivity(intent);
                    } else {
                        responseError(response, MainActivity.this, "");
                    }

                }

                @Override
                public void onFailure(Call<FriendsNamesAndRequestsDTo> call, Throwable t) {
                    Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, RegisterLoginActivity.class);
                    startActivity(intent);
                }
            });

        }
    }
}