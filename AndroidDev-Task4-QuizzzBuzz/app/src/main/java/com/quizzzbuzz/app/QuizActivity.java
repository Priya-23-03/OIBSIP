package com.quizzzbuzz.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final int TOTAL_QUESTIONS = 10;

    private TextView tvCounter, tvScore, tvQuestion;
    private ProgressBar progressBar;
    private RadioGroup radioGroup;
    private RadioButton[] optionButtons;
    private Button btnNext;

    private List<Question> questions;
    private int currentIndex = 0;
    private int score = 0;
    private boolean answered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvCounter = findViewById(R.id.tvCounter);
        tvScore = findViewById(R.id.tvScore);
        tvQuestion = findViewById(R.id.tvQuestion);
        progressBar = findViewById(R.id.progressBar);
        radioGroup = findViewById(R.id.radioGroup);
        btnNext = findViewById(R.id.btnNext);

        optionButtons = new RadioButton[]{
                findViewById(R.id.option1),
                findViewById(R.id.option2),
                findViewById(R.id.option3),
                findViewById(R.id.option4)
        };

        // Fresh, shuffled set of questions on every run
        questions = QuestionBank.getRandomQuestions(TOTAL_QUESTIONS);
        progressBar.setMax(questions.size());

        for (int i = 0; i < optionButtons.length; i++) {
            final int selected = i;
            optionButtons[i].setOnClickListener(v -> onOptionSelected(selected));
        }

        btnNext.setOnClickListener(v -> onNextClicked());

        showQuestion();
    }

    private void showQuestion() {
        Question q = questions.get(currentIndex);
        answered = false;

        tvCounter.setText(getString(R.string.question_counter, currentIndex + 1, questions.size()));
        tvScore.setText(getString(R.string.score_format, score));
        progressBar.setProgress(currentIndex + 1);
        tvQuestion.setText(q.getText());

        radioGroup.clearCheck();
        List<String> options = q.getOptions();
        for (int i = 0; i < optionButtons.length; i++) {
            optionButtons[i].setText(options.get(i));
            optionButtons[i].setBackgroundResource(R.drawable.option_default);
            optionButtons[i].setEnabled(true);
        }

        btnNext.setText(currentIndex == questions.size() - 1 ? R.string.finish : R.string.next);
    }

    private void onOptionSelected(int selectedIndex) {
        if (answered) return;
        answered = true;

        int correctIndex = questions.get(currentIndex).getCorrectIndex();

        // Lock all options so the answer can't be changed
        for (RadioButton rb : optionButtons) {
            rb.setEnabled(false);
        }

        // Always highlight the correct answer green
        optionButtons[correctIndex].setBackgroundResource(R.drawable.option_correct);

        if (selectedIndex == correctIndex) {
            score++;
            tvScore.setText(getString(R.string.score_format, score));
        } else {
            // Wrong choice highlighted red
            optionButtons[selectedIndex].setBackgroundResource(R.drawable.option_wrong);
        }
    }

    private void onNextClicked() {
        if (!answered) {
            Toast.makeText(this, R.string.pick_answer, Toast.LENGTH_SHORT).show();
            return;
        }

        currentIndex++;
        if (currentIndex < questions.size()) {
            showQuestion();
        } else {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(ResultActivity.EXTRA_SCORE, score);
            intent.putExtra(ResultActivity.EXTRA_TOTAL, questions.size());
            startActivity(intent);
            finish();
        }
    }
}
