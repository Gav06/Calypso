package me.gavin.calypso.module.mod;

import com.google.common.collect.Sets;
import me.gavin.calypso.events.PacketEvent;
import me.gavin.calypso.events.PlayerUpdateEvent;
import me.gavin.calypso.misc.Pair;
import me.gavin.calypso.misc.RenderUtil;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.ColorSetting;
import me.gavin.calypso.settings.NumSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ThrowTrails extends Module {

    private final HashMap<Integer, Pair<Long, List<Vec3d>>> map = new HashMap<>();
    private final NumSetting lifetime = new NumSetting("Lifetime", 4f, 1f, 15f);
    private final ColorSetting color = new ColorSetting("Color", 255, 255, 255, 200);
    private final NumSetting lineWidth = new NumSetting("Line Width", 2f, 1f, 8f);

    public ThrowTrails() {
        super("ThrowTrails", "Trails behind throwables 0_0 (monky sucks)", ModCategory.Visual);
        this.getSettings().add(lifetime);
        this.getSettings().add(color);
        this.getSettings().add(lineWidth);
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        for (Map.Entry<Integer, Pair<Long, List<Vec3d>>> entry : setThingy(map.entrySet())) {
            if (System.currentTimeMillis() - entry.getValue().getFirst() > lifetime.getValue() * 1000) {
                map.remove(entry.getKey());
                continue;
            }
            RenderUtil.prepare();
            final Color colorVal = color.getColor();
            GL11.glColor4f(colorVal.getRed() / 255f, colorVal.getGreen() / 255f, colorVal.getBlue() / 255f, color.getAlpha().getValue() / 255f);
            GL11.glLineWidth(lineWidth.getValue());
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (Vec3d vector : entry.getValue().getSecond()) {
                GL11.glVertex3d(vector.x - mc.getRenderManager().viewerPosX, vector.y - mc.getRenderManager().viewerPosY, vector.z - mc.getRenderManager().viewerPosZ);
            }
            GL11.glEnd();
            RenderUtil.release();
        }
    }

    // troll
    private Set<Map.Entry<Integer, Pair<Long, List<Vec3d>>>> setThingy(Set<Map.Entry<Integer, Pair<Long, List<Vec3d>>>> set) {
        return new HashSet<>(set);
    }

    @Override
    protected void onEnable() {
        map.clear();
    }

    @Override
    protected void onDisable() {
        map.clear();
    }

    @SubscribeEvent
    public void onTick(PlayerUpdateEvent event) {
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityEnderPearl && map.containsKey(e.getEntityId()) && e.ticksExisted > 2) {
                map.get(e.getEntityId()).getSecond().add(
                        new Vec3d(
                                MathHelper.clampedLerp(e.lastTickPosX, e.posX, mc.getRenderPartialTicks()),
                                MathHelper.clampedLerp(e.lastTickPosY, e.posY, mc.getRenderPartialTicks()),
                                MathHelper.clampedLerp(e.lastTickPosZ, e.posZ, mc.getRenderPartialTicks())
                        )
                );
            }
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject spawnObjectPacket = (SPacketSpawnObject) event.getPacket();
            if (spawnObjectPacket.getType() == 65) {
                map.put(spawnObjectPacket.getEntityID(), new Pair<>(System.currentTimeMillis(), new ArrayList<>()));
            }
        }
    }
}