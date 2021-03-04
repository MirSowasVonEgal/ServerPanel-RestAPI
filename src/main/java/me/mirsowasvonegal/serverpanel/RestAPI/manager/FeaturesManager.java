package me.mirsowasvonegal.serverpanel.RestAPI.manager;

import me.mirsowasvonegal.serverpanel.RestAPI.model.VServer;

/**
 * @Projekt: RestAPI
 * @Created: 16.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public class FeaturesManager {

    VServer vServer;

    public FeaturesManager(VServer vServer) {
        this.vServer = vServer;
    }

    public void allowRootLogin() {
        new SSHManager("root", vServer.getNode()+".shademc.de", 22, "Timo0580")
                .sendCommand("screen -AmdS AllowRootLogin_CT" + vServer.getServerId() + " pct exec " + vServer.getServerId() + " -- bash -c 'echo \"PermitRootLogin yes\" >> /etc/ssh/sshd_config && service sshd restart'");
    }

    public void updateLXC() {
        new SSHManager("root", vServer.getNode()+".shademc.de", 22, "Timo0580")
                .sendCommand("screen -AmdS Update_CT" + vServer.getServerId() + " pct exec " + vServer.getServerId() + " -- bash -c 'apt update -y && apt upgrade -y && apt full-upgrade -y && apt autoremove -y && apt clean -y'");
    }

}
