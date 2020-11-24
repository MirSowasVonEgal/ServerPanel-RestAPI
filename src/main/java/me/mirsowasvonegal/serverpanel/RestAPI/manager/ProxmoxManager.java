package me.mirsowasvonegal.serverpanel.RestAPI.manager;

import it.corsinvest.proxmoxve.api.PveClient;
import org.json.JSONObject;
import org.springframework.aop.ThrowsAdvice;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Projekt: RestAPI
 * @Created: 13.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public class ProxmoxManager {

    public static void main(String[] args) {
        //new FeaturesManager("123").updateLXC();
        new ProxmoxManager().createLXC(new ProxmoxManager().getNextId(), 1, 512, 10, "Test123", "192.168.2.222");
    }

    PveClient client = new PveClient("homeserver.shademc.de", 8006);

    public ProxmoxManager() {
        client.login("root", "Timo0580", "pam");
    }

    public String getNextId() {
        return client.getCluster().getNextid().nextid().getResponse().getInt("data") + "";
    }

    public String getLXC(String vmid) {
        return client.getNodes().get("pve").getLxc().get(vmid).getStatus().getCurrent().vmStatus().getResponse().getJSONObject("data").toString();
    }

    public void createLXC(String vmid, int cores, int memory, int disk, String password, String ip) {
        Map<Integer, String> netN = new HashMap<>();
        netN.put(0, URLEncoder.encode("name=eth0,rate=15,bridge=vmbr0,firewall=1,gw=192.168.2.1,ip=" + ip + "/24"));
        client.getNodes().get("pve").getLxc().createVm("local:vztmpl/debian-10-standard_10.5-1_amd64.tar.gz", Integer.parseInt(vmid), cores, memory, disk, password, netN, false, "local-lvm", true);
        new Thread(() -> {
            try {
                Thread.sleep(6*15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String status = client.getNodes().get("pve").getLxc().get(vmid).getStatus().getCurrent().vmStatus().getResponse().getJSONObject("data").getString("status");
            if (status.equals("running")) {
                new FeaturesManager(vmid).updateLXC();
            } else {
                try {
                    Thread.sleep(6*15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                status = client.getNodes().get("pve").getLxc().get(vmid).getStatus().getCurrent().vmStatus().getResponse().getJSONObject("data").getString("status");
                if (status.equals("running")) {
                    new FeaturesManager(vmid).updateLXC();
                }
            }
        }).start();
    }

}
