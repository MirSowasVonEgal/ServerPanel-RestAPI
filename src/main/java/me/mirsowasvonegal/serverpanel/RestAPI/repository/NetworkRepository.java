package me.mirsowasvonegal.serverpanel.RestAPI.repository;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Network;
import me.mirsowasvonegal.serverpanel.RestAPI.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Projekt: RestAPI
 * @Created: 14.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public interface NetworkRepository extends MongoRepository<Network, Integer> {
    List<Network> findNetworkById(String id);

    List<Network> findNetworkByIp(String ip);

    List<Network> findNetworkByServerId(String serverId);

    List<Network> findNetworkByType(String type);

    Boolean existsById(String id);

    Boolean existsByIp(String ip);

    Boolean existsByMacaddress(String macaddress);

    Boolean existsByServerId(String serverId);

    Boolean existsByType(String type);
}
