package com.spacetravel.ticket.cmd.pubsub;

import java.util.UUID;

public class Campaign {

    private UUID id;

    private int dateKey;
    private String direction;
    private String planetId;
    private String tags;

    @Override
    public String toString() {
        return "Campaign{" +
                "id=" + id +
                ", dateKey=" + dateKey +
                ", direction='" + direction + '\'' +
                ", planetId='" + planetId + '\'' +
                ", tags='" + tags + '\'' +
                '}';
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getDateKey() {
        return dateKey;
    }

    public void setDateKey(int dateKey) {
        this.dateKey = dateKey;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getPlanetId() {
        return planetId;
    }

    public void setPlanetId(String planetId) {
        this.planetId = planetId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
