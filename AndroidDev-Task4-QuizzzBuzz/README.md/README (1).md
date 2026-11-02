Quizzz Buzz - Android Quiz App

A multiple-choice quiz app for Android. Answer 10 shuffled questions one at a time, get instant green/red feedback, and see your final score at the end.

Built as Task 4 of the Android App Development Internship at Oasis Infobyte

Screenshots

+MAAwADAA-Welcome                            Question                         Results 
+MAAwAA-
+MAAwAA-
























Features

- Welcome screen with a Start Quiz button
- Question screen with question text, 4 answer options, and a +ACI-Question X of 10+ACI- counter
- Progress bar and live score tracking
- Instant feedback:the correct answer turns green, a wrong pick turns red
- Options lock after answering, so the answer can't be changed
- Next / Finish button (with a Toast if no answer is selected)
- Random questions every run: 10 picked from a pool of 15, in shuffled order, with shuffled options
- Results screen showing total score, correct answers, incorrect answers, and a Restart Quiz button
- Custom adaptive launcher icon






Tech Stack

- Android Studio
- Java
- XML (layouts, drawables, styles)
- Material Components, AppCompat

Min SDK: 21 (Android 5.0) +AHw- Target SDK: 34

Project Structure

app/src/main/
+JRwlACUA- java/com/quizzzbuzz/app/
+JQI-   +JRwlACUA- WelcomeActivity.java   +ACM- Start screen
+JQI-   +JRwlACUA- QuizActivity.java      +ACM- Question flow, feedback, scoring
+JQI-   +JRwlACUA- ResultActivity.java    +ACM- Final score and restart
+JQI-   +JRwlACUA- Question.java          +ACM- Question model (shuffles its own options)
+JQI-   +JRQlACUA- QuestionBank.java      +ACM- Question pool and random selection
+JRQlACUA- res/
    +JRwlACUA- layout/                +ACM- activity+AF8-welcome, activity+AF8-quiz, activity+AF8-result
    +JRwlACUA- drawable/              +ACM- option and card backgrounds
    +JRwlACUA- values/                +ACM- colors, strings, themes
    +JRQlACUA- mipmap-+ACo-/              +ACM- app icons






How to Run

1. Clone the repository

   git clone https://github.com/Priya-23-03/OIBSIP.git
 
2. Open the project folder in Android Studio (File +AOI- Open).
3. Wait for the Gradle sync to finish.

Author

Priyadharshini R S
Android App Development Intern, Oasis Infobyte
