package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.ProxmoxManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Network;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.model.User;
import me.mirsowasvonegal.serverpanel.RestAPI.model.VServer;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.NetworkRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.VServerRepository;
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
@RequestMapping("/v1/system")
public class NetworkController {

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private VServerRepository vServerRepository;

    @PostMapping("/network")
    public Object addIPAddress(@RequestBody Network network) {
        if (network.getIp() == null) return new Status("Bitte gebe die IP-Adresse an!", 500);
        if (network.getType() == null) return new Status("Bitte gebe den Typen an! (IPv4/IPv6)", 500);
        if (network.getGateway() == null) return new Status("Bitte gebe das Gateway an!", 500);
        if(networkRepository.existsByIp(network.getIp())) return new Status("Diese IP-Adresse ist bereits vergeben!", 500);
        if(networkRepository.existsByMacaddress(network.getMacaddress())) return new Status("Diese Mac Adresse ist bereits vergeben!", 500);
        networkRepository.save(network);
        return network;
    }

    @PostMapping("/network/addMultipleIPAddresses")
    public Object addMultipleIPAddresses(@RequestBody List<Network> network) {
        if (network.get(0).getType() == null) return new Status("Bitte gebe den Typen an! (IPv4/IPv6)", 500);
        if (network.get(0).getGateway() == null) return new Status("Bitte gebe den Gateway an!", 500);
        /*for (Network current : network) {
            if (current.getIp().contains("-")) {
                String[] ipNet = current.getIp().split("-");
                if (Utils.isInt(ipNet[0]) && Utils.isInt(ipNet[1])) {

                }
            }
        }*/
        for (Network current : network) {
            if (current.getType() == null) current.setType(network.get(0).getType());
            if (current.getGateway() == null) current.setGateway(network.get(0).getGateway());
            if (current.getBridge() == null) current.setBridge(network.get(0).getBridge());
        }
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
        if (network.getGateway() != null) oldNetwork.setGateway(network.getGateway());
        if (network.getBridge() != null) oldNetwork.setBridge(network.getBridge());
        if (network.getMacaddress() != null) oldNetwork.setMacaddress(network.getMacaddress());
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


    @PostMapping("/network/server/add/{serverid}/{type}")
    public Object addServerIP(@PathVariable String type, @PathVariable String serverid) {
        if (vServerRepository.findVServerByServerId(serverid).size() == 0) return new Status("Dieser VServer wurde nicht gefunden!", 500);
        VServer vServer = vServerRepository.findVServerByServerId(serverid).get(0);
        String VServerInfo = new ProxmoxManager().getVServerConfigInfo(vServer).getJSONObject("data").toString();

        Network network = new Network(null,null,null,null,null,null,null);
        if(type.equals("IPv4")) {

        } else {
            network.setBridge("vmbr0");
            network.setGateway("2a01:4f8:140:605e::1");
            network.setType("IPv6");
            network.setServerId(serverid);
            if(VServerInfo.contains(":" + serverid + "::")) {
                for (int i = 1; i < 999999; i++) {
                    if(!VServerInfo.contains(":" + serverid + ":" + i)) {
                        network.setIp("2a01:4f8:140:605e:" + serverid + ":" + i + "::/80");
                        break;
                    }
                }
            } else {
                network.setIp("2a01:4f8:140:605e:" + serverid + "::/80");
            }
        }

        new ProxmoxManager().addIPAddress(vServer, network);

        networkRepository.save(network);
        return network;
    }

}
