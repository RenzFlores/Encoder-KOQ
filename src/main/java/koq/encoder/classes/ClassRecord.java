package koq.encoder.classes;

public class ClassRecord {
    
    // Class Record Metadata
    private int classId;
    private int facultyId;
    private int subjectId;
    private int gradeLevel;
    private String section;
    private Integer semester;
    private String schoolYear;
    
    private GradePeriod gradePeriodQ1;
    private GradePeriod gradePeriodQ2;
    private GradeSheet gradeSheetQ1;
    private GradeSheet gradeSheetQ2;
    private FinalGradeSheet finalGradeSheet; 
    
    public ClassRecord(int classId, int facultyId, int subjectId, int gradeLevel, String section, int semester, String schoolYear) {
        this.classId = classId;
        this.facultyId = facultyId;
        this.subjectId = subjectId;
        this.gradeLevel = gradeLevel;
        this.section = section;
        this.semester = semester;
        this.schoolYear = schoolYear;
    }
    
    // Setters (important!)
    public void setData(
            GradePeriod gradePeriodQ1, 
            GradePeriod gradePeriodQ2, 
            GradeSheet gradeSheetQ1, 
            GradeSheet gradeSheetQ2,
            FinalGradeSheet finalGradeSheet) {
        this.gradePeriodQ1 = gradePeriodQ1;
        this.gradePeriodQ2 = gradePeriodQ2;
        this.gradeSheetQ1 = gradeSheetQ1;
        this.gradeSheetQ2 = gradeSheetQ2;
        this.finalGradeSheet = finalGradeSheet;
    }
    
    // Getters
    public GradePeriod getGradePeriod(int quarter) {
        switch (quarter) {
            case 1:
                return gradePeriodQ1;
            default:
                return gradePeriodQ2;
        }
    }
    
    public GradeSheet getGradeSheet(int quarter) {
        switch (quarter) {
            case 1:
                return gradeSheetQ1;
            default:
                return gradeSheetQ2;
        }
    }
    
    public FinalGradeSheet getFinalGradeSheet() {
        return finalGradeSheet;
    }
    
    public int getClassId() {
        return classId;
    }
    public int getGradeLevel() {
        return gradeLevel;
    }
    public String getSection() {
        return section;
    }
    public int getTerm() {
        return semester;
    }
    public String getSY() {
        return schoolYear;
    }
    public String getFormattedTerm() {
        switch(semester) {
            case 1:
                return "1st Quarter";
            case 2:
                return "2nd Quarter";
            case 3:
                return "3rd Quarter";
            case 4:
                return "4th Quarter";
            default:
                return "Invalid";
        }
    }
    
    public String toString() {
        return String.format("%d | %s | %s | %s", 
                getGradeLevel(),
                getSection(),
                getFormattedTerm(),
                getSY()
        );
    }
}
