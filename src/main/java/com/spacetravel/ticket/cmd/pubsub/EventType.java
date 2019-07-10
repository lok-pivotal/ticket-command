package com.spacetravel.ticket.cmd.pubsub;

public enum EventType {
    CMD_CREATE_CAMPAIGN("cmd_create_campaign"),
    CMD_UPDATE_CAMPAIGN("cmd_update_campaign"),
    CMD_ADD_STOCK("cmd_add_stock"),
    CMD_REMOVE_STOCK("cmd_remove_stock"),
    CAMPAIGN_CREATED("campaign_created"),
    CAMPAIGN_UPDATED("campaign_updated"),
    STOCK_ADDED("stock_added"),
    STOCK_REMOVED("stock_removed");

    // TODO: CAMPAIGN_CANCELLED


    public final String label;

    private EventType(String label) {
        this.label = label;
    }
}
