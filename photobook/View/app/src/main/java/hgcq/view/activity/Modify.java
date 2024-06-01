package hgcq.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class Modify extends AppCompatActivity {

    private Context context;
    private MemberController mc;

    private TextView email;
    private ImageButton nameCheck, save, back;
    private EditText name, password, passwordCheck;

    private String regName = "^[가-힣A-Za-z0-9]{1,8}$";
    private String regPw = "^[A-Za-z][A-Za-z0-9!@#$%^&*()_+]{7,19}$";
    private boolean isNotNameDuplicate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setContentView(R.layout.activity_modify);

        this.context = this;
        mc = new MemberController(this);

        email = (TextView) findViewById(R.id.email);
        nameCheck = (ImageButton) findViewById(R.id.nameCheck);
        save = (ImageButton) findViewById(R.id.save);
        back = (ImageButton) findViewById(R.id.back);
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        passwordCheck = (EditText) findViewById(R.id.passwordCheck);

        Intent mainPage = new Intent(this, Main.class);

        mc.me(new Callback<MemberDTO>() {
            @Override
            public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {
                if (response.isSuccessful()) {
                    MemberDTO own = response.body();
                    email.post(new Runnable() {
                        @Override
                        public void run() {
                            email.setText(own.getEmail());
                        }
                    });
                } else {
                    Log.e("자신 정보 불러오기 실패", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<MemberDTO> call, Throwable t) {
                Log.e("자신 정보 불러오기 실패", t.getMessage());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainPage);
            }
        });

        nameCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();

                if (userName.isEmpty()) {
                    Toast.makeText(context, "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                    name.requestFocus();
                } else {
                    mc.duplicateName(userName, new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                if (Pattern.matches(regName, userName)) {
                                    if (response.body()) {
                                        isNotNameDuplicate = true;
                                        Toast.makeText(context, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, "중복된 닉네임입니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "닉네임 형식이 잘못됐습니다. 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(context, "특수문자 제외 8자 이하", Toast.LENGTH_SHORT).show();
                                    name.requestFocus();
                                }
                            } else {
                                Toast.makeText(context, "중복된 닉네임입니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(context, "서버 통신 오류.", Toast.LENGTH_SHORT).show();
                            Log.e("닉네임 중복 체크 에러", "onFailure: " + t.getMessage());
                        }
                    });
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = name.getText().toString();
                String userPw = password.getText().toString();
                String userPwCheck = passwordCheck.getText().toString();

                if (userName.isEmpty() && userPw.isEmpty()) {
                    return;
                }

                if (!userPw.isEmpty()) {
                    if (userPwCheck.isEmpty()) {
                        Toast.makeText(context, "값을 입력하세요.", Toast.LENGTH_SHORT).show();
                        passwordCheck.requestFocus();
                        return;
                    } else if (!Pattern.matches(regPw, userPw)) {
                        Toast.makeText(context, "비밀번호 형식이 잘못됐습니다. 다시 입력하세요.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "문자, 숫자, 특수문자 포함 8~20자", Toast.LENGTH_SHORT).show();
                        password.requestFocus();
                        return;
                    } else if (!userPw.equals(userPwCheck)) {
                        Toast.makeText(context, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                        passwordCheck.requestFocus();
                        return;
                    }
                }

                if (!userName.isEmpty()) {
                    if (!isNotNameDuplicate) {
                        Toast.makeText(context, "닉네임 중복 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                MemberDTO memberDTO = new MemberDTO();
                if (!userName.isEmpty() && isNotNameDuplicate) {
                    memberDTO.setName(userName);
                }
                if (!userPw.isEmpty()) {
                    memberDTO.setPassword(userPw);
                }
                mc.updateMember(memberDTO, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "회원 정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                            startActivity(mainPage);
                        } else {
                            Toast.makeText(context, "회원 정보 수정이 실패했습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("회원 정보 수정 실패", "onResponse: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("회원 정보 수정 실패", "onFailure: " + t.getMessage());
                    }
                });

            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}