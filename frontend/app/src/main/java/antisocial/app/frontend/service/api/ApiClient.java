package antisocial.app.frontend.service.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private final static String BASE_URL = "http://anti-social-app.eu-central-1.elasticbeanstalk.com/";

    public static ApiService getApiServiceDynamic(){
        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class);
    }
}
