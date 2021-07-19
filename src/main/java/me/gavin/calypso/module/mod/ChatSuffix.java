package me.gavin.calypso.module.mod;

import me.gavin.calypso.events.PacketEvent;
import me.gavin.calypso.module.ModCategory;
import me.gavin.calypso.module.Module;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.StringEscapeUtils;

public class ChatSuffix extends Module {

    public ChatSuffix() {
        super("ChatSuffix", "Adds a suffix to chat", ModCategory.Misc);
    }

    private final String chatsuffix = StringEscapeUtils.unescapeCsv(" \uff5c \u1d9c\u1d2c\u1d38\u02b8\u1d3e\u1506\u1d3c");

    @SubscribeEvent
    public void onPacket(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketChatMessage) {
            final CPacketChatMessage packet = (CPacketChatMessage) event.getPacket();
            if (!isCommand(packet.getMessage())) {
                packet.message += chatsuffix;
            }
        }
    }

    private boolean isCommand(String msg) {
        return msg.startsWith("/") || msg.startsWith(".") || msg.startsWith(",") || msg.startsWith("-") || msg.startsWith(";") || msg.startsWith("$");
    }
}