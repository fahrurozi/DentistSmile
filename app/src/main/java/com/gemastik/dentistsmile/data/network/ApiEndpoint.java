package com.gemastik.dentistsmile.data.network;

import com.gemastik.dentistsmile.data.model.article.ResponseArticle;
import com.gemastik.dentistsmile.data.model.checkup_dentist.ResponseCheckupDentist;
import com.gemastik.dentistsmile.data.model.checkup_physic.ResponseCheckupPhysic;
import com.gemastik.dentistsmile.data.model.children.add.ResponseAddChild;
import com.gemastik.dentistsmile.data.model.children.get.ResponseGetChildren;
import com.gemastik.dentistsmile.data.model.history.ear.ResponseHistoryEar;
import com.gemastik.dentistsmile.data.model.history.eye.ResponseHistoryEye;
import com.gemastik.dentistsmile.data.model.history.physic.ResponseHistoryPhysic;
import com.gemastik.dentistsmile.data.model.kecamatan.ResponseGetKecamatanAll;
import com.gemastik.dentistsmile.data.model.kelas.ResponseGetKelasByIdSek;
import com.gemastik.dentistsmile.data.model.kelurahan.ResponseGetKelurahanByIdKec;
import com.gemastik.dentistsmile.data.model.login.ResponseLogin;
import com.gemastik.dentistsmile.data.model.maps.ResponseMaps;
import com.gemastik.dentistsmile.data.model.maps.ResponseMapsById;
import com.gemastik.dentistsmile.data.model.profil.ResponseEditProfile;
import com.gemastik.dentistsmile.data.model.profil.ResponseGetProfile;
import com.gemastik.dentistsmile.data.model.profil.ResponseStoreProfil;
import com.gemastik.dentistsmile.data.model.register.ResponseRegister;
import com.gemastik.dentistsmile.data.model.reminder.ResponseReminder;
import com.gemastik.dentistsmile.data.model.review.ResponseAddReview;
import com.gemastik.dentistsmile.data.model.review.ResponseReview;
import com.gemastik.dentistsmile.data.model.sekolah.ResponseGetSekolahByIdKel;
import com.gemastik.dentistsmile.data.model.token.ResponseToken;

import java.io.File;
import java.util.Map;

import okhttp3.MultipartBody;
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
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
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

    @GET("api/reminder")
    Call<ResponseReminder> getReminder();

    @GET("5773e356-b27e-4b02-83f1-d89e687145ad")
    Call<ResponseReminder> getTestReminder();

    @GET("api/kecamatan")
    Call<ResponseGetKecamatanAll> getKecamatanAll();

    @GET("api/kelurahan/{id}")
    Call<ResponseGetKelurahanByIdKec> getKelurahanByIdKec(
            @Path("id") Integer id
    );

    @GET("api/sekolah/{id}")
    Call<ResponseGetSekolahByIdKel> getSekolahByIdKel(
            @Path("id") Integer id
    );

    @GET("api/list-kelas/{id}")
    Call<ResponseGetKelasByIdSek> getKelasByIdSek(
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

    @FormUrlEncoded
    @POST("api/updateprofil")
    Call<ResponseEditProfile> updateProfile(
            @Field("nama") String nama,
            @Field("tempat_lahir") String tempat_lahir,
            @Field("tanggal_lahir") String tanggal_lahir,
            @Field("alamat") String alamat,
            @Field("pendidikan") String pendidikan
    );

    @FormUrlEncoded
    @POST("api/pemeriksaanfisik")
    Call<ResponseCheckupPhysic> addPhysicalCheckup(
            @Field("id_anak") String id_anak,
            @Field("id_sekolah") String id_sekolah,
            @Field("id_kelas") String id_kelas,
            @Field("tinggi_badan") String tinggi_badan,
            @Field("berat_badan") String berat_badan,
            @Field("sistole") String sistole,
            @Field("diastole") String diastole,
            @Field("msoal1") String msoal1,
            @Field("msoal2") String msoal2,
            @Field("msoal3") String msoal3,
            @Field("msoal4") String msoal4,
            @Field("msoal5") String msoal5,
            @Field("msoal6") String msoal6,
            @Field("msoal7") String msoal7,
            @Field("tsoal1") String tsoal1,
            @Field("tsoal2") String tsoal2,
            @Field("tsoal3") String tsoal3,
            @Field("tsoal4") String tsoal4,
            @Field("tsoal5") String tsoal5,
            @Field("tsoal6") String tsoal6,
            @Field("tsoal7") String tsoal7,
            @Field("tsoal8") String tsoal8,
            @Field("tsoal9") String tsoal9
    );

    @Multipart
    @POST("api/pemeriksaangigi")
    Call<ResponseCheckupDentist> addDentistCheckup(
//            @PartMap Map<String,String> queryMap,
            @Part("id_anak") RequestBody id_anak,
            @Part("id_sekolah") RequestBody id_sekolah,
            @Part("id_kelas") RequestBody id_kelas,
            @Part MultipartBody.Part  gambar1,
            @Part MultipartBody.Part  gambar2,
            @Part MultipartBody.Part  gambar3,
            @Part MultipartBody.Part  gambar4,
            @Part MultipartBody.Part  gambar5,
            @Part("gsoal1") RequestBody gsoal1,
            @Part("gsoal2") RequestBody gsoal2
    );

    @GET("api/riwayat-fisik/{id}")
    Call<ResponseHistoryPhysic> getHistoryPhysic(
            @Path("id") String childId
    );

    @GET("api/riwayat-mata/{id}")
    Call<ResponseHistoryEye> getHistoryEye(
            @Path("id") String childId
    );

    @GET("api/riwayat-telinga/{id}")
    Call<ResponseHistoryEar> getHistoryEar(
            @Path("id") String childId
    );


}
