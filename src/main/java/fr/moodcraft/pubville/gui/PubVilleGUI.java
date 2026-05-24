package fr.moodcraft.pubville.gui;

import fr.moodcraft.pubville.manager.PubVilleManager;
import fr.moodcraft.pubville.model.PubVilleCampaign;
import fr.moodcraft.townmenu.util.MoodStyle;
import fr.moodcraft.townmenu.util.SafeGUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public final class PubVilleGUI {

    public static final String TITLE = MoodStyle.guiTitle("Villes sponsorisées");
    private static final int[] SLOTS = {10, 11, 12, 13, 14, 15, 16, 20, 21, 22, 23, 24, 25, 29, 30, 31, 32, 33};

    private PubVilleGUI() {}

    public static void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 45, TITLE);
        SafeGUI.fill(inv, Material.BLACK_STAINED_GLASS_PANE, " ");

        List<PubVilleCampaign> campaigns = PubVilleManager.getActiveCampaigns();

        SafeGUI.safeSet(inv, 4, SafeGUI.glow(SafeGUI.item(
                Material.EMERALD,
                MoodStyle.button("Villes sponsorisées"),
                "§8• §7Découvrez les villes mises en avant",
                "§8• §7par leurs maires et leurs habitants",
                "",
                campaigns.isEmpty()
                        ? "§cAucune publicité active"
                        : "§eCliquez sur une ville pour la visiter"
        )));

        if (campaigns.isEmpty()) {
            SafeGUI.safeSet(inv, 22, SafeGUI.item(
                    Material.OAK_SIGN,
                    MoodStyle.button("Aucune publicité"),
                    "§8• §7Aucune ville n'est sponsorisée",
                    "§8• §7pour le moment.",
                    "",
                    "§eRevenez plus tard"
            ));
        } else {
            int index = 0;

            for (PubVilleCampaign campaign : campaigns) {
                if (index >= SLOTS.length) {
                    break;
                }

                SafeGUI.safeSet(inv, SLOTS[index], SafeGUI.item(
                        Material.BEACON,
                        MoodStyle.button(campaign.getTownName()),
                        "§8• §7Campagne : §e" + campaign.getLevel(),
                        "§8• §7Temps restant : §a" + PubVilleManager.remaining(campaign),
                        "§8• §7Achetée par : §f" + campaign.getBuyerName(),
                        "",
                        "§eCliquez pour visiter cette ville"
                ));

                index++;
            }
        }

        SafeGUI.safeSet(inv, 40, SafeGUI.item(
                Material.BARRIER,
                MoodStyle.dangerButton("Fermer"),
                "§8• §7Fermer le menu"
        ));

        player.openInventory(inv);
    }
}
