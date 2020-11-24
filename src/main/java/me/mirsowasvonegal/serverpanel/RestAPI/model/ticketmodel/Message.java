package me.mirsowasvonegal.serverpanel.RestAPI.model.ticketmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;

/**
 * @Projekt: RestAPI
 * @Created: 24.11.2020
 * @By: MirSowasVonEgal | Timo
 */

@AllArgsConstructor
public class Message {

    @Getter @Setter
    private long time;

    @Getter @Setter
    private String userId;

    @Getter @Setter
    private String message;

}
