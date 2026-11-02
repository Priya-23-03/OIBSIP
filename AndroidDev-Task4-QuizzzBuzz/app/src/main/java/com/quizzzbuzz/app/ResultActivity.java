package com.quizzzbuzz.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_SCORE = "extra_score";
    public static final String EXTRA_TOTAL = "extra_total";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int score = getIntent().getIntExtra(EXTRA_SCORE, 0);
        int total = getIntent().getIntExtra(EXTRA_TOTAL, 0);
        int incorrect = total - score;

        TextView tvEmoji = findViewById(R.id.tvEmoji);
        TextView tvMessage = findViewById(R.id.tvMessage);
        TextView tvTotalScore = findViewById(R.id.tvTotalScore);
        TextView tvCorrect = findViewById(R.id.tvCorrect);
        TextView tvIncorrect = findViewById(R.id.tvIncorrect);
        Button btnRestart = findViewById(R.id.btnRestart);

        tvTotalScore.setText(score + " / " + total);
        tvCorrect.setText(getString(R.string.correct_count, score));
        tvIncorrect.setText(getString(R.string.incorrect_count, incorrect));

        double percent = total == 0 ? 0 : (score * 100.0) / total;
        if (percent >= 80) {
            tvEmoji.setText("🏆");
            tvMessage.setText("Outstanding! You're a quiz champion.");
        } else if (percent >= 50) {
            tvEmoji.setText("👏");
            tvMessage.setText("Good job! A little more practice and you'll ace it.");
        } else {
            tvEmoji.setText("💪");
            tvMessage.setText("Keep going! Try again to beat your score.");
        }

        btnRestart.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, QuizActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}
