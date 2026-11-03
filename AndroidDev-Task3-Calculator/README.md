Android Calculator App

A clean, fully functional calculator for Android with a responsive button grid, correct operator precedence, and safe error handling.

Built as Task 3 of the Android App Development Internship at Oasis Infobyte.

Features

- Display showing the current input and the result (with the previous expression above it)
- Number buttons  and a decimal point (only one decimal per number)
- Operators: +,—,*,/,//,% with correct precedence (`2+3Ã—4 = 14`)
- = button to evaluate
- C button to clear everything
- « Backspace button to delete the last character
- Division by zero shows `Error` instead of crashing
- Button grid built with GridLayout (the `=` key spans two rows, `0` spans two columns)
- Safe against rapid or repeated taps: operators replace each other instead of stacking, and bad input never crashes the app
- Results are tidied up: no trailing `.0`, and `0.1 + 0.2` shows `0.3`
- Display text shrinks automatically for long numbers
- Custom adaptive launcher icon

Tech Stack

- Android Studio
- Java
- XML (GridLayout, drawables, styles)
- Material Components, AppCompat

Min SDK: 21 (Android 5.0) | Target SDK: 34

Project Structure

app/src/main/ 
+-- java/com/calcbuzz/app/ 
¦ +-- MainActivity.java # Button handling, StringBuilder expression, display 
¦ +-- Evaluator.java # Parses and evaluates the expression (× ÷ before + -) 
+-- res/
 +-- layout/activity_main.xml # Display + GridLayout button grid 
+-- drawable/ # Display background 
+-- values/ # colors, strings, themes and key styles 
+-- mipmap-*/ # app icons

How to Run

1. Clone the repository
   
   git clone https://github.com/Priya-23-03/OIBSIP.git
  
2. Open the project folder in Android Studio (File â†’ Open).
3. Wait for the Gradle sync to finish.
4. Run on an emulator or a physical device.

How It Works

- Every tap appends to a `StringBuilder` that holds the expression, such as `12+3.5Ã—2`.
- Input rules (no double operators, one decimal per number, no leading zeros) are checked before each character is added.
- On `=`, `Evaluator` splits the expression into numbers and operators, resolves `Ã—` and `Ã·` first, then `+` and `âˆ’`.
- Any division by zero or malformed input is caught and shown as `Error`.

Author

Priyadharshini R S
Android App Development Intern, Oasis Infobyte