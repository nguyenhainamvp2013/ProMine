package me.nam.promine.inventory;

import net.minecraft.entity.player.PlayerInventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Analyzes inventory snapshots and provides high-level insights.
 */
public class InventoryAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger("promine");

    private InventoryScanner scanner;
    private InventorySnapshot lastSnapshot;

    public InventoryAnalyzer() {
        this.scanner = new InventoryScanner();
    }

    /**
     * Scan the player inventory and analyze it.
     */
    public void scan(PlayerInventory playerInventory) {
        LOGGER.debug("[ProMine] InventoryAnalyzer starting scan");
        
        // Get raw snapshot
        InventorySnapshot rawSnapshot = scanner.scan(playerInventory);

        // Detect sample items (non-full stacks)
        List<SampleItem> sampleItems = detectSampleItems(rawSnapshot.getFilledSlots());

        // Create final snapshot with sample items
        this.lastSnapshot = new InventorySnapshot(
                rawSnapshot.getAllSlots(),
                rawSnapshot.getFilledSlots(),
                rawSnapshot.getEmptySlots(),
                sampleItems,
                rawSnapshot.getTotalCapacity()
        );

        logAnalysis();
    }

    /**
     * Detect items that are not full stacks.
     * Group by item ID.
     */
    private List<SampleItem> detectSampleItems(List<InventorySlot> filledSlots) {
        Map<String, SampleItem> samplesByItemId = new HashMap<>();

        for (InventorySlot slot : filledSlots) {
            if (!slot.isFullStack()) {
                String itemId = slot.getItemId();
                SampleItem sample = new SampleItem(
                        itemId,
                        slot.getCount(),
                        slot.getMaxStackSize(),
                        slot.getIndex()
                );

                // Log detection
                LOGGER.debug("[ProMine] Sample item detected: {}", sample);

                // Store (using itemId as key for grouping)
                samplesByItemId.put(itemId, sample);
            }
        }

        return new ArrayList<>(samplesByItemId.values());
    }

    /**
     * Log inventory analysis results.
     */
    private void logAnalysis() {
        if (lastSnapshot == null) {
            return;
        }

        double usage = lastSnapshot.getUsagePercentage();
        LOGGER.info("[ProMine] Inventory usage: {:.1f}% ({}/{} slots)",
                String.format("%.1f", usage),
                lastSnapshot.getUsedSlots(),
                lastSnapshot.getTotalCapacity());

        if (lastSnapshot.isFull()) {
            LOGGER.warn("[ProMine] Inventory full!");
        }

        int missingSpace = lastSnapshot.getTotalMissingStackSpace();
        if (missingSpace > 0) {
            LOGGER.info("[ProMine] Missing stack space: {} items", missingSpace);
        }
    }

    /**
     * Get all sample items detected.
     */
    public List<SampleItem> getSampleItems() {
        if (lastSnapshot == null) {
            return new ArrayList<>();
        }
        return lastSnapshot.getSampleItems();
    }

    /**
     * Get all empty slots.
     */
    public List<InventorySlot> getEmptySlots() {
        if (lastSnapshot == null) {
            return new ArrayList<>();
        }
        return lastSnapshot.getEmptySlots();
    }

    /**
     * Get all filled slots.
     */
    public List<InventorySlot> getFilledSlots() {
        if (lastSnapshot == null) {
            return new ArrayList<>();
        }
        return lastSnapshot.getFilledSlots();
    }

    /**
     * Check if inventory is full.
     */
    public boolean isInventoryFull() {
        if (lastSnapshot == null) {
            return false;
        }
        return lastSnapshot.isFull();
    }

    /**
     * Get inventory usage percentage.
     */
    public double getInventoryUsage() {
        if (lastSnapshot == null) {
            return 0;
        }
        return lastSnapshot.getUsagePercentage();
    }

    /**
     * Get total missing stack space.
     */
    public int getMissingStackSpace() {
        if (lastSnapshot == null) {
            return 0;
        }
        return lastSnapshot.getTotalMissingStackSpace();
    }

    /**
     * Get the last inventory snapshot.
     */
    public InventorySnapshot getLastSnapshot() {
        return lastSnapshot;
    }

    /**
     * Get sample items grouped by item ID.
     */
    public Map<String, SampleItem> getSampleItemsGroupedByItemId() {
        Map<String, SampleItem> grouped = new HashMap<>();
        
        if (lastSnapshot != null) {
            for (SampleItem sample : lastSnapshot.getSampleItems()) {
                grouped.put(sample.getItemId(), sample);
            }
        }
        
        return grouped;
    }

    /**
     * Get number of empty slots.
     */
    public int getEmptySlotsCount() {
        if (lastSnapshot == null) {
            return 0;
        }
        return lastSnapshot.getEmptySlotsCount();
    }

    /**
     * Get number of filled slots.
     */
    public int getFilledSlotsCount() {
        if (lastSnapshot == null) {
            return 0;
        }
        return lastSnapshot.getUsedSlots();
    }
}
