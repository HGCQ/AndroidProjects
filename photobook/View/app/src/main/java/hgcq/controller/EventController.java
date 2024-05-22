package hgcq.controller;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import hgcq.callback.EventListCallBack;
import hgcq.callback.EventOneCallback;
import hgcq.callback.MemberCallback;
import hgcq.config.NetworkClient;
import hgcq.model.dto.EventDTO;
import hgcq.model.dto.MemberDTO;
import hgcq.model.dto.MemberInvitationDTO;
import hgcq.model.service.EventService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    private Context context;

    public EventController(Context context) {
        NetworkClient client = NetworkClient.getInstance(context.getApplicationContext());
        eventService = client.getEventService();
        this.context = context;
    }

    public void createEvent(EventDTO eventDTO) {
        Call<ResponseBody> call = eventService.createEvent(eventDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "이벤트 생성 성공", Toast.LENGTH_SHORT).show();
                    Log.d("이벤트 생성 성공", "상태 코드: " + response.code());
                } else {
                    Toast.makeText(context, "이벤트 생성 실패", Toast.LENGTH_SHORT).show();
                    Log.e("이벤트 생성 에러:", "상태 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "이벤트 생성 실패", Toast.LENGTH_SHORT).show();
                Log.e("이벤트 생성 에러:", t.getMessage());
            }
        });
    }

    public void deleteEvent(EventDTO eventDTO) {
        Call<ResponseBody> call = eventService.deleteEvent(eventDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "이벤트 삭제 성공", Toast.LENGTH_SHORT).show();
                    Log.d("이벤트 삭제 성공", "상태 코드: " + response.code());
                } else {
                    Toast.makeText(context, "이벤트 삭제 실패", Toast.LENGTH_SHORT).show();
                    Log.e("이벤트 삭제 에러:", "상태 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "이벤트 삭제 실패", Toast.LENGTH_SHORT).show();
                Log.e("이벤트 삭제 에러:", t.getMessage());
            }
        });
    }

    public void updateEvent(EventDTO eventDTO) {
        Call<ResponseBody> call = eventService.updateEvent(eventDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "이벤트 수정 성공", Toast.LENGTH_SHORT).show();
                    Log.d("이벤트 수정 성공", "상태 코드: " + response.code());
                } else {
                    Toast.makeText(context, "이벤트 수정 실패", Toast.LENGTH_SHORT).show();
                    Log.e("이벤트 수정 에러:", "상태 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "이벤트 수정 실패", Toast.LENGTH_SHORT).show();
                Log.e("이벤트 수정 에러:", t.getMessage());
            }
        });
    }

    public void inviteEvent(MemberInvitationDTO memberInvitationDTO) {
        Call<ResponseBody> call = eventService.inviteEvent(memberInvitationDTO);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "이벤트 초대 성공", Toast.LENGTH_SHORT).show();
                    Log.d("이벤트 초대 성공", "상태 코드: " + response.code());
                } else {
                    Toast.makeText(context, "이벤트 초대 실패", Toast.LENGTH_SHORT).show();
                    Log.e("이벤트 초대 에러:", "상태 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "이벤트 초대 실패", Toast.LENGTH_SHORT).show();
                Log.e("이벤트 초대 에러:", t.getMessage());
            }
        });
    }

    public void findEventByDate(String date, EventOneCallback callback) {
        Call<EventDTO> call = eventService.findEvent(date);
        call.enqueue(new Callback<EventDTO>() {
            @Override
            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "날짜로 이벤트 찾기 성공", Toast.LENGTH_SHORT).show();
                    Log.d("날짜로 이벤트 찾기 성공", "상태 코드: " + response.code());
                    callback.onSuccess(response.body());
                } else {
                    Toast.makeText(context, "날짜로 이벤트 찾기 실패", Toast.LENGTH_SHORT).show();
                    Log.e("날짜로 이벤트 찾기 에러:", "상태 코드: " + response.code());
                    callback.onError("상태 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EventDTO> call, Throwable t) {
                Toast.makeText(context, "날짜로 이벤트 찾기 실패", Toast.LENGTH_SHORT).show();
                Log.e("날짜로 이벤트 찾기 에러:", t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    public void fineEventByName(String name, EventListCallBack callback) {
        Call<List<EventDTO>> call = eventService.findByName(name);
        call.enqueue(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "이름으로 이벤트 찾기 성공", Toast.LENGTH_SHORT).show();
                    Log.d("이름으로 이벤트 찾기 성공", "상태 코드: " + response.code());
                    callback.onSuccess(response.body());
                } else {
                    Toast.makeText(context, "이름으로 이벤트 찾기 실패", Toast.LENGTH_SHORT).show();
                    Log.e("이름으로 이벤트 찾기 에러:", "상태 코드: " + response.code());
                    callback.onError("상태 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<EventDTO>> call, Throwable t) {
                Toast.makeText(context, "이름으로 이벤트 찾기 실패", Toast.LENGTH_SHORT).show();
                Log.e("이름으로 이벤트 찾기 에러:", t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    public void eventList(EventListCallBack callback) {
        Call<List<EventDTO>> call = eventService.eventList();
        call.enqueue(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d("이벤트 리스트 조회 성공", "상태 코드: " + response.code());
                    callback.onSuccess(response.body());
                } else {
                    Log.e("이벤트 리스트 조회 에러:", "상태 코드: " + response.code());
                    callback.onError("상태 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<EventDTO>> call, Throwable t) {
                Log.e("이벤트 리스트 조회 에러:", t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }

    public void memberList(String date, MemberCallback callback) {
        Call<List<MemberDTO>> call = eventService.memberList(date);
        call.enqueue(new Callback<List<MemberDTO>>() {
            @Override
            public void onResponse(Call<List<MemberDTO>> call, Response<List<MemberDTO>> response) {
                if (response.isSuccessful()) {
                    Log.d("멤버 리스트 조회 성공", "상태 코드: " + response.code());
                    callback.onSuccess(response.body());
                } else {
                    Log.e("멤버 리스트 조회 에러:", "상태 코드: " + response.code());
                    callback.onError("상태 코드: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MemberDTO>> call, Throwable t) {
                Log.e("멤버 리스트 조회 에러:", t.getMessage());
                callback.onError(t.getMessage());
            }
        });
    }
}