package fr.moodcraft.tgrade.model;

import java.util.UUID;

public class TownSubmission {

    private final String id;
    private final String town;
    private final String buildName;
    private final String description;
    private final String world;

    private final int x;
    private final int y;
    private final int z;

    private final UUID submittedBy;
    private final long timestamp;

    private SubmissionStatus status;

    public TownSubmission(String id,
                          String town,
                          String buildName,
                          String world,
                          int x,
                          int y,
                          int z,
                          UUID submittedBy,
                          long timestamp,
                          SubmissionStatus status) {

        this(
                id,
                town,
                buildName,
                "Aucune description.",
                world,
                x,
                y,
                z,
                submittedBy,
                timestamp,
                status
        );
    }

    public TownSubmission(String id,
                          String town,
                          String buildName,
                          String description,
                          String world,
                          int x,
                          int y,
                          int z,
                          UUID submittedBy,
                          long timestamp,
                          SubmissionStatus status) {

        this.id = id;
        this.town = town;
        this.buildName = buildName;
        this.description = description;
        this.world = world;

        this.x = x;
        this.y = y;
        this.z = z;

        this.submittedBy = submittedBy;
        this.timestamp = timestamp;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getTown() {
        return town;
    }

    public String getBuildName() {
        return buildName;
    }

    public String getDescription() {
        return description;
    }

    public String getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public UUID getSubmittedBy() {
        return submittedBy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {

        this.status = status;
    }
}