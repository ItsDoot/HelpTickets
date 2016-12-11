package io.github.xdotdash.helptickets.impl.serializer;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeSerializer implements TypeSerializer<LocalDateTime> {

    static {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(LocalDateTime.class),
                new LocalDateTimeSerializer());
    }

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss O");

    @Override
    public LocalDateTime deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        return LocalDateTime.parse(value.getString(), FORMAT);
    }

    @Override
    public void serialize(TypeToken<?> type, LocalDateTime obj, ConfigurationNode value) throws ObjectMappingException {
        value.setValue(obj.format(FORMAT));
    }
}
