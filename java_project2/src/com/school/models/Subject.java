package com.school.models;

public class Subject {
    private String name;
    private int maxMarks;

    public Subject(String name, int maxMarks) {
        this.name = name;
        this.maxMarks = maxMarks;
    }

    public String getName() { return name; }
    public int getMaxMarks() { return maxMarks; }

    @Override
    public String toString() {
        return name + "," + maxMarks;
    }
}
