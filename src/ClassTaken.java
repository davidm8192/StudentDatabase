import java.util.Random;

public class ClassTaken {
    private Classes classTaken;
    private char grade;
    private final Random rand = new Random();

    public ClassTaken(Classes classTaken) {
        this.classTaken = classTaken;
        generateGrade();
    }

    public Classes getClassTaken() {
        return classTaken;
    }

    public char getGrade() {
        return grade;
    }

    public void generateGrade() {
        int g = rand.nextInt(5); // 0 - 4
        switch (g) {
            case 4:
                grade = 'A';
                break;
            case 3:
                grade = 'B';
                break;
            case 2:
                grade = 'C';
                break;
            case 1:
                grade = 'D';
                break;
            case 0:
                grade = 'F';
                break;
        }
    }

    public String toString() {
        return classTaken.toString() + " | Grade: " + grade;
    }
}
