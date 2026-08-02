package me.nam.promine.inventory;

import java.util.List;

/**
 * Represents a snapshot of the current inventory state.
 */
public class InventorySnapshot {
    private List<InventorySlot> allSlots;
    private List<InventorySlot> filledSlots;
    private List<InventorySlot> emptySlots;
    private List<SampleItem> sampleItems;
    private long scanTime;
    private int totalCapacity;

    public InventorySnapshot(
            List<InventorySlot> allSlots,
            List<InventorySlot> filledSlots,
            List<InventorySlot> emptySlots,
            List<SampleItem> sampleItems,
            int totalCapacity) {
        this.allSlots = allSlots;
        this.filledSlots = filledSlots;
        this.emptySlots = emptySlots;
        this.sampleItems = sampleItems;
        this.totalCapacity = totalCapacity;
        this.scanTime = System.currentTimeMillis();
    }

    public List<InventorySlot> getAllSlots() {
        return allSlots;
    }

    public List<InventorySlot> getFilledSlots() {
        return filledSlots;
    }

    public List<InventorySlot> getEmptySlots() {
        return emptySlots;
    }

    public List<SampleItem> getSampleItems() {
        return sampleItems;
    }

    public int getTotalCapacity() {
        return totalCapacity;
    }

    public int getUsedSlots() {
        return filledSlots.size();
    }

    public int getEmptySlotsCount() {
        return emptySlots.size();
    }

    public boolean isFull() {
        return emptySlots.isEmpty();
    }

    public double getUsagePercentage() {
        if (totalCapacity == 0) return 0;
        return (getUsedSlots() * 100.0) / totalCapacity;
    }

    public int getTotalMissingStackSpace() {
        return sampleItems.stream()
                .mapToInt(SampleItem::getMissingStackSpace)
                .sum();
    }

    public long getScanTime() {
        return scanTime;
    }

    @Override
    public String toString() {
        return String.format(
                "InventorySnapshot[filled=%d, empty=%d, samples=%d, usage=%.1f%%]",
                filledSlots.size(), emptySlots.size(), sampleItems.size(), getUsagePercentage());
    }
}
