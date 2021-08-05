package me.gavin.calypso.module.mod;

import com.mojang.authlib.GameProfile;
import me.gavin.calypso.events.CrystalExplosionEvent;
import me.gavin.calypso.events.PacketEvent;
import me.gavin.calypso.events.PlayerUpdateEvent;
import me.gavin.calypso.misc.CrystalUtil;
import me.gavin.calypso.misc.Util;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.settings.BoolSetting;
import me.gavin.calypso.settings.NumSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

public class FakePlayer extends Module {

    public final BoolSetting fakeTotemPops = new BoolSetting("Totem Pops", true);
    public final BoolSetting realDamage = new BoolSetting("Realistic Damage", true);
    public final NumSetting fakeDamageSlider = new NumSetting("Fake Damage", 6f, 0.1f, 20f);
    public final NumSetting regeneration = new NumSetting("Regeneration", 0f, 0f, 3f);
    public final BoolSetting beyblade = new BoolSetting("Beyblade", false);
    public final BoolSetting creepy = new BoolSetting("Creep Mode", false);
    public final BoolSetting punching = new BoolSetting("Punching", false);
    public final BoolSetting copyInv = new BoolSetting("Copy Inventory", false);

    public FakePlayer() {
        super("FakePlayer", "A fake player to test on", ModCategory.Misc);
        this.getSettings().add(fakeTotemPops);
        this.getSettings().add(realDamage);
        this.getSettings().add(fakeDamageSlider);
        this.getSettings().add(regeneration);
        this.getSettings().add(beyblade);
        this.getSettings().add(creepy);
        this.getSettings().add(punching);
        this.getSettings().add(copyInv);
    }

    private EntityOtherPlayerMP fakePlayer;

    @Override
    protected void onEnable() {
        spawnFakePlayer();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketExplosion) {
            if (fakeTotemPops.getValue()) {
                if (fakePlayer != null) {
                    final SPacketExplosion explosion = (SPacketExplosion) event.getPacket();
                    if (fakePlayer.getDistance(explosion.getX(), explosion.getY(), explosion.getZ()) <= 15) {
                        double damage = fakeDamageSlider.getValue();
                        if (realDamage.getValue())
                            damage = CrystalUtil.calculateDamage(explosion.getX(), explosion.getY(), explosion.getZ(), fakePlayer);
                        if (damage > 0) {
                            final float health = (float) (fakePlayer.getHealth() - damage);
                            fakePlayer.setHealth(MathHelper.clamp(health, 0f, 9999));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerLivingUpdate(PlayerUpdateEvent event) {
        if (fakeTotemPops.getValue()) {
            if (fakePlayer != null) {
                if (fakePlayer.getHealth() < 0) {
                    fakePlayer.setHealth(0f);
                }
                if (fakePlayer.ticksExisted % 2 == 0) {
                    fakePlayer.setHealth(fakePlayer.getHealth() + regeneration.getValue());
                }
                if (fakePlayer.getHealth() <= 0) {
                    fakeTotemPop(fakePlayer);
                    fakePlayer.setHealth(20);
                }
                if (creepy.getValue() && !beyblade.getValue()) {
                    double[] rotations = Util.calculateLookAt(mc.player.posX, mc.player.posY + mc.player.eyeHeight, mc.player.posZ, fakePlayer);
                    fakePlayer.rotationYawHead = (float) rotations[0];
                    fakePlayer.rotationYaw = (float) rotations[0];
                    fakePlayer.rotationPitch = (float) rotations[1];
                }
                if (beyblade.getValue()) {
                    fakePlayer.rotationYaw += 20;
                    fakePlayer.rotationYawHead += 20;
                }
                if (punching.getValue()) {
                    fakePlayer.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }

    @Override
    protected void onDisable() {
        removeFakePlayer();
    }

    private void fakeTotemPop(Entity entity) {
        mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, 30);
        mc.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
    }

    private void spawnFakePlayer() {
        if (mc.world != null && mc.player != null) {
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("2da1acb3-1a8c-471f-a877-43f13cf37e6a"), "0IMAX"));
            fakePlayer.copyLocationAndAnglesFrom(mc.player);
            fakePlayer.rotationYawHead = mc.player.rotationYaw;
            if (copyInv.getValue())
                fakePlayer.inventory.copyInventory(mc.player.inventory);
            fakePlayer.setGameType(GameType.SURVIVAL);
            fakePlayer.inventory.offHandInventory.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
            mc.world.addEntityToWorld(-1776, fakePlayer);
        } else {
            disable();
        }
    }

    private void removeFakePlayer() {
        if (mc.world != null && fakePlayer != null) {
            mc.world.removeEntityFromWorld(-1776);
        }
        fakePlayer = null;
    }
}