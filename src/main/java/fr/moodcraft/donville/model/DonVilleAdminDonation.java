package fr.moodcraft.donville.model;

public class DonVilleAdminDonation {

    private final String townName;
    private final String donorName;
    private final double amount;
    private final long createdAt;

    public DonVilleAdminDonation(
            String townName,
            String donorName,
            double amount,
            long createdAt
    ) {
        this.townName = townName;
        this.donorName = donorName;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public String getTownName() {
        return townName;
    }

    public String getDonorName() {
        return donorName;
    }

    public double getAmount() {
        return amount;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
