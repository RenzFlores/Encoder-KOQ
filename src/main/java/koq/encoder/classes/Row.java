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
    private Double computedGrades;

    public Row(Student student, ArrayList<Grade> gradesList, Double computedGrades) {
        this.student = student;
        this.gradesList = gradesList;
        this.computedGrades = computedGrades;
    }
    
    public Student getStudent() {
        return student;
    }    
    public List<Grade> getGrades() {
        return gradesList;
    }
    public Double getComputedGrades() {
        return computedGrades;
    }
    
    public void setGrades(List<Grade> gradesList) {
        this.gradesList = gradesList;
    }
    public void setGradesAt(int index, Double value) {
        if (!(gradesList.get(index) == null)) {
            gradesList.get(index).setGrade(value);
        }
    }
    
    public void setComputedGrades(Double value) {
        this.computedGrades = value;
    }
}