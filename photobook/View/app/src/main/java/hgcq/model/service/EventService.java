package hgcq.model.service;


import com.google.gson.Gson;

import java.util.List;

import hgcq.model.dto.EventDTO;
import hgcq.model.dto.MemberDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface EventService {

    @POST("/event/create")
    Call<ResponseBody> createEvent(@Body EventDTO eventDTO);

    @POST("/event/delete")
    Call<ResponseBody> deleteEvent(@Body EventDTO eventDTO);

    @POST("/event/invite")
    Call<ResponseBody> inviteEvent(@Body EventDTO eventDTO, @Body MemberDTO memberDTO);

    @GET("/event/eventlist")
    Call<List<EventDTO>> getEventList();

}
