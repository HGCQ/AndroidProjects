package hgcq.controller;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.util.List;

import hgcq.model.dto.MemberDTO;
import hgcq.model.service.MemberService;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MemberController {
    private MemberService memberService;
    private Context context;

    public MemberController(MemberService memberService, Context context) {
        this.memberService = memberService;
        this.context = context;
    };

    ///MemberController() 일단 건들지 말기
    public  MemberController() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = builder
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("서버 주소")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        memberService = retrofit.create(MemberService.class);


    }

    public void createMember(MemberDTO memberDto) {
        //
        Call<ResponseBody> call = memberService.createMember(memberDto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "성공");

                } else {
                    Log.e("TAG", "에러:" + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG", "에러", t);
            }
        });
    }


    public void loginMember(MemberDTO memberDto) {
        Call<ResponseBody> call = memberService.loginMember(memberDto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "성공");
                    // 쿠키 저장
                    saveCookie(response.headers().get("Set-Cookie"));

                } else {
                    Log.e("TAG", "에러:" + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG", "에러", t);
            }
        });
    }

    private void saveCookie(String cookieHeader) {

        List<HttpCookie> cookies = HttpCookie.parse(cookieHeader);//HttpCookieparse.parse했더니 에러 ->HttpCookie변경

        SharedPreferences sharedPreferences = context.getSharedPreferences("cookies", Context.MODE_PRIVATE);
        //SharedPreferences mPreferences = getSharedPreferences(SharedPrefFile, MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        for (HttpCookie cookie : cookies) {
            editor.putString(cookie.getName(), cookie.getValue());
        }
        editor.apply();
    }

    //updateMember
    public void updateMember(MemberDTO memberDto) {
        Call<ResponseBody> call = memberService.updateMember(memberDto);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("TAG", "성공");

                } else {
                    Log.e("TAG", "에러:" + response.code());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TAG", "에러", t);
            }
        });
    }



}
