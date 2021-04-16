package me.mirsowasvonegal.serverpanel.RestAPI.manager;

import lombok.Getter;
import lombok.Setter;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.SettingsRepository;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

@Component
public class SettingsManager {


    @Getter @Setter
    public static Object all;
    @Getter @Setter
    public static Object proxmox;
    @Getter @Setter
    public static Object plesk;

    private SettingsRepository settingsRepository;

    public SettingsManager(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;

        all = this.settingsRepository.findAll();
        proxmox = this.settingsRepository.findSettingBySetting("Proxmox").get(0);
        plesk = this.settingsRepository.findSettingBySetting("Plesk").get(0);

    }

}
