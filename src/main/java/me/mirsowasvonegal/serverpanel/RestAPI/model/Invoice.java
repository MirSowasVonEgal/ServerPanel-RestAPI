package me.mirsowasvonegal.serverpanel.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

/**
 * @Projekt: RestAPI
 * @Created: 19.02.2021
 * @By: MirSowasVonEgal | Timo
 */

@Data
@AllArgsConstructor
@Document("invoice")
public class Invoice {

    @Id
    @Getter
    @Setter
    private String id;

    @Getter @Setter
    private String serviceId;

    @Getter @Setter
    private String product;

    @Getter @Setter
    private String method;

    @Getter @Setter
    private long created;

    @Getter @Setter
    private Double amount;

    @Getter @Setter
    private String status;

    @Getter @Setter
    private Object data;

}
