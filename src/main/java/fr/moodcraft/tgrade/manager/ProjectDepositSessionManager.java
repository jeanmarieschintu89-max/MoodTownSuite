package fr.moodcraft.tgrade.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProjectDepositSessionManager {

    public enum Step {
        NAME,
        DESCRIPTION
    }

    public static class Session {

        private final String town;
        private final long startedAt;

        private Step step;
        private String projectName;

        public Session(String town) {

            this.town = town;
            this.startedAt = System.currentTimeMillis();
            this.step = Step.NAME;
        }

        public String getTown() {
            return town;
        }

        public long getStartedAt() {
            return startedAt;
        }

        public Step getStep() {
            return step;
        }

        public void setStep(Step step) {
            this.step = step;
        }

        public String getProjectName() {
            return projectName;
        }

        public void setProjectName(String projectName) {
            this.projectName = projectName;
        }
    }

    private static final Map<UUID, Session> sessions =
            new HashMap<>();

    public static void start(Player p, String town) {

        sessions.put(
                p.getUniqueId(),
                new Session(town)
        );

        p.sendMessage("");
        p.sendMessage("§8----- §6Commission Urbaine §8-----");
        p.sendMessage("§fDépôt de projet ouvert.");
        p.sendMessage("§7Écrivez le §enom du projet §7dans le chat.");
        p.sendMessage("§7Maximum : §e32 caractères§7.");
        p.sendMessage("§8Tapez §cannuler §8pour arrêter.");
        p.sendMessage("");
    }

    public static boolean has(Player p) {

        return sessions.containsKey(
                p.getUniqueId()
        );
    }

    public static Session get(Player p) {

        return sessions.get(
                p.getUniqueId()
        );
    }

    public static void remove(Player p) {

        sessions.remove(
                p.getUniqueId()
        );
    }
}