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

    }

    PveClient client = new PveClient("homeserver.shademc.de", 8006);

    public ProxmoxManager() {
        client.login("root", "Timo0580", "pam");
    }

    public int getNextId() {
        return client.getCluster().getNextid().nextid().getResponse().getInt("data");
    }

}
