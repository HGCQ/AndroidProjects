package hgcq.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hgcq.model.dto.PhotoDTO;
import hgcq.model.service.PhotoService;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoController {

    private PhotoService ps;

    public PhotoController() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .addInterceptor(logging)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.35.193:8080")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ps = retrofit.create(PhotoService.class);
    }

    /**
     * 사진 업로드
     *
     * @param fileName  사진 이름
     * @param image     사진
     * @param eventDate 이벤트 날짜
     */
    public void uploadPhoto(String fileName, Bitmap image, String eventDate) {
        byte[] convertImage = bitmapToByteArray(image);
        Call<ResponseBody> call = ps.uploadPhoto(new PhotoDTO(fileName, convertImage, eventDate));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Photo Upload", "Success: " + response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Photo Upload", "Fail: " + t.getMessage());
            }
        });
    }

    /**
     * 사진 삭제
     *
     * @param fileName  사진 이름
     * @param eventDate 이벤트 날짜
     */
    public void deletePhoto(String fileName, String eventDate) {
        Call<ResponseBody> call = ps.deletePhoto(new PhotoDTO(fileName, eventDate));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Photo Delete", "Success: " + response.code());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Photo Delete", "Fail: " + t.getMessage());
            }
        });
    }

    /**
     * 사진 리스트 받아오기
     *
     * @param eventDate 이벤트 날짜
     */
    public void getPhotos(String eventDate) {
        Call<List<PhotoDTO>> call = ps.getPhotos(eventDate);

        call.enqueue(new Callback<List<PhotoDTO>>() {
            @Override
            public void onResponse(Call<List<PhotoDTO>> call, Response<List<PhotoDTO>> response) {
                Log.d("Photo List", "Code: " + response.code());
                List<PhotoDTO> photoDTOS = response.body();
            }

            @Override
            public void onFailure(Call<List<PhotoDTO>> call, Throwable t) {
                Log.e("Photo List", "Fail: " + t.getMessage());
            }
        });
    }

    /**
     * 바이트 배열을 비트맵으로 변환
     *
     * @param byteArray 바이트 배열
     * @return 비트맵
     */
    public Bitmap byteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    /**
     * 비트맵을 바이트 배열로 변환
     *
     * @param bitmap 비트맵
     * @return 바이트 배열
     */
    public byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
