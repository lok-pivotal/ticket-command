package com.spacetravel.ticket.cmd.pubsub;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

@SuppressWarnings("All")
public interface PubSubChannels {

    public final String COMMAND_SUB = "command-sub";
    public final String DOMAIN_PUB = "domain-pub";
    public final String DOMAIN_SUB = "domain-sub";

    @Input(COMMAND_SUB)
    SubscribableChannel commandSub();

    @Output(DOMAIN_PUB)
    MessageChannel domainPub();

}
