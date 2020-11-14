package me.mirsowasvonegal.serverpanel.RestAPI.manager;

import java.io.*;

/**
 * @Projekt: RestAPI
 * @Created: 13.11.2020
 * @By: MirSowasVonEgal | Timo
 */
public class FileManager {

    public String readfile(String filepath) throws IOException {
        File file = new File(getClass().getResource(filepath).getFile());
        FileReader fr=new FileReader(file);
        BufferedReader br=new BufferedReader(fr);
        StringBuffer sb=new StringBuffer();
        String line;
        while((line=br.readLine())!=null)
        {
            sb.append(line);
        }
        fr.close();
        return sb.toString();
    }

}
