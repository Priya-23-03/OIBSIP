package com.taskbook.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.Listener {

    private DatabaseHelper db;
    private SessionManager session;
    private TaskAdapter adapter;

    private RecyclerView recyclerTasks;
    private View emptyState;
    private TextView tvSummary;

    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new SessionManager(this);
        // Not logged in? Back to the login screen.
        if (!session.isLoggedIn()) {
            goToLogin();
            return;
        }

        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        userId = session.getUserId();

        TextView tvGreeting = findViewById(R.id.tvGreeting);
        tvSummary = findViewById(R.id.tvSummary);
        emptyState = findViewById(R.id.emptyState);
        recyclerTasks = findViewById(R.id.recyclerTasks);
        Button btnLogout = findViewById(R.id.btnLogout);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        tvGreeting.setText("Hi, " + session.getUserName() + " 👋");

        adapter = new TaskAdapter(this);
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> openAddTask());
        btnLogout.setOnClickListener(v -> logout());

        loadTasks();
    }

    // ---------- Loading and showing tasks ----------

    private void loadTasks() {
        List<Task> tasks = db.getTasks(userId);
        adapter.submit(tasks);

        boolean empty = tasks.isEmpty();
        emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerTasks.setVisibility(empty ? View.GONE : View.VISIBLE);

        int pending = 0;
        for (Task t : tasks) {
            if (!t.isCompleted()) pending++;
        }
        if (empty) {
            tvSummary.setText("Nothing to do yet");
        } else {
            tvSummary.setText(pending + " pending · " + (tasks.size() - pending) + " done");
        }
    }

    // ---------- Add task (opens its own screen) ----------

    private void openAddTask() {
        startActivity(new Intent(this, AddTaskActivity.class));
    }

    /** Refresh the list whenever we come back, e.g. after adding a task. */
    @Override
    protected void onResume() {
        super.onResume();
        if (db != null) {
            loadTasks();
        }
    }

    // ---------- TaskAdapter.Listener ----------

    @Override
    public void onToggle(Task task, boolean completed) {
        db.setTaskCompleted(userId, task.getId(), completed);
        // Reload after the current layout pass so the list can't be modified mid-bind
        recyclerTasks.post(this::loadTasks);
    }

    @Override
    public void onDelete(Task task) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Delete task?")
                .setMessage("\"" + task.getTitle() + "\" will be permanently deleted.")
                .setPositiveButton("Delete", (d, which) -> {
                    db.deleteTask(userId, task.getId());
                    loadTasks();
                    Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // ---------- Logout ----------

    private void logout() {
        session.clear();
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
        goToLogin();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
