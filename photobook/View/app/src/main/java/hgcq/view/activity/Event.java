package hgcq.view.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import java.util.ArrayList;
import java.util.List;

import hgcq.config.NetworkClient;
import hgcq.controller.PhotoController;
import hgcq.view.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Event extends AppCompatActivity {

    // Controller
    private PhotoController pc;

    // View
    private ImageButton back, setting, addPhoto, save;
    private EditText title, content;
    private TextView date;
    private RecyclerView settingList;
    private ImageView photo;

    // Config
    private boolean isRecyclerViewVisible = false;
    private Context context;
    private NetworkClient client;

    // Status Code
    private static final int REQUEST_GALLERY = 1000; // 갤러리
    private static final int REQUEST_PERMISSION = 2000; // 환경 설정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        client = NetworkClient.getInstance(this);
        this.context = this;

        pc = new PhotoController(this);

        back = (ImageButton) findViewById(R.id.back);
        setting = (ImageButton) findViewById(R.id.setting);
        addPhoto = (ImageButton) findViewById(R.id.addPhoto);
        save = (ImageButton) findViewById(R.id.save);

        title = (EditText) findViewById(R.id.title);
        content = (EditText) findViewById(R.id.content);
        date = (TextView) findViewById(R.id.date);

        settingList = (RecyclerView) findViewById(R.id.settingList);
        photo = (ImageView) findViewById(R.id.photo);

        settingList.setLayoutManager(new LinearLayoutManager(context));

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mainPage);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecyclerViewVisible) {
                    // 회원 리스트 조회
                    Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out);
                    settingList.startAnimation(slideOut);
                    settingList.setVisibility(View.GONE);
                } else {
                    Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                    settingList.startAnimation(slideIn);
                    settingList.setVisibility(View.VISIBLE);
                }
                isRecyclerViewVisible = !isRecyclerViewVisible;
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이벤트 수정
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

}