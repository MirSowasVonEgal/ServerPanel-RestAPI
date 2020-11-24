package me.mirsowasvonegal.serverpanel.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.mirsowasvonegal.serverpanel.RestAPI.model.ticketmodel.Message;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Projekt: RestAPI
 * @Created: 24.11.2020
 * @By: MirSowasVonEgal | Timo
 */

@Data
@AllArgsConstructor
@Document("ticket")
public class Ticket {

    @Id
    @Getter
    @Setter
    private String id;

    @Getter @Setter
    private String userId;

    @Getter @Setter
    private String title;

    @Getter @Setter
    private String category;

    @Getter @Setter
    private long created;

    @Getter @Setter
    private int priority;

    @Getter @Setter
    private int status;

    @Getter @Setter
    private String product;

    @Getter @Setter
    private HashMap<Integer, Object> messages;


}
