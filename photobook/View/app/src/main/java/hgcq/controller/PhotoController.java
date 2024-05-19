package hgcq.controller;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hgcq.callback.PhotoCallback;
import hgcq.model.dto.PhotoDTO;
import hgcq.model.service.PhotoService;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoController {

    private PhotoService ps;
    private Context context; // 액티비티

    private final String serverIp = ""; // 서버 주소

    public PhotoController(Context context) {
        this.context = context;

        // 쿠키 생성
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));

        // Http 메시지 로그
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        // Http 커넥션 설정
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // 서버와 연결
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(serverIp)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ps = retrofit.create(PhotoService.class);
    }


    /**
     * 사진 업로드
     *
     * @param imageUri  사진 경로
     * @param date      이벤트 날짜
     * @param imageName 사진 이름
     */
    public void uploadPhoto(Uri imageUri, String date, String imageName) {
        String filePath = getRealPathFromURI(imageUri);
        File file = new File(filePath);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        RequestBody datePart = RequestBody.create(MultipartBody.FORM, date);
        RequestBody namePart = RequestBody.create(MultipartBody.FORM, imageName);

        Call<ResponseBody> call = ps.uploadPhoto(datePart, namePart, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "사진 업로드 성공", Toast.LENGTH_SHORT).show();
                    Log.d("사진 업로드 성공", "상태 코드: " + response.code());
                } else {
                    Toast.makeText(context, "사진 삭제 실패", Toast.LENGTH_SHORT).show();
                    Log.e("사진 업로드 에러:", "상태 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                Log.e("사진 업로드 에러:", t.getMessage());
            }
        });
    }

    /**
     * 사진 삭제
     *
     * @param imageName 사진 이름
     * @param date      날짜
     */
    public void deletePhoto(String imageName, String date) {
        PhotoDTO photoDTO = new PhotoDTO(imageName, date);

        Call<ResponseBody> call = ps.deletePhoto(photoDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "사진 삭제 성공", Toast.LENGTH_SHORT).show();
                    Log.d("사진 삭제 성공", "상태 코드: " + response.code());
                } else {
                    Toast.makeText(context, "사진 삭제 실패", Toast.LENGTH_SHORT).show();
                    Log.e("사진 업로드 에러:", "상태 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "사진 삭제 실패", Toast.LENGTH_SHORT).show();
                Log.e("사진 업로드 에러:", t.getMessage());
            }
        });
    }

    /**
     * 사진 리스트 조회
     *
     * @param eventDate 날짜
     * @param callback  콜백 인터페이스
     */
    public void getPhotos(String eventDate, PhotoCallback callback) {
        Call<List<String>> call = ps.getPhotos(eventDate);
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    Log.d("사진 리스트 조회 성공", "상태 코드: " + response.code());
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("사진 리스트 조회 실패 : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("사진 업로드 에러:", t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    // -- 유틸리티 메소드 --

    // URI를 String 형식의 경로로 바꿈
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
    }

    public String getServerIp() {
        return serverIp;
    }
}
