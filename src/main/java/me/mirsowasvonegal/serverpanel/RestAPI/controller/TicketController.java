package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.ProxmoxManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Network;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Status;
import me.mirsowasvonegal.serverpanel.RestAPI.model.Ticket;
import me.mirsowasvonegal.serverpanel.RestAPI.model.VServer;
import me.mirsowasvonegal.serverpanel.RestAPI.model.ticketmodel.Message;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;

/**
 * @Projekt: RestAPI
 * @Created: 24.11.2020
 * @By: MirSowasVonEgal | Timo
 */

@RestController
@RequestMapping("/support")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @PostMapping("/ticket")
    public Object createVServer(@RequestBody Ticket ticket) {
        if (ticket.getUserId() == null) return new Status("Error Nutzer wurde nicht empfangen!", 500);
        if (ticket.getTitle() == null) return new Status("Du musst einen Titel eingeben!", 500);
        if (ticket.getPriority() == 0) ticket.setPriority(2);
        HashMap<Integer, Object> messages = new HashMap<>();
        messages.put(1, new Message(System.currentTimeMillis(), ticket.getUserId(), ticket.getMessages().get(1).toString()));
        ticket.setMessages(messages);
        ticket.setCreated(System.currentTimeMillis());
        ticketRepository.save(ticket);
        return ticket;
    }

    @PostMapping("/ticket/{id}")
    public Object answer(@PathVariable String id, @RequestBody Message message) {
        if (ticketRepository.findTicketById(id).size() == 0) return new Status("Dieses Ticket existiert nicht!", 500);
        if (message.getUserId() == null) return new Status("Error Nutzer wurde nicht empfangen!", 500);
        if (message.getMessage() == null) return new Status("Du musst eine Antwort eingeben!", 500);
        Ticket ticket = ticketRepository.findTicketById(id).get(0);
        int nextId = ticketRepository.findTicketById(id).get(0).getMessages().size() + 1;
        HashMap<Integer, Object> messages = ticket.getMessages();
        messages.put(nextId, new Message(System.currentTimeMillis(), message.getUserId(), message.getMessage()));
        ticket.setMessages(messages);
        ticketRepository.save(ticket);
        return ticket;
    }

    @PutMapping("/ticket/{id}")
    public Object updateTicket(@PathVariable String id, @RequestBody Ticket ticket) {
        if (ticketRepository.findTicketById(id).size() == 0) return new Status("Dieses Ticket existiert nicht!", 500);
        Ticket oldTicket = ticketRepository.findTicketById(id).get(0);
        if (ticket.getPriority() != 0) oldTicket.setPriority(ticket.getPriority());
        if (ticket.getTitle() != null) oldTicket.setTitle(ticket.getTitle());
        if (ticket.getCategory() != null) oldTicket.setCategory(ticket.getCategory());
        if (ticket.getStatus() != 0) oldTicket.setStatus(ticket.getStatus());
        if (ticket.getProduct() != null) oldTicket.setProduct(ticket.getProduct());
        ticketRepository.save(oldTicket);
        return oldTicket;
    }

    @GetMapping("/ticket/{id}")
    public Object getTicket(@PathVariable String id) {
        if (ticketRepository.findTicketById(id).size() == 0) return new Status("Dieses Ticket existiert nicht!", 500);
        return ticketRepository.findTicketById(id).get(0);
    }

}
