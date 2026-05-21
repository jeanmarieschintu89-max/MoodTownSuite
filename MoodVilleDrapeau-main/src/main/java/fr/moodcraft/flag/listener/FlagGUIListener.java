package fr.moodcraft.flag.listener;

import com.palmergames.bukkit.towny.TownyAPI;

import com.palmergames.bukkit.towny.object.Resident;

import fr.moodcraft.flag.gui.FlagMainGUI;
import fr.moodcraft.flag.manager.FlagManager;
import fr.moodcraft.flag.model.TownFlag;
import fr.moodcraft.flag.storage.FlagStorage;
import fr.moodcraft.flag.util.BannerUtil;
import fr.moodcraft.flag.util.FlagMessages;

import org.bukkit.Material;
import org.bukkit.Sound;

import org.bukkit.block.Banner;
import org.bukkit.block.banner.Pattern;

import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import org.bukkit.event.inventory.InventoryClickEvent;

import org.bukkit.inventory.ItemStack;

import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;

public class FlagGUIListener
        implements Listener {

    @EventHandler
    public void click(
            InventoryClickEvent e
    ) {

        String title =
                e.getView()
                        .getTitle();

        if (!title.equalsIgnoreCase(FlagMainGUI.TITLE)
                && !title.equalsIgnoreCase("§8✦ Registre Héraldique")
                && !title.equalsIgnoreCase("§8✦ §6Registre des Drapeaux §8✦")
                && !title.equalsIgnoreCase("§8✦ Registre des Drapeaux")) {
            return;
        }

        e.setCancelled(true);

        if (!(e.getWhoClicked()
                instanceof Player p)) {
            return;
        }

        if (e.getCurrentItem() == null) {
            return;
        }

        Resident resident =
                TownyAPI.getInstance()
                        .getResident(p.getUniqueId());

        if (resident == null) {
            return;
        }

        String ownerName =
                FlagMainGUI.getViewedName(p);

        String ownerType =
                FlagMainGUI.getViewedType(p);

        if (ownerName == null
                || ownerType == null) {
            return;
        }

        boolean isNation =
                ownerType.equalsIgnoreCase("nation");

        boolean canManage =
                p.hasPermission("moodtownflag.admin");

        if (isNation) {

            canManage =
                    canManage
                            || resident.isKing()
                            || resident.hasNationRank("assistant")
                            || resident.hasNationRank("co-king");

        } else {

            canManage =
                    canManage
                            || resident.isMayor()
                            || resident.hasTownRank("assistant")
                            || resident.hasTownRank("co-mayor");
        }

        //
        // 🏳️ ENREGISTRER DRAPEAU
        //

        if (e.getSlot() == 20) {

            if (!canManage) {

                FlagMessages.error(
                        p,
                        "Registre des Drapeaux",
                        isNation
                                ? "Seuls les responsables de la nation peuvent modifier ce drapeau."
                                : "Seuls les responsables de la ville peuvent modifier ce drapeau."
                );

                return;
            }

            ItemStack hand =
                    p.getInventory()
                            .getItemInMainHand();

            if (!BannerUtil.isBanner(hand)) {

                FlagMessages.header(
                        p,
                        "Registre des Drapeaux"
                );

                p.sendMessage("§c✘ §fAucune bannière détectée.");
                p.sendMessage("");
                p.sendMessage("§7Vous devez tenir une bannière");
                p.sendMessage("§7dans votre main principale.");
                p.sendMessage("");

                FlagMessages.line(
                        p,
                        "Placez la bannière en main"
                );

                FlagMessages.line(
                        p,
                        "Cliquez ensuite sur enregistrer"
                );

                FlagMessages.footer(p);

                p.playSound(
                        p.getLocation(),
                        Sound.ENTITY_VILLAGER_NO,
                        1f,
                        0.85f
                );

                return;
            }

            TownFlag flag =
                    new TownFlag(
                            ownerName,
                            ownerType,
                            hand.clone()
                    );

            if (isNation) {

                FlagManager.setNation(
                        ownerName,
                        flag
                );

            } else {

                FlagManager.setTown(
                        ownerName,
                        flag
                );
            }

            FlagStorage.save();

            FlagMessages.header(
                    p,
                    "Registre des Drapeaux"
            );

            p.sendMessage("§a✔ §fDrapeau enregistré.");
            p.sendMessage("");

            if (isNation) {

                p.sendMessage("§7Nation: §e" + ownerName);

            } else {

                p.sendMessage("§7Ville: §b" + ownerName);
            }

            p.sendMessage("");

            FlagMessages.line(
                    p,
                    "Identité visuelle mise à jour"
            );

            FlagMessages.line(
                    p,
                    "Visible dans les menus"
            );

            FlagMessages.footer(p);

            p.playSound(
                    p.getLocation(),
                    Sound.UI_TOAST_CHALLENGE_COMPLETE,
                    1f,
                    1f
            );

            reopen(
                    p,
                    ownerName,
                    ownerType
            );

            return;
        }

        //
        // ❌ SUPPRIMER DRAPEAU
        //

        if (e.getSlot() == 24) {

            if (!canManage) {

                FlagMessages.error(
                        p,
                        "Registre des Drapeaux",
                        isNation
                                ? "Seuls les responsables de la nation peuvent supprimer ce drapeau."
                                : "Seuls les responsables de la ville peuvent supprimer ce drapeau."
                );

                return;
            }

            if (isNation) {

                FlagManager.removeNation(
                        ownerName
                );

            } else {

                FlagManager.removeTown(
                        ownerName
                );
            }

            FlagStorage.save();

            FlagMessages.header(
                    p,
                    "Registre des Drapeaux"
            );

            p.sendMessage("§c✘ §fDrapeau supprimé.");
            p.sendMessage("");

            if (isNation) {

                p.sendMessage("§7Nation: §e" + ownerName);

            } else {

                p.sendMessage("§7Ville: §b" + ownerName);
            }

            p.sendMessage("");

            FlagMessages.line(
                    p,
                    "Identité visuelle retirée"
            );

            FlagMessages.line(
                    p,
                    "Menus mis à jour"
            );

            FlagMessages.footer(p);

            p.playSound(
                    p.getLocation(),
                    Sound.BLOCK_ANVIL_BREAK,
                    1f,
                    1f
            );

            reopen(
                    p,
                    ownerName,
                    ownerType
            );

            return;
        }

        //
        // 🛡️ DONNER BOUCLIER
        //

        if (e.getSlot() == 22) {

            if (!p.hasPermission(
                    "moodtownflag.admin"
            )) {

                FlagMessages.error(
                        p,
                        "Armurerie des Blasons",
                        "Cette action est réservée à l'administration."
                );

                return;
            }

            TownFlag flag;

            if (isNation) {

                flag =
                        FlagManager.getNation(
                                ownerName
                        );

            } else {

                flag =
                        FlagManager.getTown(
                                ownerName
                        );
            }

            if (flag == null
                    || flag.getBanner() == null) {

                FlagMessages.header(
                        p,
                        "Armurerie des Blasons"
                );

                p.sendMessage("§c✘ §fAucun drapeau enregistré.");
                p.sendMessage("");

                if (isNation) {

                    p.sendMessage("§7Nation: §e" + ownerName);

                } else {

                    p.sendMessage("§7Ville: §b" + ownerName);
                }

                p.sendMessage("");

                FlagMessages.line(
                        p,
                        "Enregistrez d'abord un drapeau"
                );

                FlagMessages.line(
                        p,
                        "Le bouclier reprendra son motif"
                );

                FlagMessages.footer(p);

                p.playSound(
                        p.getLocation(),
                        Sound.ENTITY_VILLAGER_NO,
                        1f,
                        0.85f
                );

                return;
            }

            ItemStack shield =
                    new ItemStack(
                            Material.SHIELD
                    );

            BlockStateMeta shieldMeta =
                    (BlockStateMeta)
                            shield.getItemMeta();

            if (shieldMeta == null) {
                return;
            }

            Banner banner =
                    (Banner)
                            shieldMeta.getBlockState();

            BannerMeta bannerMeta =
                    (BannerMeta)
                            flag.getBanner()
                                    .getItemMeta();

            if (bannerMeta == null) {
                return;
            }

            banner.setBaseColor(
                    BannerUtil.getBannerColor(
                            flag.getBanner()
                    )
            );

            for (Pattern pattern :
                    bannerMeta.getPatterns()) {

                banner.addPattern(pattern);
            }

            shieldMeta.setBlockState(banner);
            shield.setItemMeta(shieldMeta);

            p.getInventory()
                    .addItem(shield);

            FlagMessages.header(
                    p,
                    "Armurerie des Blasons"
            );

            p.sendMessage("§a✔ §fBouclier distribué.");
            p.sendMessage("");

            if (isNation) {

                p.sendMessage("§7Nation: §e" + ownerName);

            } else {

                p.sendMessage("§7Ville: §b" + ownerName);
            }

            p.sendMessage("");

            FlagMessages.line(
                    p,
                    "Blason appliqué"
            );

            FlagMessages.line(
                    p,
                    "Équipement remis au joueur"
            );

            FlagMessages.footer(p);

            p.playSound(
                    p.getLocation(),
                    Sound.ITEM_ARMOR_EQUIP_IRON,
                    1f,
                    1f
            );
        }
    }

    private void reopen(
            Player p,
            String ownerName,
            String ownerType
    ) {

        if (ownerType.equalsIgnoreCase("nation")) {

            if (!p.hasPermission("moodtownflag.admin")) {

                Resident resident =
                        TownyAPI.getInstance()
                                .getResident(p.getUniqueId());

                if (resident == null
                        || !resident.hasNation()) {
                    return;
                }

                FlagMainGUI.openNation(
                        p,
                        resident.getNationOrNull()
                );

                return;
            }
        }

        FlagMainGUI.open(p);
    }
}