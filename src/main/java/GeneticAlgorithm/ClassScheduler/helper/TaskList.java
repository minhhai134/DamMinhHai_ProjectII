package GeneticAlgorithm.ClassScheduler.helper;

public class TaskList {

    private int taskListID;

    private int taskID;

    private int ResourceID;

    private int duration;

    private int startTime;

    private int endTime;


    public TaskList(int taskListID ,int taskID, int duration) {
        this.taskListID = taskListID;
        this.taskID = taskID;
        this.duration = duration;
        endTime = startTime+duration;
    }

    public void setResourceID(int resourceID) {
        ResourceID = resourceID;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getTaskID() {
        return taskID;
    }

    public int getResourceID() {
        return ResourceID;
    }

    public int getDuration() {
        return duration;
    }

    public int getStartTime() {
        return startTime;
    }

    public int getTaskListID() {
        return taskListID;
    }

    public int getEndTime() {
        return endTime;
    }
}
