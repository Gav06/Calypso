package me.gavin.calypso.misc;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickTimer {

    private long ticksPassed;

    private long tick;

    public TickTimer() {
        tick = -1;
    }

    public boolean passed(int ms) {
        return ticksPassed - this.tick >= ms;
    }

    public void reset() {
        this.tick = ticksPassed;
    }

    public long getTick() {
        return tick;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        ticksPassed++;
    }
}
