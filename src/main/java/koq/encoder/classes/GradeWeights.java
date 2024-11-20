package koq.encoder.classes;

/**
 * OUTDATED
 */
public class GradeWeights {

    private int gradeWeightId;             // Primary key
    private int classId;                   // Foreign key to Classes table
    private double seatworkWeight;         // Weight for seatwork activities
    private double homeworkWeight;         // Weight for homework activities
    private double quizWeight;             // Weight for quizzes
    private double performanceTaskWeight;  // Weight for performance tasks
    private double examWeight;             // Weight for exams

    // Constructor
    public GradeWeights(int weightId, int classId, double seatworkWeight, double homeworkWeight, 
                        double quizWeight, double performanceTaskWeight, double examWeight) {
        this.gradeWeightId = weightId;
        this.classId = classId;
        this.seatworkWeight = seatworkWeight;
        this.homeworkWeight = homeworkWeight;
        this.quizWeight = quizWeight;
        this.performanceTaskWeight = performanceTaskWeight;
        this.examWeight = examWeight;
    }

    // Getters and Setters
    public int getGradeWeightId() {
        return gradeWeightId;
    }

    public void setGradeWeightId(int gradeWeightId) {
        this.gradeWeightId = gradeWeightId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public double getSeatworkWeight() {
        return seatworkWeight;
    }

    public void setSeatworkWeight(double seatworkWeight) {
        this.seatworkWeight = seatworkWeight;
    }

    public double getHomeworkWeight() {
        return homeworkWeight;
    }

    public void setHomeworkWeight(double homeworkWeight) {
        this.homeworkWeight = homeworkWeight;
    }

    public double getQuizWeight() {
        return quizWeight;
    }

    public void setQuizWeight(double quizWeight) {
        this.quizWeight = quizWeight;
    }

    public double getPerformanceTaskWeight() {
        return performanceTaskWeight;
    }

    public void setPerformanceTaskWeight(double performanceTaskWeight) {
        this.performanceTaskWeight = performanceTaskWeight;
    }

    public double getExamWeight() {
        return examWeight;
    }

    public void setExamWeight(double examWeight) {
        this.examWeight = examWeight;
    }

    // Method to validate that the weights add up to 100%
    public boolean isValidTotalWeight() {
        double total = seatworkWeight + homeworkWeight + quizWeight + performanceTaskWeight + examWeight;
        return Math.abs(total - 100.0) < 0.01;  // Allows for minor rounding discrepancies
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "GradeWeights{" +
                "weightId=" + gradeWeightId +
                ", classId=" + classId +
                ", seatworkWeight=" + seatworkWeight +
                ", homeworkWeight=" + homeworkWeight +
                ", quizWeight=" + quizWeight +
                ", performanceTaskWeight=" + performanceTaskWeight +
                ", examWeight=" + examWeight +
                '}';
    }
}