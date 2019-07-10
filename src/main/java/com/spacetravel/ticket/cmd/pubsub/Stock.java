package com.spacetravel.ticket.cmd.pubsub;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "stock_state")
public class Stock {

    @Id
    private UUID campaignId;

    private int stock;

    @Override
    public String toString() {
        return "Stock{" +
                "campaignId=" + campaignId +
                ", stock=" + stock +
                '}';
    }

    public UUID getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(UUID campaignId) {
        this.campaignId = campaignId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
