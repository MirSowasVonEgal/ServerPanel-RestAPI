package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.ProxmoxManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Network;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.model.VServer;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.NetworkRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.PriceRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.UserRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.VServerRepository;
import org.apache.catalina.Server;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Projekt: RestAPI
 * @Created: 14.11.2020
 * @By: MirSowasVonEgal | Timo
 */


@RestController
@RequestMapping("/v1/server")
public class VServerController {

    @Autowired
    private VServerRepository vServerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private PriceRepository priceRepository;

    @PostMapping("/vserver")
    public Object createVServer(@RequestBody VServer vServer) {
        if (vServer.getUserId() == null) return new Status("Bitte gebe einen Benutzer an!", 500);
        if (vServer.getPassword() == null) return new Status("Bitte gebe das Passwort an!", 500);
        if (userRepository.findUserById(vServer.getUserId()).size() == 0) return new Status("Benutzer nicht gefunden!", 500);
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
        if (vServer.getServerId() == null) vServer.setServerId(ServerId);
        if (vServer.getCores() <= 0) vServer.setCores(1);
        if (vServer.getMemory() <= 0) vServer.setMemory(512);
        if (vServer.getDisk() <= 0) vServer.setDisk(10);
        if (vServer.getPrice() <= 0) vServer.setPrice(1.45);
        ProxmoxManager pm = new ProxmoxManager();
        String Node = pm.getNextNode();
        vServer.setNode(Node);
        vServerRepository.save(vServer);
        Network finalNetwork = network;

        new Thread(() -> {
            pm.createLXC(new ProxmoxManager().getNextId(), vServer.getCores(), vServer.getMemory(), vServer.getDisk(), vServer.getPassword(), finalNetwork, Node, vServer);
        }).start();

        return vServer;
    }
    /*
    @PutMapping("/network/{id}")
    public Object updateVServer(@RequestBody Network network, @PathVariable String id) {
        if (networkRepository.findNetworkById(id).size() == 0) return new Status("Diese IP-Adresse wurde nicht gefunden!", 500);
        Network oldNetwork = networkRepository.findNetworkById(id).get(0);
        if(networkRepository.existsByIp(network.getIp()) && (!networkRepository.findNetworkByIp(network.getIp()).get(0).equals(network)))
            return new Status("Diese IP ist bereits im System!", 500);
        if (network.getIp() != null) oldNetwork.setIp(network.getIp());
        if (network.getType() != null) oldNetwork.setType(network.getType());
        if (network.getServerId() != null) oldNetwork.setServerId(network.getServerId());
        networkRepository.save(oldNetwork);
        return oldNetwork;
    }
   */


    @PutMapping("/vserver/{message}/{date}/status")
    public Object setStatusVServer(@RequestBody VServer server, @PathVariable String message, @PathVariable long date) {
        VServer vServer = vServerRepository.findVServerByServerId(server.getId()).get(0);
        vServer.setStatus(message);
        vServer.setStatusdate(date);
        vServerRepository.save(vServer);
        return vServer;
    }

    @GetMapping("/vserver/{id}")
    public Object getVServer(@PathVariable String id) {
        if (vServerRepository.findVServerById(id).size() == 0) return new Status("Dieser VServer wurde nicht gefunden!", 500);
        return vServerRepository.findVServerById(id).get(0);
    }

    @GetMapping("/vserver/{id}/info")
    public Object getVServerInfo(@PathVariable String id) {
        if (vServerRepository.findVServerById(id).size() == 0) return new Status("Dieser VServer wurde nicht gefunden!", 500);
        VServer vServer = vServerRepository.findVServerById(id).get(0);
        return new ProxmoxManager().getLXC(vServer).toString();
    }

    @PostMapping("/vserver/{id}/stop")
    public Object stopVServer(@PathVariable String id) {
        if (vServerRepository.findVServerByServerId(id).size() == 0) return new Status("Dieser VServer wurde nicht gefunden!", 500);
        VServer vServer = vServerRepository.findVServerByServerId(id).get(0);
        return new ProxmoxManager().stopLXC(vServer);
    }

    @PostMapping("/vserver/{id}/start")
    public Object startVServer(@PathVariable String id) {
        if (vServerRepository.findVServerByServerId(id).size() == 0) return new Status("Dieser VServer wurde nicht gefunden!", 500);
        VServer vServer = vServerRepository.findVServerByServerId(id).get(0);
        return new ProxmoxManager().startLXC(vServer);
    }

    @PostMapping("/vserver/{id}/restart")
    public Object restartVServer(@PathVariable String id) {
        if (vServerRepository.findVServerByServerId(id).size() == 0) return new Status("Dieser VServer wurde nicht gefunden!", 500);
        VServer vServer = vServerRepository.findVServerByServerId(id).get(0);
        return new ProxmoxManager().restartLXC(vServer);
    }

    @PostMapping("/vserver/{id}/kill")
    public Object killServer(@PathVariable String id) {
        if (vServerRepository.findVServerByServerId(id).size() == 0) return new Status("Dieser VServer wurde nicht gefunden!", 500);
        VServer vServer = vServerRepository.findVServerByServerId(id).get(0);
        return new ProxmoxManager().killLXC(vServer);
    }

    @GetMapping("/vserver/{id}/network")
    public Object getVServerNetwork(@PathVariable String id) {
        if (vServerRepository.findVServerById(id).size() == 0) return new Status("Dieser VServer wurde nicht gefunden!", 500);
        return networkRepository.findNetworkByServerId(vServerRepository.findVServerById(id).get(0).getServerId());
    }

    @GetMapping("/vserver/user/{id}")
    public Object getVServerFromUser(@PathVariable String id) {
        HashMap<String, Double> prices = priceRepository.findPriceByProduct("VServer").get(0).getPrice();
        JSONArray vServerArray = new JSONArray();
        for (VServer vServer : vServerRepository.findVServerByUserId(id)) {
            JSONObject vServerObject = new JSONObject();
            vServerObject.put("id", vServer.getId());
            vServerObject.put("userid", vServer.getUserId());
            vServerObject.put("serverid", vServer.getServerId());
            vServerObject.put("password", vServer.getPassword());
            vServerObject.put("memory", vServer.getMemory());
            vServerObject.put("status", vServer.getStatus());
            vServerObject.put("statusdate", vServer.getStatusdate());
            vServerObject.put("paidup", vServer.getPaidup());
            vServerObject.put("disk", vServer.getDisk());
            vServerObject.put("cores", vServer.getCores());
            vServerObject.put("price", vServer.getPrice());
            vServerObject.put("node", vServer.getNode());
            vServerObject.put("proxmox", new ProxmoxManager().getLXC(vServer));
            vServerArray.put(vServerObject);
        }
        return vServerArray.toString();
    }

    @GetMapping("/vserver")
    public List<VServer> getUser()
    {
        return vServerRepository.findAll();
    }
}
