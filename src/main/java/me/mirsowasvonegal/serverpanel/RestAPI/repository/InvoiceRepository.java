package me.mirsowasvonegal.serverpanel.RestAPI.repository;

import me.mirsowasvonegal.serverpanel.RestAPI.model.Invoice;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Network;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Projekt: RestAPI
 * @Created: 04.03.2021
 * @By: MirSowasVonEgal | Timo
 */
public interface InvoiceRepository extends MongoRepository<Invoice, Integer>  {

    List<Invoice> findInvoiceById(String id);

    List<Invoice> findInvoiceByServiceId(String ip);

    Boolean existsById(String id);

    Boolean existsByServiceId(String id);

}
