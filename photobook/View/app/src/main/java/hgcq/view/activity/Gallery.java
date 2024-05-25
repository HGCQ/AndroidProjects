package hgcq.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hgcq.adapter.GalleryAdapter;
import hgcq.controller.PhotoController;
import hgcq.view.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Gallery extends AppCompatActivity {

    private PhotoController pc;

    private RecyclerView gallery;
    private TextView date;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        pc = new PhotoController(this);

        date = findViewById(R.id.date);
        back = findViewById(R.id.back);
        gallery = (RecyclerView) findViewById(R.id.photoList);

        Intent eventPage = new Intent(this, Event.class);

        Intent get = getIntent();
        String resDate = get.getStringExtra("date");
        String resTitle = get.getStringExtra("title");
        String resContent = get.getStringExtra("content");

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                date.setText(resDate);
            }
        });

        pc.getPhotos(resDate, new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    List<String> photoUrlList = response.body();
                    GalleryAdapter adapter = new GalleryAdapter(Gallery.this, photoUrlList, resDate, resTitle, resContent);
                    gallery.setAdapter(adapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventPage.putExtra("date", resDate);
                eventPage.putExtra("title", resTitle);
                eventPage.putExtra("content", resContent);
                startActivity(eventPage);
            }
        });

    }

}