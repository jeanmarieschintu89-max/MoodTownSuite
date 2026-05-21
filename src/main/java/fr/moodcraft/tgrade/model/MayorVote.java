package fr.moodcraft.tgrade.model;

import java.util.UUID;

public class MayorVote {

    //
    // 👑 MAIRE
    //

    private final UUID voter;

    //
    // 🏙️ VILLE CIBLÉE
    //

    private final String town;

    //
    // 📊 NOTES
    //

    private int beaute;
    private int ambiance;
    private int activite;
    private int originalite;
    private int popularite;

    //
    // 📅 DATE
    //

    private long timestamp;

    public MayorVote(
            UUID voter,
            String town
    ) {

        this.voter = voter;

        this.town = town;

        this.timestamp =
                System.currentTimeMillis();
    }

    //
    // 📊 TOTAL
    //

    public int getTotal() {

        return beaute
                + ambiance
                + activite
                + originalite
                + popularite;
    }

    //
    // 👑 GETTERS
    //

    public UUID getVoter() {
        return voter;
    }

    public String getTown() {
        return town;
    }

    public long getTimestamp() {
        return timestamp;
    }

    //
    // 📊 NOTES
    //

    public int getBeaute() {
        return beaute;
    }

    public int getAmbiance() {
        return ambiance;
    }

    public int getActivite() {
        return activite;
    }

    public int getOriginalite() {
        return originalite;
    }

    public int getPopularite() {
        return popularite;
    }

    //
    // ✏️ SETTERS
    //

    public void setBeaute(
            int beaute
    ) {

        this.beaute =
                Math.max(
                        0,
                        Math.min(
                                5,
                                beaute
                        )
                );
    }

    public void setAmbiance(
            int ambiance
    ) {

        this.ambiance =
                Math.max(
                        0,
                        Math.min(
                                5,
                                ambiance
                        )
                );
    }

    public void setActivite(
            int activite
    ) {

        this.activite =
                Math.max(
                        0,
                        Math.min(
                                5,
                                activite
                        )
                );
    }

    public void setOriginalite(
            int originalite
    ) {

        this.originalite =
                Math.max(
                        0,
                        Math.min(
                                5,
                                originalite
                        )
                );
    }

    public void setPopularite(
            int popularite
    ) {

        this.popularite =
                Math.max(
                        0,
                        Math.min(
                                5,
                                popularite
                        )
                );
    }

    //
    // 🕒 UPDATE
    //

    public void updateTimestamp() {

        this.timestamp =
                System.currentTimeMillis();
    }
}