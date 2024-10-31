package koq.encoder.mvc;

public class Activity {
    private int activityId;
    private String activityName;
    private double maxGrade;
    private int activityTypeId;
    private int term;

    public Activity(int activityId, String activityName, int activityTypeId, double maxGrade, int term) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.maxGrade = maxGrade;
        this.activityTypeId = activityTypeId;
        this.term = term;
    }
    
    // Constructor for new activities (No Activity ID yet)
    public Activity(String activityName, int activityTypeId, double maxGrade, int term) {
        this.activityId = activityId;
        this.activityName = activityName;
        this.maxGrade = maxGrade;
        this.activityTypeId = activityTypeId;
        this.term = term;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public double getMaxGrade() {
        return maxGrade;
    }

    public void setMaxGrade(double maxGrade) {
        this.maxGrade = maxGrade;
    }

    public int getActivityTypeId() {
        return activityTypeId;
    }
    
    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }
    
    @Override
    public String toString() {
        return "Activity{" +
                "activityId=" + activityId +
                ", activityName='" + activityName + '\'' +
                ", maxGrade=" + maxGrade +
                ", activityTypeId=" + activityTypeId +
                ", term=" + term +
                '}';
    }
}