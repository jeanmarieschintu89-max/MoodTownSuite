package fr.moodcraft.pubville.model;

public class PubVilleCampaign {
    private final String townName;
    private final String buyerName;
    private final String level;
    private final long expiresAt;
    private final double price;

    public PubVilleCampaign(String townName, String buyerName, String level, long expiresAt, double price) {
        this.townName = townName;
        this.buyerName = buyerName;
        this.level = level;
        this.expiresAt = expiresAt;
        this.price = price;
    }

    public String getTownName() { return townName; }
    public String getBuyerName() { return buyerName; }
    public String getLevel() { return level; }
    public long getExpiresAt() { return expiresAt; }
    public double getPrice() { return price; }
    public boolean isExpired() { return System.currentTimeMillis() >= expiresAt; }
    public long getRemainingMillis() { return Math.max(0L, expiresAt - System.currentTimeMillis()); }
}
