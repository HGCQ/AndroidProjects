package hgcq.controller;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.List;

import hgcq.callback.PhotoCallback;
import hgcq.config.NetworkClient;
import hgcq.model.dto.PhotoDTO;
import hgcq.model.service.PhotoService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoController {

    private PhotoService photoService;
    private Context context; // 액티비티

    public PhotoController(Context context) {
        NetworkClient client = NetworkClient.getInstance(context.getApplicationContext());
        photoService = client.getPhotoService();
        this.context = context;
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

        Call<ResponseBody> call = photoService.uploadPhoto(datePart, namePart, body);
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

        Call<ResponseBody> call = photoService.deletePhoto(photoDTO);
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
        Call<List<String>> call = photoService.getPhotos(eventDate);
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

}
