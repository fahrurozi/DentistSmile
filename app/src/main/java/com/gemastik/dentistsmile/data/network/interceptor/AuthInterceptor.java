package com.gemastik.dentistsmile.data.network.interceptor;

import androidx.annotation.NonNull;

import com.gemastik.dentistsmile.data.model.token.ResponseToken;
import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.root.App;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class AuthInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request req = chain.request();

        if (req.headers().values(ApiEndpoint.CUSTOM_HEADER).contains(ApiEndpoint.NO_AUTH)) {
            return proceedWithToken(chain, req, null);
        }

        String token = App.sharedPref.getString("token", null);
        Response res = this.proceedWithToken(chain, req, token);
        if (res.code() != 401) {
            return res;
        } else {
            Call<ResponseToken> loginCall = App.apiService.refreshToken(token);
            retrofit2.Response<ResponseToken> responseToken = loginCall.execute();
            if (responseToken.body().getToken() != null) {
                App.sharedPref.edit().putString("token", responseToken.body().getToken()).apply();
                try {
                    res.close();
                    proceedWithToken(chain, req, responseToken.body().getToken());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                res.close();
                try {
                    proceedWithToken(chain, req, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }

    private Response proceedWithToken(Chain chain, Request req, String token) throws IOException {
        Request.Builder builder = req.newBuilder();
        if (token != null) {
            builder.addHeader("token", token);
        }

        Request request = builder.removeHeader("@").build();
        return chain.proceed(request);
    }

}
