package com.spacetravel.ticket.cmd;

import com.spacetravel.ticket.cmd.pubsub.Campaign;
import com.spacetravel.ticket.cmd.pubsub.Stock;
import com.spacetravel.ticket.cmd.repo.StockStateStore;
import com.spacetravel.ticket.cmd.pubsub.EventType;
import com.spacetravel.ticket.cmd.pubsub.PubSubChannels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;

import java.time.Instant;
import static com.spacetravel.ticket.cmd.pubsub.EventHeaderConst.*;

@EnableBinding(PubSubChannels.class)
public class CommandHandler {

    Logger log = LoggerFactory.getLogger(CommandHandler.class);

    @Autowired
    private PubSubChannels pubsub;

    @StreamListener(target = PubSubChannels.COMMAND_SUB,
            condition = "headers['event_type']=='cmd_create_campaign' || headers['event_type']=='cmd_update_campaign'")
    public void handleCampaign(@Payload Campaign campaign, @Header(name=HEADER_EVENT_TYPE) String commandType){
        //TODO command verification

        if(campaign==null) throw new IllegalArgumentException("payload is null");

        EventType eventType = null;
        if(EventType.CMD_CREATE_CAMPAIGN.label.equals(commandType)) {
            eventType = EventType.CAMPAIGN_CREATED;
            campaign.setId(java.util.UUID.randomUUID());
        }else if(EventType.CMD_UPDATE_CAMPAIGN.label.equals(commandType)){
            eventType = EventType.CAMPAIGN_UPDATED;
        }else
            throw new IllegalArgumentException("unknown command type: " + String.valueOf(commandType));

        long eventTime = Instant.now().toEpochMilli();
        String eventId = java.util.UUID.randomUUID().toString();

        Message<Campaign> message = MessageBuilder
                .withPayload(campaign)
                .setHeader(HEADER_EVENT_TYPE, eventType)
                .setHeader(HEADER_EVENT_TIME, eventTime)
                .setHeader(HEADER_EVENT_ID, eventId)
                .setHeader(HEADER_ROOT_ID, campaign.getId())
                .build();

        pubsub.domainPub().send(message);

        // init stock state
        if(EventType.CMD_CREATE_CAMPAIGN.label.equals(commandType)) {
            Stock initStock = new Stock();
            initStock.setCampaignId(campaign.getId());
            initStock.setStock(0);
            stateStore.save(initStock);
        }

        log.info("eventType: " + eventType + ", payload=" + campaign.toString());
    }

    @Autowired
    StockStateStore stateStore;

    @StreamListener(target = PubSubChannels.COMMAND_SUB,
            condition = "headers['event_type']=='cmd_add_stock' || headers['event_type']=='cmd_remove_stock'")
    public void handleStock(@Payload Stock payload, @Header(name=HEADER_EVENT_TYPE) String commandType){

        if(payload==null) throw new IllegalArgumentException("payload is null");

        if(!(payload.getStock() > 0)) throw new IllegalArgumentException("stock has to be positive integer, arg: " + payload.getStock());

        if(!stateStore.existsById(payload.getCampaignId())) throw new IllegalArgumentException("campaign does not exist, id: " + payload.getCampaignId());

        final Stock entity = stateStore.findById(payload.getCampaignId()).get();

        EventType eventType = null;
        if(EventType.CMD_ADD_STOCK.label.equals(commandType)) {
            eventType = EventType.STOCK_ADDED;
            entity.setStock(entity.getStock() + payload.getStock());
        }else if(EventType.CMD_REMOVE_STOCK.label.equals(commandType)){
            eventType = EventType.STOCK_REMOVED;
            if(entity.getStock()>=payload.getStock())
                entity.setStock(entity.getStock() - payload.getStock());
            else
                throw new IllegalArgumentException("Not enough stock: curr=" + entity.getStock() +  ", need=" + payload.getStock());

        }else
            throw new IllegalArgumentException("unknown command type: " + String.valueOf(commandType));


        long eventTime = Instant.now().toEpochMilli();
        String eventId = java.util.UUID.randomUUID().toString();

        Message<Stock> message = MessageBuilder
                .withPayload(payload)
                .setHeader(HEADER_EVENT_TYPE, eventType)
                .setHeader(HEADER_EVENT_TIME, eventTime)
                .setHeader(HEADER_EVENT_ID, eventId)
                .setHeader(HEADER_ROOT_ID, payload.getCampaignId())
                .build();

        pubsub.domainPub().send(message);

        stateStore.save(entity);

        log.info("eventType: " + eventType + ", payload=" + payload.toString());

    }


}
