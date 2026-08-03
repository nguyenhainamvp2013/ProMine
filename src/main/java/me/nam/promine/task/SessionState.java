package me.nam.promine.task;

import me.nam.promine.mining.MiningPlan;

import java.util.ArrayList;
import java.util.List;

/**
 * Enumeration of possible session states.
 */
public enum SessionState {
    IDLE("Idle - waiting for command"),
    SCANNING("Scanning inventory"),
    PLANNING("Creating mining plans"),
    MINING("Mining blocks"),
    RETURNING("Returning to base"),
    DEPOSITING("Depositing items"),
    RESTOCKING("Restocking supplies"),
    RESUMING("Resuming previous session"),
    STOPPED("Session stopped");

    private String description;

    SessionState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
