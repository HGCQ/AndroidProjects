package hgcq.view.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import hgcq.config.NetworkClient;
import hgcq.view.R;

public class Loading extends AppCompatActivity {

    private NetworkClient client;
    private ImageView loading;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        client = NetworkClient.getInstance(this);

        loading = findViewById(R.id.loading);
        Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(loading);

        Intent login = new Intent(this, Login.class);
        Intent main = new Intent(this, Main.class);

        loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                if (client.isLogin()) {
                    startActivity(main);
                } else {
                    startActivity(login);
                }
            }
        });

        // MP3 파일 자동 재생
        mediaPlayer = MediaPlayer.create(this, R.raw.loading);
        mediaPlayer.start(); // 음악 자동 재생
    }
}