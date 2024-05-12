package hgcq.model.service;

import java.time.LocalDate;
import java.util.List;

import hgcq.model.dto.PhotoDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PhotoService {

    @POST("/photo/upload")
    Call<ResponseBody> uploadPhoto(@Body PhotoDTO photoDTO);

    @POST("/photo/delete")
    Call<ResponseBody> deletePhoto(@Body PhotoDTO photoDTO);

    @POST("/photo/photos")
    Call<List<PhotoDTO>> getPhotos(@Body String eventDate);
}
