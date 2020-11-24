package me.mirsowasvonegal.serverpanel.RestAPI.repository;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Price;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Token;
import me.mirsowasvonegal.serverpanel.RestAPI.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Projekt: RestAPI
 * @Created: 20.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public interface PriceRepository extends MongoRepository<Price, Integer> {

    List<Price> findPriceByProduct(String product);

    List<Price> findPriceById(String id);

    Boolean existsByProduct(String product);

}

