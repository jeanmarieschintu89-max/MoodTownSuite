package fr.moodcraft.tgrade.model;

public class TownGrade {

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
    // ✅ INSPECTION
    //

    private boolean finished;

    //
    // 💰 PAYOUT
    //

    private boolean payoutClaimed;

    //
    // 🔒 DOSSIER VERROUILLÉ
    //

    private boolean locked;

    //
    // 📊 NOTE FINALE FIGÉE
    //

    private double finalScore;

    public TownGrade(String town) {

        this.town = town;
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
    // 📈 POURCENTAGE
    //

    public double getPercentage() {

        return (getTotal() / 50.0) * 100.0;
    }

    //
    // 🏅 RANG
    //

    public String getRank() {

        double total =
                locked
                        ? finalScore
                        : getTotal();

        if (total <= 15) {

            return
                    "§8✦ Hameau en friche";
        }

        if (total <= 20) {

            return
                    "§7✦ Commune rurale";
        }

        if (total <= 25) {

            return
                    "§e✦ Ville émergente";
        }

        if (total <= 30) {

            return
                    "§a✦ Ville reconnue";
        }

        if (total <= 35) {

            return
                    "§b✦ Ville majeure";
        }

        if (total <= 40) {

            return
                    "§b✦ Métropole prospère";
        }

        if (total <= 45) {

            return
                    "§6✦ Capitale régionale";
        }

        if (total <= 49) {

            return
                    "§6✦ Capitale d'élite";
        }

        return
                "§e§l✦ Merveille Nationale";
    }

    //
    // 💰 BOURSE
    //

    public int getPayout() {

        double total =
                locked
                        ? finalScore
                        : getTotal();

        if (total <= 15) {
            return 0;
        }

        if (total <= 20) {
            return 15000;
        }

        if (total <= 25) {
            return 35000;
        }

        if (total <= 30) {
            return 75000;
        }

        if (total <= 35) {
            return 125000;
        }

        if (total <= 40) {
            return 200000;
        }

        if (total <= 45) {
            return 350000;
        }

        if (total <= 49) {
            return 500000;
        }

        return 1000000;
    }

    //
    // 🏛️ APPRECIATION
    //

    public String getAppreciation() {

        double total =
                locked
                        ? finalScore
                        : getTotal();

        if (total <= 15) {

            return
                    "§7Avis: dossier trop faible, aucun financement national accordé.";
        }

        if (total <= 20) {

            return
                    "§7Avis: commune rurale, soutien national limité.";
        }

        if (total <= 25) {

            return
                    "§eAvis: ville émergente, effort urbain reconnu.";
        }

        if (total <= 30) {

            return
                    "§aAvis: ville reconnue, développement urbain encourageant.";
        }

        if (total <= 35) {

            return
                    "§bAvis: ville majeure, organisation municipale visible.";
        }

        if (total <= 40) {

            return
                    "§bAvis: métropole attractive, prestige urbain confirmé.";
        }

        if (total <= 45) {

            return
                    "§6Avis: capitale régionale, référence nationale montante.";
        }

        if (total <= 49) {

            return
                    "§6Avis: capitale d'élite, dossier remarquable.";
        }

        return
                "§e§lAvis: merveille nationale inscrite au sommet de MoodCraft.";
    }

    //
    // 🎨 SCORE COLOR
    //

    public String getScoreColor() {

        double total =
                locked
                        ? finalScore
                        : getTotal();

        if (total <= 15) {

            return "§8";
        }

        if (total <= 20) {

            return "§7";
        }

        if (total <= 25) {

            return "§e";
        }

        if (total <= 30) {

            return "§a";
        }

        if (total <= 40) {

            return "§b";
        }

        if (total <= 49) {

            return "§6";
        }

        return "§e§l";
    }

    //
    // 📊 FORMAT SCORE
    //

    public String getFormattedScore() {

        double total =
                locked
                        ? finalScore
                        : getTotal();

        return
                getScoreColor()
                        + String.format(
                        "%.1f",
                        total
                )
                        + "§7/50";
    }

    //
    // 🏙️ GETTERS
    //

    public String getTown() {
        return town;
    }

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

    public boolean isFinished() {
        return finished;
    }

    public boolean isPayoutClaimed() {

        return payoutClaimed;
    }

    public boolean isLocked() {

        return locked;
    }

    public double getFinalScore() {

        return finalScore;
    }

    //
    // ✏️ SETTERS
    //

    public void setArchitecture(int architecture) {
        this.architecture =
                Math.max(0,
                        Math.min(10,
                                architecture));
    }

    public void setStyle(int style) {
        this.style =
                Math.max(0,
                        Math.min(6,
                                style));
    }

    public void setActivite(int activite) {
        this.activite =
                Math.max(0,
                        Math.min(8,
                                activite));
    }

    public void setBanque(int banque) {
        this.banque =
                Math.max(0,
                        Math.min(4,
                                banque));
    }

    public void setRemarquable(int remarquable) {
        this.remarquable =
                Math.max(0,
                        Math.min(8,
                                remarquable));
    }

    public void setRp(int rp) {
        this.rp =
                Math.max(0,
                        Math.min(6,
                                rp));
    }

    public void setTaille(int taille) {
        this.taille =
                Math.max(0,
                        Math.min(3,
                                taille));
    }

    public void setVotes(int votes) {
        this.votes =
                Math.max(0,
                        Math.min(5,
                                votes));
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setPayoutClaimed(
            boolean payoutClaimed
    ) {

        this.payoutClaimed =
                payoutClaimed;
    }

    public void setLocked(
            boolean locked
    ) {

        this.locked =
                locked;
    }

    public void setFinalScore(
            double finalScore
    ) {

        this.finalScore =
                finalScore;
    }
}