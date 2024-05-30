package hgcq.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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