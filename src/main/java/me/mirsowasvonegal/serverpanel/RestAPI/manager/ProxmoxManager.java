package me.mirsowasvonegal.serverpanel.RestAPI.manager;

import it.corsinvest.proxmoxve.api.PveClient;
import org.json.JSONObject;

/**
 * @Projekt: RestAPI
 * @Created: 13.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public class ProxmoxManager {

    public static void main(String[] args) {
        new ProxmoxManager().createLXC("123", 1, 512, 10, "Test123", "192.168.2.221");
    }

    PveClient client = new PveClient("homeserver.shademc.de", 8006);

    public ProxmoxManager() {
        client.login("root", "Timo0580", "pam");
    }

    public int getNextId() {
        return client.getCluster().getNextid().nextid().getResponse().getInt("data");
    }

    public void createLXC(String vmid, int cores, int memory, int disk, String password, String ip) {
        System.out.println("sdsddd: " + client.getNodes().get("pve").getLxc().createRest("local:vztmpl/debian-10-standard_10.5-1_amd64.tar.gz", 123).getError());
    }

}
