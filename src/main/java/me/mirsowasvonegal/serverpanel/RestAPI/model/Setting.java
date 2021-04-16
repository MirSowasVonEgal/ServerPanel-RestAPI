package me.mirsowasvonegal.serverpanel.RestAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

@Data
@AllArgsConstructor
@Document("settings")
public class Setting {

    @Getter
    @Setter
    private String setting;


    @Getter @Setter
    private Object value;

}

