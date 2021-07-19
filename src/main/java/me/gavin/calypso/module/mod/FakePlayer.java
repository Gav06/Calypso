package me.gavin.calypso.module.mod;

import com.mojang.authlib.GameProfile;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import org.lwjgl.input.Keyboard;

import java.util.UUID;

public class FakePlayer extends Module {

    public FakePlayer() {
        super("FakePlayer", "A fake player to test on", ModCategory.Misc);
    }

    private EntityOtherPlayerMP fakePlayer;

    @Override
    protected void onEnable() {
        if (mc.world != null && mc.player != null) {
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("2da1acb3-1a8c-471f-a877-43f13cf37e6a"), "0IMAX"));
            fakePlayer.copyLocationAndAnglesFrom(mc.player);
            fakePlayer.rotationYawHead = mc.player.rotationYaw;
            fakePlayer.inventory.copyInventory(mc.player.inventory);
            mc.world.addEntityToWorld(-1776, fakePlayer);
        } else {
            disable();
        }
    }

    @Override
    protected void onDisable() {
        if (mc.world != null && fakePlayer != null) {
            mc.world.removeEntityFromWorld(-1776);
            fakePlayer = null;
        }
    }
}
