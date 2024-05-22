package hgcq.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import hgcq.controller.MemberController;
import hgcq.model.dto.MemberDTO;
import hgcq.view.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private Context context;
    private MemberController mc;

    private EditText id, password;
    private ImageButton login, join;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.context = this;
        mc = new MemberController(this);

        id = findViewById(R.id.id);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        join = findViewById(R.id.join);

        Intent joinPage = new Intent(this, Join.class);
        Intent mainPage = new Intent(this, Main.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = id.getText().toString();
                String userPw = password.getText().toString();

                if (userId.isEmpty()) {
                    Toast.makeText(context, "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                    return;
                } else if (userPw.isEmpty()) {
                    Toast.makeText(context, "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }

                MemberDTO memberDTO = new MemberDTO(userId, userPw);

//                .post(new Runnable() {
//                    @Override
//                    public void run() {
//                        .setVisibility(View.VISIBLE);
//                    }
//                });

                // 로그인 기능 구현
                mc.loginMember(memberDTO, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "로그인 성공!", Toast.LENGTH_SHORT).show();
                            Log.d("로그인 성공", "Code: " + response.code());
                            startActivity(mainPage);
                        } else {
                            Toast.makeText(context, "존재하지 않는 아이디거나 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("로그인 실패", "Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                        Log.e("로그인 실패", t.getMessage());
                    }
                });
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(joinPage);
            }
        });
    }
}