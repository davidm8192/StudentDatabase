import java.text.DecimalFormat;
import java.util.*; 

public class Student {
    private String fname;
    private String lname;
    private int id;
    private ArrayList<String> majors;
    private ArrayList<String> minors;
    private ArrayList<Classes> classesTaking;
    private ArrayList<ClassTaken> classesTaken; 
    private double GPA;
    private int credits;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public Student() {
        fname = "";
        lname = "";
        id = 0;
        majors = new ArrayList<String>();
        minors = new ArrayList<String>();
        classesTaken = new ArrayList<ClassTaken>();
        classesTaking = new ArrayList<Classes>();
        GPA = 0;
        credits = 0;
    }

    public void setFName(String fname) {
        this.fname = fname;
    }

    public void setLName(String lname) {
        this.lname = lname;
    }

    public void setID(int id) {
        this.id = id;
    }

    public void addMajor(String major) {
        majors.add(major);
    }

    public void addMinor(String minor) {
        minors.add(minor);
    }

    public void addClassTaken(Classes classTaken) {
        ClassTaken c = new ClassTaken(classTaken);
        classesTaken.add(c);
    }

    public void addClassTaking(Classes classTaking) {
        classesTaking.add(classTaking);
    }

    public void calcGPA() {
        double totalCredits = 0;
        double totalPoints = 0;
        for (ClassTaken c : classesTaken) {
            int grade = 0; 
            switch (c.getGrade()) {
                case 'A':
                    grade = 4;
                    break;
                case 'B':
                    grade = 3;
                    break;
                case 'C':
                    grade = 2;
                    break;
                case 'D':
                    grade = 1;
                    break;
                case 'F':
                    grade = 0;
                    break;
            }

            totalCredits += c.getClassTaken().getCredits();
            totalPoints += c.getClassTaken().getCredits() * grade;
        }
        credits = (int) totalCredits; 
        try {
            GPA = totalPoints / totalCredits;
        }
        catch(Exception e) {
            GPA = 0; 
        }
    }

    public String getFName() {
        return fname;
    }

    public String getLName() {
        return lname;
    }

    public int getID() {
        return id;
    }

    public ArrayList<String> getMajors() {
        return majors;
    }

    public ArrayList<String> getMinors() {
        return minors;
    }

    public ArrayList<ClassTaken> getClassesTaken() {
        return classesTaken;
    }

    public ArrayList<Classes> getClassesTaking() {
        return classesTaking;
    }

    public int getCredits() {
        return credits; 
    }

    public double getGPA() {
        return GPA;
    }

    public String toString() {
        return "\nStudent: " + fname + " " + lname + " " + id + "\nMajors: " + majors + " Minors: " + minors + "\nClasses taken: " + classesTaken + "\nClasses taking: " + classesTaking + "\nCredits: " + credits + " GPA: " + df.format(GPA);
    }

}
