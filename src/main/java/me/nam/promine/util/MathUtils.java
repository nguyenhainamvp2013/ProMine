package me.nam.promine.util;

/**
 * Utility functions for mathematical operations.
 */
public class MathUtils {
    
    public MathUtils() {
    }

    public static double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
