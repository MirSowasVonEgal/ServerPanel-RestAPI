package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Token;
import me.mirsowasvonegal.serverpanel.RestAPI.model.User;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.TokenRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.UserRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.MD5;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @PostMapping("/login")
    public Object addToken(@RequestBody User user) {
        if (user.getUsername() == null) return new Status("Bitte gebe einen Nutzernamen/E-Mail ein!", 500);
        if (user.getPassword() == null) return new Status("Bitte gebe ein Password ein!", 500);
        User current = null;
        if (userRepository.findUserByUsername(user.getUsername()).size() != 0) {
            current = userRepository.findUserByUsername(user.getUsername()).get(0);
        } else if(userRepository.findUserByEmail(user.getUsername()).size() != 0) {
            current = userRepository.findUserByEmail(user.getUsername()).get(0);
        }
        if (current == null) return new Status("Diesen Nutzer gibt es nicht!", 500);
        if (!current.getPassword().equals(MD5.hash(user.getPassword()))) return new Status("Das Passwort stimmt nicht überein!", 500);
        Token token = new Token();
        token.setUserid(current.getId());
        token.setTime(System.currentTimeMillis());
        token.setToken(RandomString.generate(100));
        tokenRepository.save(token);
        return token;
    }

}
