package com.gemastik.dentistsmile.data.network.interceptor;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import com.gemastik.dentistsmile.data.network.ApiEndpoint;
import com.gemastik.dentistsmile.root.App;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptorDentist implements Interceptor {
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request req = chain.request();

        if (req.headers().values(ApiEndpoint.CUSTOM_HEADER).contains(ApiEndpoint.NO_AUTH)) {
            return proceedWithToken(chain, req, null);
        }

//        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiI5NzgxYWVjZC1kYmNhLTRiYWQtODE3Yy01ZDVlZDM1YjRjNWMiLCJqdGkiOiIyNmM1Zjc2ZGM5YmZjZTViODM2N2Y1MDU5OGVjMWVmMzRmZWJlMzE4NGFhNTQ2MzgwMWI3MjU5NjFiZTNmOWM3NjZmYWQzNTE3OWRjY2U3YiIsImlhdCI6MTY2NTgzOTczMi41Njg5MSwibmJmIjoxNjY1ODM5NzMyLjU2ODkxNSwiZXhwIjoxNjk3Mzc1NzMyLjU0MjA5NCwic3ViIjoiOSIsInNjb3BlcyI6W119.hB3EGRCtGGJdi8VL8NL6E7sasBE-ZexWw2gLwqTCDlr_mgSPCViiFJnDuchRgxfsiWth3AC1gjcvzrwnf5G6QcM6sBCik59GgbLVl6_1oyPTmUZkISivK-mekO8iPNCYfft3y4t87rQKnrVCRD1fbfdKHi38gFGqen1vA8-hUzZjqMEWqs4pk07RDYhn1RxSaYkYLUeZ3CkIpdMaI4ZXW7DAC3Y3yNj9TV4yCAdT0A8Ryab7_SED276RL1xa54Pb3UKJuuS-QvOBs0l75FhU91nj_DyMCuEYAcDALwHrHdWNsARdbdqVXAX4SkQtMtg9G0XA28ECmyYiV8atQyo9ozpKRjU7NPGdZZS0VCeTwc8HHiPrrohD0Q7_WRVfG_geoY_myW2SBgRq7F_FtdhVHnBWyauYy3RnmoGuVgsrC2OI4JkaT6RdyQx-aOZlHLm4lKDTklt-ULp9zLDiKsogNPcn2UU6a8cNb6GcMExEjOfrj3YekWQVOInvAjOYYIQ4xXDOwrpQXShk4aat6Kx7S5qxC3v42Nldfo1UAobLBp6-YqGE7SDqTL8igZM9Db0dI94FXdfcz8t_VH6kPMuvWfPUo7dTrYu9AIqjBJGii_wa9VEu7hEIypLb3wDHQZt6VcElk9Wgg5mUq4ZjEWfUtjSRTbZbWN330WKbwDNSDBk";
        String token = App.sharedPref.getString("token", "");
        Response res = this.proceedWithToken(chain, req, token);
        if (res.code() != 401) {
            return res;
        } else {
//            Call<ResponseToken> loginCall = App.apiServiceDentist.refreshToken(token);
//            retrofit2.Response<ResponseToken> responseToken = loginCall.execute();
//            if (responseToken.body().getToken() != null) {
//                App.sharedPref.edit().putString("token", responseToken.body().getToken()).apply();
//                try {
//                    res.close();
//                    proceedWithToken(chain, req, responseToken.body().getToken());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            } else {
//                res.close();
//                try {
//                    proceedWithToken(chain, req, null);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

        }
        return res;
    }

    private Response proceedWithToken(Chain chain, Request req, String token) throws IOException {
        Request.Builder builder = req.newBuilder();
        if (token != null) {
            builder.addHeader("Authorization", "Bearer "+token);
        }

        Request request = builder.removeHeader("@").build();
        return chain.proceed(request);
    }

}
