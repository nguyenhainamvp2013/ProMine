package me.nam.promine.task;

import me.nam.promine.inventory.InventoryAnalyzer;
import me.nam.promine.inventory.InventorySnapshot;
import me.nam.promine.inventory.SampleDetector;
import me.nam.promine.inventory.SampleItem;
import me.nam.promine.mining.MiningPlan;
import me.nam.promine.mining.MiningPlanner;
import net.minecraft.entity.player.PlayerInventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Manages session state transitions and orchestrates the mining automation workflow.
 * No actual movement or inventory manipulation - only state management and planning.
 */
public class SessionManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("promine");

    private MiningSession currentSession;
    private InventoryAnalyzer inventoryAnalyzer;
    private SampleDetector sampleDetector;
    private MiningPlanner miningPlanner;
    private boolean isRunning;

    public SessionManager() {
        this.inventoryAnalyzer = new InventoryAnalyzer();
        this.sampleDetector = new SampleDetector();
        this.miningPlanner = new MiningPlanner();
        this.isRunning = false;
    }

    /**
     * Start the mining automation workflow.
     * Transitions: IDLE -> SCANNING -> PLANNING -> MINING
     */
    public void startSession(PlayerInventory playerInventory) {
        LOGGER.info("[ProMine] ========== SESSION START ==========");
        
        // Create new session
        String sessionId = UUID.randomUUID().toString().substring(0, 8);
        currentSession = new MiningSession(sessionId);
        isRunning = true;

        // Transition to SCANNING
        transitionTo(SessionState.SCANNING, playerInventory);
    }

    /**
     * Stop the current session.
     */
    public void stopSession() {
        if (currentSession == null) {
            LOGGER.warn("[ProMine] No active session to stop");
            return;
        }

        transitionTo(SessionState.STOPPED, null);
        isRunning = false;
        
        LOGGER.info("[ProMine] ========== SESSION STOPPED ==========");
        LOGGER.info("[ProMine] Session: {}", currentSession);
        LOGGER.info("[ProMine] Statistics: {}", currentSession.getStatistics());
    }

    /**
     * Transition to a new state and execute appropriate logic.
     */
    private void transitionTo(SessionState nextState, PlayerInventory playerInventory) {
        if (currentSession == null) {
            LOGGER.error("[ProMine] No active session for state transition");
            return;
        }

        SessionState previousState = currentSession.getCurrentState();
        currentSession.setCurrentState(nextState);

        LOGGER.info("[ProMine] STATE TRANSITION: {} -> {}", previousState, nextState);
        LOGGER.info("[ProMine] {}", nextState.getDescription());

        // Execute state-specific logic
        switch (nextState) {
            case SCANNING:
                handleScanning(playerInventory);
                break;
            case PLANNING:
                handlePlanning();
                break;
            case MINING:
                handleMining();
                break;
            case RETURNING:
                handleReturning();
                break;
            case DEPOSITING:
                handleDepositing();
                break;
            case RESTOCKING:
                handleRestocking();
                break;
            case RESUMING:
                handleResuming();
                break;
            case STOPPED:
                handleStopped();
                break;
            case IDLE:
                handleIdle();
                break;
        }
    }

    private void handleScanning(PlayerInventory playerInventory) {
        if (playerInventory == null) {
            LOGGER.error("[ProMine] PlayerInventory is null, cannot scan");
            return;
        }

        LOGGER.debug("[ProMine] handleScanning() - Starting inventory scan");
        
        // InventoryAnalyzer uses SampleDetector internally - single source of truth
        inventoryAnalyzer.scan(playerInventory);
        InventorySnapshot snapshot = inventoryAnalyzer.getLastSnapshot();
        LOGGER.info("[ProMine] Inventory snapshot: {}", snapshot);
        LOGGER.debug("[ProMine] handleScanning() - Snapshot contains {} samples", snapshot.getSampleItems().size());

        // Get samples that were already detected by InventoryAnalyzer
        List<SampleItem> samples = snapshot.getSampleItems();
        LOGGER.debug("[ProMine] handleScanning() - Using {} samples from InventoryAnalyzer", samples.size());

        LOGGER.info("[ProMine] Inventory scanned");
        if (samples.isEmpty()) {
            LOGGER.info("[ProMine] No sample items detected");
        } else {
            LOGGER.debug("[ProMine] handleScanning() - {} samples found, logging details:", samples.size());
            for (SampleItem sample : samples) {
                LOGGER.info("[ProMine] Sample:");
                LOGGER.info("[ProMine] {}", sample.getItemId());
                LOGGER.info("[ProMine] Current:{}", sample.getCurrentAmount());
                LOGGER.info("[ProMine] Slots:{}", sample.getReservedSlots());
                LOGGER.info("[ProMine] Need:{}", sample.getMissingAmount());
                LOGGER.info("[ProMine] ----------------");
            }
        }

        currentSession.setCurrentState(SessionState.IDLE);
        LOGGER.info("[ProMine] STATE TRANSITION: {} -> {}", SessionState.SCANNING, SessionState.IDLE);
        LOGGER.info("[ProMine] {}", SessionState.IDLE.getDescription());
    }

    /**
     * Handle PLANNING state: create mining plans and transition to MINING.
     */
    private void handlePlanning() {
        if (currentSession == null) {
            return;
        }

        // Get the last snapshot from analyzer
        InventorySnapshot snapshot = inventoryAnalyzer.getLastSnapshot();
        if (snapshot == null) {
            LOGGER.error("[ProMine] No inventory snapshot available for planning");
            return;
        }

        // Create mining plans
        List<MiningPlan> plans = miningPlanner.createPlans(snapshot);

        if (plans.isEmpty()) {
            LOGGER.warn("[ProMine] No mining plans created (inventory is balanced)");
            currentSession.setCurrentState(SessionState.IDLE);
            LOGGER.info("[ProMine] STATE TRANSITION: {} -> {}", SessionState.PLANNING, SessionState.IDLE);
            return;
        }

        currentSession.setMiningPlans(plans);

        // Set first plan as target
        if (!plans.isEmpty()) {
            currentSession.setCurrentTarget(plans.get(0));
            LOGGER.info("[ProMine] Target set: {}", plans.get(0).getTargetItem());
        }

        LOGGER.info("[ProMine] {} mining plans created", plans.size());
        for (MiningPlan plan : plans) {
            LOGGER.info("[ProMine] {}", plan);
        }

        // Transition to MINING
        currentSession.setCurrentState(SessionState.MINING);
        LOGGER.info("[ProMine] STATE TRANSITION: {} -> {}", SessionState.PLANNING, SessionState.MINING);
        LOGGER.info("[ProMine] {}", SessionState.MINING.getDescription());

        handleMining();
    }

    /**
     * Handle MINING state: simulate mining operation.
     */
    private void handleMining() {
        if (currentSession == null || currentSession.getCurrentTarget() == null) {
            LOGGER.warn("[ProMine] No target for mining");
            return;
        }

        MiningPlan target = currentSession.getCurrentTarget();
        LOGGER.info("[ProMine] Mining: {} (need {} items, reserved {} slots)",
                target.getTargetItem(),
                target.getMissingAmount(),
                target.getReservedSlots());

        // Simulate mining (no actual Baritone movement)
        // In real implementation, would command Baritone to mine
        LOGGER.debug("[ProMine] [SIMULATED] Mining {} items of {}",
                target.getMissingAmount(), target.getTargetItem());

        // Mark as completed (simulation)
        target.setCompleted(true);
        target.setCurrentAmount(target.getRequiredAmount());
        currentSession.getStatistics().addBlocksMined(target.getMissingAmount());
        currentSession.getStatistics().addItemsCollected(target.getMissingAmount());

        // Find next target
        MiningPlan nextTarget = null;
        for (MiningPlan plan : currentSession.getMiningPlans()) {
            if (!plan.isCompleted()) {
                nextTarget = plan;
                break;
            }
        }

        if (nextTarget != null) {
            currentSession.setCurrentTarget(nextTarget);
            LOGGER.info("[ProMine] Next target: {}", nextTarget.getTargetItem());
            // Continue mining
            handleMining();
        } else {
            // All targets completed
            LOGGER.info("[ProMine] All mining targets completed");
            transitionTo(SessionState.RETURNING, null);
        }
    }

    /**
     * Handle RETURNING state: return to base.
     */
    private void handleReturning() {
        LOGGER.debug("[ProMine] [SIMULATED] Returning to base...");
        transitionTo(SessionState.DEPOSITING, null);
    }

    /**
     * Handle DEPOSITING state: deposit items.
     */
    private void handleDepositing() {
        LOGGER.debug("[ProMine] [SIMULATED] Depositing mined items...");
        currentSession.getStatistics().incrementDepositCount();
        transitionTo(SessionState.RESTOCKING, null);
    }

    /**
     * Handle RESTOCKING state: restock supplies.
     */
    private void handleRestocking() {
        LOGGER.debug("[ProMine] [SIMULATED] Restocking supplies...");
        currentSession.getStatistics().incrementInventorySortCount();
        transitionTo(SessionState.IDLE, null);
    }

    /**
     * Handle RESUMING state: resume from previous session.
     */
    private void handleResuming() {
        LOGGER.debug("[ProMine] Resuming previous session...");
        transitionTo(SessionState.SCANNING, null);
    }

    /**
     * Handle IDLE state.
     */
    private void handleIdle() {
        LOGGER.debug("[ProMine] Session idle, waiting for commands");
    }

    /**
     * Handle STOPPED state.
     */
    private void handleStopped() {
        LOGGER.debug("[ProMine] Session stopped");
    }

    /**
     * Get current session.
     */
    public MiningSession getCurrentSession() {
        return currentSession;
    }

    /**
     * Check if session is running.
     */
    public boolean isSessionRunning() {
        return isRunning && currentSession != null;
    }

    /**
     * Get current state.
     */
    public SessionState getCurrentState() {
        if (currentSession == null) {
            return SessionState.IDLE;
        }
        return currentSession.getCurrentState();
    }
}
