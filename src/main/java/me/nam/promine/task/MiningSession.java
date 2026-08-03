package me.nam.promine.task;

import me.nam.promine.mining.MiningPlan;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a mining session with state, plans, and statistics.
 */
public class MiningSession {
    private String sessionId;
    private SessionState currentState;
    private List<MiningPlan> miningPlans;
    private MiningPlan currentTarget;
    private SessionStatistics statistics;
    private long sessionStartTime;
    private long lastStateChangeTime;

    public MiningSession(String sessionId) {
        this.sessionId = sessionId;
        this.currentState = SessionState.IDLE;
        this.miningPlans = new ArrayList<>();
        this.currentTarget = null;
        this.statistics = new SessionStatistics();
        this.sessionStartTime = System.currentTimeMillis();
        this.lastStateChangeTime = System.currentTimeMillis();
    }

    public String getSessionId() {
        return sessionId;
    }

    public SessionState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(SessionState state) {
        this.currentState = state;
        this.lastStateChangeTime = System.currentTimeMillis();
    }

    public List<MiningPlan> getMiningPlans() {
        return miningPlans;
    }

    public void setMiningPlans(List<MiningPlan> plans) {
        this.miningPlans = new ArrayList<>(plans);
    }

    public MiningPlan getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(MiningPlan target) {
        this.currentTarget = target;
    }

    public SessionStatistics getStatistics() {
        return statistics;
    }

    public long getSessionStartTime() {
        return sessionStartTime;
    }

    public long getLastStateChangeTime() {
        return lastStateChangeTime;
    }

    public long getSessionDuration() {
        return System.currentTimeMillis() - sessionStartTime;
    }

    public int getCompletedPlans() {
        return (int) miningPlans.stream()
                .filter(MiningPlan::isCompleted)
                .count();
    }

    public int getTotalPlans() {
        return miningPlans.size();
    }

    public boolean isSessionActive() {
        return currentState != SessionState.IDLE && 
               currentState != SessionState.STOPPED;
    }

    @Override
    public String toString() {
        return String.format("MiningSession[%s: state=%s, plans=%d, active=%b]",
                sessionId, currentState, miningPlans.size(), isSessionActive());
    }

    /**
     * Statistics tracking for the session.
     */
    public static class SessionStatistics {
        private long blocksMinedTotal;
        private long itemsCollected;
        private int inventorySortCount;
        private int depositCount;
        private long totalMiningTime;

        public SessionStatistics() {
            this.blocksMinedTotal = 0;
            this.itemsCollected = 0;
            this.inventorySortCount = 0;
            this.depositCount = 0;
            this.totalMiningTime = 0;
        }

        public long getBlocksMinedTotal() {
            return blocksMinedTotal;
        }

        public void addBlocksMined(long count) {
            this.blocksMinedTotal += count;
        }

        public long getItemsCollected() {
            return itemsCollected;
        }

        public void addItemsCollected(long count) {
            this.itemsCollected += count;
        }

        public int getInventorySortCount() {
            return inventorySortCount;
        }

        public void incrementInventorySortCount() {
            this.inventorySortCount++;
        }

        public int getDepositCount() {
            return depositCount;
        }

        public void incrementDepositCount() {
            this.depositCount++;
        }

        public long getTotalMiningTime() {
            return totalMiningTime;
        }

        public void addMiningTime(long time) {
            this.totalMiningTime += time;
        }

        @Override
        public String toString() {
            return String.format("SessionStats[blocks=%d, items=%d, sorts=%d, deposits=%d]",
                    blocksMinedTotal, itemsCollected, inventorySortCount, depositCount);
        }
    }
}
