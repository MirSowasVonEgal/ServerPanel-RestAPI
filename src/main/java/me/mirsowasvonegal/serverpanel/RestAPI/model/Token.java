package me.mirsowasvonegal.serverpanel.RestAPI.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@Document("token")
public class Token {

    @Id
    @Getter
    @Setter
    private String id;

    @NotBlank
    @Getter @Setter
    private String token;

    @NotBlank
    @Getter @Setter
    private String userid;

    @NotBlank
    @Getter @Setter
    private long time;

}

