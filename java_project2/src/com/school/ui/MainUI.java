package com.school.ui;

import com.school.models.*;
import com.school.processor.ResultProcessor;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class MainUI extends JFrame {
    private ResultProcessor processor = new ResultProcessor();
    private JPanel mainContent;

    public MainUI() {
        setTitle("Student Result Processing System");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ============ LEFT SIDEBAR ============
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridLayout(5, 1, 10, 10));
        sidePanel.setBackground(new Color(235, 235, 235));

        JButton addBtn = new JButton("➕ Add Student");
        JButton delBtn = new JButton("🗑️ Delete Student");
        JButton marksBtn = new JButton("🧾 Enter Marks");
        JButton viewBtn = new JButton("📄 View Report");
        JButton exitBtn = new JButton("🚪 Exit");

        sidePanel.add(addBtn);
        sidePanel.add(delBtn);
        sidePanel.add(marksBtn);
        sidePanel.add(viewBtn);
        sidePanel.add(exitBtn);
        add(sidePanel, BorderLayout.WEST);

        // ============ RIGHT PANEL ============
        mainContent = new JPanel();
        mainContent.setLayout(new BorderLayout());
        add(mainContent, BorderLayout.CENTER);

        JLabel welcome = new JLabel("Welcome to Student Result Processing System", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 18));
        mainContent.add(welcome, BorderLayout.CENTER);

        // ============ ACTIONS ============
        addBtn.addActionListener(e -> showAddStudent());
        delBtn.addActionListener(e -> showDeleteStudent());
        marksBtn.addActionListener(e -> showEnterMarks());
        viewBtn.addActionListener(e -> showViewReport());
        exitBtn.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "💾 Data saved successfully. Exiting...");
            System.exit(0);
        });

        setVisible(true);
    }

    // ============ ADD STUDENT ============
    private void showAddStudent() {
        JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField id = new JTextField();
        JTextField name = new JTextField();
        JTextField age = new JTextField();

        p.add(new JLabel("Student ID:"));
        p.add(id);
        p.add(new JLabel("Name:"));
        p.add(name);
        p.add(new JLabel("Age:"));
        p.add(age);

        int opt = JOptionPane.showConfirmDialog(this, p, "Add New Student", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                Student s = new Student(id.getText().trim(), name.getText().trim(), Integer.parseInt(age.getText().trim()));
                boolean added = processor.addStudent(s);
                JOptionPane.showMessageDialog(this,
                        added ? "✅ Student added successfully!" : "⚠️ Student ID already exists!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Invalid input!");
            }
        }
    }

    // ============ DELETE STUDENT ============
    private void showDeleteStudent() {
        String id = JOptionPane.showInputDialog(this, "Enter Student ID to delete:");
        if (id == null || id.trim().isEmpty()) return;

        boolean removed = processor.deleteStudent(id.trim());
        JOptionPane.showMessageDialog(this,
                removed ? "🗑️ Student deleted successfully!" : "❌ Student not found!");
    }

    // ============ ENTER MARKS ============
    private void showEnterMarks() {
        String sid = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (sid == null || sid.trim().isEmpty()) return;

        Student st = processor.getStudents().stream()
                .filter(s -> s.getId().equals(sid.trim()))
                .findFirst().orElse(null);

        if (st == null) {
            JOptionPane.showMessageDialog(this, "❌ Student not found!");
            return;
        }

        Map<String, Integer> existingMarks = processor.getStudentMarks(sid.trim());
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        Map<String, JTextField> fields = new HashMap<>();

        for (Subject sub : processor.getSubjects()) {
            String subjectName = sub.getName();
            JLabel label = new JLabel(subjectName + " (" + sub.getMaxMarks() + "):");
            JTextField field = new JTextField(existingMarks.containsKey(subjectName)
                    ? String.valueOf(existingMarks.get(subjectName))
                    : "");

            fields.put(subjectName, field);
            panel.add(label);
            panel.add(field);
        }

        int opt = JOptionPane.showConfirmDialog(this, panel,
                "Enter Marks for " + st.getName(), JOptionPane.OK_CANCEL_OPTION);

        if (opt == JOptionPane.OK_OPTION) {
            for (String sub : fields.keySet()) {
                String valStr = fields.get(sub).getText().trim();
                if (valStr.isEmpty()) continue;

                try {
                    int val = Integer.parseInt(valStr);
                    if (val < 0 || val > 100) {
                        JOptionPane.showMessageDialog(this, "⚠️ Marks for " + sub + " must be between 0–100!");
                        continue;
                    }

                    if (existingMarks.containsKey(sub)) {
                        int confirm = JOptionPane.showConfirmDialog(this,
                                "Marks for '" + sub + "' already exist (" + existingMarks.get(sub)
                                        + ").\nDo you want to update them?",
                                "Update Confirmation", JOptionPane.YES_NO_OPTION);
                        if (confirm != JOptionPane.YES_OPTION) continue;
                    }

                    processor.addOrUpdateMarks(sid.trim(), sub, val);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "❌ Invalid number for " + sub);
                }
            }
            JOptionPane.showMessageDialog(this, "✅ Marks updated successfully!");
        }
    }

    // ============ VIEW REPORT ============
    private void showViewReport() {
        String sid = JOptionPane.showInputDialog(this, "Enter Student ID to view report:");
        if (sid == null || sid.trim().isEmpty()) return;

        String report = processor.generateReport(sid.trim());
        JTextArea area = new JTextArea(report);
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        JScrollPane sp = new JScrollPane(area);

        JOptionPane.showMessageDialog(this, sp, "Student Report", JOptionPane.INFORMATION_MESSAGE);
    }

    // ============ MAIN ============
    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }
}
