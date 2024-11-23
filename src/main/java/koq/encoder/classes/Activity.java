package koq.encoder.classes;

public class Activity {
    private int activityId;
    private int classId;
    private String name;
    private double totalScore;
    private int activityTypeId;
    private int quarter;

    public Activity(int activityId, int classId, String name, double totalScore, int activityTypeId, int quarter) {
        this.activityId = activityId;
        this.classId = classId;
        this.name = name;
        this.totalScore = totalScore;
        this.activityTypeId = activityTypeId;
        this.quarter = quarter;
    }
    
    // Constructor for new activities (No Activity ID yet)
    public Activity(int classId, String name, double totalScore, int activityTypeId, int quarter) {
        this.classId = classId;
        this.name = name;
        this.totalScore = totalScore;
        this.activityTypeId = activityTypeId;
        this.quarter = quarter;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    public int getActivityTypeId() {
        return activityTypeId;
    }
    
    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }
    
    @Override
    public String toString() {
        return "Activity{" +
                "activityId=" + activityId +
                ", activityName='" + name + '\'' +
                ", maxGrade=" + totalScore +
                ", activityTypeId=" + activityTypeId +
                ", term=" + quarter +
                '}';
    }
}