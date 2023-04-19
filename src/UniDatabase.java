import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.sql.ResultSetMetaData; 
import java.util.*;

public class UniDatabase {

    static String DATABASE_URL = "";
    static String USER = "";
    static String PASSWORD = "";
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) throws Exception {
        // Store commandline info
        DATABASE_URL = "jdbc:mysql://" + args[0];
        USER = args[1];
        PASSWORD = args[2];

        // Create database with tables and 100 random students
        createDatabase(); 

        // Create views to help
        createViews();
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the university database! Here are the available queries:" + 
        "\n1. Search students by name." + 
        "\n2. Search students by year." +
        "\n3. Search for students with a GPA >= threshold." +
        "\n4. Search for students with a GPA <= threshold." + 
        "\n5. Get department statistics." + 
        "\n6. Get class statistics." + 
        "\n7. Execute an abitrary SQL query." + 
        "\n8. Exit the application."); 
        boolean run = true; 

        while (run) {
            System.out.println("Which query would you like to run (1-8)?");
            int query = sc.nextInt();

            switch (query) {
                case 1:
                    System.out.print("Please enter the name: ");
                    String name = sc.next();
                    searchByName(name.toLowerCase());
                    break;
                case 2:
                    System.out.print("Please enter the year: ");
                    String year = sc.next();
                    searchByYear(year.toLowerCase());
                    break;
                case 3:
                    System.out.print("Please enter the threshold: ");
                    String threshold = sc.next();
                    searchByGPA(threshold, true);
                    break;
                case 4:
                    System.out.print("Please enter the threshold: ");
                    String threshold2 = sc.next();
                    searchByGPA(threshold2, false);
                    break;
                case 5:
                    System.out.print("Please enter the department: ");
                    String dept = sc.next();
                    getDeptStats(dept);
                    break;
                case 6:
                    System.out.print("Please enter the class: ");
                    String className = sc.next();
                    getClassStats(className);
                    break;
                case 7:
                    executeQuery();
                    break;
                case 8:
                    System.out.println("Goodbye!");
                    run = false;
                    break;
                default:
                    System.out.println("Invalid query. Please try again.");
                    break;
            }
        }
        sc.close(); 
    }


    private static void executeQuery() throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            System.out.println("Please enter the query: ");
            Scanner sc = new Scanner(System.in);
            String query = sc.nextLine();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            boolean printColumns = true; 
            while (rs.next()) {
                if(printColumns) {
                    for(int i = 1; i <= columnsNumber; i++) {
                        if (i > 1) System.out.print("\t");
                        System.out.print(rsmd.getColumnName(i));
                        if(i == columnsNumber) {
                            printColumns = false; 
                            System.out.println();
                        }
                    }
                }
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print("\t");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue);
                }
                System.out.println();
            }

        } 
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void getClassStats(String className) throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            String s1 = "SELECT (SELECT COUNT(*) FROM IsTaking WHERE name = ?) AS numStudents, " + 
            "(SELECT COUNT(*) FROM HasTaken WHERE name = ? AND grade = 'A') AS numA, " +
            "(SELECT COUNT(*) FROM HasTaken WHERE name = ? AND grade = 'B') AS numB, " +
            "(SELECT COUNT(*) FROM HasTaken WHERE name = ? AND grade = 'C') AS numC, " +
            "(SELECT COUNT(*) FROM HasTaken WHERE name = ? AND grade = 'D') as numD, " +
            "(SELECT COUNT(*) FROM HasTaken WHERE name = ? AND grade = 'F') as numF;";

            PreparedStatement ps = conn.prepareStatement(s1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, className);
            ps.setString(2, className);
            ps.setString(3, className);
            ps.setString(4, className);
            ps.setString(5, className);
            ps.setString(6, className);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                System.out.println("Number of students: " + rs.getInt("numStudents") + "\nGrades of previous enrollees: " + 
                "\nA: " + rs.getInt("numA") + "\nB: " + rs.getInt("numB") + "\nC: " + rs.getInt("numC") + "\nD: " + rs.getInt("numD") + "\nF: " + rs.getInt("numF"));
            }
        } 
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void getDeptStats(String dept) throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            String s1 = "SELECT AVG(GPA) AS avgGPA, COUNT(*) AS numStudents FROM StudentGPA WHERE id IN (SELECT ma.sid FROM Majors ma WHERE ma.dname = ? UNION SELECT mi.sid FROM Minors mi WHERE mi.dname = ?);";
            PreparedStatement ps = conn.prepareStatement(s1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, dept);
            ps.setString(2, dept);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                System.out.println("Number of students: " + rs.getInt("numStudents") + "\nAverage GPA: " + df.format(rs.getDouble("avgGPA")));
            }
        } 
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void searchByGPA(String threshold, boolean above) throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            String s1 = "";
            if (above) {
                s1 = "SELECT * FROM StudentGPA WHERE GPA >= ?;";
            }
            else {
                s1 = "SELECT * FROM StudentGPA WHERE GPA <= ?;";
            }
            PreparedStatement ps = conn.prepareStatement(s1, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            try {
                Double.parseDouble(threshold);
            }
            catch (Exception e) {
                System.out.println("Invalid threshold. Please try again.");
                return;
            }
            ps.setDouble(1, Double.parseDouble(threshold));
            ResultSet rs = ps.executeQuery();
            System.out.println(getResults(rs));
        } 
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private static void searchByYear(String year) throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            try {
                stmt.executeUpdate("CREATE VIEW ValidClasses AS SELECT sid, name, grade FROM HasTaken WHERE grade <> 'F';");
            } catch (SQLException e) {}

            String s = "SELECT Students.* From Students WHERE Students.id IN (SELECT id FROM ((ValidClasses INNER JOIN Classes ON ValidClasses.name = Classes.name) INNER JOIN Students ON ValidClasses.sid = Students.id) GROUP BY ValidClasses.sid HAVING sum(credits) >= ? AND sum(credits) <= ?);";
            PreparedStatement ps = conn.prepareStatement(s, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            switch(year) {
                case "fr":
                    ps.setInt(1, 0);
                    ps.setInt(2, 29);
                    break;
                case "so":
                    ps.setInt(1, 30);
                    ps.setInt(2, 59);
                    break;
                case "jr":
                    ps.setInt(1, 60);
                    ps.setInt(2, 89);
                    break;
                case "sr":
                    ps.setInt(1, 90);
                    ps.setInt(2, 999);
                    break;
                default:
                    System.out.println("Invalid year. Please try again.");
                    return;
            }
            ResultSet rs = ps.executeQuery();
            System.out.println(getResults(rs));

        } 
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void searchByName(String name) throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            String s = "SELECT * FROM Students WHERE LOWER(first_name) LIKE ? OR LOWER(last_name) LIKE ?;";
            PreparedStatement ps = conn.prepareStatement(s, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, "%" + name + "%");
            ps.setString(2, "%" + name + "%");
            ResultSet rs = ps.executeQuery();
            System.out.println(getResults(rs));
        } 

        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static String getResults(ResultSet rs) throws ClassNotFoundException {        
        String result = ""; 
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);        

            int studentCount = 0; 
            if (rs.last()) {
                studentCount = rs.getRow();
                rs.beforeFirst(); 
            }
            result += studentCount + " students found.\n";

            while(rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                result += lastName + ", " + firstName + "\nID: " + id + "\n";
                
                ArrayList<String> majors = new ArrayList<String>();
                String getMajors = "SELECT * FROM Majors WHERE sid = ?";
                PreparedStatement tps = conn.prepareStatement(getMajors, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                tps.setInt(1, id);
                ResultSet trs = tps.executeQuery();

                int majorCount = 1; 
                if (trs.last()) {
                    majorCount = trs.getRow();
                    trs.beforeFirst(); 
                }
                if(majorCount > 1) {
                    result += "Majors: ";
                }
                else {
                    result += "Major: ";
                }
                while (trs.next()) {
                    if(trs.isLast()) {
                        result += trs.getString("dname");
                    }
                    else {
                        result += trs.getString("dname") + ", ";
                    }
                }
                trs.close(); 

                String getMinors = "SELECT * FROM Minors WHERE sid = ?";
                PreparedStatement mps = conn.prepareStatement(getMinors, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                mps.setInt(1, id);
                ResultSet mrs = mps.executeQuery();

                int minorCount = 0;
                if (mrs.last()) {
                    minorCount = mrs.getRow();
                    mrs.beforeFirst(); 
                }
                if(minorCount > 1) {
                    result += "\nMinors: ";
                }
                else if (minorCount == 1) {
                    result += "\nMinor: ";
                }
                while (mrs.next()) {
                    if(mrs.isLast()) {
                        result += mrs.getString("dname");
                    }
                    else {
                        result += mrs.getString("dname") + ", ";
                    }
                }
                mrs.close(); 

                result += "\nGPA: " + df.format(getGPA(id)) + "\nCredits: " + getCredits(id) + "\n\n";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result; 
    }

    private static int getCredits(int id) throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            String s = "SELECT sum(credits) FROM Classes INNER JOIN ValidClasses on Classes.name = ValidClasses.name WHERE sid = ?;";
            PreparedStatement ps = conn.prepareStatement(s);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            int credits = 0; 
            while(rs.next()) {
                credits = rs.getInt(1);
            }
            return credits; 
        } 
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    private static double getGPA(int id) throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            double GPA = 0; 

            String s = "SELECT GPA FROM StudentGPA WHERE id = ?;";
            PreparedStatement ps = conn.prepareStatement(s);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                GPA = rs.getDouble("GPA");
            }

            return GPA;
        } 
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    private static void createViews() throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            
            try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate("CREATE VIEW ValidClasses AS SELECT sid, name, grade FROM HasTaken WHERE grade <> 'F';");
            } catch (SQLException e) {}

            try {
                Statement stmt2 = conn.createStatement();
                String s = "CREATE VIEW StudentGPA AS SELECT s.first_name, s.last_name, s.id, " + 
                "SUM(CASE WHEN h.grade = 'A' THEN 4 * c.credits " +
                "WHEN h.grade = 'B' THEN 3 * c.credits " + 
                "WHEN h.grade = 'C' THEN 2 * c.credits " + 
                "WHEN h.grade = 'D' THEN 1 * c.credits " + 
                "ELSE 0 END) / SUM(c.credits) AS GPA " + 
                "FROM Students s INNER JOIN HasTaken h ON s.id = h.sid INNER JOIN Classes c ON h.name = c.name " +
                "GROUP BY s.first_name, s.last_name, s.id;";
                stmt2.executeUpdate(s);
            } catch (SQLException e) {}

            

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createDatabase() throws ClassNotFoundException {
        try {
            // Connect to the database
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            // Create tables
            String deptTable = "CREATE TABLE Departments (name VARCHAR(50) PRIMARY KEY, campus VARCHAR(50));";
            String studentTable = "CREATE TABLE Students (first_name VARCHAR(50), last_name VARCHAR(50), id INT PRIMARY KEY);";
            String classTable = "CREATE TABLE Classes (name VARCHAR(50) PRIMARY KEY, credits INT);";
            String majorsTable = "CREATE TABLE Majors (sid INT, dname VARCHAR(50), PRIMARY KEY(sid, dname), FOREIGN KEY(sid) REFERENCES Students(id), FOREIGN KEY(dname) REFERENCES Departments(name));";
            String minorsTable = "CREATE TABLE Minors (sid INT, dname VARCHAR(50), PRIMARY KEY(sid, dname), FOREIGN KEY(sid) REFERENCES Students(id), FOREIGN KEY(dname) REFERENCES Departments(name));";
            String isTakingTable = "CREATE TABLE IsTaking (sid INT, name VARCHAR(50), PRIMARY KEY(sid, name), FOREIGN KEY(sid) REFERENCES Students(id), FOREIGN KEY(name) REFERENCES Classes(name));";
            String hasTakenTable = "CREATE TABLE HasTaken (sid INT, name VARCHAR(50), grade VARCHAR(1), PRIMARY KEY(sid, name), FOREIGN KEY(sid) REFERENCES Students(id), FOREIGN KEY (name) REFERENCES Classes(name));";
            
            try {
                stmt.executeUpdate(deptTable);
                stmt.executeUpdate(studentTable);
                stmt.executeUpdate(classTable);
                stmt.executeUpdate(majorsTable);
                stmt.executeUpdate(minorsTable);
                stmt.executeUpdate(hasTakenTable);
                stmt.executeUpdate(isTakingTable);
            } catch (SQLException e) {}

            try {
                // Insert data into departments table
                String insertDepartment = "INSERT INTO Departments(name, campus) VALUES (?, ?);";
                PreparedStatement pstmt = conn.prepareStatement(insertDepartment);
                for(Departments dept : Departments.values()) {
                    pstmt.setString(1, dept.getName());
                    pstmt.setString(2, dept.getCampus());
                    pstmt.executeUpdate();
                }
                // Insert data into classes table
                String insertClass = "INSERT INTO Classes(name, credits) VALUES (?, ?);";
                pstmt = conn.prepareStatement(insertClass);
                for(Classes c : Classes.values()) {
                    pstmt.setString(1, c.getName());
                    pstmt.setInt(2, c.getCredits());
                    pstmt.executeUpdate();
                }

                // Generate 100 random students and insert them into the database
                ArrayList<Student> students = DataGenerator.generateRandStudents();

                String insertStudent = "INSERT INTO Students(first_name, last_name, id) VALUES (?, ?, ?);";
                String insertMajor = "INSERT INTO Majors(sid, dname) VALUES (?, ?);";
                String insertMinor = "INSERT INTO Minors(sid, dname) VALUES (?, ?);";
                String insertIsTaking = "INSERT INTO IsTaking(sid, name) VALUES (?, ?);";
                String insertHasTaken = "INSERT INTO HasTaken(sid, name, grade) VALUES (?, ?, ?);";
                pstmt = conn.prepareStatement(insertStudent);
                PreparedStatement pstmt2 = conn.prepareStatement(insertMajor);
                PreparedStatement pstmt3 = conn.prepareStatement(insertMinor);
                PreparedStatement pstmt4 = conn.prepareStatement(insertIsTaking);
                PreparedStatement pstmt5 = conn.prepareStatement(insertHasTaken);
                
                for(Student s : students) {
                    // Insert student into Students table
                    pstmt.setString(1, s.getFName());
                    pstmt.setString(2, s.getLName());
                    pstmt.setInt(3, s.getID());
                    pstmt.executeUpdate();

                    // Insert student's majors into Majors table
                    for(String major : s.getMajors()) {
                        pstmt2.setInt(1, s.getID());
                        pstmt2.setString(2, major);
                        pstmt2.executeUpdate();
                    }

                    // Insert student's minors into Minors table
                    for(String minor : s.getMinors()) {
                        pstmt3.setInt(1, s.getID());
                        pstmt3.setString(2, minor);
                        pstmt3.executeUpdate();
                    }

                    // Insert student's current classes into IsTaking table
                    for(Classes c : s.getClassesTaking()) {
                        pstmt4.setInt(1, s.getID());
                        pstmt4.setString(2, c.getName());
                        pstmt4.executeUpdate();
                    }

                    // Insert student's past classes into HasTaken table
                    for(ClassTaken c : s.getClassesTaken()) {
                        pstmt5.setInt(1, s.getID());
                        pstmt5.setString(2, c.getClassTaken().getName());
                        pstmt5.setString(3, String.valueOf(c.getGrade()));
                        pstmt5.executeUpdate();
                    }
                }
            } catch (SQLException e) {}
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
