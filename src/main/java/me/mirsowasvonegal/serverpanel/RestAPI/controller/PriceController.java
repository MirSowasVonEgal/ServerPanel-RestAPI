package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.model.*;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.NetworkRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.PriceRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.UserRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.VServerRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Projekt: RestAPI
 * @Created: 20.11.2020
 * @By: MirSowasVonEgal | Timo
 */

@RestController
@RequestMapping("/v1/system")
public class PriceController {

    @Autowired
    private VServerRepository vServerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private PriceRepository priceRepository;

    @PostMapping("/price")
    public Object setPrice(@RequestBody Price price) {
        if(price.getProduct() == null) return new Status("Du musst ein Produkt angeben!", 500);
        if(price.getPrice() == null) return new Status("Du musst die Preise angeben!", 500);
        if(priceRepository.existsByProduct(price.getProduct())) return new Status("Dieses Produkt wurde schon erstellt!", 500);
        priceRepository.save(price);
        return price;
    }

    @GetMapping("/price")
    public Object getPrices() {
        return priceRepository.findAll();
    }

    @GetMapping("/price/{id}")
    public Price getPrice(@PathVariable String id) {
        return priceRepository.findPriceById(id).get(0);
    }

    @GetMapping("/price/product/{product}")
    public Price getPriceByProduct(@PathVariable String product) {
        return priceRepository.findPriceByProduct(product).get(0);
    }

    @PutMapping("/price/{id}")
    public Object updatePrice(@RequestBody Price price, @PathVariable String id) {
        if (priceRepository.findPriceById(id).size() == 0) return new Status("Dieses Produkt wurde nicht gefunden!", 500);
        Price OldPrice = priceRepository.findPriceById(id).get(0);
        if(price.getProduct() == null) return new Status("Du musst ein Produkt angeben!", 500);
        if(price.getPrice().size() == 0) return new Status("Du musst die Preise angeben!", 500);
        OldPrice.setProduct(price.getProduct());
        OldPrice.setPrice(price.getPrice());
        priceRepository.save(OldPrice);
        return OldPrice;
    }


}
