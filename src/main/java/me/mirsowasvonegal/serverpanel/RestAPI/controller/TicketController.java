package me.mirsowasvonegal.serverpanel.RestAPI.controller;

import me.mirsowasvonegal.serverpanel.RestAPI.manager.ProxmoxManager;
import me.mirsowasvonegal.serverpanel.RestAPI.model.*;
import me.mirsowasvonegal.serverpanel.RestAPI.model.ticketmodel.Message;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.TicketRepository;
import me.mirsowasvonegal.serverpanel.RestAPI.repository.UserRepository;
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
@RequestMapping("/v1/support")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/ticket")
    public Object createTicket(@RequestBody Ticket ticket) {
        if (ticket.getUserId() == null) return new Status("Error Nutzer wurde nicht empfangen!", 500);
        if (ticket.getTitle() == null) return new Status("Du musst einen Titel eingeben!", 500);
        if (ticket.getCategory() == null) return new Status("Du musst eine Kategorie auswählen!", 500);
        if (userRepository.findUserById(ticket.getUserId()).size() == 0) return new Status("Benutzer nicht gefunden!", 500);
        User user = userRepository.findUserById(ticket.getUserId()).get(0);
        if (ticket.getPriority() == 0) ticket.setPriority(2);
        if (ticket.getStatus() == 0) ticket.setStatus(1);
        HashMap<Integer, Object> messages = new HashMap<>();
        messages.put(1, new Message(System.currentTimeMillis(), ticket.getUserId(),user.getUsername(), user.getRankid(), user.getRankname(), ticket.getMessages().get(1).toString()));
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
        if (userRepository.findUserById(message.getUserId()).size() == 0) return new Status("Benutzer nicht gefunden!", 500);
        User user = userRepository.findUserById(message.getUserId()).get(0);
        Ticket ticket = ticketRepository.findTicketById(id).get(0);
        int nextId = ticketRepository.findTicketById(id).get(0).getMessages().size() + 1;
        HashMap<Integer, Object> messages = ticket.getMessages();
        messages.put(nextId, new Message(System.currentTimeMillis(), message.getUserId(), user.getUsername(), user.getRankid(), user.getRankname(), message.getMessage()));
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

    @GetMapping("/ticket")
    public Object getAllTicket() {
        return ticketRepository.findAll();
    }

    @GetMapping("/ticket/user/{id}")
    public Object getTicketsFromUser(@PathVariable String id) {
        if (ticketRepository.findTicketByUserId(id).size() == 0) return new Status("Du hast noch kein Ticket erstellt!", 500);
        return ticketRepository.findTicketByUserId(id);
    }

}
