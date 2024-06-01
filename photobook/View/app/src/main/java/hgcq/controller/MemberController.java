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
        // NetworkClient 싱글톤 인스턴스를 가져와 MemberService를 초기화합니다.
        NetworkClient client = NetworkClient.getInstance(context.getApplicationContext());
        memberService = client.getMemberService();
    }

    /**
     * 회원 가입 API 호출
     * memberDto 회원 정보 DTO
     *callback 회원 가입 결과 콜백
     */
    public void createMember(MemberDTO memberDto, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.joinMember(memberDto);
        call.enqueue(callback);
    }

    /**
     * 회원 이름 중복 체크 API 호출
     * name 회원 이름
     * callback 중복 체크 결과 콜백
     */
    public void duplicateName(String name, Callback<Boolean> callback) {
        Call<Boolean> call = memberService.duplicateName(name);
        call.enqueue(callback);
    }

    /**
     * 회원 이메일 중복 체크 API 호출
     * email 회원 이메일
     * callback 중복 체크 결과 콜백
     */
    public void duplicateEmail(String email, Callback<Boolean> callback) {
        Call<Boolean> call = memberService.duplicateEmail(email);
        call.enqueue(callback);
    }

    /**
     * 로그인 API 호출
     * memberDto 회원 정보 DTO
     * callback 로그인 결과 콜백
     */
    public void loginMember(MemberDTO memberDto, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.loginMember(memberDto);
        call.enqueue(callback);
    }

    /**
     * 로그아웃 API 호출
     * callback 로그아웃 결과 콜백
     */
    public void logoutMember(Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.logoutMember();
        call.enqueue(callback);
    }

    /**
     * 회원 정보 수정 API 호출
     * memberDto 수정된 회원 정보 DTO
     * callback 수정 결과 콜백
     */
    public void updateMember(MemberDTO memberDto, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.updateMember(memberDto);
        call.enqueue(callback);
    }

    /**
     * 친구 추가 API 호출
     * memberDto 추가할 친구 정보 DTO
     * callback 친구 추가 결과 콜백
     */
    public void addFriend(MemberDTO memberDto, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.addFriend(memberDto);
        call.enqueue(callback);
    }

    /**
     * 친구 삭제 API 호출
     * memberDto 삭제할 친구 정보 DTO
     * callback 친구 삭제 결과 콜백
     */
    public void deleteFriend(MemberDTO memberDto, Callback<ResponseBody> callback) {
        Call<ResponseBody> call = memberService.deleteFriend(memberDto);
        call.enqueue(callback);
    }

    /**
     * 친구 리스트 조회 API 호출
     *  callback 친구 리스트 조회 결과 콜백
     */
    public void friendList(Callback<List<MemberDTO>> callback) {
        Call<List<MemberDTO>> call = memberService.friendList();
        call.enqueue(callback);
    }

    /**
     * 내 정보 조회 API 호출
     *  callback 내 정보 조회 결과 콜백
     */
    public void me(Callback<MemberDTO> callback) {
        Call<MemberDTO> call = memberService.me();
        call.enqueue(callback);
    }

    /**
     * 로그인 상태 확인 API 호출
     * callback 로그인 상태 확인 결과 콜백
     */
    public void islogin(Callback<Boolean> callback) {
        Call<Boolean> call = memberService.islogin();
        call.enqueue(callback);
    }

    /**
     * 친구 검색 API 호출
     * name 검색할 친구 이름
     *callback 친구 검색 결과 콜백
     */
    public void searchFriend(String name, Callback<List<MemberDTO>> callback) {
        Call<List<MemberDTO>> call = memberService.searchFriend(name);
        call.enqueue(callback);
    }
}
