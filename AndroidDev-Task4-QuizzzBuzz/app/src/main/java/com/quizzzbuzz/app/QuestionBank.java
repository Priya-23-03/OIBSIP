package com.quizzzbuzz.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank {

    /** Returns {@code count} questions picked at random from the pool, in shuffled order. */
    public static List<Question> getRandomQuestions(int count) {
        List<Question> pool = buildPool();
        Collections.shuffle(pool);
        return new ArrayList<>(pool.subList(0, Math.min(count, pool.size())));
    }

    private static List<Question> buildPool() {
        List<Question> q = new ArrayList<>();

        q.add(new Question("What is the capital of Australia?",
                "Canberra", "Sydney", "Melbourne", "Perth"));
        q.add(new Question("Which planet is known as the Red Planet?",
                "Mars", "Venus", "Jupiter", "Mercury"));
        q.add(new Question("Who wrote the play \"Romeo and Juliet\"?",
                "William Shakespeare", "Charles Dickens", "Jane Austen", "Mark Twain"));
        q.add(new Question("What is the chemical symbol for water?",
                "H2O", "CO2", "O2", "NaCl"));
        q.add(new Question("Which is the largest ocean on Earth?",
                "Pacific Ocean", "Atlantic Ocean", "Indian Ocean", "Arctic Ocean"));
        q.add(new Question("In which year did India gain independence?",
                "1947", "1950", "1942", "1935"));
        q.add(new Question("How many continents are there on Earth?",
                "7", "5", "6", "8"));
        q.add(new Question("What is the largest mammal in the world?",
                "Blue whale", "African elephant", "Giraffe", "Hippopotamus"));
        q.add(new Question("Which gas do plants absorb from the atmosphere for photosynthesis?",
                "Carbon dioxide", "Oxygen", "Nitrogen", "Hydrogen"));
        q.add(new Question("Who is known as the Father of the Indian Constitution?",
                "B. R. Ambedkar", "Mahatma Gandhi", "Jawaharlal Nehru", "Sardar Patel"));
        q.add(new Question("What does CPU stand for in computing?",
                "Central Processing Unit", "Computer Personal Unit", "Central Program Utility", "Core Processing Update"));
        q.add(new Question("Which language is primarily used to build native Android apps alongside Kotlin?",
                "Java", "Swift", "Ruby", "PHP"));
        q.add(new Question("What is the hardest natural substance on Earth?",
                "Diamond", "Gold", "Iron", "Quartz"));
        q.add(new Question("Which country is home to the kangaroo?",
                "Australia", "South Africa", "Brazil", "New Zealand"));
        q.add(new Question("What is the square root of 144?",
                "12", "14", "10", "16"));

        return q;
    }
}
