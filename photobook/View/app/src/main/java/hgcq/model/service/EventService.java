package hgcq.model.service;


import com.google.gson.Gson;

import java.util.List;

import hgcq.model.dto.EventDTO;
import hgcq.model.dto.MemberDTO;
import hgcq.model.dto.MemberInvitationDTO;
import kotlin.ParameterName;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface EventService {

    @POST("/event/create")
    Call<ResponseBody> createEvent(@Body EventDTO eventDTO);

    @POST("/event/delete")
    Call<ResponseBody> deleteEvent(@Body EventDTO eventDTO);

    @POST("/event/update")
    Call<ResponseBody> updateEvent(@Body EventDTO eventDTO);

    @POST("/event/invite")
    Call<ResponseBody> inviteEvent(@Body MemberInvitationDTO memberInvitationDTO);

    @GET("/event/find/date")
    Call<EventDTO> findEvent(@Query("date") String date);

    @GET("/event/find/name")
    Call<List<EventDTO>> findByName(@Query("name") String name);

    @GET("/event/eventlist")
    Call<List<EventDTO>> eventList();

    @GET("/event/member/list")
    Call<List<MemberDTO>> memberList(@Query("date") String date);





}
