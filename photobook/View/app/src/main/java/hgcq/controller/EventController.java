package hgcq.controller;

import android.content.Context;
import java.util.List;
import hgcq.config.NetworkClient;
import hgcq.model.dto.EventDTO;
import hgcq.model.dto.MemberDTO;
import hgcq.model.dto.MemberInvitationDTO;
import hgcq.model.service.EventService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 서버와 통신 API (이벤트 관련)
 * 이벤트 생성 - createEvent(EventDTO eventDTO)
 * 이벤트 삭제 - deleteEvent(EventDTO eventDTO)
 * 이벤트 수정 - updateEvent(EventDTO eventDTO)
 * 친구 초대 - inviteEvent(MemberInvitationDTO memberInvitationDTO)
 * 이벤트 조회(날짜) - findEventByDate(String date, EventOneCallback callback)
 * 이벤트 조회(이름) - fineEventByName(String name, EventListCallBack callback)
 * 이벤트 조회(친구) - // 제작중
 * 이벤트 리스트 조회 - eventList(EventListCallBack callback)
 * 이벤트 회원 리스트 조회 - memberList(String date, MemberCallback callback)
 */
public class EventController {

    private EventService eventService;

    public EventController(Context context) {
        NetworkClient client = NetworkClient.getInstance(context.getApplicationContext());
        eventService = client.getEventService();
    }

    public void createEvent(EventDTO eventDTO, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = eventService.createEvent(eventDTO);
        call.enqueue(callback);
    }

    public void deleteEvent(EventDTO eventDTO, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = eventService.deleteEvent(eventDTO);
        call.enqueue(callback);
    }

    public void updateEvent(EventDTO eventDTO, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = eventService.updateEvent(eventDTO);
        call.enqueue(callback);
    }

    public void inviteEvent(MemberInvitationDTO memberInvitationDTO, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = eventService.inviteEvent(memberInvitationDTO);
        call.enqueue(callback);
    }

    public void findEventByDate(String date, Callback<EventDTO> callback) {
        Call<EventDTO> call = eventService.findEvent(date);
        call.enqueue(callback);
    }

    public void fineEventByName(String name, Callback<List<EventDTO>> callback) {
        Call<List<EventDTO>> call = eventService.findByName(name);
        call.enqueue(callback);
    }

    public void eventList(Callback<List<EventDTO>> callback) {
        Call<List<EventDTO>> call = eventService.eventList();
        call.enqueue(callback);
    }

    public void memberList(String date, Callback<List<MemberDTO>> callback) {
        Call<List<MemberDTO>> call = eventService.memberList(date);
        call.enqueue(callback);
    }
}