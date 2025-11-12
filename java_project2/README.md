# 🧮 Student Result Processing System (Swing Edition)

A Java Swing–based desktop application to manage students, subjects, and their exam results.  
It uses file handling to persist data even after the app is closed.

---

## 🗂️ Project Structure

Student_Result_Processing_System/
│
├── src/
│ ├── com/school/models/
│ │ ├── Student.java
│ │ └── Subject.java
│ │
│ ├── com/school/processor/
│ │ └── ResultProcessor.java
│ │
│ └── com/school/ui/
│ └── MainUI.java
│
├── student_data.txt
├── subject_data.txt
├── student_result.txt
└── README.md

yaml
Copy code

---

## 💡 Overview

The **Student Result Processing System** helps in:
- Adding new students
- Deleting students
- Entering or updating subject marks
- Viewing student performance reports
- Automatically saving and loading data from text files

All data persists using `.txt` files, so even after you exit and reopen the app, your data remains intact.

---

## ⚙️ How to Run

1. Open the project in **Eclipse** or **IntelliJ IDEA**.
2. Make sure all `.java` files are inside `src/com/school/...` packages.
3. Run the main class:
com.school.ui.MainUI

yaml
Copy code
4. The Swing window will appear.

---

## 📄 Text Files Used

| File Name | Purpose |
|------------|----------|
| **student_data.txt** | Stores basic student details (`id`, `name`, `age`). |
| **subject_data.txt** | Stores subject names and their max marks. |
| **student_result.txt** | Stores each student's marks per subject. |

---

## 🧰 Features

### 🧑‍🎓 Add Student
- Enter student ID, name, and age.
- Checks for duplicate IDs.
- Automatically saved in `student_data.txt`.

### ❌ Delete Student
- Deletes a student and all related results from all files.

### 🧾 Enter Marks
- Select a student by ID.
- Enter marks for each subject.
- If marks already exist, app shows:
> “Marks for this subject already exist. Do you want to update?”
- Updates or skips based on user choice.
- Marks are validated (0–100).

### 📊 View Report
- Enter student ID.
- Shows report with subject-wise marks and total.
- Fetches live data from `student_result.txt`.

### 💾 Persistent Data
- All files auto-load at startup and save after every operation.

---

## 🖥️ User Interface

### 🪟 Left Sidebar (Operations)
- ➕ Add Student  
- 🗑️ Delete Student  
- 🧾 Enter Marks  
- 📄 View Report  
- 🚪 Exit  

### 📋 Right Panel
- Displays messages, forms, and results dynamically.

---

## 🧱 Example Data

### `subject_data.txt`
Maths,100
Science,100
English,100

shell
Copy code

### `student_data.txt`
S101,Arnav,22
S102,Purav,23

shell
Copy code

### `student_result.txt`
S101,Maths,85
S101,Science,90
S101,English,78
S102,Maths,65
S102,Science,70
S102,English,80

yaml
Copy code

---

## 🧩 Developer Notes

- Built using **Java 17** and **Swing GUI**.
- No external database needed — simple text file handling.
- Uses OOP principles: encapsulation, file I/O, and modular packages.

---
