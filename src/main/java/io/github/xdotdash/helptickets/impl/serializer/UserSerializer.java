package io.github.xdotdash.helptickets.impl.serializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.NoSuchElementException;
import java.util.UUID;

public class UserSerializer implements TypeSerializer<User> {

    private final UserStorageService service;

    public UserSerializer(UserStorageService service) {
        this.service = service;
    }

    @Override
    public User deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        return service.get(value.getValue(TypeToken.of(UUID.class)))
                .orElseThrow(() -> new ObjectMappingException(new NoSuchElementException()));
    }

    @Override
    public void serialize(TypeToken<?> type, User obj, ConfigurationNode value) throws ObjectMappingException {
        value.setValue(TypeToken.of(UUID.class), obj.getUniqueId());
    }
}
