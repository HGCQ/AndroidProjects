package hgcq.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

import hgcq.controller.EventController;
import hgcq.model.dto.EventDTO;
import hgcq.view.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventcreateActivity extends AppCompatActivity {

    private EventController ec;
    private Context context;

    private ImageButton back, save;
    private EditText title, date, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventcreate);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        String regDate = "^\\d{4}-\\d{2}-\\d{2}$";

        this.context = this;

        ec = new EventController(this);

        back = (ImageButton) findViewById(R.id.back);
        save = (ImageButton) findViewById(R.id.save);

        title = (EditText) findViewById(R.id.title);
        date = (EditText) findViewById(R.id.date);
        content = (EditText) findViewById(R.id.content);

        Intent mainPage = new Intent(this, Main.class);
        Intent eventPage = new Intent(this, Event.class);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainPage);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resTitle = title.getText().toString();
                String resDate = date.getText().toString();
                String resContent = content.getText().toString();

                if (resTitle.isEmpty()) {
                    Toast.makeText(context, "값을 입력해주세요", Toast.LENGTH_SHORT).show();
                    title.requestFocus();
                    return;
                } else if (resDate.isEmpty()) {
                    Toast.makeText(context, "값을 입력해주세요", Toast.LENGTH_SHORT).show();
                    date.requestFocus();
                    return;
                } else if (resContent.isEmpty()) {
                    Toast.makeText(context, "값을 입력해주세요", Toast.LENGTH_SHORT).show();
                    content.requestFocus();
                    return;
                }

                if (!Pattern.matches(regDate, resDate)) {
                    Toast.makeText(context, "날짜 형식은 2000-00-00 으로 입력해주세요.", Toast.LENGTH_SHORT).show();
                    date.requestFocus();
                    return;
                } else {
                    // 이벤트 생성 후 메인 페이지 이동
                    EventDTO eventDTO = new EventDTO(resTitle, resDate, resContent);
                    ec.createEvent(eventDTO, new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(context, "이벤트 생성 성공!", Toast.LENGTH_SHORT).show();
                                Log.d("이벤트 생성 성공", "Code: " + response.code());
                                eventPage.putExtra("title", resTitle);
                                eventPage.putExtra("date", resDate);
                                eventPage.putExtra("content", resContent);
                                startActivity(eventPage);
                            } else {
                                Toast.makeText(context, "이벤트 생성 실패", Toast.LENGTH_SHORT).show();
                                Log.d("이벤트 생성 실패", "Code: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(context, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                            Log.e("이벤트 생성 실패", t.getMessage());
                        }
                    });
                }

            }
        });

    }
}