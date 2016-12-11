package io.github.xdotdash.helptickets.impl;

import io.github.xdotdash.helptickets.api.Ticket;
import org.spongepowered.api.entity.Transform;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.world.World;

import java.time.LocalDateTime;

class TicketImpl implements Ticket {

    private final long id;
    private final User creator;
    private final String message;
    private final Transform<World> transform;
    private final LocalDateTime createdTime;
    private boolean completed;

    public TicketImpl(User creator, long id, String message, Transform<World> transform, LocalDateTime createdTime, boolean completed) {
        this.creator = creator;
        this.id = id;
        this.message = message;
        this.transform = transform;
        this.createdTime = createdTime;
        this.completed = completed;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public User getCreator() {
        return creator;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Transform<World> getTransform() {
        return transform;
    }

    @Override
    public LocalDateTime getCreationDate() {
        return createdTime;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }
}
