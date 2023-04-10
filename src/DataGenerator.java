import java.util.*;

public class DataGenerator {
    private static final int STARTING_ID = 100000000;
    private static final String[] FIRST_NAMES = {"Ethan", "Olivia", "Liam", "Ava", "Noah", "Emma", "Sophia", "Logan", "Mia", "Jackson", "Charlotte", "Lucas", "Amelia", "Avery", "Harper", "Mason", "Evelyn", "Ella", "Aria", "Benjamin"};
    private static final String[] LAST_NAMES = {"Garcia", "Smith", "Rodriguez", "Kim", "Lee", "Hernandez", "Nguyen", "Jones", "Chen", "Gonzalez", "Patel", "Gupta", "Singh", "Ali", "Ahmed", "Yilmaz", "Avila", "Perez", "Wang", "Chan"};
    private static final String[] MAJORS = {"Bio", "Chem", "CS", "Eng", "Math", "Phys"};
    private static final String[] MINORS = {"Bio", "Chem", "CS", "Eng", "Math", "Phys"};

    public static ArrayList<Student> generateRandStudents() {
        ArrayList<Student> randomData = new ArrayList<Student>();
        Random rand = new Random();

        for (int i = 0; i < 100; i++) {
            Student s = new Student();

            int id = STARTING_ID + i;
            s.setID(id);

            String firstName = FIRST_NAMES[rand.nextInt(FIRST_NAMES.length)];
            s.setFName(firstName);

            String lastName = LAST_NAMES[rand.nextInt(LAST_NAMES.length)];
            s.setLName(lastName);

            int numMajors = rand.nextInt(2) + 1; // one or two majors
            for (int j = 0; j < numMajors; j++) {
                String major = MAJORS[rand.nextInt(MAJORS.length)];
                while(s.getMajors().contains(major)) {
                    major = MAJORS[rand.nextInt(MAJORS.length)];
                }
                s.addMajor(major); 
            }

            int numMinors = rand.nextInt(3); // zero to two minors
            for (int j = 0; j < numMinors; j++) {
                String minor = MINORS[rand.nextInt(MINORS.length)];
                while(s.getMinors().contains(minor) || s.getMajors().contains(minor)) {
                    minor = MINORS[rand.nextInt(MINORS.length)];
                }
                s.addMinor(minor); 
            }

            int year = rand.nextInt(4) + 1; // freshman to senior
            int numCredits = 0; 
            switch (year) {
                case 1:
                    // 0 - 29 credits
                    numCredits = rand.nextInt(30);
                    break;
                case 2:
                    // 30 - 59 credits
                    numCredits = rand.nextInt(30) + 30;
                    break;
                case 3:
                    // 60 - 89 credits
                    numCredits = rand.nextInt(30) + 60;
                    break;
                case 4:
                    // 90+ credits
                    numCredits = rand.nextInt(30) + 90;
                    break;
            }

            int numClassesTaking = rand.nextInt(5) + 1; // 1 - 5 classes
            EnumSet<Classes> classes = EnumSet.allOf(Classes.class);

            while(numCredits > 0 && !classes.isEmpty()) {
                Classes randomClass = (Classes) classes.toArray()[rand.nextInt(classes.size())];

                if (numClassesTaking > 0) {
                    s.addClassTaking(randomClass);
                    numClassesTaking--;
                } else {
                    s.addClassTaken(randomClass);
                    numCredits -= randomClass.getCredits();
                }
                classes.remove(randomClass);
            }

            s.calcGPA();
            randomData.add(s);
        }
        
        return randomData;
    }

}

