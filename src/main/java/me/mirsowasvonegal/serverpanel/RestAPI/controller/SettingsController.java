package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.ProxmoxManager;
import me.mirsowasvonegal.serverpanel.RestAPI.manager.SettingsManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Setting;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.SettingsRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/system")
public class SettingsController {

    @Autowired
    public SettingsRepository settingsRepository;

    @PostMapping("/settings")
    public Object addSettings(@RequestBody Setting setting) {
        settingsRepository.save(setting);
        return setting;
    }

    @GetMapping("/settings")
    public Object getAll() {
        JSONArray array = new JSONArray(settingsRepository.findAll());
        JSONArray newArray = new JSONArray();
        array.forEach((Object current) -> {
            JSONObject object = (JSONObject) current;
            object.getJSONObject("value").getJSONObject("auth").remove("key");
            object.getJSONObject("value").getJSONObject("auth").remove("password");
            newArray.put(object);
        });
        return newArray.toString();
    }


    @GetMapping("/settings/debug")
    public Object getDebug() {
        return new ProxmoxManager();
    }

}
