public enum Classes {
    // Bio courses
    BIO101("Intro to Biology", 3),
    BIO201("Genetics", 3),
    BIO301("Ecology", 4),
    BIO401("Cell Biology", 4),
    BIO501("Immunology", 4),
    BIO601("Biochemistry", 4),

    // Chem courses
    CHEM101("Intro to Chemistry", 3),
    CHEM201("Organic Chemistry", 4),
    CHEM301("Analytical Chemistry", 4),
    CHEM401("Inorganic Chemistry", 4),
    CHEM501("Physical Chemistry", 4),
    CHEM601("Romantic Chemistry", 3),

    // CS courses
    CS101("Intro to Computer Science", 3),
    CS201("Data Structures", 4),
    CS301("Algorithms", 4),
    CS401("Programming Languages", 3),
    CS501("Artificial Intelligence", 4),
    CS601("Computer Networks", 4),

    // Eng courses
    ENG101("Composition and Rhetoric", 3),
    ENG201("British Literature", 4),
    ENG301("American Literature", 4),
    ENG401("Shakespeare", 3),
    ENG501("Creative Writing", 3),
    ENG601("Modern Poetry", 3),

    // Math courses
    MATH101("Pre-Algebra", 3),
    MATH201("Calculus I", 4),
    MATH301("Calculus II", 4),
    MATH401("Linear Algebra", 4),
    MATH501("Differential Equations", 4),
    MATH601("Advanced Calculus", 4),

    // Phys courses
    PHYS101("Intro to Physics", 3),
    PHYS201("Mechanics", 3),
    PHYS301("Electricity and Magnetism", 3),
    PHYS401("Quantum Mechanics", 4),
    PHYS501("Relativity", 4),
    PHYS601("Astrophysics", 4),
    
    // Misc courses
    ABC101("Intro to ABC", 3),
    ABC201("Advanced ABC", 4),
    ABC301("No more ABC", 4),
    XYZ101("Intro to Dumpster Diving", 4), 
    QWE201("Typing with Toes", 3),
    QWE981("Typing with Nose", 4),
    BNM890("Intro to Competitive Eating", 3);

    private final String name;
    private final int credits;

    Classes(String name, int credits) {
        this.name = name;
        this.credits = credits;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

}
