package com.school.models;

public class GraduatingStudent extends Student {
    private String graduationYear;

    public GraduatingStudent(String id, String name, int age, String graduationYear) {
        super(id, name, age);
        this.graduationYear = graduationYear;
    }

    public String getGraduationYear() { return graduationYear; }

    @Override
    public String toString() {
        return super.toString() + "," + graduationYear;
    }
}
