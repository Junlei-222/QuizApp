package com.example.quizapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import com.example.quizzapp.Question;

public class QuizActivity extends AppCompatActivity {

    private TextView tvQuestion, tvProgressText;
    private RadioGroup radioGroup;
    private RadioButton rbOption0, rbOption1, rbOption2, rbOption3;
    private Button btnSubmit, btnNext;
    private ProgressBar progressBar;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int answeredCount = 0;      // 已提交的题目数量
    private boolean isSubmitted = false; // 当前题目是否已提交

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // 初始化视图
        tvQuestion = findViewById(R.id.tv_question);
        tvProgressText = findViewById(R.id.tv_progress_text);
        radioGroup = findViewById(R.id.radioGroup);
        rbOption0 = findViewById(R.id.rb_option0);
        rbOption1 = findViewById(R.id.rb_option1);
        rbOption2 = findViewById(R.id.rb_option2);
        rbOption3 = findViewById(R.id.rb_option3);
        btnSubmit = findViewById(R.id.btn_submit);
        btnNext = findViewById(R.id.btn_next);
        progressBar = findViewById(R.id.progressBar);

        // 加载题目数据
        loadQuestions();

        // 显示第一题
        updateQuestionDisplay();

        // 提交按钮点击
        btnSubmit.setOnClickListener(v -> submitAnswer());

        // 下一题按钮点击
        btnNext.setOnClickListener(v -> {
            currentQuestionIndex++;
            updateQuestionDisplay();
            // 重置状态
            isSubmitted = false;
            btnSubmit.setEnabled(true);
            btnNext.setEnabled(false);
            resetOptionsBackground();
        });
    }

    private void loadQuestions() {
        questionList = new ArrayList<>();
        // 题目1
        questionList.add(new Question("What is the capital of France?",
                new String[]{"Berlin", "Madrid", "Paris", "Lisbon"}, 2));
        // 题目2
        questionList.add(new Question("Which planet is known as the Red Planet?",
                new String[]{"Earth", "Mars", "Jupiter", "Saturn"}, 1));
        // 题目3
        questionList.add(new Question("Who painted the Mona Lisa?",
                new String[]{"Van Gogh", "Picasso", "Da Vinci", "Rembrandt"}, 2));
        // 题目4
        questionList.add(new Question("What is the largest ocean on Earth?",
                new String[]{"Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"}, 3));
        // 你可以继续添加更多题目，只要保证每个题目有4个选项和正确的索引
    }

    private void updateQuestionDisplay() {
        int total = questionList.size();
        int percent = (answeredCount * 100) / total;
        progressBar.setProgress(percent);
        tvProgressText.setText(answeredCount + " / " + total + " completed");

        if (currentQuestionIndex < total) {
            Question q = questionList.get(currentQuestionIndex);
            tvQuestion.setText(q.getQuestionText());
            String[] opts = q.getOptions();
            rbOption0.setText(opts[0]);
            rbOption1.setText(opts[1]);
            rbOption2.setText(opts[2]);
            rbOption3.setText(opts[3]);

            radioGroup.clearCheck();
            enableOptions(true);
        } else {
            // 所有题目已完成，跳转到结果界面
            Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", total);
            startActivity(intent);
            finish();
        }
    }

    private void submitAnswer() {
        if (isSubmitted) {
            Toast.makeText(this, "You already submitted this question!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedIndex = -1;
        if (selectedId == R.id.rb_option0) selectedIndex = 0;
        else if (selectedId == R.id.rb_option1) selectedIndex = 1;
        else if (selectedId == R.id.rb_option2) selectedIndex = 2;
        else if (selectedId == R.id.rb_option3) selectedIndex = 3;

        Question currentQuestion = questionList.get(currentQuestionIndex);
        int correctIndex = currentQuestion.getCorrectAnswerIndex();

        boolean isCorrect = (selectedIndex == correctIndex);
        if (isCorrect) {
            score++;
        }

        // 高亮显示
        highlightOptions(correctIndex, selectedIndex, isCorrect);

        // 禁用提交按钮和选项，启用下一题
        btnSubmit.setEnabled(false);
        enableOptions(false);
        isSubmitted = true;
        answeredCount++;
        btnNext.setEnabled(true);

        // 更新进度条
        int total = questionList.size();
        int percent = (answeredCount * 100) / total;
        progressBar.setProgress(percent);
        tvProgressText.setText(answeredCount + " / " + total + " completed");
    }

    private void highlightOptions(int correctIndex, int selectedIndex, boolean isCorrect) {
        resetOptionsBackground();
        // 正确选项变绿
        setOptionBackground(correctIndex, Color.GREEN);
        // 如果选错，所选变红
        if (!isCorrect) {
            setOptionBackground(selectedIndex, Color.RED);
        }
    }

    private void setOptionBackground(int index, int color) {
        RadioButton rb = getRadioButtonAtIndex(index);
        if (rb != null) {
            rb.setBackgroundColor(color);
        }
    }

    private void resetOptionsBackground() {
        rbOption0.setBackgroundColor(Color.TRANSPARENT);
        rbOption1.setBackgroundColor(Color.TRANSPARENT);
        rbOption2.setBackgroundColor(Color.TRANSPARENT);
        rbOption3.setBackgroundColor(Color.TRANSPARENT);
    }

    private RadioButton getRadioButtonAtIndex(int index) {
        switch (index) {
            case 0: return rbOption0;
            case 1: return rbOption1;
            case 2: return rbOption2;
            case 3: return rbOption3;
            default: return null;
        }
    }

    private void enableOptions(boolean enable) {
        rbOption0.setEnabled(enable);
        rbOption1.setEnabled(enable);
        rbOption2.setEnabled(enable);
        rbOption3.setEnabled(enable);
    }
}
