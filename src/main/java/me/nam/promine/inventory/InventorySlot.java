package me.nam.promine.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

/**
 * Represents a single inventory slot.
 */
public class InventorySlot {
    private int index;
    private String itemId;
    private int count;
    private int maxStackSize;
    private NbtCompound nbt;

    public InventorySlot(int index, String itemId, int count, int maxStackSize, NbtCompound nbt) {
        this.index = index;
        this.itemId = itemId;
        this.count = count;
        this.maxStackSize = maxStackSize;
        this.nbt = nbt;
    }

    public int getIndex() {
        return index;
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

    public NbtCompound getNbt() {
        return nbt;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFullStack() {
        return count == maxStackSize;
    }

    public int getAvailableSpace() {
        return maxStackSize - count;
    }

    @Override
    public String toString() {
        return String.format("Slot[%d: %s x%d/%d]", index, itemId, count, maxStackSize);
    }
}
