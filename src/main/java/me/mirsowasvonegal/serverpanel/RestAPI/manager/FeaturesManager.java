package me.mirsowasvonegal.serverpanel.RestAPI.manager;

/**
 * @Projekt: RestAPI
 * @Created: 16.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public class FeaturesManager {

    String vmid;

    public FeaturesManager(String vmid) {
        this.vmid = vmid;
    }

    public void updateLXC() {
        new SSHManager("root", "homeserver.shademc.de", 22, "Timo0580")
                .sendCommand("screen -AmdS Update_CT" + vmid + " pct exec " + vmid + " -- bash -c 'apt update -y && apt upgrade -y && apt full-upgrade -y && apt autoremove -y && apt clean -y'");
    }

}
