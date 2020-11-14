package me.mirsowasvonegal.serverpanel.RestAPI.repository;

import me.mirsowasvonegal.serverpanel.RestAPI.model.VServer;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Projekt: RestAPI
 * @Created: 14.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public interface VServerRepository extends MongoRepository<VServer, Integer> {
}
