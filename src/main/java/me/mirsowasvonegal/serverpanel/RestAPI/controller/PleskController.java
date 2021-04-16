package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.PleskManager;
import me.mirsowasvonegal.serverpanel.RestAPI.manager.ProxmoxManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.*;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.PleskRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.SettingsRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.UserRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.RandomString;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/v1/product")
public class PleskController {

    @Autowired
    public PleskRepository pleskRepository;

    @Autowired
    public UserRepository userRepository;

    @PostMapping("/plesk/{plan}/{type}")
    public Object addPlesk(@RequestBody User userid, @PathVariable String plan, @PathVariable String type) {
        if (userRepository.findUserById(userid.getId()).size() == 0) return new Status("Benutzer nicht gefunden!", 500);
        User user = userRepository.findUserById(userid.getId()).get(0);
        Plesk plesk = new Plesk(null, user.getId(), plan, user.getUsername(), RandomString.generatePassword(16), user.getEmail(), 0.5D,999999999999999L, 0L, null, type,  null, 0);
        pleskRepository.save(plesk);
        new Thread(() -> {
            Object object = new PleskManager().createUser(plesk.getUsername().toLowerCase(), plesk.getPassword(), plesk.getEmail(), plesk.getType(), plesk.getPlan());
            if (object == null) return;
            JSONObject jsonUser = (JSONObject) object;
            plesk.setGuid(jsonUser.getString("guid"));
            plesk.setUid(jsonUser.getInt("id"));
            pleskRepository.save(plesk);
        }).start();
        return plesk;
    }

    @GetMapping("/plesk")
    public Object getAll() {
        return pleskRepository.findAll();
    }


    @GetMapping("/plesk/user/{id}")
    public Object getPleskFromUser(@PathVariable String id) {
        JSONArray vServerArray = new JSONArray();
        for (Plesk plesk : pleskRepository.findPleskByUserid(id)) {
            JSONObject vServerObject = new JSONObject();
            vServerObject.put("id", plesk.getId());
            vServerObject.put("status", plesk.getStatus());
            vServerObject.put("statusdate", plesk.getStatusdate());
            vServerObject.put("paidup", plesk.getPaidup());
            vServerObject.put("price", plesk.getPrice());
            vServerArray.put(vServerObject);
        }

        return vServerArray.toString();
    }


    @GetMapping("/plesk/{id}")
    public Object getPlesk(@PathVariable String id) {
        if (pleskRepository.findPleskById(id).size() == 0) return new Status("Dieser Plesk WebHost wurde nicht gefunden!", 500);
        return pleskRepository.findPleskById(id).get(0);
    }

    @GetMapping("/plesk/{id}/autologin")
    public Object getPleskLink(@PathVariable String id) {
        if (pleskRepository.findPleskById(id).size() == 0) return new Status("Dieser Plesk WebHost wurde nicht gefunden!", 500);
        return new PleskManager().getLoginLink(pleskRepository.findPleskById(id).get(0).getUsername().toLowerCase());
    }


}
