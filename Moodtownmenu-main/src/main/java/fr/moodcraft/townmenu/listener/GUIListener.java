package fr.moodcraft.townmenu.listener;

import fr.moodcraft.townmenu.gui.TownBankGUI;
import fr.moodcraft.townmenu.gui.TownClaimsGUI;
import fr.moodcraft.townmenu.gui.TownDangerGUI;
import fr.moodcraft.townmenu.gui.TownMayorCitizensGUI;
import fr.moodcraft.townmenu.gui.TownMayorCityGUI;
import fr.moodcraft.townmenu.gui.TownMayorEconomyGUI;
import fr.moodcraft.townmenu.gui.TownMayorGUI;
import fr.moodcraft.townmenu.gui.TownMenuGUI;
import fr.moodcraft.townmenu.gui.TownNationGUI;
import fr.moodcraft.townmenu.gui.TownPermissionsGUI;
import fr.moodcraft.townmenu.gui.TownPlotGUI;
import fr.moodcraft.townmenu.gui.TownResidentsGUI;
import fr.moodcraft.townmenu.gui.TownSettingsGUI;
import fr.moodcraft.townmenu.gui.TownTaxGUI;

import fr.moodcraft.townmenu.handler.TownBankHandler;
import fr.moodcraft.townmenu.handler.TownClaimsHandler;
import fr.moodcraft.townmenu.handler.TownDangerHandler;
import fr.moodcraft.townmenu.handler.TownMayorHandler;
import fr.moodcraft.townmenu.handler.TownMenuHandler;
import fr.moodcraft.townmenu.handler.TownNationHandler;
import fr.moodcraft.townmenu.handler.TownPermissionsHandler;
import fr.moodcraft.townmenu.handler.TownPlotHandler;
import fr.moodcraft.townmenu.handler.TownResidentsHandler;
import fr.moodcraft.townmenu.handler.TownSettingsHandler;
import fr.moodcraft.townmenu.handler.TownTaxHandler;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        String title = event.getView().getTitle();
        MenuType type = getMenuType(title);

        if (type == null) {
            return;
        }

        event.setCancelled(true);

        HumanEntity clicked = event.getWhoClicked();

        if (!(clicked instanceof Player player)) {
            return;
        }

        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getRawSlot() >= event.getView().getTopInventory().getSize()) {
            return;
        }

        ItemStack current = event.getCurrentItem();

        if (current == null || current.getType() == Material.AIR) {
            return;
        }

        int slot = event.getRawSlot();

        switch (type) {

            case MAIN -> TownMenuHandler.handle(player, slot);
            case RESIDENTS -> TownResidentsHandler.handle(player, slot);
            case PLOT -> TownPlotHandler.handle(player, slot);
            case CLAIMS -> TownClaimsHandler.handle(player, slot);
            case BANK -> TownBankHandler.handle(player, slot);
            case NATION -> TownNationHandler.handle(player, slot);
            case MAYOR -> TownMayorHandler.handle(player, slot);
            case MAYOR_CITIZENS -> TownMayorHandler.handleCitizens(player, slot);
            case MAYOR_CITY -> TownMayorHandler.handleCity(player, slot);
            case MAYOR_ECONOMY -> TownMayorHandler.handleEconomy(player, slot);
            case SETTINGS -> TownSettingsHandler.handle(player, slot);
            case PERMISSIONS -> TownPermissionsHandler.handle(player, slot);
            case TAX -> TownTaxHandler.handle(player, slot);
            case DANGER -> TownDangerHandler.handle(player, slot);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {

        if (getMenuType(event.getView().getTitle()) != null) {
            event.setCancelled(true);
        }
    }

    private MenuType getMenuType(String title) {

        if (title == null) {
            return null;
        }

        if (matches(title, TownMenuGUI.TITLE, "§8✦ §6Ville MoodCraft")) {
            return MenuType.MAIN;
        }

        if (matches(title, TownResidentsGUI.TITLE, "§8✦ §6Habitants")) {
            return MenuType.RESIDENTS;
        }

        if (matches(title, TownPlotGUI.TITLE, "§8✦ §6Ma Parcelle")) {
            return MenuType.PLOT;
        }

        if (matches(title, TownClaimsGUI.TITLE, "§8✦ §6Claims")) {
            return MenuType.CLAIMS;
        }

        if (matches(title, TownBankGUI.TITLE, "§8✦ §6Banque Ville")) {
            return MenuType.BANK;
        }

        if (matches(title, TownNationGUI.TITLE, "§8✦ §6Nation")) {
            return MenuType.NATION;
        }

        if (matches(title, TownMayorGUI.TITLE, "§8✦ §6Gestion Maire", "§8✦ §6Menu Maire")) {
            return MenuType.MAYOR;
        }

        if (matches(title, TownMayorCitizensGUI.TITLE)) {
            return MenuType.MAYOR_CITIZENS;
        }

        if (matches(title, TownMayorCityGUI.TITLE)) {
            return MenuType.MAYOR_CITY;
        }

        if (matches(title, TownMayorEconomyGUI.TITLE)) {
            return MenuType.MAYOR_ECONOMY;
        }

        if (matches(title, TownSettingsGUI.TITLE, "§8✦ §6Paramètres Ville")) {
            return MenuType.SETTINGS;
        }

        if (matches(title, TownPermissionsGUI.TITLE, "§8✦ §6Permissions Ville")) {
            return MenuType.PERMISSIONS;
        }

        if (matches(title, TownTaxGUI.TITLE, "§8✦ §6Fiscalité Ville")) {
            return MenuType.TAX;
        }

        if (matches(title, TownDangerGUI.TITLE, "§8✦ §cZone Sensible")) {
            return MenuType.DANGER;
        }

        return null;
    }

    private boolean matches(String title, String... possibleTitles) {

        for (String possibleTitle : possibleTitles) {

            if (possibleTitle != null && possibleTitle.equals(title)) {
                return true;
            }
        }

        return false;
    }

    private enum MenuType {
        MAIN,
        RESIDENTS,
        PLOT,
        CLAIMS,
        BANK,
        NATION,
        MAYOR,
        MAYOR_CITIZENS,
        MAYOR_CITY,
        MAYOR_ECONOMY,
        SETTINGS,
        PERMISSIONS,
        TAX,
        DANGER
    }
}
