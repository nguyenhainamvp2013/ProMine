package me.nam.promine.inventory;

import java.util.Objects;

/**
 * Represents a grouped sample item from the main inventory.
 */
public class SampleItem {
    private String itemId;
    private int currentAmount;
    private int reservedSlots;
    private int requiredAmount;
    private int missingAmount;
    private boolean completed;
    private int maxStackSize;

    public SampleItem(String itemId, int currentAmount, int reservedSlots, int requiredAmount, int missingAmount, boolean completed) {
        this.itemId = itemId;
        this.currentAmount = currentAmount;
        this.reservedSlots = reservedSlots;
        this.requiredAmount = requiredAmount;
        this.missingAmount = missingAmount;
        this.completed = completed;
        this.maxStackSize = requiredAmount > 0 ? requiredAmount / Math.max(1, reservedSlots) : 64;
    }

    public SampleItem(String itemId, int currentAmount, int maxStackSize, int ignoredSlotIndex) {
        this(itemId, currentAmount, 1, maxStackSize, Math.max(0, maxStackSize - currentAmount), currentAmount >= maxStackSize);
        this.maxStackSize = maxStackSize;
    }

    public String getItemId() {
        return itemId;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(int currentAmount) {
        this.currentAmount = currentAmount;
        this.missingAmount = Math.max(0, this.requiredAmount - this.currentAmount);
        this.completed = this.missingAmount == 0;
    }

    public int getReservedSlots() {
        return reservedSlots;
    }

    public void setReservedSlots(int reservedSlots) {
        this.reservedSlots = reservedSlots;
        this.requiredAmount = this.reservedSlots * this.maxStackSize;
        this.missingAmount = Math.max(0, this.requiredAmount - this.currentAmount);
        this.completed = this.missingAmount == 0;
    }

    public int getRequiredAmount() {
        return requiredAmount;
    }

    public void setRequiredAmount(int requiredAmount) {
        this.requiredAmount = requiredAmount;
        this.missingAmount = Math.max(0, this.requiredAmount - this.currentAmount);
        this.completed = this.missingAmount == 0;
    }

    public int getMissingAmount() {
        return missingAmount;
    }

    public void setMissingAmount(int missingAmount) {
        this.missingAmount = missingAmount;
        this.completed = this.missingAmount == 0;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getCount() {
        return currentAmount;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getAvailableSpace() {
        return Math.max(0, getMaxStackSize() - currentAmount);
    }

    public int getMissingStackSpace() {
        return getAvailableSpace();
    }

    @Override
    public String toString() {
        return String.format("SampleItem[%s: current=%d, slots=%d, required=%d, missing=%d, completed=%s]",
                itemId, currentAmount, reservedSlots, requiredAmount, missingAmount, completed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleItem that = (SampleItem) o;
        return Objects.equals(itemId, that.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}
