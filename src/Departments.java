public enum Departments {
    BIO("Bio", "Busch"),
    CHEM("Chem", "Livi"),
    CS("CS", "CAC"),
    ENG("Eng", "CD"),
    MATH("Math", "Busch"),
    PHYS("Phys", "Livi");

    private String name;
    private String campus;

    Departments(String name, String campus) {
        this.name = name;
        this.campus = campus;
    }

    public String getName() {
        return name;
    }

    public String getCampus() {
        return campus;
    }
}
