package antisocial.app.frontend.page;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import antisocial.app.frontend.MainActivity;
import antisocial.app.frontend.R;
import antisocial.app.frontend.SharedPreferencesManager;
import antisocial.app.frontend.data.PasswordCheck;
import antisocial.app.frontend.data.dto.JwtResponseDto;
import antisocial.app.frontend.data.dto.RegisterLoginRequestDto;
import antisocial.app.frontend.data.dto.ResponseMessageDto;
import antisocial.app.frontend.service.ApiClient;
import antisocial.app.frontend.service.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterLoginActivity extends AppCompatActivity {
    private SharedPreferencesManager sharedPreferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);

        sharedPreferencesManager = new SharedPreferencesManager(getApplicationContext());

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editTextUsername.getText().toString();
                if(!checkUsername(username)){
                    Toast.makeText(RegisterLoginActivity.this,
                            "Username can only have letters, numbers and underscore",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String password = editTextPassword.getText().toString();
                PasswordCheck passwordCheck = checkPassword(password);
                if(!passwordCheck.isValid()){
                    Toast.makeText(RegisterLoginActivity.this,
                            passwordCheck.getMessage(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                loginUser(username, password);
            }
        });

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                if(!checkUsername(username)){
                    Toast.makeText(RegisterLoginActivity.this,
                            "Username can only have letters, numbers and underscore",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                String password = editTextPassword.getText().toString();
                PasswordCheck passwordCheck = checkPassword(password);
                if(!passwordCheck.isValid()){
                    Toast.makeText(RegisterLoginActivity.this,
                            passwordCheck.getMessage(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                registerUser(username, password);
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
                    try {
                        ResponseMessageDto responseBody = new Gson().fromJson(response.errorBody().string(),
                                ResponseMessageDto.class);
                        String errorMes = responseBody.getMessage();
                        Toast.makeText(RegisterLoginActivity.this,
                                errorMes,
                                Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
                    String responseMes = response.body().getMessage();
                    Toast.makeText(RegisterLoginActivity.this, responseMes + ", " +
                            "now please login",
                            Toast.LENGTH_LONG).show();
                } else {
                    try {
                        ResponseMessageDto responseBody = new Gson().fromJson(response.errorBody().string(), ResponseMessageDto.class);
                        String errorMes = responseBody.getMessage();
                        Toast.makeText(RegisterLoginActivity.this,
                                errorMes + ", please try again accordingly",
                                Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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

    private boolean checkUsername(String username){
        return username.matches("\\w+");
    }

    private PasswordCheck checkPassword(String password){
        if(!password.matches("^(?=.*[a-z])(?=.*[A-Z]).{1,}$")){
            String message = "Password must contain at least 1 upper case and 1 lower case";
            PasswordCheck passwordCheck = new PasswordCheck(false, message);
            return passwordCheck;

        } else if(!password.matches("^(?=.*\\d).{1,}")){
            String message = "Password must contain at least one digit";
            PasswordCheck passwordCheck = new PasswordCheck(false, message);
            return passwordCheck;

        } else if(!password.matches("^.{8,}$")){
            String message = "Password must be at least 8 characters long";
            PasswordCheck passwordCheck = new PasswordCheck(false, message);
            return passwordCheck;
        }

        return new PasswordCheck(true, "ok");
    }
}
