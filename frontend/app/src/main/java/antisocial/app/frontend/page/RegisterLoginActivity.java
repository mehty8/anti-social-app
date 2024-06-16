package antisocial.app.frontend.page;

import static antisocial.app.frontend.service.HandleResponseFailure.responseError;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.service.credential.CheckPassword;
import antisocial.app.frontend.service.credential.CheckUsername;
import antisocial.app.frontend.data.dto.JwtResponseDto;
import antisocial.app.frontend.data.dto.RegisterLoginRequestDto;
import antisocial.app.frontend.data.dto.ResponseMessageDto;
import antisocial.app.frontend.service.api.ApiClient;
import antisocial.app.frontend.service.api.ApiService;
import antisocial.app.frontend.service.credential.ICheckCredential;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterLoginActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;
    private List<ICheckCredential> checkCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

        CheckUsername checkUsername = new CheckUsername();
        CheckPassword checkPassword = new CheckPassword();
        checkCredentials = new ArrayList<>();
        checkCredentials.add(checkUsername);
        checkCredentials.add(checkPassword);

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if(checkingCredentials(username, password)){
                    loginUser(username, password);
                }
            }
        });

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                if(checkingCredentials(username, password)){
                    registerUser(username, password);
                }
            }
        });
    }
    private void loginUser(String username, String password){
        RegisterLoginRequestDto registerLoginRequestDto = new RegisterLoginRequestDto(username, password);
        ApiService apiService = ApiClient.getApiServiceDynamic();
        Call<JwtResponseDto> call = apiService.loginUser(registerLoginRequestDto);
        call.enqueue(new Callback<JwtResponseDto>() {
            @Override
            public void onResponse(Call<JwtResponseDto> call, Response<JwtResponseDto> response) {
                if(response.isSuccessful()){
                    JwtResponseDto jwtResponse = response.body();
                    sharedPreferencesManager.saveJwt(jwtResponse.getJwt());
                    Intent intent = new Intent(RegisterLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    responseError(response, RegisterLoginActivity.this, "");
                }
            }
            @Override
            public void onFailure(Call<JwtResponseDto> call, Throwable t) {
                Toast.makeText(RegisterLoginActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void registerUser(String username, String password){
        RegisterLoginRequestDto registerLoginRequestDto = new RegisterLoginRequestDto(username, password);
        ApiService apiService = ApiClient.getApiServiceDynamic();
        Call<ResponseMessageDto> call = apiService.registerUser(registerLoginRequestDto);
        call.enqueue(new Callback<ResponseMessageDto>() {
            @Override
            public void onResponse(Call<ResponseMessageDto> call, Response<ResponseMessageDto> response) {
                if(response.isSuccessful()) {
                    String responseMessage = response.body().getMessage();
                    Toast.makeText(RegisterLoginActivity.this, responseMessage + ", " +
                            "now please login",
                            Toast.LENGTH_LONG).show();
                } else {
                   responseError(response, RegisterLoginActivity.this,
                           ", please try again accordingly");
                }
            }
            @Override
            public void onFailure(Call<ResponseMessageDto> call, Throwable t) {
                Toast.makeText(RegisterLoginActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkingCredentials(String username, String password){
        ICheckCredential checkUsername = checkCredentials.stream().filter(credential ->
                credential.isNeeded("Username")).collect(Collectors.toList()).get(0);
        boolean isUsernameValid = checkUsername.checkCredential(username,
                RegisterLoginActivity.this);

        if(!isUsernameValid){
            return false;
        }

        ICheckCredential checkPassword = checkCredentials.stream().filter(credential ->
                credential.isNeeded("Password")).collect(Collectors.toList()).get(0);
        boolean isPasswordValid = checkPassword.checkCredential(password,
                RegisterLoginActivity.this);

        if(!isPasswordValid){
            return false;
        }

        return true;
    }
}
