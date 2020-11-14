package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.MailManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Token;
import me.mirsowasvonegal.serverpanel.RestAPI.model.User;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.TokenRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.UserRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.MD5;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @PostMapping("/login")
    public Object loginUser(@RequestBody User user) throws IOException, MessagingException {
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
        if (!current.getConfirmed().equals("true")) {
            new MailManager().sendRegisterMail(current.getId(), current.getConfirmed(), current.getEmail());
            return new Status("Deine E-Mail muss erst bestätigt werden!", 500);
        }
        Token token = new Token();
        token.setUserid(current.getId());
        token.setTime(System.currentTimeMillis());
        token.setToken(RandomString.generate(100));
        tokenRepository.save(token);
        updateTokens();
        return token;
    }

    @PostMapping("/register")
    public Object registerUser(@RequestBody User user) throws IOException, MessagingException {
        if (user.getUsername() == null) return new Status("Bitte gebe einen Nutzernamen ein!", 500);
        if (user.getEmail() == null) return new Status("Bitte gebe eine E-Mail ein!", 500);
        if (user.getPassword() == null) return new Status("Bitte gebe ein Password ein!", 500);
        if (user.getUsername().split("").length <= 3) return new Status("Bitte gebe einen Nutzernamen mit min. 4 Zeichen ein!", 500);
        if (user.getEmail().split("").length <= 5) return new Status("Bitte gebe eine E-Mail mit min. 6 Zeichen ein!", 500);
        if (user.getPassword().split("").length <= 7) return new Status("Bitte gebe ein Password mit min. 8 Zeichen ein!", 500);
        if(userRepository.existsByUsername(user.getUsername())) return new Status("Dieser Nutzername ist bereits vergeben!", 500);
        if(userRepository.existsByEmail(user.getEmail())) return new Status("Diese E-Mail ist bereits vergeben!", 500);
        if(user.getRankname() == null) user.setRankname("User");
        user.setPassword(MD5.hash(user.getPassword()));
        user.setRankid(1);
        user.setConfirmed(RandomString.generate(32));
        userRepository.save(user);
        new MailManager().sendRegisterMail(user.getId(), user.getConfirmed(), user.getEmail());
        return user;
    }

    @GetMapping("/token/{token}/{id}")
    public Object updateUser(@PathVariable String token, @PathVariable String id) {
        if (userRepository.findUserById(id).size() == 0) return new Status("Dieser Account wurde nicht gefunden!", 500);
        User current = userRepository.findUserById(id).get(0);
        if (current.getConfirmed().equals(token)) {
            current.setConfirmed("true");
            userRepository.save(current);
            return new Status("Dein Account wurde Aktiviert!", 200);
        } else if(current.getConfirmed().equals("true")) {
            return new Status("Dein Account ist schon Aktiv", 500);
        } else {
            return new Status("Dieser Token ist nicht gültig!", 500);
        }
    }

    public void updateTokens() {
        List<Token> tokens =  tokenRepository.findAll();
        for (Token current : tokens) {
            long time = System.currentTimeMillis() - 86400000;
            if (current.getTime() < time) {
                tokenRepository.delete(current);
            }
        }
    }

}
