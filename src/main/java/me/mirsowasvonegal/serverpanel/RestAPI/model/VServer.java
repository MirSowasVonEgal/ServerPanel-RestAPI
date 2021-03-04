package me.mirsowasvonegal.serverpanel.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Document("vserver")
public class VServer {

    @Id
    @Getter @Setter
    private String id;

    @NotBlank
    @Getter @Setter
    private String serverId;

    @NotBlank
    @Getter @Setter
    private String userId;

    @NotBlank
    @Size(max = 120, min = 8)
    @Getter @Setter
    private String password;

    @NotBlank
    @Getter @Setter
    private int memory;

    @NotBlank
    @Getter @Setter
    private int cores;

    @Getter @Setter
    private String node;

    @NotBlank
    @Getter @Setter
    private int disk;


    @Getter @Setter
    private String status;


    @Getter @Setter
    private long statusdate;


    @Getter @Setter
    private long paidup;


    @Getter @Setter
    private double price;
}
