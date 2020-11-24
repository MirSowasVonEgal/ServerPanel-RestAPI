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
 * @Created: 20.11.2020
 * @By: MirSowasVonEgal | Timo
 */

@Data
@AllArgsConstructor
@Document("price")
public class Price {

    @Id
    @Getter
    @Setter
    private String id;

    @Getter @Setter
    private String product;

    @Getter @Setter
    private HashMap<String, Double> price = new HashMap<>();

}
