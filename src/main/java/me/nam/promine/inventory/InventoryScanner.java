package me.nam.promine.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Scans the player inventory and creates a snapshot.
 * Excludes hotbar (slots 0-8).
 */
public class InventoryScanner {
    private static final Logger LOGGER = LoggerFactory.getLogger("promine");
    
    // In PlayerInventory.main: hotbar is slots 0-8, main inventory is 9-35.
    private static final int HOTBAR_SIZE = 9;
    private static final int MAIN_INVENTORY_SIZE = 27; // 3 rows of 9

    public InventoryScanner() {
    }

    /**
     * Scan inventory and create a snapshot (excluding hotbar).
     */
    public InventorySnapshot scan(PlayerInventory playerInventory) {
        LOGGER.debug("[ProMine] InventoryScanner.scan() - Starting inventory scan");

        List<InventorySlot> allSlots = new ArrayList<>();
        List<InventorySlot> filledSlots = new ArrayList<>();
        List<InventorySlot> emptySlots = new ArrayList<>();

        // Scan main inventory (excluding hotbar): slots 9-35
        int mainStartIndex = HOTBAR_SIZE;
        int mainEndExclusive = HOTBAR_SIZE + MAIN_INVENTORY_SIZE;
        for (int i = mainStartIndex; i < mainEndExclusive; i++) {
            ItemStack itemStack = playerInventory.getStack(i);
            InventorySlot slot = createSlotFromItemStack(i, itemStack);
            allSlots.add(slot);

            if (slot.isEmpty()) {
                emptySlots.add(slot);
            } else {
                filledSlots.add(slot);
                LOGGER.debug("[ProMine] InventoryScanner - Found item: {} x{}/{}", 
                    slot.getItemId(), slot.getCount(), slot.getMaxStackSize());
            }
        }

        // Total capacity = main inventory size (excluding hotbar)
        int totalCapacity = MAIN_INVENTORY_SIZE;

        LOGGER.info("[ProMine] Inventory scanned: {} filled, {} empty", 
            filledSlots.size(), emptySlots.size());

        return new InventorySnapshot(allSlots, filledSlots, emptySlots, new ArrayList<>(), totalCapacity);
    }

    /**
     * Create an InventorySlot from an ItemStack.
     */
    private InventorySlot createSlotFromItemStack(int index, ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return new InventorySlot(index, "empty", 0, 0, null);
        }

        String itemId = Registries.ITEM.getId(itemStack.getItem()).toString();
        int count = itemStack.getCount();
        int maxStackSize = itemStack.getMaxCount();
        
        // NBT handling - create empty for now
        net.minecraft.nbt.NbtCompound nbt = new net.minecraft.nbt.NbtCompound();

        return new InventorySlot(index, itemId, count, maxStackSize, nbt);
    }
}
