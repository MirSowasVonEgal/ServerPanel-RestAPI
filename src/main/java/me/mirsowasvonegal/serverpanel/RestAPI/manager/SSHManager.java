package me.mirsowasvonegal.serverpanel.RestAPI.manager;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;

/**
 * @Projekt: RestAPI
 * @Created: 16.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public class SSHManager {

    Session session = null;
    ChannelExec channel = null;
    String username, host, password;
    int port, i;

    public SSHManager(String username, String host, int port, String password) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        try {
            session = new JSch().getSession(username, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(String command)  {
        i++;
        if(i > 5) return;
        if (session == null) new SSHManager(username, host, port, password).sendCommand(command);
        try {
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            channel.connect();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }

}
