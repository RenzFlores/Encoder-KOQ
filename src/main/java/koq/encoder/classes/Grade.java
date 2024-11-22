package koq.encoder.classes;

public class Grade {
    private int gradeId;     
    private int studentId;
    private int classId;       
    private int activityId;   
    private Integer grade;
    private int totalScore;

    public Grade(int gradeId, int studentId, int classId, int activityId, Integer grade, int totalScore) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.classId = classId;
        this.activityId = activityId;
        this.grade = grade;
        this.totalScore = totalScore;
    }
    
    // Constructor for a new Grade instance (no assigned gradeId yet)
    public Grade(int studentId, int classId, int activityId, Integer grade, int totalScore) {
        this.studentId = studentId;
        this.classId = classId;
        this.activityId = activityId;
        this.grade = grade;
        this.totalScore = totalScore;
    }

    // Getter and setter methods
    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }
    
    public int getTotalScore() {
        return totalScore;
    }

    // Method to display grade information as a string
    @Override
    public String toString() {
        return "Grade{" +
                "gradeId=" + gradeId +
                ", studentId=" + studentId +
                ", classId=" + classId +
                ", activityId=" + activityId +
                ", grade=" + grade +
                '}';
    }
}