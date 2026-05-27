package fr.moodcraft.donville.manager;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.donville.model.DonVilleDonation;
import fr.moodcraft.donville.storage.DonVilleStorage;
import fr.moodcraft.tgrade.Main;

import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class DonVilleManager {

    private static final DecimalFormat MONEY = new DecimalFormat("#,###");
    private static Set<String> enabledTowns;

    private DonVilleManager() {}

    public static void init() {
        DonVilleStorage.init();
        reload();
    }

    public static void reload() {
        enabledTowns = DonVilleStorage.loadEnabledTowns();

        if (isAutoCreationEnabled()) {
            createMissingBoxesForAllTowns();
        }
    }

    public static void save() {
        DonVilleStorage.save();
    }

    public static boolean isAutoCreationEnabled() {
        return Main.get().getConfig().getBoolean("boite-dons-ville.creation-automatique", true);
    }

    public static int createMissingBoxesForAllTowns() {
        ensureLoaded();

        int created = 0;

        try {
            for (Town town : TownyAPI.getInstance().getTowns()) {
                String townName = safeTownName(town);
                String townKey = key(townName);

                if (townKey.isEmpty() || enabledTowns.contains(townKey)) {
                    continue;
                }

                enabledTowns.add(townKey);
                DonVilleStorage.setEnabled(townName, true);
                created++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return created;
    }

    public static boolean isEnabled(String townName) {
        ensureLoaded();

        String townKey = key(townName);

        if (enabledTowns.contains(townKey)) {
            return true;
        }

        if (!isAutoCreationEnabled()) {
            return false;
        }

        Town town = TownyAPI.getInstance().getTown(townName);

        if (town == null) {
            return false;
        }

        String realTownName = safeTownName(town);
        enabledTowns.add(key(realTownName));
        DonVilleStorage.setEnabled(realTownName, true);
        return true;
    }

    public static void createBox(Town town) {
        ensureLoaded();

        String townName = safeTownName(town);

        enabledTowns.add(key(townName));
        DonVilleStorage.setEnabled(townName, true);
    }

    public static boolean removeBox(Town town) {
        ensureLoaded();

        if (isAutoCreationEnabled()) {
            return false;
        }

        String townName = safeTownName(town);
        boolean removed = enabledTowns.remove(key(townName));

        DonVilleStorage.setEnabled(townName, false);
        return removed;
    }

    public static List<Town> getEnabledTowns() {
        ensureLoaded();

        if (isAutoCreationEnabled()) {
            createMissingBoxesForAllTowns();
        }

        List<Town> towns = new ArrayList<>();

        for (String townName : enabledTowns) {
            Town town = TownyAPI.getInstance().getTown(townName);

            if (town != null) {
                towns.add(town);
            }
        }

        towns.sort((a, b) -> safeTownName(a).compareToIgnoreCase(safeTownName(b)));
        return towns;
    }

    public static List<DonVilleDonation> getHistory(String townName) {
        return DonVilleStorage.loadHistory(townName);
    }

    public static DonateResult donate(Player player, String townName, double amount) {
        ensureLoaded();

        if (!Main.get().getConfig().getBoolean("boite-dons-ville.actif", true)) {
            return DonateResult.fail("§cLes boîtes à dons sont désactivées.");
        }

        Town town = TownyAPI.getInstance().getTown(townName);

        if (town == null) {
            return DonateResult.fail("§cVille introuvable : §e" + townName);
        }

        String realTownName = safeTownName(town);

        if (!isEnabled(realTownName)) {
            return DonateResult.fail("§cCette ville n'a pas encore de boîte à dons active.");
        }

        double min = Main.get().getConfig().getDouble("boite-dons-ville.montant-minimum", 10D);
        double max = Main.get().getConfig().getDouble("boite-dons-ville.montant-maximum", 100000D);

        if (amount < min) {
            return DonateResult.fail("§cMontant trop faible. Minimum : §e" + format(min) + "€");
        }

        if (amount > max) {
            return DonateResult.fail("§cMontant trop élevé. Maximum : §e" + format(max) + "€");
        }

        Resident resident = TownyAPI.getInstance().getResident(player);

        if (resident == null) {
            return DonateResult.fail("§cCompte joueur Towny introuvable.");
        }

        try {
            double balance = resident.getAccount().getHoldingBalance();

            if (balance < amount) {
                return DonateResult.fail("§cVous n'avez pas assez d'argent. Don : §e" + format(amount) + "€");
            }

            boolean withdrawn = resident.getAccount().withdraw(amount, "Don à la ville " + realTownName);

            if (!withdrawn) {
                return DonateResult.fail("§cImpossible de débiter votre compte.");
            }

            boolean deposited = town.getAccount().deposit(amount, "Don reçu de " + player.getName());

            if (!deposited) {
                resident.getAccount().deposit(amount, "Remboursement don ville échoué");
                return DonateResult.fail("§cImpossible de créditer la banque de ville. Don remboursé.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return DonateResult.fail("§cPaiement impossible pour le moment.");
        }

        int maxHistory = Main.get().getConfig().getInt("boite-dons-ville.historique-max", 20);
        DonVilleStorage.addDonation(
                realTownName,
                new DonVilleDonation(player.getName(), amount, System.currentTimeMillis()),
                maxHistory
        );

        return DonateResult.success("§aMerci ! Vous avez donné §e" + format(amount) + "€ §aà §b" + realTownName + "§a.");
    }

    public static String format(double value) {
        return MONEY.format(value).replace(",", " ");
    }

    public static String safeTownName(Town town) {
        if (town == null) {
            return "Inconnue";
        }

        try {
            return town.getName();
        } catch (Exception e) {
            return "Inconnue";
        }
    }

    private static void ensureLoaded() {
        if (enabledTowns == null) {
            enabledTowns = DonVilleStorage.loadEnabledTowns();
        }
    }

    private static String key(String townName) {
        return townName == null ? "" : townName.toLowerCase();
    }

    public record DonateResult(boolean success, String message) {
        public static DonateResult success(String message) { return new DonateResult(true, message); }
        public static DonateResult fail(String message) { return new DonateResult(false, message); }
    }
}
