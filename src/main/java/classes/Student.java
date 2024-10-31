package classes;

public class Student {
    private int studentId;
    private String firstName;
    private String lastName;

    public Student(int studentId, String firstName, String lastName) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getStudentId() { return studentId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getStudentName() { 
        return firstName + " " + lastName;
    }
}