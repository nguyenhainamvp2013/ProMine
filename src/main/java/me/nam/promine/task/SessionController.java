package me.nam.promine.task;

import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller that bridges commands to SessionManager.
 * Manages the active session and provides command handlers.
 */
public class SessionController {
    private static final Logger LOGGER = LoggerFactory.getLogger("promine");
    private static SessionController instance;
    private SessionManager sessionManager;

    private SessionController() {
        this.sessionManager = new SessionManager();
    }

    /**
     * Get singleton instance.
     */
    public static SessionController getInstance() {
        if (instance == null) {
            instance = new SessionController();
        }
        return instance;
    }

    /**
     * Handle /promine start command.
     */
    public void handleStartCommand(PlayerEntity player) {
        if (sessionManager.isSessionRunning()) {
            LOGGER.warn("[ProMine] Session already running");
            return;
        }

        LOGGER.info("[ProMine] Starting automation session...");
        sessionManager.startSession(player.getInventory());
    }

    /**
     * Handle /promine stop command.
     */
    public void handleStopCommand() {
        if (!sessionManager.isSessionRunning()) {
            LOGGER.warn("[ProMine] No active session to stop");
            return;
        }

        sessionManager.stopSession();
    }

    /**
     * Get session manager.
     */
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    /**
     * Get current session.
     */
    public MiningSession getCurrentSession() {
        return sessionManager.getCurrentSession();
    }

    /**
     * Check if session is running.
     */
    public boolean isSessionRunning() {
        return sessionManager.isSessionRunning();
    }
}
