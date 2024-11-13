package koq.encoder.classes;

public class ActivityType {
    private int activityTypeId;
    private String typeName;

    public ActivityType(int activityTypeId, String typeName) {
        this.activityTypeId = activityTypeId;
        this.typeName = typeName;
    }
    
    public int getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(int activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "ActivityType{" +
                "activityTypeId=" + activityTypeId +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}