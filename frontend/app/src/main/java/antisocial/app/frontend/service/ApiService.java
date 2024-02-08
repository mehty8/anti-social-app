package antisocial.app.frontend.service;

import antisocial.app.frontend.dto.FriendsNamesAndRequestsDTo;
import antisocial.app.frontend.dto.FriendsNamesDto;
import antisocial.app.frontend.dto.PreassignedUrlToUploadVideoDto;
import antisocial.app.frontend.dto.JwtResponseDto;
import antisocial.app.frontend.dto.RegisterLoginRequestDto;
import antisocial.app.frontend.dto.PreassignedUrlDetailsDto;
import antisocial.app.frontend.dto.ResponseMessageDto;
import antisocial.app.frontend.dto.VideosDto;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface ApiService {

    @GET("friend")
    Call<FriendsNamesAndRequestsDTo> getFriendsNamesAndRequests(@Header("Authorization") String jwt);

    @POST("user/register")
    Call<ResponseMessageDto> registerUser(@Body RegisterLoginRequestDto registerLoginRequestDto);

    @POST("user/login")
    Call<JwtResponseDto> loginUser(@Body RegisterLoginRequestDto loginRequest);

    @PATCH("friend/{requesttype}/{endpoint}")
    Call<ResponseMessageDto> sendOrAcceptFriendRequest(@Path("requesttype") String requesttype,
                                                       @Path("endpoint") String endpoint,
                                                       @Header("Authorization") String jwt);
    @GET("friend/finduser/{endpoint}")
    Call<FriendsNamesDto> getFriendsNames(@Path("endpoint") String endpoint, @Header("Authorization") String jwt);

    @GET("video/{endpoint}")
    Call<VideosDto> getVideos(@Path("endpoint")String endpoint, @Header("Authorization") String jwt);

    @POST("video/aws/preassignedurl/put")
    Call<PreassignedUrlToUploadVideoDto> getPreassignedUrlToUploadVideo(@Header("Authorization") String jwt,
                                                                        @Body PreassignedUrlDetailsDto preassignedUrlDto);

    @PUT
    Call<Void> uploadVideoToS3Bucket(@Url String url, @Body RequestBody video);

    @POST("video/aws/preassignedurl/get/{endpoint}")
    Call<ResponseMessageDto> getPreassignedUrlToWatch(@Path("endpoint")String endpoint,
                                                      @Header("Authorization") String jwt,
                                                      @Body PreassignedUrlDetailsDto preassignedUrlDto);
}
