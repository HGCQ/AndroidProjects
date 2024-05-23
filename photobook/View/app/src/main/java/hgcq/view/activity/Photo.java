package hgcq.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;

import hgcq.adapter.PhotoAdapter;
import hgcq.controller.PhotoController;
import hgcq.model.dto.PhotoDTO;
import hgcq.view.R;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Photo extends AppCompatActivity {

    private Context context;
    private PhotoController pc;
    private List<String> photoUrlList;

    private ViewPager2 viewPager;
    private ImageButton back, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        this.context = this;
        pc = new PhotoController(this);

        viewPager = findViewById(R.id.viewPager);
        back = findViewById(R.id.back);
        delete = findViewById(R.id.delete);

        Intent galleryPage = new Intent(this, Gallery.class);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        String date = intent.getStringExtra("date");
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        pc.getPhotos(date, new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    photoUrlList = response.body();
                    PhotoAdapter adapter = new PhotoAdapter(context, photoUrlList);
                    viewPager.setAdapter(adapter);
                    viewPager.setCurrentItem(position, false);
                } else {
                    Log.d("사진 불러오기 실패", "Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Log.e("서버 응답 실패", t.getMessage());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryPage.putExtra("date", date);
                galleryPage.putExtra("title", title);
                galleryPage.putExtra("content", content);
                startActivity(galleryPage);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = viewPager.getCurrentItem();
                String path = photoUrlList.get(pos);
                PhotoDTO photoDTO = new PhotoDTO();
                photoDTO.setPath(path);
                pc.deletePhoto(photoDTO, new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            photoUrlList.remove(pos);
                            viewPager.getAdapter().notifyItemRemoved(pos);
                            viewPager.getAdapter().notifyItemRangeChanged(pos, photoUrlList.size());

                            int newItemPosition = pos;
                            if (pos == photoUrlList.size()) {
                                newItemPosition = pos - 1;
                            }
                            if (!photoUrlList.isEmpty()) {
                                viewPager.setCurrentItem(newItemPosition, true);
                            }
                        } else {
                            Log.d("사진 삭제 실패", "Code: " + response.code());
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