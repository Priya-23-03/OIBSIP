package com.taskbook.app;

public class Task {
    private final long id;
    private final String title;
    private final String notes;
    private final boolean completed;

    public Task(long id, String title, String notes, boolean completed) {
        this.id = id;
        this.title = title;
        this.notes = notes;
        this.completed = completed;
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getNotes() { return notes; }
    public boolean isCompleted() { return completed; }
}
