package me.gavin.calypso.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CrystalExplosionEvent extends Event {
    private final double x, y, z;
    private final float strength;

    public CrystalExplosionEvent(double x, double y, double z, float strength) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.strength = strength;
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

    public float getStrength() {
        return strength;
    }
}
