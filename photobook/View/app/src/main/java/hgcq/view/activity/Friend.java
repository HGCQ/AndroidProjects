package hgcq.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hgcq.adapter.FriendAdapter;
import hgcq.controller.MemberController;
import hgcq.model.dto.MemberDTO;
import hgcq.view.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Friend extends AppCompatActivity {
    private ImageButton back, search, add, save;
    private TextView userName, userEmail;
    private EditText searchText, friendNickname;
    private RecyclerView friendListView, addFriendView;
    private Context context;
    private MemberController mc;

    private FriendAdapter friendAdapter;
    private boolean isShowRecyclerView = false; // RecyclerView가 표시되는지 여부를 나타내는 플래그


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        // 액션바 제거
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mc = new MemberController(this);

        this.context = this;

        back = (ImageButton) findViewById(R.id.back);
        add = (ImageButton) findViewById(R.id.add);
        search = (ImageButton) findViewById(R.id.search);
        save = (ImageButton) findViewById(R.id.save);

        searchText = (EditText) findViewById(R.id.searchText);
        friendNickname = (EditText) findViewById(R.id.friendNickname);

        userEmail = (TextView) findViewById(R.id.email);
        userName = (TextView) findViewById(R.id.name);

        friendListView = (RecyclerView) findViewById(R.id.friendList);
        addFriendView = (RecyclerView) findViewById(R.id.addFriend);

        Intent mainPage = new Intent(this, Main.class);

        /**
         * 뒤로 가기 버튼
         */
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //메인 페이지가 실행
                startActivity(mainPage);
            }
        });

        /**
         * 서버에서 사용자 정보를 가져와서 화면에 표시
         */
        mc.me(new Callback<MemberDTO>() {
            @Override
            public void onResponse(Call<MemberDTO> call, Response<MemberDTO> response) {
                if (response.isSuccessful()) {
                    // 서버에서 받은 사용자 정보 데이터
                    MemberDTO owner = response.body();
                    // 사용자 이름을 화면에 표시
                    userName.setText(owner.getName());
                    // 사용자 이메일을 화면에 표시
                    userEmail.setText(owner.getEmail());
                }
            }

            @Override
            public void onFailure(Call<MemberDTO> call, Throwable t) {
                // 서버 응답이 실패한 경우
                Toast.makeText(context, "서버 응답 오류", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * 서버에서 친구 리스트를 가져와서 화면에 표시
         */
        mc.friendList(new Callback<List<MemberDTO>>() {
            @Override
            public void onResponse(Call<List<MemberDTO>> call, Response<List<MemberDTO>> response) {
                // 서버 응답이 성공한 경우
                if (response.isSuccessful()) {
                    // 서버에서 받은 친구 리스트 데이터
                    List<MemberDTO> fList = response.body();
                    // 친구 리스트 데이터로 어댑터 생성
                    friendAdapter = new FriendAdapter(context, fList);
                    // 친구 리스트 뷰에 어댑터 설정
                    friendListView.setAdapter(friendAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<MemberDTO>> call, Throwable t) {
                // 서버 응답이 실패한 경우
                Log.e("서버 응답 실패", t.getMessage());
            }
        });

        /**
         * "add" 버튼에 대한 클릭 이벤트 처리
         */
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // RecyclerView가 표시 중인지 확인
                if (isShowRecyclerView) {
                    // 애니메이션을 사용하여 친구 추가 뷰를 숨김
                    Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out);
                    addFriendView.startAnimation(slideOut);
                    addFriendView.setVisibility(View.GONE);
                    friendNickname.startAnimation(slideOut);
                    friendNickname.setVisibility(View.GONE);
                    save.startAnimation(slideOut);
                    save.setVisibility(View.GONE);
                } else {
                    // 애니메이션을 사용하여 친구 추가 뷰를 표시
                    Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                    addFriendView.startAnimation(slideIn);
                    addFriendView.setVisibility(View.VISIBLE);
                    friendNickname.startAnimation(slideIn);
                    friendNickname.setVisibility(View.VISIBLE);
                    save.startAnimation(slideIn);
                    save.setVisibility(View.VISIBLE);
                }
                // RecyclerView 표시 여부를 반전
                isShowRecyclerView = !isShowRecyclerView;
            }
        });

        /**
         * "save" 버튼에 대한 클릭 이벤트 처리
         */
        
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 친구 닉네임을 가져옴
                String friendName = friendNickname.getText().toString();
                // MemberDTO 객체 생성 및 친구 닉네임 설정
                MemberDTO memberDTO = new MemberDTO();
                memberDTO.setName(friendName);

                //MemberController를 사용하여 친구 저장 요청
                mc.addFriend(memberDTO, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // 친구 추가 성공 시
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "친구 추가 성공", Toast.LENGTH_SHORT).show();
                            Log.d("친구 추가 성공", "onResponse: " + response.code());
                            // 어댑터를 통해 친구를 추가하고 리스트뷰 갱신
                            friendAdapter.addFriend(memberDTO);
                        } else {
                            // 친구 추가 실패 시
                            Toast.makeText(context, "친구 추가 실패", Toast.LENGTH_SHORT).show();
                            Log.d("친구 추가 실패", "onResponse: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        // 서버 응답 실패 시
                        Toast.makeText(context, "서버 응답 실패", Toast.LENGTH_SHORT).show();
                        Log.e("서버 응답 실패", t.getMessage());
                    }
                });
            }
        });

        /**
         * "search" 버튼에 대한 클릭 이벤트 처리
         */
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //사용자가 입력한 값을 text에 받음
                String text = searchText.getText().toString();

                mc.searchFriend(text, new Callback<List<MemberDTO>>() {
                    //MemberController를 사용하여 친구 찾기 요청
                    @Override
                    public void onResponse(Call<List<MemberDTO>> call, Response<List<MemberDTO>> response) {
                        // 성공적으로 응답을 받았을 때
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "친구 조회 성공", Toast.LENGTH_SHORT).show();
                            Log.d("친구 조회 성공", "onResponse: " + response.code());
                            // 검색 결과를 어댑터에 연결하여 리스트뷰에 표시
                            List<MemberDTO> newSearch = response.body();
                            FriendAdapter friendAdapter = new FriendAdapter(context, newSearch);
                            friendListView.setAdapter(friendAdapter);
                        } else {
                            //친구 조회 실패 시
                            Toast.makeText(context, "친구 조회 실패", Toast.LENGTH_SHORT).show();
                            Log.d("친구 조회 실패", "onResponse: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MemberDTO>> call, Throwable t) {
                        // 서버 응답 실패 시
                        Toast.makeText(context, "서버 응답 실패", Toast.LENGTH_SHORT).show();
                        Log.e("서버 응답 실패", t.getMessage());
                    }
                });
            }
        });
    }

    /**
     * 터치 이벤트를 처리하는 메서드
     * EditText가 포커스된 상태에서 외부를 터치할 때 키보드를 숨기고 포커스를 해제함
     */
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // 터치 이벤트가 ACTION_DOWN일 때
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 현재 포커스된 View 가져오기
            View v = getCurrentFocus();
            // 포커스된 View가 EditText인 경우
            if (v instanceof EditText) {
                // EditText의 전역 가시 영역 가져오기
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                // 터치 위치가 EditText 영역 밖인 경우
                if (!outRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                    // EditText 포커스 해제
                    v.clearFocus();
                    // 키보드 숨기기
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        }
        // 상위 클래스의 dispatchTouchEvent 메서드 호출
        return super.dispatchTouchEvent(ev);
    }

}