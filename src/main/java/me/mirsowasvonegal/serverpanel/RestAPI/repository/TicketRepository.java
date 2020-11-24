package me.mirsowasvonegal.serverpanel.RestAPI.repository;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Price;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Projekt: RestAPI
 * @Created: 24.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public interface TicketRepository extends MongoRepository<Ticket, Integer> {

    List<Ticket> findTicketByUserId(String userId);

    List<Ticket> findTicketById(String id);

    Boolean existsById(String id);

}
