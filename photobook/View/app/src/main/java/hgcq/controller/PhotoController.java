package hgcq.controller;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.List;

import hgcq.config.NetworkClient;
import hgcq.model.dto.PhotoDTO;
import hgcq.model.service.PhotoService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 서버와 통신 API (사진 관련)
 * 사진 업로드 - uploadPhoto(Uri photoUri, String photoName, String eventDate, Callback<ResponseBody> callback) // 현재 갤러리에서 사진 하나만 선택이 가능
 * 사진 삭제 - deletePhoto(String photoName, String eventDate, Callback<ResponseBody> callback) // 현재 갤러리에서 사진 하나만 선택이 가능
 * 사진 리스트 조회 - getPhotos(String eventDate, Callback<List<String>> callback)
 */
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
     * @param photoUri  사진 URI
     * @param photoName 사진 이름
     * @param eventDate 이벤트 날짜
     * @param callback  콜백
     */
    public void uploadPhoto(Uri photoUri, String photoName, String eventDate, Callback<ResponseBody> callback) {
        String filePath = getRealPathFromURI(photoUri);
        File file = new File(filePath);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        RequestBody datePart = RequestBody.create(MultipartBody.FORM, eventDate);
        RequestBody namePart = RequestBody.create(MultipartBody.FORM, photoName);

        Call<ResponseBody> call = photoService.uploadPhoto(datePart, namePart, body);
        call.enqueue(callback);
    }


    /**
     * 사진 삭제
     *
     * @param photoName 사진 이름
     * @param eventDate 이벤트 날짜
     * @param callback  콜백
     */
    public void deletePhoto(String photoName, String eventDate, Callback<ResponseBody> callback) {
        PhotoDTO photoDTO = new PhotoDTO(photoName, eventDate);

        Call<ResponseBody> call = photoService.deletePhoto(photoDTO);
        call.enqueue(callback);
    }


    /**
     * 사진 리스트 조회
     *
     * @param eventDate 이벤트 날짜
     * @param callback  콜백
     */
    public void getPhotos(String eventDate, Callback<List<String>> callback) {
        Call<List<String>> call = photoService.getPhotos(eventDate);
        call.enqueue(callback);
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
