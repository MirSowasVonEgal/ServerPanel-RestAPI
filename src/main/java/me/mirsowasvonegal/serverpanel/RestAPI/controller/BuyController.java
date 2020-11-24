package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.ProxmoxManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Network;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.model.User;
import me.mirsowasvonegal.serverpanel.RestAPI.model.VServer;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.NetworkRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.PriceRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.UserRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.VServerRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.MD5;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.RandomString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @Projekt: RestAPI
 * @Created: 20.11.2020
 * @By: MirSowasVonEgal | Timo
 */

@RestController
@RequestMapping("/system")
public class BuyController {

    @Autowired
    private VServerRepository vServerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private PriceRepository priceRepository;

    @PostMapping("/buy/vserver")
    public Object getPrice(@RequestBody VServer vServer) {
        if (vServer.getMemory() < 512) return new Status("Der kleinste Server unterstützt min. 512 MB Ram!", 500);
        if (vServer.getCores() == 0) return new Status("Der kleinste Server unterstützt min. einen Kern!", 500);
        if (vServer.getCores() > 4) return new Status("Der größte Server unterstützt max. vier Kerne!", 500);
        if (vServer.getDisk() < 10) return new Status("Der kleinste Server unterstützt min. 10 GB Festplatte!", 500);
        if (vServer.getDisk() > 200) return new Status("Der größte Server unterstützt max. 200 GB Festplatte!", 500);
        HashMap<String, Double> prices = priceRepository.findPriceByProduct("VServer").get(0).getPrice();
        Double price = 1.0;
        price = price + ((vServer.getMemory() * prices.get("RAM")) / 1024);
        price = price + (vServer.getCores() * prices.get("CORES"));
        price = price + (vServer.getDisk() * prices.get("DISK"));
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Price", price);
        return jsonObject.toString();
    }

    @PostMapping("/buy/vserver/{id}")
    public Object buyVServer(@RequestBody VServer vServer, @PathVariable String id) {
        if (userRepository.findUserById(id).size() == 0) return new Status("Benutzer nicht gefunden!", 500);
        User user = userRepository.findUserById(id).get(0);
        if (vServer.getMemory() < 512) return new Status("Der kleinste Server unterstützt min. 512 MB Ram!", 500);
        if (vServer.getCores() == 0) return new Status("Der kleinste Server unterstützt min. einen Kern!", 500);
        if (vServer.getCores() > 4) return new Status("Der größte Server unterstützt max. vier Kerne!", 500);
        if (vServer.getDisk() < 10) return new Status("Der kleinste Server unterstützt min. 10 GB Festplatte!", 500);
        if (vServer.getDisk() > 200) return new Status("Der größte Server unterstützt max. 200 GB Festplatte!", 500);
        if (vServer.getPassword() == null) return new Status("Du musst dem Server ein Passwort vergeben!", 500);
        HashMap<String, Double> prices = priceRepository.findPriceByProduct("VServer").get(0).getPrice();
        Double price = 1.0;
        price = price + ((vServer.getMemory() * prices.get("RAM")) / 1024);
        price = price + (vServer.getCores() * prices.get("CORES"));
        price = price + (vServer.getDisk() * prices.get("DISK"));
        if (user.getCredit() >= price) {
            vServer.setUserId(user.getId());
            ProxmoxManager proxmoxManager = new ProxmoxManager();
            String ServerId = proxmoxManager.getNextId();
            String IPv4 = null;
            for (Network current : networkRepository.findNetworkByType("IPv4")) {
                if (current.getServerId() == null) {
                    IPv4 = current.getIp();
                    current.setServerId(ServerId + "");
                    networkRepository.save(current);
                    break;
                }
            }
            if (IPv4 == null) return new Status("Es wurde keine frei IPv4 Adresse gefunden!", 500);
            user.setCredit(user.getCredit() - price);
            userRepository.save(user);
            if (vServer.getServerId() == null) vServer.setServerId(ServerId);
            if (vServer.getCores() <= 0) vServer.setCores(1);
            if (vServer.getMemory() <= 0) vServer.setMemory(512);
            if (vServer.getDisk() <= 0) vServer.setDisk(10);
            vServerRepository.save(vServer);
            String finalIPv4 = IPv4;
            new Thread(() -> {
                new ProxmoxManager().createLXC(new ProxmoxManager().getNextId(), vServer.getCores(), vServer.getMemory(), vServer.getDisk(), vServer.getPassword(), finalIPv4);
            }).start();
            return new Status("Dein Server wird nun erstellt in wenigen Minuten solltest du auf ihn zugreifen können!", 200);
        } else {
            return new Status("Du hast zu wenig Guthaben!", 500);
        }
    }

}
