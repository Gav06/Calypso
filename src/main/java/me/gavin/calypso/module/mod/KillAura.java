package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PlayerPostUpdateEvent;
import me.gavin.calypso.events.PlayerPreUpdateEvent;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.BoolSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class KillAura extends Module {

    private final BoolSetting players = new BoolSetting("Players", true);
    private final BoolSetting mobs = new BoolSetting("Mobs", false);
    private final BoolSetting animals = new BoolSetting("Animals", false);

    public KillAura() {
        super("KillAura", "Attack entities automatically", ModCategory.Combat);
        this.getSettings().add(players);
        this.getSettings().add(mobs);
        this.getSettings().add(animals);
    }

    @SubscribeEvent
    public void pre(PlayerPreUpdateEvent event) {

    }

    @SubscribeEvent
    public void post(PlayerPostUpdateEvent event) {

    }
}