package me.mirsowasvonegal.serverpanel.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@Document("plesk")
public class Plesk {

    @Id
    @Getter
    @Setter
    private String id;

    @Getter @Setter
    private String userid;

    @Getter @Setter
    private String plan;

    @Getter @Setter
    private String username;

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private double price;

    @Getter @Setter
    private long paidup;

    @Getter @Setter
    private long statusdate;

    @Getter @Setter
    private String status;

    @Getter @Setter
    private String type;

    @Getter @Setter
    private String guid;

    @Getter @Setter
    private int uid;

}
