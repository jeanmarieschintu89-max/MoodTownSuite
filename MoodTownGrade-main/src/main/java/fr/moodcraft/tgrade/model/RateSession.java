package fr.moodcraft.tgrade.model;

public class RateSession {

    //
    // 🏛 VILLE
    //

    private final String town;

    //
    // ⭐ NOTES
    //

    private int architecture;
    private int coherence;
    private int activite;
    private int banque;
    private int build;
    private int roleplay;
    private int taille;
    private int votes;

    //
    // 🏗 CONSTRUCTOR
    //

    public RateSession(
            String town
    ) {

        this.town = town;
    }

    //
    // 🏛 GET TOWN
    //

    public String getTown() {

        return town;
    }

    //
    // 🏗 ARCHITECTURE
    //

    public int getArchitecture() {

        return architecture;
    }

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

    //
    // 🎨 COHERENCE
    //

    public int getCoherence() {

        return coherence;
    }

    public void setCoherence(
            int coherence
    ) {

        this.coherence =
                Math.max(
                        0,
                        Math.min(
                                6,
                                coherence
                        )
                );
    }

    //
    // ⚡ ACTIVITE
    //

    public int getActivite() {

        return activite;
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

    //
    // 💰 BANQUE
    //

    public int getBanque() {

        return banque;
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

    //
    // 🏛 BUILD
    //

    public int getBuild() {

        return build;
    }

    public void setBuild(
            int build
    ) {

        this.build =
                Math.max(
                        0,
                        Math.min(
                                8,
                                build
                        )
                );
    }

    //
    // 🎭 ROLEPLAY
    //

    public int getRoleplay() {

        return roleplay;
    }

    public void setRoleplay(
            int roleplay
    ) {

        this.roleplay =
                Math.max(
                        0,
                        Math.min(
                                6,
                                roleplay
                        )
                );
    }

    //
    // 🌍 TAILLE
    //

    public int getTaille() {

        return taille;
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

    //
    // 🗳 VOTES
    //

    public int getVotes() {

        return votes;
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
    // 🏆 TOTAL /50
    //

    public int getTotal() {

        return architecture
                + coherence
                + activite
                + banque
                + build
                + roleplay
                + taille
                + votes;
    }
}