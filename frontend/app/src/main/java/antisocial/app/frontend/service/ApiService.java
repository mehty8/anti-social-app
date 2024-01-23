package antisocial.app.frontend.service;

import antisocial.app.frontend.dto.FriendsNamesAndRequestsDTo;
import antisocial.app.frontend.dto.JwtResponseDto;
import antisocial.app.frontend.dto.LoginRequestDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("friend")
    Call<FriendsNamesAndRequestsDTo> getFriendsNamesAndRequests(@Header("Authorization") String jwt);

    @POST("user/{endpoint}")
    Call<JwtResponseDto> loginUser(@Path("endpoint") String endpoint, @Body LoginRequestDto loginRequest);
}
