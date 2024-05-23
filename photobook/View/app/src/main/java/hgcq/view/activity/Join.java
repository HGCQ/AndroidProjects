package hgcq.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

import hgcq.controller.MemberController;
import hgcq.model.dto.MemberDTO;
import hgcq.view.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Join extends AppCompatActivity {

    private EditText email, pwd, pwdCheck, name;
    private ImageButton join, back;
    private Spinner spinner;

    private Context context;

    private MemberController mc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        mc = new MemberController(this);

        String regName = "^[가-힣A-Za-z0-9]{1,8}$";
        String regPw = "^[A-Za-z][A-Za-z0-9!@#$%^&*()_+]{7,19}$";
        String regEmail = "\\w+@\\w+\\.\\w+(\\.\\w+)?";

        this.context = this;

        back = (ImageButton) findViewById(R.id.back);
        email = (EditText) findViewById(R.id.email);
        pwd = (EditText) findViewById(R.id.password);
        pwdCheck = (EditText) findViewById(R.id.passwordCheck);
        name = (EditText) findViewById(R.id.name);

        join = (ImageButton) findViewById(R.id.join);

        spinner = (Spinner) findViewById(R.id.spinner);

        Intent loginPage = new Intent(this, Login.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginPage);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString();
                String userPw = pwd.getText().toString();
                String userPwCheck = pwdCheck.getText().toString();
                String userName = name.getText().toString();

                // 비어 있는지 확인
                if (userEmail.isEmpty()) {
                    Toast.makeText(context, "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                } else if (userPw.isEmpty()) {
                    Toast.makeText(context, "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    pwd.requestFocus();
                    return;
                } else if (userPwCheck.isEmpty()) {
                    Toast.makeText(context, "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    pwdCheck.requestFocus();
                    return;
                } else if (userName.isEmpty()) {
                    Toast.makeText(context, "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }

                userEmail = userEmail + "@" + spinner.getSelectedItem().toString();

                // 정규식 확인
                if (!Pattern.matches(regEmail, userEmail)) {
                    Toast.makeText(context, "이메일 형식이 잘못됐습니다. 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                    return;
                } else if (!Pattern.matches(regPw, userPw)) {
                    Toast.makeText(context, "비밀번호 형식이 잘못됐습니다. 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                    pwd.requestFocus();
                    return;
                } else if (!Pattern.matches(regName, userName)) {
                    Toast.makeText(context, "닉네임 형식이 잘못됐습니다. 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                    return;
                }

                // 비밀번호 일치 확인
                if (userPw.equals(userPwCheck)) {
                    // 회원 가입
                    mc.createMember(new MemberDTO(userName, userEmail, userPw), new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "회원 가입 성공!", Toast.LENGTH_SHORT).show();
                                Log.d("회원 가입 성공", "Code: " + response.code());
                                startActivity(loginPage);
                            } else {
                                Toast.makeText(context, "이미 존재하지 않는 이메일입니다.", Toast.LENGTH_SHORT).show();
                                Log.d("회원 가입 실패", "Code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                            Log.e("회원 가입 실패", t.getMessage());
                        }
                    });
                } else {
                    Toast.makeText(context, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}