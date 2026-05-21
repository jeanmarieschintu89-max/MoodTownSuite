package fr.moodcraft.tgrade.listener;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import fr.moodcraft.tgrade.gui.CitizenTownListGUI;
import fr.moodcraft.tgrade.gui.ClassementGUI;
import fr.moodcraft.tgrade.gui.MayorTownListGUI;
import fr.moodcraft.tgrade.gui.UrbanismeAdminGUI;
import fr.moodcraft.tgrade.gui.UrbanismeMainGUI;
import fr.moodcraft.tgrade.manager.ProjectDepositSessionManager;
import fr.moodcraft.tgrade.towny.TownyHook;
import fr.moodcraft.tgrade.util.MoodStyle;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class UrbanismeMainListener implements Listener {

    @EventHandler
    public void click(InventoryClickEvent e) {

        if (!MoodStyle.titleEquals(e.getView().getTitle(), UrbanismeMainGUI.TITLE)) {
            return;
        }

        e.setCancelled(true);

        if (!(e.getWhoClicked() instanceof Player p)) {
            return;
        }

        if (e.getClickedInventory() == null) {
            return;
        }

        if (e.getRawSlot() >= e.getView().getTopInventory().getSize()) {
            return;
        }

        if (e.getCurrentItem() == null || e.getCurrentItem().getType().isAir()) {
            return;
        }

        int slot = e.getRawSlot();
        MoodStyle.click(p);

        if (slot == 11) {
            startProjectDeposit(p);
            return;
        }

        if (slot == 13) {
            openVoteMenu(p);
            return;
        }

        if (slot == 15) {
            ClassementGUI.open(p);
            return;
        }

        if (slot == 31) {
            if (!p.hasPermission("moodtowngrade.staff")) {
                MoodStyle.deny(
                        p,
                        "Accès refusé.",
                        "Ce centre est réservé à l'administration nationale."
                );
                return;
            }

            p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
            MoodStyle.send(
                    p,
                    MoodStyle.MODULE,
                    MoodStyle.info("Ouverture du Centre National."),
                    MoodStyle.detail("Demandes, notes et subventions")
            );
            UrbanismeAdminGUI.open(p);
            return;
        }

        if (slot == 40) {
            p.closeInventory();
            p.performCommand("menu");
        }
    }

    private void openVoteMenu(Player p) {

        if (TownyHook.canManage(p)) {

            p.playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1f, 1f);
            MoodStyle.send(
                    p,
                    MoodStyle.MODULE,
                    MoodStyle.info("Conseil des maires ouvert."),
                    MoodStyle.detail("Votez pour les projets validés"),
                    MoodStyle.detail("Votre avis compte pour le classement")
            );
            MayorTownListGUI.open(p);
            return;
        }

        MoodStyle.send(
                p,
                MoodStyle.MODULE,
                MoodStyle.info("Vote citoyen ouvert."),
                MoodStyle.detail("Votez pour les villes ayant un projet validé"),
                MoodStyle.detail("Votre vote aide le classement hebdo")
        );
        CitizenTownListGUI.open(p);
    }

    private void startProjectDeposit(Player p) {

        if (!TownyHook.canManage(p)) {
            MoodStyle.deny(
                    p,
                    "Accès refusé.",
                    "Seuls les maires et assistants peuvent déposer des projets."
            );
            return;
        }

        Resident resident = TownyAPI.getInstance().getResident(p.getUniqueId());

        if (resident == null || !resident.hasTown() || resident.getTownOrNull() == null) {
            MoodStyle.deny(
                    p,
                    "Aucune ville détectée.",
                    "Impossible d'ouvrir un dossier urbain."
            );
            return;
        }

        Town town = resident.getTownOrNull();

        ProjectDepositSessionManager.start(p, town.getName());
        p.closeInventory();
        MoodStyle.ok(p);
    }
}
