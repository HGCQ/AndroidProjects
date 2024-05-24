package hgcq.view.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hgcq.adapter.EventAdapter;
import hgcq.adapter.MemberAdapter;
import hgcq.config.NetworkClient;
import hgcq.controller.EventController;
import hgcq.controller.MemberController;
import hgcq.model.dto.EventDTO;
import hgcq.model.dto.MemberDTO;
import hgcq.view.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main extends AppCompatActivity {

    private EventController ec;
    private MemberController mc;
    private Context context;
    private EventAdapter eventAdapter;

    private ImageButton set, search, plus, date, logout;
    private RecyclerView eventListView;
    private RecyclerView friendListView;

    private EditText searchText;
    private boolean isShowRecyclerView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;
        ec = new EventController(this);
        mc = new MemberController(this);

        set = (ImageButton) findViewById(R.id.set);
        search = (ImageButton) findViewById(R.id.search);
        plus = (ImageButton) findViewById(R.id.plus);
        date = (ImageButton) findViewById(R.id.date);
        logout = (ImageButton) findViewById(R.id.logout);

        eventListView = (RecyclerView) findViewById(R.id.eventList);
        friendListView = (RecyclerView) findViewById(R.id.friendList);

        searchText = (EditText) findViewById(R.id.searchText);

        Intent eventCreatePage = new Intent(this, EventcreateActivity.class);
        Intent eventPage = new Intent(this, Event.class);
        Intent loginPage = new Intent(this, Login.class);

        ec.eventList(new Callback<List<EventDTO>>() {
            @Override
            public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                if (response.isSuccessful()) {
                    List<EventDTO> eventList = response.body();
                    eventAdapter = new EventAdapter(eventList);
                    eventListView.setAdapter(eventAdapter);
                    eventAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            EventDTO eventDTO = eventList.get(position);
                            String reqDate = eventDTO.getDate();
                            String reqTitle = eventDTO.getName();
                            String reqContent = eventDTO.getContent();
                            eventPage.putExtra("date", reqDate);
                            eventPage.putExtra("title", reqTitle);
                            eventPage.putExtra("content", reqContent);
                            startActivity(eventPage);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<EventDTO>> call, Throwable t) {
                Log.e("서버 응답 실패", t.getMessage());
            }
        });

        mc.friendList(new Callback<List<MemberDTO>>() {
            @Override
            public void onResponse(Call<List<MemberDTO>> call, Response<List<MemberDTO>> response) {
                if (response.isSuccessful()) {
                    List<MemberDTO> friendList = response.body();
                    MemberAdapter adapter = new MemberAdapter(friendList);
                    friendListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<MemberDTO>> call, Throwable t) {
                Log.e("서버 응답 실패", t.getMessage());
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowRecyclerView) {
                    Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out);
                    friendListView.startAnimation(slideOut);
                    friendListView.setVisibility(View.GONE);
                    logout.startAnimation(slideOut);
                    logout.setVisibility(View.GONE);
                } else {
                    Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                    friendListView.startAnimation(slideIn);
                    friendListView.setVisibility(View.VISIBLE);
                    logout.startAnimation(slideIn);
                    logout.setVisibility(View.VISIBLE);
                }
                isShowRecyclerView = !isShowRecyclerView;
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mc.logoutMember(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "로그아웃 성공!", Toast.LENGTH_SHORT).show();
                            Log.d("로그아웃 성공", "Code: " + response.code());
                            NetworkClient.getInstance(context).deleteCookie();
                            startActivity(loginPage);
                        } else {
                            Toast.makeText(context, "로그아웃 실패", Toast.LENGTH_SHORT).show();
                            Log.d("로그아웃 실패", "Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("서버 응답 실패", t.getMessage());
                    }
                });
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resSearch = searchText.getText().toString();
                ec.fineEventByName(resSearch, new Callback<List<EventDTO>>() {
                    @Override
                    public void onResponse(Call<List<EventDTO>> call, Response<List<EventDTO>> response) {
                        if (response.isSuccessful()) {
                            Log.d("이벤트 검색 성공", "Code: " + response.code());
                            List<EventDTO> eventList = response.body();
                            eventAdapter = new EventAdapter(eventList);
                            eventListView.setAdapter(eventAdapter);
                            eventAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    EventDTO eventDTO = eventList.get(position);
                                    String reqDate = eventDTO.getDate();
                                    String reqTitle = eventDTO.getName();
                                    String reqContent = eventDTO.getContent();
                                    eventPage.putExtra("date", reqDate);
                                    eventPage.putExtra("title", reqTitle);
                                    eventPage.putExtra("content", reqContent);
                                    startActivity(eventPage);
                                }
                            });
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            if (response.body().isEmpty()) {
                                Toast.makeText(context, "이벤트가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "이벤트가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("이벤트 검색 실패", "Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<EventDTO>> call, Throwable t) {
                        Toast.makeText(context, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                        Log.e("이벤트 검색 실패", t.getMessage());
                    }
                });
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 이벤트 날짜 검색 후 리스트 갱신
                        String reqDate = LocalDate.of(year, month + 1, dayOfMonth).toString();
                        ec.findEventByDate(reqDate, new Callback<EventDTO>() {
                            @Override
                            public void onResponse(Call<EventDTO> call, Response<EventDTO> response) {
                                if (response.isSuccessful()) {
                                    Log.d("이벤트 검색 성공", "Code: " + response.code());
                                    EventDTO eventDTO = response.body();
                                    List<EventDTO> eventList = new ArrayList<>();
                                    eventList.add(eventDTO);
                                    eventAdapter = new EventAdapter(eventList);
                                    eventListView.setAdapter(eventAdapter);
                                    eventAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            EventDTO eventDTO = eventList.get(position);
                                            String reqDate = eventDTO.getDate();
                                            String reqTitle = eventDTO.getName();
                                            String reqContent = eventDTO.getContent();
                                            eventPage.putExtra("date", reqDate);
                                            eventPage.putExtra("title", reqTitle);
                                            eventPage.putExtra("content", reqContent);
                                            startActivity(eventPage);
                                        }
                                    });
                                } else {
                                    Toast.makeText(context, "이벤트가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    Log.d("이벤트 검색 실패", "Code: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<EventDTO> call, Throwable t) {
                                Toast.makeText(context, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                                Log.e("이벤트 검색 실패", t.getMessage());
                            }
                        });
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(eventCreatePage);
            }
        });
    }
}