package hgcq.view.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import hgcq.view.R;

public class Event extends AppCompatActivity {

    private ImageView imageView3;
    private EditText editText;
    private Button button;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        imageView3 = (ImageView) findViewById(R.id.imageView3);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);

        // 완료
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editText.getText().toString();
            }
        });

        // 사진 추가
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}