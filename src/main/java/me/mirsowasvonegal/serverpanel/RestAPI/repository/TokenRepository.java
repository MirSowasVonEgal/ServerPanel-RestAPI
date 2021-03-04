package me.mirsowasvonegal.serverpanel.RestAPI.repository;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Ticket;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Token;
import me.mirsowasvonegal.serverpanel.RestAPI.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TokenRepository extends MongoRepository<Token, Integer> {

    List<Token> findTokenByToken(String token);

}
