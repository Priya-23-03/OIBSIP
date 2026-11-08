package com.taskbook.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout tilEmail, tilPassword;
    private TextInputEditText etEmail, etPassword;
    private DatabaseHelper db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
        // Already logged in? Skip straight to the task list.
        if (session.isLoggedIn()) {
            openMain();
            return;
        }

        setContentView(R.layout.activity_login);
        db = new DatabaseHelper(this);

        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnGoSignUp = findViewById(R.id.btnGoSignUp);

        btnLogin.setOnClickListener(v -> attemptLogin());
        btnGoSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }

    private void attemptLogin() {
        tilEmail.setError(null);
        tilPassword.setError(null);

        String email = text(etEmail).toLowerCase();
        String password = text(etPassword);

        boolean valid = true;
        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("Enter a valid email address");
            valid = false;
        }
        if (TextUtils.isEmpty(password)) {
            tilPassword.setError("Enter your password");
            valid = false;
        }
        if (!valid) return;

        DatabaseHelper.User user = db.loginUser(email, password);
        if (user == null) {
            tilPassword.setError("Incorrect email or password");
            return;
        }

        session.saveSession(user.id, user.name);
        Toast.makeText(this, "Welcome back, " + user.name + "!", Toast.LENGTH_SHORT).show();
        openMain();
    }

    private void openMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private String text(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }
}
