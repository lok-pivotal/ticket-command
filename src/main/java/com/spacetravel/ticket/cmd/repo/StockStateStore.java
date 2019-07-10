package com.spacetravel.ticket.cmd.repo;

import com.spacetravel.ticket.cmd.pubsub.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockStateStore extends CrudRepository<Stock, UUID> {
}
