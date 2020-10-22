package me.mirsowasvonegal.serverpanel.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class Status {

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private int statuscode;

}
