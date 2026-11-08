# ✅ TaskBook — To-Do App with Login (Android)

A secure task manager for Android. Users register and log in before they can see their own personal to-do list, and all data is stored locally in SQLite.

Built as **Task 2** of the **Android App Development Internship** at **Oasis Infobyte**.

## 📸 Screenshots

| Login | Sign up | Empty state |
|:---:|:---:|:---:|
| <img src="screenshots/login.png" width="200" alt="Login"> | <img src="screenshots/signup.png" width="200" alt="Sign up"> | <img src="screenshots/empty.png" width="200" alt="Empty state"> |

| Add task | Task list | Completed task |
|:---:|:---:|:---:|
| <img src="screenshots/add_task.png" width="200" alt="Add task screen"> | <img src="screenshots/tasks.png" width="200" alt="Task list"> | <img src="screenshots/completed.png" width="200" alt="Completed task with strikethrough"> |

## ✨ Features

- **Sign-up screen:** name, email, password, and a Register button, with validation and a check for duplicate emails
- **Login screen:** email and password with a Login button and clear error messages
- **Secure passwords:** hashed with **SHA-256 plus a random salt per user**, so plain-text passwords are never stored
- **Session handling:** the app remembers who is logged in; **Logout** clears the session and returns to the login screen
- **Personal task list:** each user only sees their own tasks (tasks are linked to the account in SQLite)
- **Add Task** screen with a task name and optional notes
- **Mark as complete:** completed tasks get a strikethrough and move to the bottom of the list
- **Delete permanently:** with a confirmation dialog
- **Empty state:** a friendly message when there are no tasks
- Navy blue theme and a custom adaptive launcher icon

## 🛠️ Tech Stack

- Android Studio
- Java
- XML (layouts, drawables, styles)
- SQLite (`SQLiteOpenHelper`)
- RecyclerView, Material Components

**Min SDK:** 21 (Android 5.0) | **Target SDK:** 34

## 🗄️ Database Design

```
users (id, name, email UNIQUE, password_hash, salt)
tasks (id, user_id → users.id, title, notes, completed, created_at)
```

Every task query includes the logged-in `user_id`, so one account can never read, change, or delete another account's tasks.

## 📂 Project Structure

```
app/src/main/java/com/taskbook/app/
├── LoginActivity.java      # Login screen, skips ahead if already logged in
├── SignUpActivity.java     # Registration with validation
├── MainActivity.java       # Task list, complete, delete, logout
├── AddTaskActivity.java    # Add Task screen (name + notes)
├── TaskAdapter.java        # RecyclerView adapter for task rows
├── DatabaseHelper.java     # SQLiteOpenHelper: users and tasks tables
├── PasswordUtil.java       # Salted SHA-256 password hashing
├── SessionManager.java     # Stores and clears the logged-in user
└── Task.java               # Task model
```

## 🚀 How to Run

1. Clone the repository
   ```
   git clone https://github.com/<your-username>/<repo-name>.git
   ```
2. Open the project folder in **Android Studio** (File → Open).
3. Wait for the Gradle sync to finish.
4. Run on an emulator or a physical device.

## 🔐 How Passwords Are Protected

1. On sign-up, a random 16-byte salt is generated for the user.
2. The app stores `SHA-256(salt + password)` together with the salt, never the password itself.
3. On login, the same calculation is repeated and compared with the stored hash.

## 👩‍💻 Author

**Priyadharshini R S**
Android App Development Intern, Oasis Infobyte
