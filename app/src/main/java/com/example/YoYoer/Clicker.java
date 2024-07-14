package com.example.YoYoer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//TODO 按键声音，小分页面，设置计分规则，新建比赛（去打分）等等
public class Clicker extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private TextView clicker_plus_num;
    private TextView clicker_minus_num;
    private TextView clicker_restart_num;
    private TextView clicker_deprecated_num;
    private TextView clicker_break_up_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clicker);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        clicker_plus_num = findViewById(R.id.clicker_plus_num);
        clicker_minus_num = findViewById(R.id.clicker_minus_num);
        clicker_restart_num = findViewById(R.id.clicker_restart_num);
        clicker_deprecated_num = findViewById(R.id.clicker_deprecated_num);
        clicker_break_up_num = findViewById(R.id.clicker_break_up_num);

        LinearLayout layout_clicker_plus = findViewById(R.id.clicker_plus);
        layout_clicker_plus.setOnClickListener(this);
        LinearLayout layout_clicker_minus = findViewById(R.id.clicker_minus);
        layout_clicker_minus.setOnClickListener(this);
        LinearLayout layout_clicker_restart = findViewById(R.id.clicker_restart);
        layout_clicker_restart.setOnClickListener(this);
        LinearLayout layout_clicker_deprecated = findViewById(R.id.clicker_deprecated);
        layout_clicker_deprecated.setOnClickListener(this);
        LinearLayout layout_clicker_break_up = findViewById(R.id.clicker_break_up);
        layout_clicker_break_up.setOnClickListener(this);
        Button button_clicker_reset = findViewById(R.id.clicker_reset);
        button_clicker_reset.setOnLongClickListener(Clicker.this);

        Toolbar clicker_toolBar = findViewById(R.id.clicker_toolBar);
        clicker_toolBar.setTitle("打分器");
        setSupportActionBar(clicker_toolBar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        clicker_toolBar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.clicker_plus) {
            int i = Integer.parseInt(clicker_plus_num.getText().toString()) + 1;
            clicker_plus_num.setText(String.valueOf(i));
        }
        if (view.getId() == R.id.clicker_minus) {
            clicker_minus_num = view.findViewById(R.id.clicker_minus_num);
            int i = Integer.parseInt(clicker_minus_num.getText().toString()) + 1;
            clicker_minus_num.setText(String.valueOf(i));
        }
        if (view.getId() == R.id.clicker_restart) {
            clicker_restart_num = view.findViewById(R.id.clicker_restart_num);
            int i = Integer.parseInt(clicker_restart_num.getText().toString()) + 1;
            clicker_restart_num.setText(String.valueOf(i));
        }
        if (view.getId() == R.id.clicker_deprecated) {
            clicker_deprecated_num = view.findViewById(R.id.clicker_deprecated_num);
            int i = Integer.parseInt(clicker_deprecated_num.getText().toString()) + 1;
            clicker_deprecated_num.setText(String.valueOf(i));
        }
        if (view.getId() == R.id.clicker_break_up) {
            clicker_break_up_num = view.findViewById(R.id.clicker_break_up_num);
            int i = Integer.parseInt(clicker_break_up_num.getText().toString()) + 1;
            clicker_break_up_num.setText(String.valueOf(i));
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getId() == R.id.clicker_reset) {
            clicker_plus_num.setText("0");
            clicker_minus_num.setText("0");
            clicker_restart_num.setText("0");
            clicker_deprecated_num.setText("0");
            clicker_break_up_num.setText("0");
            return true;
        }
        return false;
    }
}