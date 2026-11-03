package com.calcbuzz.app;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_LENGTH = 30;
    private static final String ERROR_TEXT = "Error";

    private TextView tvDisplay, tvExpression;

    // The expression being typed, e.g. "12+3.5×2"
    private final StringBuilder expression = new StringBuilder();
    private boolean justEvaluated = false;  // true right after "=" is pressed
    private boolean hasError = false;       // true while "Error" is on screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);
        tvExpression = findViewById(R.id.tvExpression);

        // Number buttons 0-9
        int[] digitIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int i = 0; i < digitIds.length; i++) {
            final String digit = String.valueOf(i);
            findViewById(digitIds[i]).setOnClickListener(v -> onDigit(digit));
        }

        findViewById(R.id.btnDot).setOnClickListener(v -> onDecimal());

        // Operators
        findViewById(R.id.btnAdd).setOnClickListener(v -> onOperator(Evaluator.ADD));
        findViewById(R.id.btnSubtract).setOnClickListener(v -> onOperator(Evaluator.SUB));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> onOperator(Evaluator.MUL));
        findViewById(R.id.btnDivide).setOnClickListener(v -> onOperator(Evaluator.DIV));

        findViewById(R.id.btnEquals).setOnClickListener(v -> onEquals());
        findViewById(R.id.btnClear).setOnClickListener(v -> onClear());
        findViewById(R.id.btnBackspace).setOnClickListener(v -> onBackspace());

        updateDisplay();
    }

    // ---------- Button handlers ----------

    private void onDigit(String digit) {
        startFreshIfNeeded();
        if (expression.length() >= MAX_LENGTH) return;

        // Avoid numbers like "007": replace a lone leading zero
        String current = currentNumber();
        if (current.equals("0")) {
            expression.setLength(expression.length() - 1);
        }
        expression.append(digit);
        updateDisplay();
    }

    private void onDecimal() {
        startFreshIfNeeded();
        if (expression.length() >= MAX_LENGTH) return;

        String current = currentNumber();
        if (current.contains(".")) return;          // only one decimal point per number
        if (current.isEmpty()) expression.append('0'); // ".5" becomes "0.5"
        expression.append('.');
        updateDisplay();
    }

    private void onOperator(char op) {
        if (hasError) return;
        if (justEvaluated && expression.indexOf("E") >= 0) return; // huge result shown in E-notation

        if (expression.length() == 0) {
            // Only a leading minus makes sense on an empty expression
            if (op == Evaluator.SUB) {
                expression.append(op);
                updateDisplay();
            }
            return;
        }

        justEvaluated = false;  // continue calculating with the previous result
        tvExpression.setText("");

        char last = expression.charAt(expression.length() - 1);
        if (expression.length() == 1 && last == Evaluator.SUB) return; // lone "−"

        if (Evaluator.isOperator(last)) {
            // Replace the previous operator instead of stacking them
            expression.setCharAt(expression.length() - 1, op);
        } else {
            if (last == '.') expression.setLength(expression.length() - 1); // "5." -> "5"
            if (expression.length() >= MAX_LENGTH) return;
            expression.append(op);
        }
        updateDisplay();
    }

    private void onEquals() {
        if (hasError || expression.length() == 0) return;

        // Drop a dangling operator or decimal point, e.g. "5+" -> "5"
        char last = expression.charAt(expression.length() - 1);
        if (Evaluator.isOperator(last) || last == '.') {
            expression.setLength(expression.length() - 1);
        }
        if (expression.length() == 0) return;

        String shownExpression = expression.toString();
        try {
            double result = Evaluator.evaluate(shownExpression);
            String formatted = format(result);

            tvExpression.setText(shownExpression + " =");
            expression.setLength(0);
            expression.append(formatted);
            justEvaluated = true;
        } catch (ArithmeticException e) {
            showError(shownExpression);   // division by zero
        } catch (Exception e) {
            showError(shownExpression);   // anything unexpected must never crash the app
        }
        updateDisplay();
    }

    private void onClear() {
        expression.setLength(0);
        justEvaluated = false;
        hasError = false;
        tvExpression.setText("");
        updateDisplay();
    }

    private void onBackspace() {
        if (hasError) {
            onClear();
            return;
        }
        if (expression.length() > 0) {
            expression.setLength(expression.length() - 1);
        }
        justEvaluated = false;
        tvExpression.setText("");
        updateDisplay();
    }

    // ---------- Helpers ----------

    /** After "=" or an error, typing a number starts a brand-new calculation. */
    private void startFreshIfNeeded() {
        if (hasError || justEvaluated) {
            expression.setLength(0);
            hasError = false;
            justEvaluated = false;
            tvExpression.setText("");
        }
    }

    /** The number currently being typed (everything after the last operator). */
    private String currentNumber() {
        int i = expression.length() - 1;
        while (i >= 0 && !Evaluator.isOperator(expression.charAt(i))) {
            i--;
        }
        return expression.substring(i + 1);
    }

    private void showError(String failedExpression) {
        expression.setLength(0);
        hasError = true;
        justEvaluated = false;
        tvExpression.setText(failedExpression + " =");
    }

    private void updateDisplay() {
        if (hasError) {
            tvDisplay.setText(ERROR_TEXT);
        } else if (expression.length() == 0) {
            tvDisplay.setText(R.string.zero);
        } else {
            tvDisplay.setText(expression.toString());
        }
    }

    /** Formats a result: no trailing ".0", at most 10 decimals, scientific for huge values. */
    private String format(double value) {
        if (Math.abs(value) >= 1e15) {
            return String.format(java.util.Locale.US, "%.6E", value).replace('-', Evaluator.SUB);
        }
        BigDecimal bd = new BigDecimal(value).setScale(10, RoundingMode.HALF_UP).stripTrailingZeros();
        String text = bd.toPlainString();
        if (text.equals("-0")) text = "0";
        return text.replace('-', Evaluator.SUB);
    }
}
