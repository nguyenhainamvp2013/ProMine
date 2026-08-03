package me.nam.promine.mining;

/**
 * Represents a mining plan for a specific item type.
 */
public class MiningPlan {
    private String targetItem;      // Item ID (e.g., "minecraft:diamond")
    private int reservedSlots;      // Number of inventory slots to reserve
    private int requiredAmount;     // Total items needed (from all stacks)
    private int currentAmount;      // Current count in inventory
    private boolean completed;      // Whether mining is complete for this item

    public MiningPlan(String targetItem, int reservedSlots, int requiredAmount, int currentAmount) {
        this.targetItem = targetItem;
        this.reservedSlots = reservedSlots;
        this.requiredAmount = requiredAmount;
        this.currentAmount = currentAmount;
        this.completed = false;
    }

    public String getTargetItem() {
        return targetItem;
    }

    public int getReservedSlots() {
        return reservedSlots;
    }

    public void setReservedSlots(int reservedSlots) {
        this.reservedSlots = reservedSlots;
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }

    public void setRequiredAmount(int requiredAmount) {
        this.requiredAmount = requiredAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getMissingAmount() {
        return Math.max(0, requiredAmount - currentAmount);
    }

    public int getCapacity() {
        return reservedSlots * 64; // Assuming max stack size of 64
    }

    @Override
    public String toString() {
        return String.format("MiningPlan[%s: %d/%d items, %d slots reserved]",
                targetItem, currentAmount, requiredAmount, reservedSlots);
    }
}
