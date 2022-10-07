package com.gemastik.dentistsmile.data.network;

import com.gemastik.dentistsmile.data.model.article.ResponseArticle;
import com.gemastik.dentistsmile.data.model.maps.ResponseMaps;
import com.gemastik.dentistsmile.data.model.maps.ResponseMapsById;
import com.gemastik.dentistsmile.data.model.token.ResponseToken;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiEndpoint {
    String CUSTOM_HEADER = "@";
    String NO_AUTH = "NoAuth";

    @Headers(CUSTOM_HEADER + ": " + NO_AUTH)
    @POST("token_authentication/refresh_token")
    Call<ResponseToken> refreshToken(
            @Header("token") String token
    );

    @POST("api/v1/article")
    Call<ResponseArticle> getArticle(
            @Body RequestBody body
    );

    @GET("api/v1/maps")
    Call<ResponseMaps> getMaps(
            @Query("json_body") String body
    );

    @GET("api/v1/maps")
    Call<ResponseMapsById> getMapsById(
            @Query("json_body") String body
    );
}
