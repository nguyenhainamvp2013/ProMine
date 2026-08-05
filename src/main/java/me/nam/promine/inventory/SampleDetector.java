package me.nam.promine.inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Detects sample items from the main inventory.
 */
public class SampleDetector {
    private static final Logger LOGGER = LoggerFactory.getLogger("promine");

    private final List<InventorySlot> inventorySlots = new ArrayList<>();
    private final List<SampleItem> samples = new ArrayList<>();

    public void setInventorySlots(List<InventorySlot> inventorySlots) {
        this.inventorySlots.clear();
        if (inventorySlots != null) {
            this.inventorySlots.addAll(inventorySlots);
        }
    }

    public List<SampleItem> detectSamples() {
        this.samples.clear();

        LOGGER.debug("[ProMine] SampleDetector.detectSamples() - Starting detection with {} slots", 
            this.inventorySlots.size());

        if (this.inventorySlots.isEmpty()) {
            LOGGER.debug("[ProMine] SampleDetector.detectSamples() - Inventory slots is empty, returning empty samples");
            return this.samples;
        }

        Map<String, SampleItem> samplesByItemId = new LinkedHashMap<>();

        for (InventorySlot slot : this.inventorySlots) {
            LOGGER.debug("[ProMine] SampleDetector - Processing slot [{}]: {} x{}/{}", 
                slot.getIndex(), slot.getItemId(), slot.getCount(), slot.getMaxStackSize());

            if (slot == null || slot.isEmpty()) {
                LOGGER.debug("[ProMine] SampleDetector - Skipping empty slot");
                continue;
            }

            if (slot.getMaxStackSize() <= 1) {
                LOGGER.debug("[ProMine] SampleDetector - Skipping non-stackable item (maxStackSize={})", 
                    slot.getMaxStackSize());
                continue;
            }

            if (slot.getCount() >= slot.getMaxStackSize()) {
                LOGGER.debug("[ProMine] SampleDetector - Skipping full stack {} x{}/{}", 
                    slot.getItemId(), slot.getCount(), slot.getMaxStackSize());
                continue;
            }

            String itemId = slot.getItemId();
            if (itemId == null || itemId.isBlank()) {
                LOGGER.debug("[ProMine] SampleDetector - Skipping slot with blank itemId");
                continue;
            }

            SampleItem sampleItem = samplesByItemId.get(itemId);
            if (sampleItem == null) {
                LOGGER.debug("[ProMine] SampleDetector - Creating new SampleItem for {}: count={}, maxStack={}", 
                    itemId, slot.getCount(), slot.getMaxStackSize());
                sampleItem = new SampleItem(itemId, slot.getCount(), slot.getMaxStackSize(), slot.getIndex());
                samplesByItemId.put(itemId, sampleItem);
            } else {
                LOGGER.debug("[ProMine] SampleDetector - Aggregating {} into existing SampleItem: {} + {} = {}", 
                    itemId, sampleItem.getCurrentAmount(), slot.getCount(), 
                    sampleItem.getCurrentAmount() + slot.getCount());
                sampleItem.setCurrentAmount(sampleItem.getCurrentAmount() + slot.getCount());
                sampleItem.setReservedSlots(sampleItem.getReservedSlots() + 1);
            }
        }

        LOGGER.debug("[ProMine] SampleDetector.detectSamples() - Found {} unique sample items before final calculation", 
            samplesByItemId.size());

        for (SampleItem sampleItem : samplesByItemId.values()) {
            sampleItem.setRequiredAmount(sampleItem.getReservedSlots() * sampleItem.getMaxStackSize());
            sampleItem.setMissingAmount(Math.max(0, sampleItem.getRequiredAmount() - sampleItem.getCurrentAmount()));
            sampleItem.setCompleted(sampleItem.getMissingAmount() == 0);

            LOGGER.info("[ProMine] Sample detected: {}", sampleItem.getItemId());
            LOGGER.info("[ProMine] Reserved slots: {}", sampleItem.getReservedSlots());
            LOGGER.info("[ProMine] Required amount: {}", sampleItem.getRequiredAmount());
            LOGGER.info("[ProMine] Missing amount: {}", sampleItem.getMissingAmount());
            LOGGER.info("[ProMine] ----------------");

            this.samples.add(sampleItem);
        }

        LOGGER.debug("[ProMine] SampleDetector.detectSamples() - Returning {} sample items", this.samples.size());

        return this.samples;
    }

    public List<SampleItem> getSamples() {
        return this.samples;
    }

    public void clear() {
        this.samples.clear();
        this.inventorySlots.clear();
    }
}
