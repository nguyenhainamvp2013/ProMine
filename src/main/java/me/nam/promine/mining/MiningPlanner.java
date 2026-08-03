package me.nam.promine.mining;

import me.nam.promine.inventory.InventoryAnalyzer;
import me.nam.promine.inventory.InventorySnapshot;
import me.nam.promine.inventory.SampleItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Converts InventorySnapshot into MiningPlan list.
 * Divides available inventory slots equally among sample items.
 */
public class MiningPlanner {
    private static final Logger LOGGER = LoggerFactory.getLogger("promine");
    private static final int TOTAL_INVENTORY_SLOTS = 27; // Main inventory only

    public MiningPlanner() {
    }

    /**
     * Create mining plans from inventory snapshot.
     * Divides available slots equally among sample items.
     */
    public List<MiningPlan> createPlans(InventorySnapshot snapshot) {
        List<MiningPlan> plans = new ArrayList<>();

        // Get sample items grouped by item ID
        Map<String, SampleItem> sampleItems = groupSampleItems(snapshot);

        if (sampleItems.isEmpty()) {
            LOGGER.debug("[ProMine] No sample items detected, no plans created");
            return plans;
        }

        // Calculate slots available for planning
        int filledSlots = snapshot.getUsedSlots();
        int availableSlots = TOTAL_INVENTORY_SLOTS - filledSlots;

        // Divide available slots equally among sample items
        int slotsPerItem = availableSlots / sampleItems.size();
        int remainder = availableSlots % sampleItems.size();

        LOGGER.info("[ProMine] Creating mining plans: {} items, {} available slots, {} per item",
                sampleItems.size(), availableSlots, slotsPerItem);

        int index = 0;
        for (String itemId : sampleItems.keySet()) {
            SampleItem sampleItem = sampleItems.get(itemId);
            
            // Distribute remainder across first items
            int reservedSlots = slotsPerItem + (index < remainder ? 1 : 0);
            
            // Required amount = missing space + current amount to fill to max
            int missingSpace = sampleItem.getMissingStackSpace();
            int requiredAmount = missingSpace;
            
            MiningPlan plan = new MiningPlan(
                    itemId,
                    reservedSlots,
                    requiredAmount,
                    sampleItem.getCount()
            );

            LOGGER.debug("[ProMine] Mining plan created: {}", plan);
            plans.add(plan);
            index++;
        }

        return plans;
    }

    /**
     * Group sample items by item ID from snapshot.
     */
    private Map<String, SampleItem> groupSampleItems(InventorySnapshot snapshot) {
        // Create a temporary analyzer to reuse grouping logic
        InventoryAnalyzer analyzer = new InventoryAnalyzer();
        
        // Manually populate the sample items from snapshot
        Map<String, SampleItem> grouped = new java.util.HashMap<>();
        for (SampleItem sample : snapshot.getSampleItems()) {
            grouped.put(sample.getItemId(), sample);
        }
        
        return grouped;
    }
}
