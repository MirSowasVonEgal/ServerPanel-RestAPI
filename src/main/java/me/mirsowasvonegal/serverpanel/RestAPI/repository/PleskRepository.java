package me.mirsowasvonegal.serverpanel.RestAPI.repository;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Plesk;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Price;
import me.mirsowasvonegal.serverpanel.RestAPI.model.VServer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PleskRepository extends MongoRepository<Plesk, Integer> {

    List<Plesk> findPleskById(String id);

    List<Plesk> findPleskByUsername(String username);

    List<Plesk> findPleskByUserid(String userid);

}