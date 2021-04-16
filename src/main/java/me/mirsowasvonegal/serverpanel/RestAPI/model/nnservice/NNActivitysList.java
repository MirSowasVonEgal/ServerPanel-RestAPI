package me.mirsowasvonegal.serverpanel.RestAPI.model.nnservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class NNActivitysList {

    @Getter
    @Setter
    private String type;

    @Getter
    @Setter
    private double amount;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private long date;

}
