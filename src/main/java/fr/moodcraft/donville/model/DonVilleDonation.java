package fr.moodcraft.donville.model;

public class DonVilleDonation {

    private final String donorName;
    private final double amount;
    private final long createdAt;

    public DonVilleDonation(String donorName, double amount, long createdAt) {
        this.donorName = donorName;
        this.amount = amount;
        this.createdAt = createdAt;
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
