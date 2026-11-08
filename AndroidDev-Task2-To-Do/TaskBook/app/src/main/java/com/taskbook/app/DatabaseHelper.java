package com.taskbook.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * SQLite storage for users and their tasks.
 * Every task row carries a user_id, so each account only ever sees its own tasks.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "taskbook.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String TABLE_TASKS = "tasks";

    /** The logged-in user returned by a successful login. */
    public static class User {
        public final long id;
        public final String name;

        User(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    public DatabaseHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "email TEXT NOT NULL UNIQUE, "
                + "password_hash TEXT NOT NULL, "
                + "salt TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_TASKS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL, "
                + "title TEXT NOT NULL, "
                + "notes TEXT, "
                + "completed INTEGER NOT NULL DEFAULT 0, "
                + "created_at INTEGER NOT NULL, "
                + "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // ---------------- Users ----------------

    public boolean emailExists(String email) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{"id"}, "email = ?",
                new String[]{email}, null, null, null);
        try {
            return c.moveToFirst();
        } finally {
            c.close();
        }
    }

    /** @return true if the account was created, false if the email is already taken. */
    public boolean registerUser(String name, String email, String password) {
        if (emailExists(email)) return false;

        String salt = PasswordUtil.generateSalt();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("salt", salt);
        values.put("password_hash", PasswordUtil.hash(password, salt));

        return getWritableDatabase().insert(TABLE_USERS, null, values) != -1;
    }

    /** @return the user if the email and password match, otherwise null. */
    public User loginUser(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS,
                new String[]{"id", "name", "password_hash", "salt"},
                "email = ?", new String[]{email}, null, null, null);
        try {
            if (c.moveToFirst()) {
                String hash = c.getString(c.getColumnIndexOrThrow("password_hash"));
                String salt = c.getString(c.getColumnIndexOrThrow("salt"));
                if (PasswordUtil.matches(password, salt, hash)) {
                    return new User(
                            c.getLong(c.getColumnIndexOrThrow("id")),
                            c.getString(c.getColumnIndexOrThrow("name")));
                }
            }
            return null;
        } finally {
            c.close();
        }
    }

    // ---------------- Tasks (always scoped to a user) ----------------

    public long addTask(long userId, String title, String notes) {
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("title", title);
        values.put("notes", notes);
        values.put("completed", 0);
        values.put("created_at", System.currentTimeMillis());
        return getWritableDatabase().insert(TABLE_TASKS, null, values);
    }

    /** Pending tasks first (newest on top), completed tasks moved to the bottom. */
    public List<Task> getTasks(long userId) {
        List<Task> tasks = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_TASKS,
                new String[]{"id", "title", "notes", "completed"},
                "user_id = ?", new String[]{String.valueOf(userId)},
                null, null, "completed ASC, created_at DESC");
        try {
            while (c.moveToNext()) {
                tasks.add(new Task(
                        c.getLong(c.getColumnIndexOrThrow("id")),
                        c.getString(c.getColumnIndexOrThrow("title")),
                        c.getString(c.getColumnIndexOrThrow("notes")),
                        c.getInt(c.getColumnIndexOrThrow("completed")) == 1));
            }
        } finally {
            c.close();
        }
        return tasks;
    }

    public void setTaskCompleted(long userId, long taskId, boolean completed) {
        ContentValues values = new ContentValues();
        values.put("completed", completed ? 1 : 0);
        getWritableDatabase().update(TABLE_TASKS, values, "id = ? AND user_id = ?",
                new String[]{String.valueOf(taskId), String.valueOf(userId)});
    }

    public void deleteTask(long userId, long taskId) {
        getWritableDatabase().delete(TABLE_TASKS, "id = ? AND user_id = ?",
                new String[]{String.valueOf(taskId), String.valueOf(userId)});
    }
}
