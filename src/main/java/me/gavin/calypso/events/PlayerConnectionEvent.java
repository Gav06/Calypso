package me.gavin.calypso.events;

import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.UUID;

public class PlayerConnectionEvent extends Event {

    private final String name;
    private final UUID uuid;

    private PlayerConnectionEvent(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public static class Join extends PlayerConnectionEvent {

        public Join(String name, UUID uuid) {
            super(name, uuid);
        }
    }

    public static class Leave extends PlayerConnectionEvent {

        public Leave(String name, UUID uuid) {
            super(name, uuid);
        }
    }
}
