# Simple LMS

A simple **Learning Management System** made with Java Swing for the
CSIT227 Object Oriented Programming capstone.

MEMBERS:
CALABROSO, JAUSH MAR D.
GEROGALIN, JOVITO V.
GOTINGCO, RYAN REY A.
MALINAO, JEKAIAH LI HOWARD R.

## What it does

| User    | Can do |
|---------|--------|
| Admin   | Add users (student, teacher or admin), remove users |
| Teacher | Create assignments, read students' answers, give scores and feedback |
| Student | See assignments, submit an answer, see their score and feedback |

## How to run

1. Open this folder in IntelliJ IDEA.
2. Run `main` in `src/LMSApp.java`.
3. Log in with one of the sample accounts (password `1234` for all):

| User ID   | Role    |
|-----------|---------|
| `admin`   | Admin   |
| `teacher` | Teacher |
| `student` | Student |

**Demo flow:** log in as `teacher` and create an assignment → log out, log in as
`student` and submit an answer → log in as `teacher` and grade it → log in as
`student` and see the score and feedback.

Data is saved to `lms_data.dat`. Delete that file to start over with the sample data.

## Classes

| Class | What it is |
|-------|------------|
| `User` | Abstract parent class of all users (ID, name, password) |
| `Student`, `Teacher`, `Admin` | The three kinds of users |
| `Assignment` | Title, course, instructions, max score, and its submissions |
| `Submission` | A student's answer, score and feedback |
| `LMSException` | Our own exception for errors |
| `FileManager` | Holds all data and saves/loads it to a file (Singleton) |
| `LMSApp` | The main window |
| `LoginPanel`, `StudentPanel`, `TeacherPanel`, `AdminPanel` | The screens |

The class diagram is in [docs/class-diagram.md](docs/class-diagram.md).

## Four OOP Principles

**Abstraction** — `User` is an abstract class. It has two abstract methods,
`getRole()` and `createDashboard()`, which have no code in `User`. Each child
class writes its own version.

**Encapsulation** — all fields are `private`. For example, `User`'s password can't
be read by other classes; they can only ask `checkPassword()` if it matches.

**Inheritance** — `Student`, `Teacher` and `Admin` extend `User`, so they get the
ID, name and password without writing them again. `LMSException` extends
`Exception`, and the screens extend `JPanel`.

**Polymorphism** — after logging in, `LMSApp.showDashboard(user)` just calls
`user.createDashboard(this)`. A `Student` returns a `StudentPanel`, a `Teacher`
returns a `TeacherPanel`, and an `Admin` returns an `AdminPanel`. No `if`
statements are needed to check the type of user.

## Exception Handling

- **Creating:** `LMSException` is our own exception class (`extends Exception`).
- **Throwing:** we throw it when something is wrong, for example:
  - empty ID/name/password (`User` constructor)
  - wrong ID or password (`FileManager.login()`)
  - ID already used (`FileManager.addUser()`)
  - score higher than the max score (`Submission.grade()`)
  - submitting the same assignment twice (`Assignment.addSubmission()`)
- **Catching:** the screens catch it with `try / catch` and show the message in a
  pop-up (for example `LoginPanel.login()`).

## File Handling

`FileManager` saves the list of users and the list of assignments to
`lms_data.dat` using `ObjectOutputStream`, and reads them back with
`ObjectInputStream`. Submissions are inside the assignments, so they are saved too.

- `load()` runs when the program starts. The first time (no file yet), it adds
  the sample accounts.
- `save()` runs after every change, when logging out, and when closing the window.

## Design Pattern: Singleton

`FileManager` is a **Singleton**: there is only one `FileManager` object in the
whole program.

```java
private static FileManager instance;

private FileManager() { }          // nobody else can use "new FileManager()"

public static FileManager getInstance() {
    if (instance == null) {
        instance = new FileManager();
    }
    return instance;
}
```

**Where:** every screen calls `FileManager.getInstance()` (login, adding users,
creating assignments, saving).

**Why:** all screens must use the same data. If each screen made its own
`FileManager`, a user added by the admin would not appear when someone else logs in.
