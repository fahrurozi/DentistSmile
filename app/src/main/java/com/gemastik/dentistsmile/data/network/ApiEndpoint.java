package com.gemastik.dentistsmile.data.network;

import com.gemastik.dentistsmile.data.model.article.ResponseArticle;
import com.gemastik.dentistsmile.data.model.children.add.ResponseAddChild;
import com.gemastik.dentistsmile.data.model.children.get.ResponseGetChildren;
import com.gemastik.dentistsmile.data.model.kecamatan.ResponseGetKecamatanAll;
import com.gemastik.dentistsmile.data.model.kelurahan.ResponseGetKelurahanByIdKec;
import com.gemastik.dentistsmile.data.model.login.ResponseLogin;
import com.gemastik.dentistsmile.data.model.maps.ResponseMaps;
import com.gemastik.dentistsmile.data.model.maps.ResponseMapsById;
import com.gemastik.dentistsmile.data.model.profil.ResponseGetProfile;
import com.gemastik.dentistsmile.data.model.profil.ResponseStoreProfil;
import com.gemastik.dentistsmile.data.model.register.ResponseRegister;
import com.gemastik.dentistsmile.data.model.review.ResponseAddReview;
import com.gemastik.dentistsmile.data.model.review.ResponseReview;
import com.gemastik.dentistsmile.data.model.token.ResponseToken;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
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

    @GET("api/v1/review")
    Call<ResponseReview> getReview(
            @Query("json_body") String body
    );

    @POST("api/v1/review")
    Call<ResponseAddReview> addReview(
            @Body RequestBody body
    );


//    API FOR LARAVEL //
    @Multipart
    @POST("api/register")
    Call<ResponseRegister> register(
            @Part("email") RequestBody email,
            @Part("password") RequestBody password
    );

    @FormUrlEncoded
    @POST("api/login")
    Call<ResponseLogin> login(
            @Field("email") String email,
            @Field("password") String password
    );


    @GET("api/orangtua")
    Call<ResponseGetProfile> getProfile();

    @GET("api/kecamatan")
    Call<ResponseGetKecamatanAll> getKecamatanAll();

    @GET("api/kelurahan/{id}")
    Call<ResponseGetKelurahanByIdKec> getKelurahanByIdKec(
            @Path("id") Integer id
    );

    @FormUrlEncoded
    @POST("api/orangtua")
    Call<ResponseStoreProfil> storeProfile(
            @Field("nama") String nama,
            @Field("id_kecamatan") String id_kecamatan,
            @Field("id_kelurahan") String id_kelurahan,
            @Field("tempat_lahir") String tempat_lahir,
            @Field("tanggal_lahir") String tanggal_lahir,
            @Field("alamat") String alamat,
            @Field("pendidikan") String pendidikan
    );

    @GET("api/anak")
    Call<ResponseGetChildren> getChildren();

    @FormUrlEncoded
    @POST("api/anak")
    Call<ResponseAddChild> storeChildren(
            @Field("nama") String nama,
            @Field("tempat_lahir") String tempat_lahir,
            @Field("tanggal_lahir") String tanggal_lahir,
            @Field("jenis_kelamin") String jenis_kelamin
    );
}
