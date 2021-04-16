package me.mirsowasvonegal.serverpanel.RestAPI.repository;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Setting;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SettingsRepository extends MongoRepository<Setting, Integer> {

    List<Setting> findSettingBySetting(String Setting);

    List<Setting> findSettingByValue(String value);

}
