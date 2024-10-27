/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package koq.encoder.mvc;

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