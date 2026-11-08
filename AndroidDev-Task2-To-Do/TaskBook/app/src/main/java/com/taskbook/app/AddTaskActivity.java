package com.taskbook.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/** Full-screen form for adding a task (name + optional notes) for the logged-in user. */
public class AddTaskActivity extends AppCompatActivity {

    private TextInputLayout tilTaskName;
    private TextInputEditText etTaskName, etTaskNotes;
    private DatabaseHelper db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
        if (!session.isLoggedIn()) {   // safety: never add tasks without a user
            finish();
            return;
        }

        setContentView(R.layout.activity_add_task);
        db = new DatabaseHelper(this);

        tilTaskName = findViewById(R.id.tilTaskName);
        etTaskName = findViewById(R.id.etTaskName);
        etTaskNotes = findViewById(R.id.etTaskNotes);

        Button btnSave = findViewById(R.id.btnSaveTask);
        Button btnCancel = findViewById(R.id.btnCancelTask);

        btnSave.setOnClickListener(v -> saveTask());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void saveTask() {
        tilTaskName.setError(null);

        String title = etTaskName.getText() == null ? "" : etTaskName.getText().toString().trim();
        String notes = etTaskNotes.getText() == null ? "" : etTaskNotes.getText().toString().trim();

        if (title.isEmpty()) {
            tilTaskName.setError("Task name can't be empty");
            return;
        }

        long id = db.addTask(session.getUserId(), title, notes);
        if (id == -1) {
            Toast.makeText(this, "Couldn't save the task. Please try again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show();
        finish();   // MainActivity reloads the list in onResume()
    }
}
