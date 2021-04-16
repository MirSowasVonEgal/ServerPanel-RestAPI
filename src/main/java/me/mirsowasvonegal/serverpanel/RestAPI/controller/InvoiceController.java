package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import com.paypal.http.exceptions.SerializeException;
import com.paypal.orders.Order;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Invoice;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Ticket;
import me.mirsowasvonegal.serverpanel.RestAPI.model.User;
import me.mirsowasvonegal.serverpanel.RestAPI.model.ticketmodel.Message;
import me.mirsowasvonegal.serverpanel.RestAPI.payment.paypal.PayPalManager;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.InvoiceRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.UserRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.utils.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/v1/system")
public class InvoiceController {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/invoice")
    public Object createInvoice(@RequestBody Invoice invoice) {
        if (invoice.getUserId() == null) return new Status("Error Nutzer wurde nicht empfangen!", 500);
        if (invoice.getAmount() == null) return new Status("Du musst einen Preis eingeben!", 500);
        if (invoice.getProduct() == null) return new Status("Du musst ein Produkt eingeben!", 500);
        if (invoice.getStatus() == null) return new Status("Du musst einen Status eingeben!", 500);
        if (invoice.getMethod() == null) return new Status("Du musst eine Methode auswählen!", 500);
        if (userRepository.findUserById(invoice.getUserId()).size() == 0) return new Status("Benutzer nicht gefunden!", 500);
        invoice.setCreated(System.currentTimeMillis());
        invoiceRepository.save(invoice);
        return invoice;
    }

    @PutMapping("/invoice/{id}")
    public Object updateInvoice(@PathVariable String id, @RequestBody Invoice invoice) {
        if (invoiceRepository.findInvoiceById(id).size() == 0) return new Status("Diese Rechnung existiert nicht!", 500);
        Invoice oldInvoice = invoiceRepository.findInvoiceById(id).get(0);
        if (invoice.getUserId() != null) oldInvoice.setUserId(invoice.getUserId());
        if (invoice.getCreated() != 0L) oldInvoice.setCreated(invoice.getCreated());
        if (invoice.getStatus() != null) oldInvoice.setStatus(invoice.getStatus());
        if (invoice.getServiceId() != null) oldInvoice.setServiceId(invoice.getServiceId());
        if (invoice.getProduct() != null) oldInvoice.setProduct(invoice.getProduct());
        if (invoice.getData() != null) oldInvoice.setData(invoice.getData());
        if (invoice.getMethod() != null) oldInvoice.setMethod(invoice.getMethod());
        if (invoice.getAmount() != null) oldInvoice.setAmount(invoice.getAmount());
        invoiceRepository.save(oldInvoice);
        return oldInvoice;
    }

    @GetMapping("/invoice/{id}")
    public Object getInvoice(@PathVariable String id) {
        if (invoiceRepository.findInvoiceById(id).size() == 0) return new Status("Diese Rechnung existiert nicht!", 500);
        return invoiceRepository.findInvoiceById(id).get(0);
    }

    @GetMapping("/invoice")
    public Object getAllInvoices() {
        return invoiceRepository.findAll();
    }

    @GetMapping("/invoice/user/{id}")
    public Object getInvoicesFromUser(@PathVariable String id) {
        return invoiceRepository.findInvoiceByUserId(id);
    }
}
