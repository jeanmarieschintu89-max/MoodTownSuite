package fr.moodcraft.townmenu.gui;

public final class GuiTitle {

    private GuiTitle() {}

    public static String of(String title) {
        if (title == null || title.isBlank()) {
            title = "Ville";
        }

        return "§6✦ §8§l" + title + " §6✦";
    }
}
