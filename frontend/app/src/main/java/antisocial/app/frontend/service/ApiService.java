package antisocial.app.frontend.service;

import antisocial.app.frontend.dto.FriendsNamesAndRequestsDTo;
import antisocial.app.frontend.dto.FriendsNamesDto;
import antisocial.app.frontend.dto.JwtResponseDto;
import antisocial.app.frontend.dto.LoginRequestDto;
import antisocial.app.frontend.dto.ResponseMessageDto;
import antisocial.app.frontend.dto.VideosDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @GET("friend")
    Call<FriendsNamesAndRequestsDTo> getFriendsNamesAndRequests(@Header("Authorization") String jwt);

    @POST("user/{endpoint}")
    Call<JwtResponseDto> loginUser(@Path("endpoint") String endpoint, @Body LoginRequestDto loginRequest);

    @PATCH("friend/{requesttype}/{endpoint}")
    Call<ResponseMessageDto> sendOrAcceptFriendRequest(@Path("requesttype") String requesttype,
                                                       @Path("endpoint") String endpoint,
                                                       @Header("Authorization") String jwt);
    @GET("friend/finduser/{endpoint}")
    Call<FriendsNamesDto> getFriendsNames(@Path("endpoint") String endpoint, @Header("Authorization") String jwt);

    @GET("video/{endpoint}")
    Call<VideosDto> getVideos(@Path("endpoint")String endpoint, @Header("Authorization") String jwt);
}
