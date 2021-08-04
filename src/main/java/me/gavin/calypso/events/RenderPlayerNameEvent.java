package me.gavin.calypso.events;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderPlayerNameEvent extends Event {

    private final AbstractClientPlayer player;

    private final double x;
    private final double y;
    private final double z;

    private final String text;

    private final double distanceSq;

    public RenderPlayerNameEvent(AbstractClientPlayer player, double x, double y, double z, String text, double distanceSq) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.z = z;
        this.text = text;
        this.distanceSq = distanceSq;
    }

    public AbstractClientPlayer getPlayer() {
        return player;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getText() {
        return text;
    }

    public double getDistanceSq() {
        return distanceSq;
    }
}