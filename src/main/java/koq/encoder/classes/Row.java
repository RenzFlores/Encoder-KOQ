package koq.encoder.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing ClassRecord row data
 * Contains Student object and a list of Activity objects
 */
public class Row {
    private Student student;
    private List<Grade> gradesList;

    public Row(Student student, ArrayList<Grade> gradesList) {
        this.student = student;
        this.gradesList = gradesList;
    }
    
    public Student getStudent() {
        return student;
    }    
    public List<Grade> getGrades() {
        return gradesList;
    }
    
    public void setGrades(List<Grade> gradesList) {
        this.gradesList = gradesList;
    }
    public void setGradesAt(int index, Integer value) {
        if (!(gradesList.get(index) == null)) {
            gradesList.get(index).setGrade(value);
        }
    }
}