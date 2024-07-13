package com.example.YoYoer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button button_clicker = findViewById(R.id.clicker);
        button_clicker.setOnClickListener(this);
        Button button_trainer = findViewById(R.id.trainer);
        button_trainer.setOnClickListener(this);
        Button button_learner = findViewById(R.id.learner);
        button_learner.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        if (view.getId() == R.id.clicker) {
            intent.setClass(MainActivity.this, Clicker.class);
        } else if (view.getId() == R.id.trainer) {
            intent.setClass(MainActivity.this, Trainer.class);
        } else if (view.getId() == R.id.learner) {
            intent.setClass(MainActivity.this, Learner.class);
        }
        startActivity(intent);
    }
}