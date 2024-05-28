package hgcq.view.activity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

import hgcq.adapter.EventAdapter;
import hgcq.adapter.FriendAdapter;
import hgcq.adapter.MemberAdapter;
import hgcq.controller.MemberController;
import hgcq.model.dto.EventDTO;
import hgcq.model.dto.MemberDTO;
import hgcq.view.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Friend extends AppCompatActivity {
    private ImageButton back, search, set,add;
    private TextView userName, userEmail;
    private EditText searchText;
    private RecyclerView friendListView;
    private Context context;
    private MemberController mc;
    private MemberAdapter memberAdapter;

    private FriendAdapter friendAdapter;

    private RecyclerView settingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friend);

        mc = new MemberController(this);

        this.context = this;

        back = (ImageButton) findViewById(R.id.back);
        searchText = (EditText) findViewById(R.id.searchText);
        set = (ImageButton) findViewById(R.id.set);

        userEmail = (TextView) findViewById(R.id.email);
        userName = (TextView) findViewById(R.id.name);

        friendListView = (RecyclerView) findViewById(R.id.friendList);

        Intent mainPage = new Intent(this, Main.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(mainPage);
            }
        });

        //내 정보 보여주기
        mc.me(new Callback<MemberDTO>() {
            @Override
            public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {
                if (response.isSuccessful()) {
                    String name = response.body().getName();
                    String email = response.body().getEmail();
                    userName.setText(name);
                    userEmail.setText(email);


                }
            }

            @Override
            public void onFailure(Call<MemberDTO> call, Throwable t) {
                Toast.makeText(context, "서버 응답 오류", Toast.LENGTH_SHORT).show();

            }
        });

//        mc.addFriend(new MemberDTO(), new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                // 서버의 성공 응답 처리
//                if (response.isSuccessful()) {
//                    // 성공 시 처리 로직
//                    int statusCode = response.code();
//                    String responseBody = response.body().string();
//                    // 추가 처리 로직
//                } else {
//                    // 실패 시 처리 로직
//                    int statusCode = response.code();
//                    String errorBody = response.errorBody().string();
//                    // 오류 처리 로직
//                }
//            }

//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                // 오류 응답 처리
//                // 오류 처리 로직
//            }
//        });




        //친구 리스트 나열
        mc.friendList(new Callback<List<MemberDTO>>() {
            @Override
            public void onResponse(Call<List<MemberDTO>> call, Response<List<MemberDTO>> response) {
                if (response.isSuccessful()) {
                    List<MemberDTO>fList=response.body();
                    List<MemberDTO>delete=response.body();
                    friendAdapter=new FriendAdapter(this, fList, new FriendAdapter {
                        @Override
                        public void onDeleteClick(int position) {

                        }
                    });
                    settingList.setAdapter(memberAdapter);

                }
            }

            @Override
            public void onFailure(Call<List<MemberDTO>> call, Throwable t) {
                Log.e("서버 응답 실패", t.getMessage());
            }
        });




    }
}