package com.quizzzbuzz.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {
    private final String text;
    private final List<String> options;
    private int correctIndex;

    /**
     * @param text         the question text
     * @param correct      the correct answer
     * @param wrongAnswers the three wrong answers
     */
    public Question(String text, String correct, String... wrongAnswers) {
        this.text = text;
        this.options = new ArrayList<>();
        this.options.add(correct);
        Collections.addAll(this.options, wrongAnswers);
        shuffleOptions(correct);
    }

    /** Shuffle the answer order and remember where the correct answer ended up. */
    public void shuffleOptions(String correct) {
        Collections.shuffle(options);
        correctIndex = options.indexOf(correct);
    }

    public String getText() {
        return text;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }
}
