package me.mirsowasvonegal.serverpanel.RestAPI.model.nnservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @Projekt: RestAPI
 * @Created: 14.11.2020
 * @By: MirSowasVonEgal | Timo
 */
@Data
@AllArgsConstructor
@Document("nnactivity")
public class NNActivity {

    @Id
    @Getter
    @Setter
    private String id;

    @Getter @Setter
    private String userid;

    @Getter @Setter
    private double balance;

    @Getter @Setter
    private ArrayList<NNActivitysList> activitys;

}