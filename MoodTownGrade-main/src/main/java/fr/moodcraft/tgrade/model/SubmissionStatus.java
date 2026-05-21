package fr.moodcraft.tgrade.model;

public enum SubmissionStatus {

    //
    // 🟡 DOSSIER EN ÉTUDE
    //

    PENDING(
            "§eEN ÉTUDE"
    ),

    //
    // 🟢 PERMIS ACCORDÉ
    //

    APPROVED(
            "§aPERMIS ACCORDÉ"
    ),

    //
    // 🔵 INSPECTION FINALE
    //

    FINISHED(
            "§bINSPECTION FINALE"
    ),

    //
    // 🔴 REFUS ADMINISTRATIF
    //

    REJECTED(
            "§cREFUS ADMINISTRATIF"
    );

    //
    // 📄 DISPLAY
    //

    private final String display;

    SubmissionStatus(String display) {

        this.display = display;
    }

    //
    // 🎨 GET DISPLAY
    //

    public String getDisplay() {

        return display;
    }
}