import java.util.*; 

public class Student {
    private String fname;
    private String lname;
    private int id;
    private ArrayList<String> majors;
    private ArrayList<String> minors;
    private ArrayList<Classes> classesTaking;
    private ArrayList<ClassTaken> classesTaken; 

    public Student() {
        fname = "";
        lname = "";
        id = 0;
        majors = new ArrayList<String>();
        minors = new ArrayList<String>();
        classesTaken = new ArrayList<ClassTaken>();
        classesTaking = new ArrayList<Classes>();
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

}
