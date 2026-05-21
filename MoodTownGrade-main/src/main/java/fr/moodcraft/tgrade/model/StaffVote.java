package fr.moodcraft.tgrade.model;

import java.util.UUID;

public class StaffVote {

    //
    // 👤 INSPECTEUR
    //

    private final UUID voter;

    //
    // 🏙️ VILLE
    //

    private final String town;

    //
    // 🏛️ NOTES
    //

    private int architecture;
    private int style;
    private int activite;
    private int banque;
    private int remarquable;
    private int rp;
    private int taille;
    private int votes;

    //
    // 📅 DATE
    //

    private long timestamp;

    public StaffVote(
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

        return architecture
                + style
                + activite
                + banque
                + remarquable
                + rp
                + taille
                + votes;
    }

    //
    // 👤 GETTER
    //

    public UUID getVoter() {
        return voter;
    }

    //
    // 🏙️ GETTER
    //

    public String getTown() {
        return town;
    }

    //
    // 📅 GETTER
    //

    public long getTimestamp() {
        return timestamp;
    }

    //
    // 🏛️ GETTERS NOTES
    //

    public int getArchitecture() {
        return architecture;
    }

    public int getStyle() {
        return style;
    }

    public int getActivite() {
        return activite;
    }

    public int getBanque() {
        return banque;
    }

    public int getRemarquable() {
        return remarquable;
    }

    public int getRp() {
        return rp;
    }

    public int getTaille() {
        return taille;
    }

    public int getVotes() {
        return votes;
    }

    //
    // ✏️ SETTERS
    //

    public void setArchitecture(
            int architecture
    ) {

        this.architecture =
                Math.max(
                        0,
                        Math.min(
                                10,
                                architecture
                        )
                );
    }

    public void setStyle(
            int style
    ) {

        this.style =
                Math.max(
                        0,
                        Math.min(
                                6,
                                style
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
                                8,
                                activite
                        )
                );
    }

    public void setBanque(
            int banque
    ) {

        this.banque =
                Math.max(
                        0,
                        Math.min(
                                4,
                                banque
                        )
                );
    }

    public void setRemarquable(
            int remarquable
    ) {

        this.remarquable =
                Math.max(
                        0,
                        Math.min(
                                8,
                                remarquable
                        )
                );
    }

    public void setRp(
            int rp
    ) {

        this.rp =
                Math.max(
                        0,
                        Math.min(
                                6,
                                rp
                        )
                );
    }

    public void setTaille(
            int taille
    ) {

        this.taille =
                Math.max(
                        0,
                        Math.min(
                                3,
                                taille
                        )
                );
    }

    public void setVotes(
            int votes
    ) {

        this.votes =
                Math.max(
                        0,
                        Math.min(
                                5,
                                votes
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