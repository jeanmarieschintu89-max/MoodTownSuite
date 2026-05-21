package fr.moodcraft.townmenu.handler;

import fr.moodcraft.townmenu.gui.TownSettingsGUI;

import fr.moodcraft.townmenu.manager.TownInputManager;

import fr.moodcraft.townmenu.util.MessageUtil;
import fr.moodcraft.townmenu.util.SoundUtil;

import org.bukkit.entity.Player;

public class TownTaxHandler {

    public static void handle(
            Player p,
            int slot
    ) {

        switch (slot) {

            case 10 -> {

                SoundUtil.click(p);

                p.closeInventory();

                TownInputManager.wait(
                        p,
                        TownInputManager.Type.TAX_TOWN
                );

                MessageUtil.send(
                        p,
                        "Fiscalité urbaine",
                        "§7Écrivez la nouvelle §etaxe de ville §7dans le chat.\n" +
                                "§8Exemple: §e250\n" +
                                "§cTapez annuler pour quitter."
                );
            }

            case 12 -> {

                SoundUtil.click(p);

                p.closeInventory();

                TownInputManager.wait(
                        p,
                        TownInputManager.Type.TAX_PLOT
                );

                MessageUtil.send(
                        p,
                        "Fiscalité urbaine",
                        "§7Écrivez la nouvelle §etaxe de parcelle §7dans le chat.\n" +
                                "§8Exemple: §e50\n" +
                                "§cTapez annuler pour quitter."
                );
            }

            case 14 -> {

                SoundUtil.click(p);

                p.closeInventory();

                TownInputManager.wait(
                        p,
                        TownInputManager.Type.TAX_SHOP
                );

                MessageUtil.send(
                        p,
                        "Fiscalité urbaine",
                        "§7Écrivez la nouvelle §etaxe commerce §7dans le chat.\n" +
                                "§8Exemple: §e150\n" +
                                "§cTapez annuler pour quitter."
                );
            }

            case 16 -> {

                SoundUtil.click(p);

                p.closeInventory();

                TownInputManager.wait(
                        p,
                        TownInputManager.Type.TAX_EMBASSY
                );

                MessageUtil.send(
                        p,
                        "Fiscalité urbaine",
                        "§7Écrivez la nouvelle §etaxe ambassade §7dans le chat.\n" +
                                "§8Exemple: §e300\n" +
                                "§cTapez annuler pour quitter."
                );
            }

            case 31 -> {

                SoundUtil.back(p);

                TownSettingsGUI.open(p);
            }
        }
    }
}