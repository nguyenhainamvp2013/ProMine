package me.nam.promine.task;

/**
 * Represents a task that can be executed by ProMine.
 */
public class Task {
    private String id;
    private String name;
    private TaskStatus status;

    public enum TaskStatus {
        PENDING, RUNNING, COMPLETED, FAILED, CANCELLED
    }

    public Task(String id, String name) {
        this.id = id;
        this.name = name;
        this.status = TaskStatus.PENDING;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
