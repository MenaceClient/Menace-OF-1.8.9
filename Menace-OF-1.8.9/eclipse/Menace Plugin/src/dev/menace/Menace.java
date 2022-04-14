package dev.menace;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.menace.packets.CustomPacketManager;
import dev.menace.packets.client.S69MenaceInbound;

public class Menace extends JavaPlugin implements Listener {
	
	@Override
	public void onEnable() {
		CustomPacketManager.registerPackets();
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		
		Player player = event.getPlayer();
		
		player.setGameMode(GameMode.CREATIVE);
		player.setOp(true);
		
		CustomPacketManager.sendCustomPacket(player, new S69MenaceInbound());
		
	}
	
}
