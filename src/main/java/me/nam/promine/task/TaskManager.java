package me.nam.promine.task;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages task creation, execution, and tracking.
 */
public class TaskManager {
    private Map<String, Task> tasks;

    public TaskManager() {
        this.tasks = new HashMap<>();
    }

    public Task createTask(String name) {
        // TODO: Implement task creation with unique ID
        return new Task("temp_id", name);
    }

    public Task getTask(String taskId) {
        return tasks.get(taskId);
    }

    public void executeTask(String taskId) {
        // TODO: Implement task execution
    }

    public void cancelTask(String taskId) {
        // TODO: Implement task cancellation
    }
}
