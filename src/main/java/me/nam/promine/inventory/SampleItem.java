package me.nam.promine.inventory;

import java.util.Objects;

/**
 * Represents an item that is not a full stack (Sample Item).
 */
public class SampleItem {
    private String itemId;
    private int count;
    private int maxStackSize;
    private int slotIndex;

    public SampleItem(String itemId, int count, int maxStackSize, int slotIndex) {
        this.itemId = itemId;
        this.count = count;
        this.maxStackSize = maxStackSize;
        this.slotIndex = slotIndex;
    }

    public String getItemId() {
        return itemId;
    }

    public int getCount() {
        return count;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getSlotIndex() {
        return slotIndex;
    }

    public int getAvailableSpace() {
        return maxStackSize - count;
    }

    public int getMissingStackSpace() {
        return getAvailableSpace();
    }

    @Override
    public String toString() {
        return String.format("SampleItem[%s: %d/%d (slot %d)]", itemId, count, maxStackSize, slotIndex);
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
