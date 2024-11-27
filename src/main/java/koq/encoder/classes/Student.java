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
    private String email;
    private char[] password;

    // Default constructor for existing student with email and password
    public Student(int studentId, String firstName, String middleName, String lastName, int lrn, String gender, String dateOfBirth, String strand, String email, char[] password) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.lrn = lrn;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.strand = strand;
        this.email = email;
        this.password = password;
    }
    
    // Constructor for student with no email and password
    public Student(int studentId, String firstName, String middleName, String lastName, int lrn, String gender, String dateOfBirth, String strand) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.lrn = lrn;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.strand = strand;
        this.email = null;
        this.password = null;
    }
    
    // Constructor for new student (No student_id yet)
    public Student(String firstName, String middleName, String lastName, int lrn, String gender, String dateOfBirth, String strand, String email, char[] password) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.lrn = lrn;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.strand = strand;
        this.email = email;
        this.password = password;
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
    public String getDateOfBirthFormatted() { 
        String[] dob = dateOfBirth.split("-");  // YYYY-MM-DD
        
        return dob[1] + "/" + dob[2] + "/" + dob[0];  // MM/DD/YYYY
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
    public String getStudentFullName() { 
        if (middleName != null) {
            return lastName + ", " + firstName + " " + middleName;
        } else {
            return lastName + ", " + firstName;
        }
    }
}