package me.mirsowasvonegal.serverpanel.RestAPI.manager;

import it.corsinvest.proxmoxve.api.PveClient;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Network;
import me.mirsowasvonegal.serverpanel.RestAPI.model.VServer;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Projekt: RestAPI
 * @Created: 13.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public class ProxmoxManager {

    public static void main(String[] args) {
        //new FeaturesManager("123").updateLXC();
        //new ProxmoxManager().createLXC(new ProxmoxManager().getNextId(), 1, 512, 10, "Test123", "192.168.2.222");
        System.out.println(new ProxmoxManager().getNextNode());
    }

    PveClient client = new PveClient("prox01.shademc.de", 443);

    public ProxmoxManager() {
        client.login("root", "Timo0580", "pam");
    }

    public String getNextId() {
        return client.getCluster().getNextid().nextid().getResponse().getInt("data") + "";
    }

    public JSONObject getLXC(VServer vServer) {
        return client.getNodes().get(vServer.getNode()).getLxc().get(vServer.getServerId()).getStatus().getCurrent().vmStatus().getResponse();
    }

    public String getNextNode() {
        long lowest = 999999999999999999L;
        JSONObject currentFinal = null;

        for (Object currentObject : client.getNodes().getRest().getResponse().getJSONArray("data")) {
            JSONObject current = (JSONObject) currentObject;
            if(current.getLong("mem") < lowest) {
                lowest = current.getLong("mem");
                currentFinal = current;
            } else {
            }
        }
        if(currentFinal == null) return null;
        return currentFinal.getString("node");
    }

    public JSONObject stopLXC(VServer vServer) {
        return client.getNodes().get(vServer.getNode()).getLxc().get(vServer.getServerId()).getStatus().getShutdown().vmShutdown().getResponse();
    }

    public JSONObject startLXC(VServer vServer) {
        return client.getNodes().get(vServer.getNode()).getLxc().get(vServer.getServerId()).getStatus().getStart().vmStart().getResponse();
    }

    public JSONObject restartLXC(VServer vServer) {
        return client.getNodes().get(vServer.getNode()).getLxc().get(vServer.getServerId()).getStatus().getReboot().vmReboot().getResponse();
    }

    public JSONObject killLXC(VServer vServer) {
        return client.getNodes().get(vServer.getNode()).getLxc().get(vServer.getServerId()).getStatus().getStop().vmStop().getResponse();
    }

    public JSONObject getVServerConfigInfo(VServer vServer) {
        return client.getNodes().get(vServer.getNode()).getLxc().get(vServer.getServerId()).getConfig().getRest().getResponse();
    }

    public void addIPAddress(VServer vServer, Network network) {
        String VServerInfo = client.getNodes().get(vServer.getNode()).getLxc().get(vServer.getServerId()).getConfig().getRest().getResponse().getJSONObject("data").toString();
        int NetID = 0;
        for (int i = 0; i < 999999; i++) {
            if(!VServerInfo.contains("net" + i)) {
                NetID = i;
                break;
            }
        }
        String NetSettings = null;

        if(network.getType().equals("IPv4")) {
            if(network.getBridge() == null) network.setBridge("vmbr0");
            NetSettings = "name=eth" + NetID + ",rate=15,bridge=" + network.getBridge() + ",firewall=1,gw=" + network.getGateway() + ",ip=" + network.getIp();
        } else {
            if(network.getBridge() == null) network.setBridge("vmbr0");
            NetSettings = "name=eth" + NetID + ",rate=15,bridge=" + network.getBridge() + ",firewall=1,gw6=" + network.getGateway() + ",ip6=" + network.getIp();
        }

        Map<Integer, String> netN = new HashMap<>();
        netN.put(NetID, URLEncoder.encode(NetSettings));
        client.getNodes().get(vServer.getNode()).getLxc().get(vServer.getServerId()).getConfig().updateVmNetwork(netN);
    }

    public void removeIPAddress(VServer vServer, String ip) {
        JSONObject VServerInfo = client.getNodes().get(vServer.getNode()).getLxc().get(vServer.getServerId()).getConfig().getRest().getResponse().getJSONObject("data");
        int NetID = 0;
        if(!VServerInfo.toString().contains(ip)) return;
        for (int i = 0; i < 10; i++) {
            if(!VServerInfo.isNull("net" + i)) {
                if(VServerInfo.get("net" + i).toString().contains(ip))  {
                    NetID = i;
                    break;
                }
            }
        }
        if (NetID == 0) {
            return;
        }

        System.out.println(client.getNodes().get(vServer.getNode()).getLxc().get(vServer.getServerId()).getConfig().updateVmdelete("net" + NetID));
    }

    public void createLXC(String vmid, int cores, int memory, int disk, String password, Network network, String Node, VServer vServer) {
        Map<Integer, String> netN = new HashMap<>();
        if(network.getBridge() == null) network.setBridge("vmbr0");
        netN.put(0, URLEncoder.encode("name=eth0,rate=15,bridge=" + network.getBridge() + ",firewall=1,gw=" + network.getGateway() + ",ip=" + network.getIp()));
        client.getNodes().get(Node).getLxc().createVm("local:vztmpl/debian-10-standard_10.5-1_amd64.tar.gz", Integer.parseInt(vmid), cores, memory, disk, password, netN, true, "local-zfs", true);
        new Thread(() -> {
            try {
                Thread.sleep(2*13000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String status = client.getNodes().get(Node).getLxc().get(vmid).getStatus().getCurrent().vmStatus().getResponse().getJSONObject("data").getString("status");
            if (status.equals("running")) {
                new FeaturesManager(vServer).allowRootLogin();
                new FeaturesManager(vServer).updateLXC();
            } else {
                try {
                    Thread.sleep(2*13000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                status = client.getNodes().get(Node).getLxc().get(vmid).getStatus().getCurrent().vmStatus().getResponse().getJSONObject("data").getString("status");
                if (status.equals("running")) {
                    new FeaturesManager(vServer).allowRootLogin();
                    new FeaturesManager(vServer).updateLXC();
                }
            }
        }).start();
    }

}
