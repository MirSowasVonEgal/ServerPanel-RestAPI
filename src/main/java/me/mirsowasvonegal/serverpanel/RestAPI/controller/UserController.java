package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.model.User;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.UserRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository repository;

    @PostMapping("/addUser")
    public Object addUser(@RequestBody User user) {
        if (user.getUsername() == null) return new Status("Bitte gebe einen Nutzernamen ein!", 500);
        if (user.getEmail() == null) return new Status("Bitte gebe eine E-Mail ein!", 500);
        if (user.getPassword() == null) return new Status("Bitte gebe ein Password ein!", 500);
        if (user.getUsername().split("").length <= 3) return new Status("Bitte gebe einen Nutzernamen mit min. 4 Zeichen ein!", 500);
        if (user.getEmail().split("").length <= 5) return new Status("Bitte gebe eine E-Mail mit min. 6 Zeichen ein!", 500);
        if (user.getPassword().split("").length <= 7) return new Status("Bitte gebe ein Password mit min. 8 Zeichen ein!", 500);
        if(repository.existsByUsername(user.getUsername())) return new Status("Dieser Nutzername ist bereits vergeben!", 500);
        if(repository.existsByEmail(user.getEmail())) return new Status("Diese E-Mail ist bereits vergeben!", 500);
        if(user.getRankname() == null) user.setRankname("User");
        user.setPassword(MD5.hash(user.getPassword()));
        repository.save(user);
        return user;
    }

    @GetMapping("/getUser/{id}")
    public List<User> getUser(@PathVariable String id) {
        return repository.findUserById(id);
    }

    @GetMapping("/getUserByUsername/{username}")
    public List<User> getUserByUsername(@PathVariable String username) {
        return repository.findUserByUsername(username);
    }

    @GetMapping("/getUserByEmail/{email}")
    public List<User> getUserByEmail(@PathVariable String email) {
        return repository.findUserByEmail(email);
    }

    @GetMapping("/getUsers")
    public List<User> getUser() {
        return repository.findAll();
    }
}
