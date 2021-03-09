package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.ProxmoxManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.*;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.NetworkRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.PriceRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.UserRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.VServerRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.MD5;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.RandomString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

/**
 * @Projekt: RestAPI
 * @Created: 20.11.2020
 * @By: MirSowasVonEgal | Timo
 */

@RestController
@RequestMapping("/v1/system")
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
        if (vServer.getDisk() < 10) return new Status("Der kleinste Server unterstützt min. 10 GB Festplatte!", 500);
        HashMap<String, Double> prices = priceRepository.findPriceByProduct("VServer").get(0).getPrice();
        Double price = 0.5;
        price = BigDecimal.valueOf(price).add(BigDecimal.valueOf(BigDecimal.valueOf(vServer.getMemory()).multiply(BigDecimal.valueOf(BigDecimal.valueOf(prices.get("RAM")).divide(BigDecimal.valueOf(1024)).doubleValue())).doubleValue())).doubleValue();
        price = BigDecimal.valueOf(price).add(BigDecimal.valueOf(BigDecimal.valueOf(vServer.getCores()).multiply(BigDecimal.valueOf(prices.get("CORES"))).doubleValue())).doubleValue();
        price = BigDecimal.valueOf(price).add(BigDecimal.valueOf(BigDecimal.valueOf(vServer.getDisk()).multiply(BigDecimal.valueOf(prices.get("DISK"))).doubleValue())).doubleValue();
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
        if (vServer.getPassword() == null) vServer.setPassword(RandomString.generate(12));
        HashMap<String, Double> prices = priceRepository.findPriceByProduct("VServer").get(0).getPrice();
        Double price = 0.5;
        price = BigDecimal.valueOf(price).add(BigDecimal.valueOf(BigDecimal.valueOf(vServer.getMemory()).multiply(BigDecimal.valueOf(BigDecimal.valueOf(prices.get("RAM")).divide(BigDecimal.valueOf(1024)).doubleValue())).doubleValue())).doubleValue();
        price = BigDecimal.valueOf(price).add(BigDecimal.valueOf(BigDecimal.valueOf(vServer.getCores()).multiply(BigDecimal.valueOf(prices.get("CORES"))).doubleValue())).doubleValue();
        price = BigDecimal.valueOf(price).add(BigDecimal.valueOf(BigDecimal.valueOf(vServer.getDisk()).multiply(BigDecimal.valueOf(prices.get("DISK"))).doubleValue())).doubleValue();
        if (user.getCredits() >= price) {
            vServer.setUserId(user.getId());
            ProxmoxManager proxmoxManager = new ProxmoxManager();
            String ServerId = proxmoxManager.getNextId();
            Network network = null;
            for (Network current : networkRepository.findNetworkByType("IPv4")) {
                if (current.getServerId() == null) {
                    current.setServerId(ServerId + "");
                    networkRepository.save(current);
                    network = current;
                    break;
                }
            }
            if (network == null) return new Status("Es wurde keine frei IPv4 Adresse gefunden!", 500);
            user.setCredits(BigDecimal.valueOf(user.getCredits()).subtract(BigDecimal.valueOf(price)).doubleValue());
            userRepository.save(user);
            if (vServer.getServerId() == null) vServer.setServerId(ServerId);
            if (vServer.getCores() <= 0) vServer.setCores(1);
            if (vServer.getMemory() <= 0) vServer.setMemory(512);
            if (vServer.getDisk() <= 0) vServer.setDisk(10);
            if (vServer.getPaidup() == 0) vServer.setPaidup(System.currentTimeMillis() + (86400000L  * 30) + 3600000L);
            if(vServer.getPrice() == 0) vServer.setPrice(price);
            vServer.setStatus("Installation");
            vServer.setStatusdate(System.currentTimeMillis() + 25000L);
            ProxmoxManager pm = new ProxmoxManager();
            String Node = pm.getNextNode();
            vServer.setNode(Node);
            vServerRepository.save(vServer);
            Network finalNetwork = network;
            new Thread(() -> {
                pm.createLXC(new ProxmoxManager().getNextId(), vServer.getCores(), vServer.getMemory(), vServer.getDisk(), vServer.getPassword(), finalNetwork, Node, vServer);
            }).start();
            return vServer;
        } else {
            return new Status("Du hast zu wenig Guthaben!", 500);
        }
    }

    @PutMapping("/buy/vserver/{time}/time")
    public Object extendVServer(@RequestBody VServer server, @PathVariable Integer time) {
        VServer vServer = vServerRepository.findVServerById(server.getId()).get(0);
        if (userRepository.findUserById(vServer.getUserId()).size() == 0) return new Status("Benutzer nicht gefunden!", 500);
        User user = userRepository.findUserById(vServer.getUserId()).get(0);
        double price = BigDecimal.valueOf(vServer.getPrice()).multiply(BigDecimal.valueOf(time)).doubleValue();
        if (user.getCredits() >= price) {
            user.setCredits(BigDecimal.valueOf(user.getCredits()).subtract(BigDecimal.valueOf(price)).doubleValue());
            userRepository.save(user);
            vServer.setPaidup(vServer.getPaidup() + ((86400000L  * 30) * time));
            vServerRepository.save(vServer);
            return vServer;
        } else {
            return new Status("Du hast zu wenig Guthaben!", 500);
        }
    }
}
