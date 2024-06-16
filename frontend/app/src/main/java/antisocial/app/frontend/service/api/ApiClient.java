package antisocial.app.frontend.service.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import antisocial.app.frontend.BuildConfig;

public class ApiClient {

    private final static String BASE_URL = BuildConfig.BASE_URL;

    public static ApiService getApiServiceDynamic(){
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }
}
