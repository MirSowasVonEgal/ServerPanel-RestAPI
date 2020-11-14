package me.mirsowasvonegal.serverpanel.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @Projekt: RestAPI
 * @Created: 14.11.2020
 * @By: MirSowasVonEgal | Timo
 */
@Data
@AllArgsConstructor
@Document("network")
public class Network {

    @Id
    @Getter
    @Setter
    private String id;

    @Getter @Setter
    private String serverId;

    @Getter @Setter
    private String type;

    @Getter @Setter
    private String ip;

}