package io.github.xdotdash.helptickets.impl.service;

import com.google.common.reflect.TypeToken;
import io.github.xdotdash.helptickets.api.Ticket;
import io.github.xdotdash.helptickets.api.config.Config;
import io.github.xdotdash.helptickets.api.service.TicketService;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.User;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ConfigTicketService implements TicketService {

    private final Config ticketConfig;

    public ConfigTicketService(Config ticketConfig) {
        this.ticketConfig = ticketConfig;
    }

    @Override
    public Collection<Ticket> getTickets() {
        return ticketConfig.get().getChildrenList().stream()
                .map(node -> {
                    try {
                        return node.getValue(TypeToken.of(Ticket.class));
                    } catch (ObjectMappingException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Ticket> getTicketsFor(User user) {
        return ticketConfig.get().getChildrenList().stream()
                .filter(node -> {
                    try {
                        return node.getNode("creator").getValue(TypeToken.of(UUID.class)).equals(user.getUniqueId());
                    } catch (ObjectMappingException e) {
                        return false;
                    }
                })
                .map(node -> {
                    try {
                        return node.getValue(TypeToken.of(Ticket.class));
                    } catch (ObjectMappingException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Ticket> getTicket(long id) {
        try {
            return ticketConfig.get(id).isVirtual()
                    ? Optional.empty()
                    : Optional.of(ticketConfig.get(id).getValue(TypeToken.of(Ticket.class)));
        } catch (ObjectMappingException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean hasTicketsFor(User user) {
        return ticketConfig.get().getChildrenList().stream()
                .filter(node -> {
                    try {
                        return node.getNode("creator").getValue(TypeToken.of(UUID.class)).equals(user.getUniqueId());
                    } catch (ObjectMappingException e) {
                        return false;
                    }
                })
                .count() >= 1;
    }

    @Override
    public boolean removeTicket(long id) {
        boolean virtual = ticketConfig.get(id).isVirtual();

        if (!virtual) {
            ticketConfig.get(id).setValue(null);
        }

        return !virtual;
    }

    @Override
    public boolean removeTicket(Ticket ticket) {
        return removeTicket(ticket.getId());
    }

    @Override
    public Optional<Ticket> addTicket(Ticket ticket) {
        try {
            Optional<Ticket> oldTicket = Optional.ofNullable(ticketConfig.get(ticket.getId()).getValue(TypeToken.of(Ticket.class)));

            ticketConfig.get(ticket.getId()).setValue(TypeToken.of(Ticket.class), ticket);

            return oldTicket;
        } catch (ObjectMappingException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean hasTicket(long id) {
        return !ticketConfig.get(id).isVirtual();
    }

    @Override
    public boolean hasTicket(Ticket ticket) {
        return hasTicket(ticket.getId());
    }

    @Override
    public Collection<Long> getAllIds() {
        return ticketConfig.get().getChildrenList().stream()
                .map(node -> node.getNode("id").getLong())
                .collect(Collectors.toSet());
    }
}
