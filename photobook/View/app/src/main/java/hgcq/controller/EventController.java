package hgcq.controller;

import android.util.Log;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import hgcq.model.dto.EventDTO;
import hgcq.model.service.EventService;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EventController {

    private EventService es;

    public EventController() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("서버주소")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        es = retrofit.create(EventService.class);
    }

    public void createEvent(EventDTO eventDTO) {
        Call<ResponseBody> call = es.createEvent(eventDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Event Create", "Code: " + response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Event Create", "Fail: " + t.getMessage());
            }
        });
    }

    public void deleteEvent(EventDTO eventDTO) {
        Call<ResponseBody> call = es.deleteEvent(eventDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Event Delete", "Code: " + response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Event Delete", "Fail: " + t.getMessage());
            }
        });
    }
}