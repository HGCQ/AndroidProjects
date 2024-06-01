package hgcq.view.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import hgcq.adapter.EventInviteAdapter;
import hgcq.adapter.MemberAdapter;
import hgcq.config.NetworkClient;
import hgcq.controller.EventController;
import hgcq.controller.MemberController;
import hgcq.controller.PhotoController;
import hgcq.model.dto.EventDTO;
import hgcq.model.dto.MemberDTO;
import hgcq.model.dto.MemberInvitationDTO;
import hgcq.view.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Event extends AppCompatActivity {

    // Controller
    private PhotoController pc;
    private MemberController mc;
    private EventController ec;

    // View
    private ImageButton back, setting, addPhoto, save, exit, add;
    private EditText title, content;
    private TextView date;
    private RecyclerView settingList, friendList;
    private ImageView photo;

    // Config
    private boolean isSettingViewVisible = false;
    private boolean isOwner = false;
    private boolean isFriendListEmpty = false;
    private Context context;
    private NetworkClient client;
    private EventInviteAdapter eventInviteAdapter;
    private MemberAdapter memberAdapter;

    // Status Code
    private static final int REQUEST_GALLERY = 1000; // 갤러리
    private static final int REQUEST_PERMISSION = 2000; // 환경 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        client = NetworkClient.getInstance(this);
        this.context = this;

        pc = new PhotoController(this);
        ec = new EventController(this);
        mc = new MemberController(this);

        back = (ImageButton) findViewById(R.id.back);
        setting = (ImageButton) findViewById(R.id.setting);
        addPhoto = (ImageButton) findViewById(R.id.addPhoto);
        save = (ImageButton) findViewById(R.id.save);
        exit = (ImageButton) findViewById(R.id.exit);
        add = (ImageButton) findViewById(R.id.add);

        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        date = (TextView) findViewById(R.id.date);

        photo = (ImageView) findViewById(R.id.photo);

        settingList = (RecyclerView) findViewById(R.id.settingList);
        settingList.setLayoutManager(new LinearLayoutManager(context));

        friendList = (RecyclerView) findViewById(R.id.friendList);
        friendList.setLayoutManager(new LinearLayoutManager(context));

        Intent mainPage = new Intent(this, Main.class);
        Intent galleryPage = new Intent(this, Gallery.class);

        Intent get = getIntent();
        String resDate = get.getStringExtra("date");
        String resTitle = get.getStringExtra("title");
        String resContent = get.getStringExtra("content");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                date.setText(resDate);
                title.setText(resTitle);
                if (resContent == null) {
                    content.setText("");
                } else {
                    content.setText(resContent);
                }
                pc.getPhotos(resDate, new Callback<List<String>>() {
                    @Override
                    public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                        if (response.isSuccessful()) {
                            List<String> photoUrlList = response.body();
                            if (!photoUrlList.isEmpty()) {
                                int random = (int) (Math.random() * photoUrlList.size());
                                String url = photoUrlList.get(random);
                                String serverIp = client.getServerIp();

                                Glide.with(context)
                                        .load(serverIp + url)
                                        .into(photo);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<String>> call, Throwable t) {
                        Log.e("서버 응답 실패", t.getMessage());
                    }
                });
            }
        });

        mc.friendList(new Callback<List<MemberDTO>>() {
            @Override
            public void onResponse(Call<List<MemberDTO>> call, Response<List<MemberDTO>> response) {
                if (response.isSuccessful()) {
                    List<MemberDTO> friends = response.body();
                    if (friends.isEmpty()) {
                        isFriendListEmpty = true;
                    }
                    eventInviteAdapter = new EventInviteAdapter(friends);
                    friendList.setAdapter(eventInviteAdapter);
                    eventInviteAdapter.setOnItemClickListener(new EventInviteAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            MemberDTO memberDTO = friends.get(position);
                            EventDTO eventDTO = new EventDTO(resTitle, resDate, resContent);
                            MemberInvitationDTO memberInvitationDTO = new MemberInvitationDTO(eventDTO, memberDTO);
                            ec.inviteEvent(memberInvitationDTO, new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.isSuccessful()) {
                                        Toast.makeText(context, "친구 초대 성공!", Toast.LENGTH_SHORT).show();
                                        Log.d("친구 초대 성공", "Code: " + response.code());
                                        memberAdapter.addFriend(memberDTO);
                                        friendList.setVisibility(View.GONE);
                                    } else {
                                        Toast.makeText(context, "친구 초대 실패", Toast.LENGTH_SHORT).show();
                                        Log.d("친구 초대 실패", "Code: " + response.code());
                                        friendList.setVisibility(View.GONE);
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.e("서버 응답 실패", t.getMessage());
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<MemberDTO>> call, Throwable t) {
                Log.e("서버 응답 실패", t.getMessage());
            }
        });

        ec.memberList(resDate, new Callback<List<MemberDTO>>() {
            @Override
            public void onResponse(Call<List<MemberDTO>> call, Response<List<MemberDTO>> response) {
                if (response.isSuccessful()) {
                    List<MemberDTO> memberList = response.body();
                    memberAdapter = new MemberAdapter(memberList);
                    settingList.setAdapter(memberAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<MemberDTO>> call, Throwable t) {
                Log.e("서버 응답 실패", t.getMessage());
            }
        });

        ec.owner(resDate, new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    isOwner = response.body();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("서버 응답 실패", t.getMessage());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainPage);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSettingViewVisible) {
                    Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out);
                    settingList.startAnimation(slideOut);
                    exit.startAnimation(slideOut);
                    settingList.setVisibility(View.GONE);
                    exit.setVisibility(View.GONE);
                    if (isOwner) {
                        add.startAnimation(slideOut);
                        add.setVisibility(View.GONE);
                    }

                } else {
                    Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                    settingList.startAnimation(slideIn);
                    exit.startAnimation(slideIn);
                    settingList.setVisibility(View.VISIBLE);
                    exit.setVisibility(View.VISIBLE);
                    if (isOwner) {
                        add.startAnimation(slideIn);
                        add.setVisibility(View.VISIBLE);
                    }
                }
                isSettingViewVisible = !isSettingViewVisible;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFriendListEmpty) {
                    friendList.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "친구가 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                    startActivityForResult(intent, REQUEST_PERMISSION);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        openGallery();
                    } else {
                        Toast.makeText(context, "권한이 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryPage.putExtra("date", resDate);
                galleryPage.putExtra("title", resTitle);
                galleryPage.putExtra("content", resContent);
                startActivity(galleryPage);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventDTO eventDTO = new EventDTO();
                eventDTO.setDate(resDate);
                ec.deleteEvent(eventDTO, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "이벤트 삭제 성공!", Toast.LENGTH_SHORT).show();
                            Log.d("이벤트 삭제 성공", "Code: " + response.code());
                            startActivity(mainPage);
                        } else {
                            Toast.makeText(context, "이벤트 삭제 실패", Toast.LENGTH_SHORT).show();
                            Log.d("이벤트 삭제 실패", "Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                        Log.e("이벤트 삭제 실패", t.getMessage());
                    }
                });
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reqTitle = title.getText().toString();
                String reqContent = content.getText().toString();
                EventDTO eventDTO = new EventDTO();
                eventDTO.setDate(resDate);
                eventDTO.setName(reqTitle);
                eventDTO.setContent(reqContent);
                ec.updateEvent(eventDTO, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "이벤트 수정 성공!", Toast.LENGTH_SHORT).show();
                            Log.d("이벤트 수정 성공", "Code: " + response.code());
                        } else {
                            Toast.makeText(context, "이벤트 수정 실패", Toast.LENGTH_SHORT).show();
                            Log.d("이벤트 수정 실패", "Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context, "서버 응답 오류", Toast.LENGTH_SHORT).show();
                        Log.e("이벤트 수정 실패", t.getMessage());
                    }
                });
            }
        });

    }

    // 갤러리 열기
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_GALLERY);
    }

    // 갤러리에서 선택 후 실행
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        date = (TextView) findViewById(R.id.date);
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri photoUrl = data.getData();
            // 사진 업로드
            pc.uploadPhoto(photoUrl, getImageName(photoUrl), date.getText().toString(), new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "사진 업로드 성공!", Toast.LENGTH_SHORT).show();
                        Log.d("사진 업로드 성공", "Code: " + response.code());
                        Glide.with(context)
                                .load(photoUrl)
                                .into(photo);
                    } else {
                        Toast.makeText(context, "사진 업로드 실패", Toast.LENGTH_SHORT).show();
                        Log.d("사진 업로드 실패", "Code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(context, "서버 응답 실패", Toast.LENGTH_SHORT).show();
                    Log.e("서버 응답 실패", t.getMessage());
                }
            });
        }
    }

    // 갤러리에서 가져온 이미지 이름 추출
    private String getImageName(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
        try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                return cursor.getString(columnIndex);
            }
        } catch (Exception e) {
            Log.e("이미지 이름 가져오기 실패", e.getMessage());
        }
        return null;
    }

    // Uri에서 진짜 경로 추출
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(column_index);
        cursor.close();
        return path;
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