package me.mirsowasvonegal.serverpanel.RestAPI.repository;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Network;
import me.mirsowasvonegal.serverpanel.RestAPI.model.VServer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Projekt: RestAPI
 * @Created: 14.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public interface VServerRepository extends MongoRepository<VServer, Integer> {
    List<VServer> findVServerById(String id);

    List<VServer> findVServerByServerId(String serverid);

    List<VServer> findVServerByUserId(String userId);

    Boolean existsById(String id);

}
