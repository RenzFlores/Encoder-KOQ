package koq.encoder.classes;

public class Student {
    private int studentId;
    private String firstName;
    private String middleName;
    private String lastName;
    private int lrn;
    private String gender;
    private String dateOfBirth;
    private String strand;

    // Default constructor for existing student
    public Student(int studentId, String firstName, String middleName, String lastName, int lrn, String gender, String dateOfBirth, String strand) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.lrn = lrn;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.strand = strand;
    }
    
    // Constructor for new student (No student_id yet)
    public Student(String firstName, String middleName, String lastName, int lrn, String gender, String dateOfBirth, String strand) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.lrn = lrn;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.strand = strand;
        
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
    public int getLrn() { 
        return lrn; 
    }
    public String getGender() { 
        return gender; 
    }
    public String getGenderInitial() { 
        return String.valueOf(gender.charAt(0));
    }
    public String getDateOfBirth() { 
        return dateOfBirth; 
    }
    public String getStrand() { 
        return strand; 
    }
    public String getStudentName() { 
        return firstName + " " + lastName;
    }
    public String getStudentNameFormatted() { 
        if (middleName != null) {
            return lastName + ", " + firstName + " " + middleName.charAt(0) + ".";
        } else {
            return lastName + ", " + firstName;
        }
    }
}