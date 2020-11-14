package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.ProxmoxManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Network;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.model.VServer;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.VServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Projekt: RestAPI
 * @Created: 14.11.2020
 * @By: MirSowasVonEgal | Timo
 */
@RestController
@RequestMapping("/system")
public class VServerController {

    @Autowired
    private VServerRepository vServerRepository;

    /*@PostMapping("/vserver")
    public Object createVServer(@RequestBody VServer vServer) {
        ProxmoxManager proxmoxManager = new ProxmoxManager();
        if (vServer.getUserId() == null) return new Status("Bitte gebe einen Benutzer an!", 500);
        if (vServer.getPassword() == null) return new Status("Bitte gebe das Passwort an!", 500);
        if (vServer.getServerId() == 0) vServer.setServerId(proxmoxManager.getNextId());
        if (vServer.)
        networkRepository.save(network);
        return network;
    }

    @PutMapping("/network/{id}")
    public Object updateIPAddress(@RequestBody Network network, @PathVariable String id) {
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

    @GetMapping("/network/{id}")
    public Object getIPAddress(@PathVariable String id) {
        if (networkRepository.findNetworkById(id).size() == 0) return new Status("Diese IP-Adresse wurde nicht gefunden!", 500);
        return networkRepository.findNetworkById(id).get(0);
    }

    @GetMapping("/network/getNetworkByServerId/{serverId}")
    public List<Network> getNetworkByServerId(@PathVariable String serverId) {
        return networkRepository.findNetworkByServerId(serverId);
    }

    @GetMapping("/network/getNetworkByIp/{ip}")
    public Object getNetworkByIp(@PathVariable String ip) {
        if (networkRepository.findNetworkByIp(ip).size() == 0) return new Status("Diese IP-Adresse wurde nicht gefunden!", 500);
        return networkRepository.findNetworkByIp(ip).get(0);
    }

    @GetMapping("/network/getNetworkByType/{type}")
    public List<Network> getNetworkByType(@PathVariable String type) {
        return networkRepository.findNetworkByType(type);
    }

    @GetMapping("/network/getNetworkNextIP/{type}")
    public Object getNetworkNextIP(@PathVariable String type) {
        for (Network current : networkRepository.findNetworkByType(type))
            if (current.getServerId() == null) return current;
        return new Status("Es wurde keine freie IP-Adresse gefunden!", 500);
    }

    @GetMapping("/network")
    public List<Network> getUser() {
        return networkRepository.findAll();
    }*/
}
