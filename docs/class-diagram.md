# Simple LMS — UML Class Diagram

GitHub draws this diagram automatically. To get an image, paste the code into
<https://mermaid.live> and use **Actions → PNG**.

`+` public, `-` private, `*` abstract method, `$` static, underlined = static.
Plain getters are left out to keep the diagram short.

```mermaid
classDiagram
    direction TB

    class User {
        <<abstract>>
        -id: String
        -name: String
        -password: String
        +User(id: String, name: String, password: String)
        +checkPassword(password: String) boolean
        +getRole()* String
        +createDashboard(app: LMSApp)* JPanel
    }
    class Student {
        +getRole() String
        +createDashboard(app: LMSApp) JPanel
    }
    class Teacher {
        +getRole() String
        +createDashboard(app: LMSApp) JPanel
    }
    class Admin {
        +getRole() String
        +createDashboard(app: LMSApp) JPanel
    }

    class Assignment {
        -title: String
        -course: String
        -instructions: String
        -maxScore: int
        -submissions: ArrayList~Submission~
        +addSubmission(submission: Submission) void
        +getSubmissionOf(student: Student) Submission
    }
    class Submission {
        -student: Student
        -answer: String
        -score: int
        -feedback: String
        -graded: boolean
        +grade(score: int, maxScore: int, feedback: String) void
    }

    class FileManager {
        <<Singleton>>
        -instance: FileManager$
        -FILE_NAME: String$
        -users: ArrayList~User~
        -assignments: ArrayList~Assignment~
        -FileManager()
        +getInstance()$ FileManager
        +save() void
        +load() void
        +login(id: String, password: String) User
        +addUser(user: User) void
        +removeUser(user: User) void
        +addAssignment(assignment: Assignment) void
    }

    class LMSException {
        +LMSException(message: String)
    }

    class LMSApp {
        +showLogin() void
        +showDashboard(user: User) void
        +logout() void
        +saveData() void
        +createHeader(user: User) JPanel
        +main(args: String[])$ void
    }
    class LoginPanel
    class StudentPanel
    class TeacherPanel
    class AdminPanel

    User <|-- Student
    User <|-- Teacher
    User <|-- Admin
    Exception <|-- LMSException
    JFrame <|-- LMSApp
    JPanel <|-- LoginPanel
    JPanel <|-- StudentPanel
    JPanel <|-- TeacherPanel
    JPanel <|-- AdminPanel

    Assignment "1" *-- "0..*" Submission : receives
    Student "1" -- "0..*" Submission : makes
    FileManager "1" o-- "0..*" User : stores
    FileManager "1" o-- "0..*" Assignment : stores

    Student ..> StudentPanel : creates
    Teacher ..> TeacherPanel : creates
    Admin ..> AdminPanel : creates
    LMSApp ..> FileManager : uses
    FileManager ..> LMSException : throws
```
