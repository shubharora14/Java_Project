package com.school.processor;

import com.school.models.*;
import java.io.*;
import java.util.*;

public class ResultProcessor {
    private List<Student> students = new ArrayList<>();
    private List<Subject> subjects = new ArrayList<>();
    private Map<String, Map<String, Integer>> studentResults = new HashMap<>();

    private final String STUDENT_FILE = "student_data.txt";
    private final String SUBJECT_FILE = "subject_data.txt";
    private final String RESULT_FILE = "student_result.txt";

    public ResultProcessor() {
        ensureFilesExist();
        loadAll();
    }

    // Create empty files if missing
    private void ensureFilesExist() {
        try {
            new File(STUDENT_FILE).createNewFile();
            new File(SUBJECT_FILE).createNewFile();
            new File(RESULT_FILE).createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ======== LOAD ALL ========
    public void loadAll() {
        loadStudents();
        loadSubjects();
        loadResults();
    }

    private void loadStudents() {
        students.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length == 3)
                    students.add(new Student(p[0], p[1], Integer.parseInt(p[2])));
            }
        } catch (Exception ignored) {}
    }

    private void loadSubjects() {
        subjects.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(SUBJECT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length == 2)
                    subjects.add(new Subject(p[0], Integer.parseInt(p[1])));
            }
        } catch (Exception ignored) {}
    }

    private void loadResults() {
        studentResults.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(RESULT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = line.split(",");
                if (p.length == 3) {
                    String sid = p[0], sub = p[1];
                    int marks = Integer.parseInt(p[2]);
                    studentResults.computeIfAbsent(sid, k -> new HashMap<>()).put(sub, marks);
                }
            }
        } catch (Exception ignored) {}
    }

    private void saveStudents() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (Student s : students)
                pw.println(s);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void saveResults() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RESULT_FILE))) {
            for (String sid : studentResults.keySet()) {
                for (Map.Entry<String, Integer> entry : studentResults.get(sid).entrySet()) {
                    pw.println(sid + "," + entry.getKey() + "," + entry.getValue());
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ======== CORE ========
    public boolean addStudent(Student s) {
        for (Student st : students)
            if (st.getId().equals(s.getId())) return false;
        students.add(s);
        saveStudents();
        return true;
    }

    public boolean deleteStudent(String id) {
        boolean removed = students.removeIf(s -> s.getId().equals(id));
        if (removed) saveStudents();
        return removed;
    }

    public List<Student> getStudents() { return students; }
    public List<Subject> getSubjects() { return subjects; }

    public boolean addOrUpdateMarks(String sid, String sub, int marks) {
        if (!studentResults.containsKey(sid))
            studentResults.put(sid, new HashMap<>());
        studentResults.get(sid).put(sub, marks);
        saveResults();
        return true;
    }

    public Map<String, Integer> getStudentMarks(String sid) {
        return studentResults.getOrDefault(sid, new HashMap<>());
    }

    public String generateReport(String sid) {
        Student stu = students.stream().filter(s -> s.getId().equals(sid)).findFirst().orElse(null);
        if (stu == null) return "❌ Student not found!";
        Map<String, Integer> marks = getStudentMarks(sid);
        if (marks.isEmpty()) return "⚠️ No marks found for this student.";

        StringBuilder sb = new StringBuilder();
        sb.append("===== REPORT =====\n");
        sb.append("Name: ").append(stu.getName()).append(" (ID: ").append(stu.getId()).append(")\n\n");

        int total = 0;
        for (Subject sub : subjects) {
            int m = marks.getOrDefault(sub.getName(), 0);
            total += m;
            sb.append(sub.getName()).append(" : ").append(m)
              .append("/").append(sub.getMaxMarks()).append("\n");
        }
        double avg = total / (double) subjects.size();
        sb.append("\nTotal: ").append(total)
          .append("\nAverage: ").append(String.format("%.2f", avg))
          .append("\nGrade: ").append(getGrade(avg));

        return sb.toString();
    }

    private String getGrade(double avg) {
        if (avg >= 90) return "A";
        else if (avg >= 75) return "B";
        else if (avg >= 60) return "C";
        else if (avg >= 40) return "D";
        else return "F";
    }
}
