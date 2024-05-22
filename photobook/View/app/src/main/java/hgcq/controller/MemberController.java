package hgcq.controller;

import android.content.Context;
import java.util.List;
import hgcq.config.NetworkClient;
import hgcq.model.dto.MemberDTO;
import hgcq.model.service.MemberService;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * 서버와 통신 API (회원 관련)
 * 회원 가입 - createMember(MemberDTO memberDto, Callback<ResponseBody> callback)
 * 로그인 - loginMember(MemberDTO memberDto, Callback<ResponseBody> callback)
 * 로그아웃 - logoutMember(Callback<ResponseBody> callback)
 * 회원 정보 수정 - updateMember(MemberDTO memberDto, Callback<ResponseBody> callback)
 * 친구 추가 - addFriend(MemberDTO memberDto, Callback<ResponseBody> callback)
 * 친구 삭제 - deleteFriend(MemberDTO memberDto, Callback<ResponseBody> callback)
 * 친구 리스트 조회 - friendList(Callback<List<MemberDTO>> callback)
 */
public class MemberController {

    private MemberService memberService;

    public MemberController(Context context) {
        NetworkClient client = NetworkClient.getInstance(context.getApplicationContext());
        memberService = client.getMemberService();
    }

    public void createMember(MemberDTO memberDto, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.joinMember(memberDto);
        call.enqueue(callback);
    }

    public void loginMember(MemberDTO memberDto, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.loginMember(memberDto);
        call.enqueue(callback);
    }

    public void logoutMember(Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.logoutMember();
        call.enqueue(callback);
    }

    public void updateMember(MemberDTO memberDto, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.updateMember(memberDto);
        call.enqueue(callback);
    }

    public void addFriend(MemberDTO memberDto, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.addFriend(memberDto);
        call.enqueue(callback);
    }

    public void deleteFriend(MemberDTO memberDto, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.deleteFriend(memberDto);
        call.enqueue(callback);
    }

    public void friendList(Callback<List<MemberDTO>> callback) {
        Call<List<MemberDTO>> call = memberService.friendList();
        call.enqueue(callback);
    }
}