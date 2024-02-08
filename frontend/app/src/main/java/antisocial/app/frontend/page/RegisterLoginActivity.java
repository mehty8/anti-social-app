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
import antisocial.app.frontend.dto.JwtResponseDto;
import antisocial.app.frontend.dto.RegisterLoginRequestDto;
import antisocial.app.frontend.dto.ResponseMessageDto;
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
                String password = editTextPassword.getText().toString();
                loginUser(username, password);
            }
        });

        Button buttonRegister = findViewById(R.id.buttonRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
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
                }
            }

            @Override
            public void onFailure(Call<JwtResponseDto> call, Throwable t) {
                Toast.makeText(RegisterLoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
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
                    Toast.makeText(RegisterLoginActivity.this, responseMes + ", now please login", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        ResponseMessageDto responseMes = new Gson().fromJson(response.errorBody().string(), ResponseMessageDto.class);
                        String errorMes = responseMes.getMessage();
                        Toast.makeText(RegisterLoginActivity.this, errorMes + ", please try again accordingly", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseMessageDto> call, Throwable t) {
                Toast.makeText(RegisterLoginActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
