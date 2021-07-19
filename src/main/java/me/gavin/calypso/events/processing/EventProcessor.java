package me.gavin.calypso.events.processing;

import me.gavin.calypso.Calypso;
import me.gavin.calypso.events.PacketEvent;
import me.gavin.calypso.events.PlayerTotemPopEvent;
import me.gavin.calypso.gui.HUDEditor;
import me.gavin.calypso.misc.RenderUtil;
import me.gavin.calypso.misc.Util;
import me.gavin.calypso.module.HUDModule;
import me.gavin.calypso.module.Module;
import me.gavin.calypso.module.mod.ClickGUIModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class EventProcessor {

    public EventProcessor() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        if (!(Minecraft.getMinecraft().currentScreen instanceof HUDEditor)) {
            for (Module hudModule : Calypso.INSTANCE.getHudEditor().getHudModules()) {
                if (hudModule.isEnabled())
                    ((HUDModule) hudModule).drawComponent(event.getPartialTicks());
            }
        }
    }

    @SubscribeEvent
    public void onKey(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_NONE)
                return;

            for (Module module : Calypso.INSTANCE.getModuleManager().getModules()) {
                if (module.getKeybind().getBind() == Keyboard.getEventKey())
                    module.toggle();
            }
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
//        if (event.getPacket() instanceof SPacketPlayerListItem) {
//            if (((SPacketPlayerListItem) event.getPacket()).getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
//                for (SPacketPlayerListItem.AddPlayerData data : ((SPacketPlayerListItem) event.getPacket()).getEntries()) {
//                    if (data.getProfile().getId() == Minecraft.getMinecraft().player.getUniqueID())
//                        continue;
//                    MinecraftForge.EVENT_BUS.post(new PlayerConnectionEvent.Join(data.getProfile().getName(), data.getProfile().getId()));
//                }
//            } else if (((SPacketPlayerListItem) event.getPacket()).getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
//                for (SPacketPlayerListItem.AddPlayerData data : ((SPacketPlayerListItem) event.getPacket()).getEntries()) {
//                    if (data.getProfile().getId() == Minecraft.getMinecraft().player.getUniqueID())
//                        continue;
//                    new Thread(() -> MinecraftForge.EVENT_BUS.post(new PlayerConnectionEvent.Leave(UUIDResolver.resolveName(data.getProfile().getId().toString()), data.getProfile().getId()))).start();
//                }
//            }
//        }
        if (event.getPacket() instanceof SPacketEntityStatus) {
            SPacketEntityStatus packet = (SPacketEntityStatus) event.getPacket();
            if (packet.getOpCode() == 35) {
                if (packet.getEntity(Minecraft.getMinecraft().world) instanceof EntityPlayer) {
                    MinecraftForge.EVENT_BUS.post(new PlayerTotemPopEvent((EntityPlayer) packet.getEntity(Minecraft.getMinecraft().world)));
                }
            }
        }
    }
}