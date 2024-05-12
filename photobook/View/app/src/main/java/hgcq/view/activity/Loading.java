package hgcq.view.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import hgcq.view.R;

public class Loading extends AppCompatActivity {
    private ImageView imageView2;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        imageView2 = findViewById(R.id.imageView2);
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(imageView2);

        // MP3 파일 자동 재생
        mediaPlayer = MediaPlayer.create(this, R.raw.loading);
        mediaPlayer.start(); // 음악 자동 재생
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
