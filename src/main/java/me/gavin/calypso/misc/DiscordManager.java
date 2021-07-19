package me.gavin.calypso.misc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import me.gavin.calypso.Calypso;

public class DiscordManager {

    private final String appId = "864637343297699841";

    private long startTime = -1L;

    private final DiscordRPC rpc;
    private final DiscordEventHandlers eventHandlers;
    private final DiscordRichPresence richPresence;

    public DiscordManager() {
        rpc = DiscordRPC.INSTANCE;
        eventHandlers = new DiscordEventHandlers();
        richPresence = new DiscordRichPresence();
    }

    public void startRpc() {
        startTime = System.currentTimeMillis() / 1000L;

        rpc.Discord_ClearPresence();
        richPresence.largeImageText = Calypso.MOD_NAME + " " + Calypso.MOD_NAME;
        richPresence.largeImageKey = "cat";
        richPresence.startTimestamp = startTime;
        rpc.Discord_UpdatePresence(richPresence);
        rpc.Discord_Initialize(appId, eventHandlers, false, null);
    }

    public void stopRpc() {
        rpc.Discord_ClearPresence();
        rpc.Discord_Shutdown();
    }
}