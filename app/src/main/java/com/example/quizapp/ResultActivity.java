package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView tvScore;
    private Button btnTakeNewQuiz, btnFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScore = findViewById(R.id.tv_score);
        btnTakeNewQuiz = findViewById(R.id.btn_take_new_quiz);
        btnFinish = findViewById(R.id.btn_finish);

        int score = getIntent().getIntExtra("score", 0);
        int total = getIntent().getIntExtra("total", 0);
        tvScore.setText("Your score: " + score + " / " + total);

        btnTakeNewQuiz.setOnClickListener(v -> {
            // 返回主界面，MainActivity会自动从SharedPreferences读取姓名
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnFinish.setOnClickListener(v -> {
            // 关闭整个应用
            finishAffinity();
        });
    }
}
