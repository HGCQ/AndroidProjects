package hgcq.view.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

import hgcq.view.R;

public class Main extends AppCompatActivity {

    private Context context;

    private ImageButton set, search, plus, date;
    private RecyclerView eventListView;

    private EditText searchText;
    private boolean isShowRecyclerView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        set = (ImageButton) findViewById(R.id.set);
        search = (ImageButton) findViewById(R.id.search);
        plus = (ImageButton) findViewById(R.id.plus);
        date = (ImageButton) findViewById(R.id.date);

        Intent eventCreatePage = new Intent(this, EventcreateActivity.class);
        Intent eventPage = new Intent(this, Event.class);

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShowRecyclerView) {
                    // 이벤트 리스트 조회
                    Animation slideOut = AnimationUtils.loadAnimation(context, R.anim.slide_out);
                    eventListView.startAnimation(slideOut);
                    eventListView.setVisibility(View.GONE);
                } else {
                    Animation slideIn = AnimationUtils.loadAnimation(context, R.anim.slide_in);
                    eventListView.startAnimation(slideIn);
                    eventListView.setVisibility(View.VISIBLE);
                }
                isShowRecyclerView = !isShowRecyclerView;
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resSearch = searchText.getText().toString();
                // 이벤트 이름 검색 후 리스트 갱신
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