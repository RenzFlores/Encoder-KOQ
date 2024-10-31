package classes;

public class Grade {
    private int gradeId;     
    private int studentId;    
    private int classId;       
    private int activityId;   
    private Double grade;
    private double maxGrade;

    /**
     * 
     * @param gradeId
     * @param studentId
     * @param classId
     * @param activityId
     * @param grade 
     */
    public Grade(int gradeId, int studentId, int classId, int activityId, Double grade, double maxGrade) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.classId = classId;
        this.activityId = activityId;
        this.grade = grade;
        this.maxGrade = maxGrade;
    }
    
    // Constructor for a new Grade instance (no assigned gradeId yet)
    public Grade(int studentId, int classId, int activityId, Double grade, double maxGrade) {
        this.studentId = studentId;
        this.classId = classId;
        this.activityId = activityId;
        this.grade = grade;
        this.maxGrade = maxGrade;
        
        this.gradeId = 999;     // TODO: Change this later
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

    public Double getGrade() {
        return grade;
    }

    public void setGrade(Double grade) {
        this.grade = grade;
    }
    
    public double getMaxGrade() {
        return maxGrade;
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