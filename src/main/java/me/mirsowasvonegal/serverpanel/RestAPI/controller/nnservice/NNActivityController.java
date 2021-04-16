package me.mirsowasvonegal.serverpanel.RestAPI.controller.nnservice;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.ProxmoxManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Setting;
import me.mirsowasvonegal.serverpanel.RestAPI.model.nnservice.NNActivity;
import me.mirsowasvonegal.serverpanel.RestAPI.model.nnservice.NNActivitysList;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.SettingsRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.nnservice.NNActivityRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/v1/nnservice")
public class NNActivityController {

    @Autowired
    public NNActivityRepository nnActivityRepository;

    @GetMapping("/nnactivity")
    public Object getNNActivitys() {
        return nnActivityRepository.findAll();
    }

    @GetMapping("/nnactivity/{userid}")
    public Object getNNActivityUser(@PathVariable String userid) {
        return nnActivityRepository.findNNActivityByUserid(userid);
    }

    @PostMapping("/nnactivity/{userid}")
    public Object addActivity(@RequestBody NNActivitysList NNActivitysList, @PathVariable String userid) {
        NNActivity nnActivity = nnActivityRepository.findNNActivityByUserid(userid).get(0);
        nnActivity.getActivitys().add(NNActivitysList);
        double current_balance = nnActivity.getBalance();
        double balance = 0;
        balance = BigDecimal.valueOf(current_balance).add(BigDecimal.valueOf(NNActivitysList.getAmount())).doubleValue();
        nnActivity.setBalance(balance);
        nnActivityRepository.save(nnActivity);
        return nnActivity;
    }

    @PostMapping("/nnactivity/user")
    public Object addUser(@RequestBody NNActivity nnActivity) {
        nnActivityRepository.save(nnActivity);
        return nnActivity;
    }

}
