package io.github.xdotdash.helptickets.api.service;

import io.github.xdotdash.helptickets.api.Ticket;
import org.spongepowered.api.entity.living.player.User;

import java.util.Collection;
import java.util.Optional;

public interface TicketService {

    /**
     * Gets all recorded tickets.
     *
     * @return All recorded tickets
     */
    Collection<Ticket> getTickets();

    /**
     * Gets all tickets for the given {@link User}.
     *
     * @param user The user
     * @return All tickets under the specified user
     */
    Collection<Ticket> getTicketsFor(User user);

    /**
     * Gets the ticket for the given id, if available.
     *
     * @param id The id
     * @return The ticket, if available
     */
    Optional<Ticket> getTicket(long id);

    /**
     * Checks if the specified user has 1 or more tickets.
     *
     * @param user The user
     * @return True if the user has 1 or more tickets, false otherwise
     */
    boolean hasTicketsFor(User user);

    /**
     * Removes a ticket.
     *
     * @param id The ticket id
     * @return Whether the ticket was present in this ticket service
     */
    boolean removeTicket(long id);

    /**
     * Removes a ticket.
     *
     * @param ticket The ticket
     * @return Whether the ticket was present in this ticket service
     */
    boolean removeTicket(Ticket ticket);

    /**
     * Adds a ticket.
     *
     * <p>If the creator of the ticket already has a ticket of the same id,
     * the passed in ticket will replace the existing ticket.</p>
     *
     * @param ticket The ticket
     * @return The previous ticket, if available
     */
    Optional<Ticket> addTicket(Ticket ticket);

    /**
     * Checks if the specified ticket is present.
     *
     * @param id The ticket id
     * @return True if the ticket exists in this ticket service, false otherwise
     */
    boolean hasTicket(long id);

    /**
     * Checks if the specified ticket is present.
     *
     * @param ticket The ticket
     * @return True if the ticket exists in this ticket service, false otherwise
     */
    boolean hasTicket(Ticket ticket);

    /**
     * Gets the ids of all recorded tickets.
     *
     * @return Ids of all recorded tickets
     */
    Collection<Long> getAllIds();

}
