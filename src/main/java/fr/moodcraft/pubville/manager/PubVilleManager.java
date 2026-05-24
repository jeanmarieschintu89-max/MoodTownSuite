package fr.moodcraft.pubville.manager;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.pubville.model.PubVilleCampaign;
import fr.moodcraft.pubville.storage.PubVilleStorage;
import fr.moodcraft.tgrade.Main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PubVilleManager {

    private static final Map<String, PubVilleCampaign> campaigns = new HashMap<>();
    private static final Map<String, Location> spawns = new HashMap<>();
    private static final DecimalFormat MONEY = new DecimalFormat("#,###");

    private PubVilleManager() {}

    public static void init() {
        PubVilleStorage.init();
        reload();
        Bukkit.getScheduler().runTaskTimer(Main.get(), PubVilleManager::cleanupExpired, 20L * 60L, 20L * 60L * 5L);
    }

    public static void reload() {
        campaigns.clear();
        campaigns.putAll(PubVilleStorage.loadCampaigns());
        spawns.clear();
        spawns.putAll(PubVilleStorage.loadSpawns());
        cleanupExpired();
    }

    public static void save() {
        PubVilleStorage.saveCampaigns(campaigns);
        PubVilleStorage.saveSpawns(spawns);
    }

    public static List<PubVilleCampaign> getActiveCampaigns() {
        cleanupExpired();
        return campaigns.values().stream()
                .filter(c -> !c.isExpired())
                .sorted(Comparator.comparing(PubVilleCampaign::getExpiresAt).reversed())
                .toList();
    }

    public static PubVilleCampaign getCampaign(String townName) {
        cleanupExpired();
        return campaigns.get(key(townName));
    }

    public static boolean hasSpawn(String townName) {
        return spawns.containsKey(key(townName));
    }

    public static Location getSpawn(String townName) {
        Location location = spawns.get(key(townName));
        return location == null ? null : location.clone();
    }

    public static void setSpawn(String townName, Location location) {
        spawns.put(key(townName), location.clone());
        PubVilleStorage.saveSpawns(spawns);
    }

    public static boolean removeCampaign(String townName) {
        boolean removed = campaigns.remove(key(townName)) != null;
        if (removed) {
            PubVilleStorage.saveCampaigns(campaigns);
        }
        return removed;
    }

    public static BuyResult buy(Player player, String level) {
        cleanupExpired();

        if (!Main.get().getConfig().getBoolean("publicite-ville.actif", true)) {
            return BuyResult.fail("§cLes publicités de ville sont désactivées.");
        }

        Town town = TownyAPI.getInstance().getTown(player);

        if (town == null) {
            return BuyResult.fail("§cVous devez être dans une ville.");
        }

        String townName = town.getName();

        if (!hasSpawn(townName)) {
            return BuyResult.fail("§cAucun point publicitaire n'est défini pour votre ville.");
        }

        if (campaigns.containsKey(key(townName))) {
            return BuyResult.fail("§cVotre ville possède déjà une publicité active.");
        }

        int max = Main.get().getConfig().getInt("publicite-ville.campagnes-max-actives", 18);

        if (getActiveCampaigns().size() >= max) {
            return BuyResult.fail("§cLa limite de publicités actives est atteinte.");
        }

        double price = price(level);
        int hours = hours(level);

        if (price <= 0 || hours <= 0) {
            return BuyResult.fail("§cFormat invalide. Utilisez petite, moyenne ou grande.");
        }

        try {
            double balance = town.getAccount().getHoldingBalance();

            if (balance < price) {
                return BuyResult.fail("§cLa banque de ville n'a pas assez d'argent. Prix : §e" + format(price) + "€");
            }

            town.getAccount().withdraw(price, "Publicité ville MoodCraft");
        } catch (Exception e) {
            e.printStackTrace();
            return BuyResult.fail("§cPaiement impossible depuis la banque de ville.");
        }

        PubVilleCampaign campaign = new PubVilleCampaign(
                townName,
                player.getName(),
                level.toLowerCase(),
                System.currentTimeMillis() + (hours * 60L * 60L * 1000L),
                price
        );

        campaigns.put(key(townName), campaign);
        PubVilleStorage.saveCampaigns(campaigns);

        return BuyResult.success("§aPublicité achetée pour §b" + townName + "§a pendant §e" + hours + "h§a.");
    }

    public static void teleport(Player player, String townName) {
        PubVilleCampaign campaign = getCampaign(townName);

        if (campaign == null) {
            player.sendMessage("§cCette publicité n'est plus active.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 0.8f);
            return;
        }

        Location location = getSpawn(townName);

        if (location == null || location.getWorld() == null) {
            player.sendMessage("§cLe point d'arrivée de cette ville est introuvable.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 0.8f);
            return;
        }

        int delay = Math.max(0, Main.get().getConfig().getInt("publicite-ville.delai-teleportation-secondes", 3));

        player.closeInventory();

        if (delay <= 0) {
            player.teleport(location);
            player.sendMessage("§aBienvenue à §b" + campaign.getTownName() + "§a.");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 1.1f);
            return;
        }

        player.sendMessage("§eTéléportation vers §b" + campaign.getTownName() + "§e dans " + delay + " secondes...");
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.8f, 1.2f);

        Bukkit.getScheduler().runTaskLater(Main.get(), () -> {
            player.teleport(location);
            player.sendMessage("§aBienvenue à §b" + campaign.getTownName() + "§a.");
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.8f, 1.1f);
        }, 20L * delay);
    }

    public static void cleanupExpired() {
        List<String> expired = new ArrayList<>();

        for (Map.Entry<String, PubVilleCampaign> entry : campaigns.entrySet()) {
            if (entry.getValue().isExpired()) {
                expired.add(entry.getKey());
            }
        }

        if (expired.isEmpty()) {
            return;
        }

        for (String town : expired) {
            campaigns.remove(town);
        }

        PubVilleStorage.saveCampaigns(campaigns);
    }

    public static double price(String level) {
        return switch (level.toLowerCase()) {
            case "petite" -> Main.get().getConfig().getDouble("publicite-ville.durees-campagnes.petite.prix", 5000D);
            case "moyenne" -> Main.get().getConfig().getDouble("publicite-ville.durees-campagnes.moyenne.prix", 15000D);
            case "grande" -> Main.get().getConfig().getDouble("publicite-ville.durees-campagnes.grande.prix", 40000D);
            default -> 0D;
        };
    }

    public static int hours(String level) {
        return switch (level.toLowerCase()) {
            case "petite" -> Main.get().getConfig().getInt("publicite-ville.durees-campagnes.petite.duree-heures", 24);
            case "moyenne" -> Main.get().getConfig().getInt("publicite-ville.durees-campagnes.moyenne.duree-heures", 72);
            case "grande" -> Main.get().getConfig().getInt("publicite-ville.durees-campagnes.grande.duree-heures", 168);
            default -> 0;
        };
    }

    public static String remaining(PubVilleCampaign campaign) {
        long seconds = campaign.getRemainingMillis() / 1000L;
        long days = seconds / 86400L;
        long hours = (seconds % 86400L) / 3600L;
        long minutes = (seconds % 3600L) / 60L;

        if (days > 0) {
            return days + "j " + hours + "h";
        }

        if (hours > 0) {
            return hours + "h " + minutes + "min";
        }

        return Math.max(1, minutes) + "min";
    }

    public static String format(double value) {
        return MONEY.format(value).replace(",", " ");
    }

    private static String key(String townName) {
        return townName == null ? "" : townName.toLowerCase();
    }

    public record BuyResult(boolean success, String message) {
        public static BuyResult success(String message) { return new BuyResult(true, message); }
        public static BuyResult fail(String message) { return new BuyResult(false, message); }
    }
}
