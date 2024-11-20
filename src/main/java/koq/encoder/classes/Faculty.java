package koq.encoder.classes;

public class Faculty {
    private int faculty_id;
    private int teacher_id;
    private String name;
    private String role;
    private char[] password;

    public Faculty(int faculty_id, int teacher_id, String name, String role, char[] password) {
        this.faculty_id = faculty_id;
        this.teacher_id = teacher_id;
        this.name = name;
        this.role = role;
        this.password = password;
    }

    public int getFacultyId() {
        return faculty_id;
    }

    public int getTeacherId() {
        return teacher_id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public char[] getPassword() {
        return password;
    }
}
