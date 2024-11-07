package classes;

public class Student {
    private int studentId;
    private String firstName;
    private String lastName;

    // Default constructor for existing student
    public Student(int studentId, String firstName, String lastName) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Constructor for new student (No student_id yet)
    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        
    }

    public int getStudentId() { 
        return studentId; 
    }
    public String getFirstName() { 
        return firstName; 
    }
    public String getLastName() { 
        return lastName; 
    }
    public String getStudentName() { 
        return firstName + " " + lastName;
    }
}