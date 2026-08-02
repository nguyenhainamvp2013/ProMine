package me.nam.promine.config;

/**
 * Configuration settings for ProMine.
 */
public class Config {
    private boolean enabled;
    private HomePosition home;
    private ShulkerPosition shulker;

    public static class HomePosition {
        public double x;
        public double y;
        public double z;

        public HomePosition() {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        public HomePosition(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public static class ShulkerPosition {
        public double x;
        public double y;
        public double z;

        public ShulkerPosition() {
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        public ShulkerPosition(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    public Config() {
        this.enabled = true;
        this.home = new HomePosition();
        this.shulker = new ShulkerPosition();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public HomePosition getHome() {
        return home;
    }

    public void setHome(HomePosition home) {
        this.home = home;
    }

    public ShulkerPosition getShulker() {
        return shulker;
    }

    public void setShulker(ShulkerPosition shulker) {
        this.shulker = shulker;
    }
}
