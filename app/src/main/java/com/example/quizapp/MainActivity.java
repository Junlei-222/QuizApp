package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etName;
    private Button btnStart;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.et_name);
        btnStart = findViewById(R.id.btn_start);
        prefs = getSharedPreferences("QuizPrefs", MODE_PRIVATE);

        // 加载保存的姓名（如果有）
        String savedName = prefs.getString("userName", "");
        etName.setText(savedName);

        btnStart.setOnClickListener(v -> {
            String userName = etName.getText().toString().trim();
            if (userName.isEmpty()) {
                etName.setError("Please enter your name");
                return;
            }
            // 保存姓名
            prefs.edit().putString("userName", userName).apply();

            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            startActivity(intent);
        });
    }
}