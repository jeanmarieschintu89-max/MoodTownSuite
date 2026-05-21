package fr.moodcraft.tgrade.model;

import java.util.UUID;

public class CitizenVote {

    //
    // 👤 JOUEUR
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

    public CitizenVote(
            UUID voter,
            String town
    ) {

        this.voter =
                voter;

        this.town =
                town;

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
    // 👤 GETTERS
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
    // ✏️ SETTERS NOTES
    //

    public void setBeaute(
            int beaute
    ) {

        this.beaute =
                clamp(beaute);
    }

    public void setAmbiance(
            int ambiance
    ) {

        this.ambiance =
                clamp(ambiance);
    }

    public void setActivite(
            int activite
    ) {

        this.activite =
                clamp(activite);
    }

    public void setOriginalite(
            int originalite
    ) {

        this.originalite =
                clamp(originalite);
    }

    public void setPopularite(
            int popularite
    ) {

        this.popularite =
                clamp(popularite);
    }

    //
    // 📅 SET TIMESTAMP
    //

    public void setTimestamp(
            long timestamp
    ) {

        this.timestamp =
                timestamp;
    }

    //
    // 🕒 UPDATE
    //

    public void updateTimestamp() {

        this.timestamp =
                System.currentTimeMillis();
    }

    //
    // 🔒 LIMIT
    //

    private int clamp(
            int value
    ) {

        return Math.max(
                0,
                Math.min(
                        3,
                        value
                )
        );
    }
}