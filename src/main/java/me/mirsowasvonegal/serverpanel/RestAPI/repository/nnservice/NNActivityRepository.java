package me.mirsowasvonegal.serverpanel.RestAPI.repository.nnservice;

import me.mirsowasvonegal.serverpanel.RestAPI.model.nnservice.NNActivity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NNActivityRepository extends MongoRepository<NNActivity, Integer> {

    List<NNActivity> findNNActivityById(String id);

    List<NNActivity> findNNActivityByUserid(String userid);

}
