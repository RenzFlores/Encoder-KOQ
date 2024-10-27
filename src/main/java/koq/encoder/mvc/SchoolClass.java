package koq.encoder.mvc;

import java.util.List;

public class SchoolClass {
    private int classId;
    private String section;
    private String subject;
    private List<Student> students;

    public SchoolClass(int classId, String section, String subject,  List<Student> students) {
        this.classId = classId;
        this.section = section;
        this.subject = subject;
        this.students = students;
    }
    
    // Constructor for a new SchoolClass object with no prior data
    /*
    public SchoolClass() {
        classId = ;
        section = ;
        subject = ;
        students = ;
    }
    */

    public int getClassId() { return classId; }
    public String getSection() { return section; }
    public String getSubject() { return subject; }
    public List<Student> getStudents() { return students; }

    public void setStudents(List<Student> students) { this.students = students; }
}