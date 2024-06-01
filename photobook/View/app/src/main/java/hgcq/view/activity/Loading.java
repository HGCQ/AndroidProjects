package hgcq.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import hgcq.controller.MemberController;
import hgcq.view.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Loading extends AppCompatActivity {

    private MemberController mc;
    private Context context;

    private ImageView loading;
    private MediaPlayer mediaPlayer;

    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mc = new MemberController(this);
        this.context = this;

        loading = findViewById(R.id.loading);
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(loading);

        Intent login = new Intent(this, Login.class);
        Intent main = new Intent(this, Main.class);

        mc.islogin(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        isLogin = true;
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(context, "서버 응답 오류", Toast.LENGTH_SHORT).show();
            }
        });

        loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if (isLogin) {
                    startActivity(main);
                } else {
                    Toast.makeText(context, "세션이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                    startActivity(login);
                }
            }
        });

        // MP3 파일 자동 재생
        mediaPlayer = MediaPlayer.create(this, R.raw.loading);
        mediaPlayer.start(); // 음악 자동 재생
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