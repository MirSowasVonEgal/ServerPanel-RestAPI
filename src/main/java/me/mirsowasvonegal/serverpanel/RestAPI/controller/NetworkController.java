package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Network;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.model.User;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.NetworkRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.MD5;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.RandomString;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.Utils;
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
public class NetworkController {

    @Autowired
    private NetworkRepository networkRepository;

    @PostMapping("/network")
    public Object addIPAddress(@RequestBody Network network) {
        if (network.getIp() == null) return new Status("Bitte gebe die IP-Adresse an!", 500);
        if (network.getType() == null) return new Status("Bitte gebe den Typen an! (IPv4/IPv6)", 500);
        if(networkRepository.existsByIp(network.getIp())) return new Status("Diese IP-Adresse ist bereits vergeben!", 500);
        networkRepository.save(network);
        return network;
    }

    @PostMapping("/network/addMultipleIPAddresses")
    public Object addMultipleIPAddresses(@RequestBody List<Network> network) {
        if (network.get(0).getType() == null) return new Status("Bitte gebe den Typen an! (IPv4/IPv6)", 500);
        /*for (Network current : network) {
            if (current.getIp().contains("-")) {
                String[] ipNet = current.getIp().split("-");
                if (Utils.isInt(ipNet[0]) && Utils.isInt(ipNet[1])) {

                }
            }
        }*/
        for (Network current : network)
            if (current.getType() == null) current.setType(network.get(0).getType());
        // Removes the duplicates
        List<Network> newNetwork = network.stream().distinct().collect(Collectors.toList());
        newNetwork.removeIf(current -> networkRepository.existsByIp(current.getIp()) || current.getIp() == null);
        if (newNetwork.size() == 0) return new Status("Es wurden keine neuen IP-Adressen hinzugefügt!", 500);
        networkRepository.saveAll(newNetwork);
        return newNetwork;
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
    }

}
