package hgcq.model.service;


import hgcq.model.dto.MemberDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MemberService {

    @POST("/member/join")
    Call<ResponseBody> createMember(@Body MemberDTO memberDTO);

    @POST("/member/login")
    Call<ResponseBody> loginMember(@Body MemberDTO memberDTO);

    @POST("/member/update")
    Call<ResponseBody> updateMember(@Body MemberDTO memberDTO);


}
