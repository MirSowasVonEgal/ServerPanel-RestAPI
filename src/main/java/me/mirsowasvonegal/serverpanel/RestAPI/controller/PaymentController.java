package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import com.paypal.http.exceptions.SerializeException;
import com.paypal.http.serializer.Json;
import com.paypal.orders.Order;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Invoice;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Price;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.payment.paypal.PayPalManager;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.InvoiceRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.RandomString;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Projekt: RestAPI
 * @Created: 04.03.2021
 * @By: MirSowasVonEgal | Timo
 */
@RestController
@RequestMapping("/v1/payment")
public class PaymentController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @PostMapping("/paypal/create")
    public Object createPayPalOrder(@RequestBody Invoice invoice) throws SerializeException {
        if(invoice.getAmount() == null) return new Status("Du musst einen Preis angeben!", 500);
        if(invoice.getAmount() < 1.0) return new Status("Du musst mindestens 1€ aufladen!", 500);
        invoice.setCreated(System.currentTimeMillis());
        invoice.setMethod("PayPal");
        invoice.setStatus("Unbezahlt");
        invoice.setProduct("Guthaben");
        String invoiceId = RandomString.generateInt(16);
        Order order = new PayPalManager().createOrder(invoiceId, invoice.getAmount());
        invoice.setData(new JSONObject(new Json().serialize(order)));
        invoice.setServiceId(order.id());
        invoiceRepository.save(invoice);
        return order.links().get(1).href();
    }

    @PostMapping("/paypal/capture")
    public Object capturePayPalOrder(@RequestBody Invoice invoiceBody) throws SerializeException {
        if(invoiceBody.getServiceId() == null) return new Status("Die Service ID ist ungültig!", 500);
        Invoice invoice = invoiceRepository.findInvoiceByServiceId(invoiceBody.getServiceId()).get(0);
        System.out.println(invoice.getServiceId());
        Order order = new PayPalManager().captureOrder(invoice.getServiceId());
        System.out.println("Or " + order);
        System.out.println("In " + invoice);
        if(order == null) return new Status("Fehler bei der Zahlung", 500);
        invoice.setData(new JSONObject(new Json().serialize(order)));
        if(!order.status().equals("COMPLETED")) return new Status("Zahlung ist Fehlgeschlagen, bitte erstelle ein Ticket.", 500);
        invoice.setStatus("Bezahlt");
        invoiceRepository.save(invoice);
        return invoice;
    }

}
