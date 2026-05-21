package fr.moodcraft.tgrade.listener;

import fr.moodcraft.tgrade.gui.EvaluationManagerGUI;
import fr.moodcraft.tgrade.gui.ProjectReviewGUI;
import fr.moodcraft.tgrade.gui.UrbanismeAdminGUI;
import fr.moodcraft.tgrade.manager.GradeManager;
import fr.moodcraft.tgrade.model.SubmissionStatus;
import fr.moodcraft.tgrade.model.TownGrade;
import fr.moodcraft.tgrade.model.TownSubmission;
import fr.moodcraft.tgrade.storage.SubmissionStorage;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EvaluationManagerListener
        implements Listener {

    @EventHandler
    public void click(
            InventoryClickEvent e
    ) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), EvaluationManagerGUI.TITLE)) {
            return;
        }

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        if (e.getRawSlot() < 0 || e.getRawSlot() >= e.getView().getTopInventory().getSize()) {
            return;
        }

        if (e.getRawSlot() == 49) {

            p.playSound(
                    p.getLocation(),
                    Sound.UI_BUTTON_CLICK,
                    1f,
                    1f
            );

            UrbanismeAdminGUI.open(p);
            return;
        }

        if (!isTownSlot(e.getRawSlot())) {
            return;
        }

        ItemStack item =
                e.getCurrentItem();

        if (item == null
                || item.getType() == Material.AIR
                || item.getType() == Material.BLACK_STAINED_GLASS_PANE
                || item.getType() == Material.BARRIER
                || !item.hasItemMeta()) {

            return;
        }

        ItemMeta meta =
                item.getItemMeta();

        if (meta == null
                || meta.getDisplayName() == null) {

            return;
        }

        String town =
                MoodStyle.tag(item, MoodStyle.TAG_TOWN);

        if (town == null || town.isBlank()) {
            town =
                    cleanTownName(
                            meta.getDisplayName()
                    );
        }

        if (town == null
                || town.isBlank()) {

            return;
        }

        TownGrade grade =
                GradeManager.get(town);

        if (grade == null) {

            deny(
                    p,
                    "§cDossier introuvable.",
                    "§7La ville n'est plus dans les registres."
            );

            return;
        }

        TownSubmission project =
                getActiveProject(town);

        if (project == null) {

            deny(
                    p,
                    "§cAucun projet actif.",
                    "§7Cette ville n'a aucun projet validé."
            );

            return;
        }

        p.playSound(
                p.getLocation(),
                Sound.BLOCK_BEACON_ACTIVATE,
                1f,
                1f
        );

        p.sendMessage("");
        p.sendMessage("§8----- §6✦ Commission Urbaine ✦ §8-----");
        p.sendMessage("");
        p.sendMessage("§fOuverture du dossier staff.");
        p.sendMessage("");
        p.sendMessage("§7Ville: §b" + project.getTown());
        p.sendMessage("§7Projet: §e" + project.getBuildName());
        p.sendMessage("§7État: §aValidé");
        p.sendMessage("");
        p.sendMessage("§8• §7Inspection nationale chargée");
        p.sendMessage("§8-----------------------------");

        ProjectReviewGUI.open(
                p,
                project
        );
    }

    private static boolean isTownSlot(
            int slot
    ) {

        return slot == 10
                || slot == 11
                || slot == 12
                || slot == 13
                || slot == 14
                || slot == 15
                || slot == 16
                || slot == 19
                || slot == 20
                || slot == 21
                || slot == 22
                || slot == 23
                || slot == 24
                || slot == 25
                || slot == 28
                || slot == 29
                || slot == 30
                || slot == 31
                || slot == 32
                || slot == 33
                || slot == 34
                || slot == 37
                || slot == 38
                || slot == 39
                || slot == 40
                || slot == 41
                || slot == 42
                || slot == 43;
    }

    private static String cleanTownName(
            String displayName
    ) {

        String clean =
                ChatColor.stripColor(displayName);

        if (clean == null) {
            return null;
        }

        return clean
                .replace("✦", "")
                .trim();
    }

    private static TownSubmission getActiveProject(
            String town
    ) {

        for (TownSubmission submission : SubmissionStorage.getAll()) {

            if (!submission.getTown().equalsIgnoreCase(town)) {
                continue;
            }

            if (submission.getStatus() == SubmissionStatus.APPROVED) {
                return submission;
            }
        }

        return null;
    }

    private static void deny(
            Player p,
            String message,
            String detail
    ) {

        p.playSound(
                p.getLocation(),
                Sound.ENTITY_VILLAGER_NO,
                1f,
                1f
        );

        p.sendMessage("");
        p.sendMessage("§8----- §6✦ Commission Urbaine ✦ §8-----");
        p.sendMessage("");
        p.sendMessage(message);
        p.sendMessage(detail);
        p.sendMessage("");
        p.sendMessage("§8• §7Service officiel de §aMood§6Craft");
        p.sendMessage("§8-----------------------------");
    }
}
