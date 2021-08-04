package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PlayerPostUpdateEvent;
import me.gavin.calypso.events.PlayerPreUpdateEvent;
import me.gavin.calypso.misc.Util;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.BoolSetting;
import me.gavin.calypso.settings.NumSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Comparator;

public class KillAura extends Module {

    private final NumSetting range = new NumSetting("Range", 4f, 1f, 5f);
    private final BoolSetting rotate = new BoolSetting("Rotate", true);

    private final BoolSetting players = new BoolSetting("Players", true);
    private final BoolSetting mobs = new BoolSetting("Mobs", false);
    private final BoolSetting animals = new BoolSetting("Animals", false);

    public KillAura() {
        super("KillAura", "Attack entities automatically", ModCategory.Combat);
        this.getSettings().add(range);
        this.getSettings().add(rotate);
        this.getSettings().add(players);
        this.getSettings().add(mobs);
        this.getSettings().add(animals);
    }

    private float prevYaw;
    private float prevPitch;

    private EntityLivingBase target = null;

    @SubscribeEvent
    public void pre(PlayerPreUpdateEvent event) {
        if (target != null) {
            if (target.getHealth() <= 0 || target.getDistance(mc.player) > range.getValue()) {
                target = null;
            }
            if (target != null) {
                if (rotate.getValue()) {
                    prevYaw = mc.player.rotationYaw;
                    prevPitch = mc.player.rotationPitch;

                    double[] rotations = Util.calculateLookAt(target.posX, target.posY + (target.height / 2.0d), target.posZ, mc.player);

                    mc.player.rotationYaw = (float) rotations[0];
                    mc.player.rotationPitch = (float) rotations[1];
                }
            }
        } else {
            target = mc.world.loadedEntityList.stream()
                    .filter(entity -> entity instanceof EntityLivingBase)
                    .map(entity -> (EntityLivingBase) entity)
                    .filter(EntityLivingBase::isEntityAlive)
                    .filter(this::shouldAttack)
                    .filter(entityLivingBase -> !entityLivingBase.isDead)
                    .filter(entity -> entity.getHealth() > 0)
                    .filter(entityLivingBase -> !entityLivingBase.equals(mc.player))
                    .filter(entityLivingBase -> entityLivingBase.getDistance(mc.player) <= range.getValue())
                    .min(Comparator.comparing(entityLivingBase -> entityLivingBase.getDistance(mc.player)))
                    .orElse(null);
        }
    }

    @SubscribeEvent
    public void post(PlayerPostUpdateEvent event) {
        if (target != null) {
            if (rotate.getValue()) {
                mc.player.rotationYaw = prevYaw;
                mc.player.rotationPitch = prevPitch;
            }
            if (mc.player.getCooledAttackStrength(0f) >= 1f) {
                mc.playerController.attackEntity(mc.player, target);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    private boolean shouldAttack(EntityLivingBase entityLivingBase) {
        if (entityLivingBase instanceof EntityPlayer && players.getValue()) {
            return true;
        } else if (entityLivingBase instanceof EntityMob && mobs.getValue()) {
            return true;
        } else return entityLivingBase instanceof EntityAnimal && animals.getValue();
    }
}