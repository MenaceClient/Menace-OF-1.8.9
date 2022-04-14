package dev.menace.packets;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Protocol;
import com.comphenix.protocol.PacketType.Sender;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.FieldUtils;
import com.comphenix.protocol.reflect.accessors.Accessors;
import com.google.common.collect.BiMap;

import dev.menace.packets.client.S69MenaceInbound;
import net.minecraft.server.v1_8_R3.EnumProtocol;
import net.minecraft.server.v1_8_R3.EnumProtocolDirection;
import net.minecraft.server.v1_8_R3.Packet;

public class CustomPacketManager {
	
	private static final HashMap<Class<? extends MenacePacket>, PacketType> packetToType = new HashMap<Class<? extends MenacePacket>, PacketType>();
	
	public static void registerPackets() {
		
		registerPacket(S69MenaceInbound.class, PacketIds.S69MenaceInbound, Sender.SERVER);
		
	}
	
	public static void registerPacket(Class<? extends MenacePacket> packetClass, int packetId, Sender sender) {
		
		final PacketType packetType = new PacketType(Protocol.PLAY, sender, packetId, -1);
		
		packetToType.put(packetClass, packetType);
		
		final EnumProtocol protocol = EnumProtocol.PLAY;
		final EnumProtocolDirection direction = packetType.isClient() ? EnumProtocolDirection.SERVERBOUND : EnumProtocolDirection.CLIENTBOUND;
		
		try {
			
			Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet<?>>>> theMap = (Map<EnumProtocolDirection, BiMap<Integer, Class<? extends Packet<?>>>>) FieldUtils.readField(protocol, "j", true);
			
			BiMap<Integer, Class<? extends Packet<?>>> biMap = theMap.get(direction);
			
			biMap.put(packetId, (Class<? extends Packet<?>>) packetClass);
			
			theMap.put(direction, biMap);
			
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		Map<Class<?>, EnumProtocol> map = (Map<Class<?>, EnumProtocol>) Accessors.getFieldAccessor(EnumProtocol.class, Map.class, true).get(protocol);
		
		map.put(packetClass, protocol);
		
	}
	
	public static void sendCustomPacket(Player player, MenacePacket packet) {
		
		PacketContainer container = new PacketContainer(packetToType.get(packet.getClass()), packet);
		
		try {
			
			ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
			
		}
		catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	public static PacketType getCustomPacketType(Class<? extends MenacePacket> clazz) {
		return packetToType.get(clazz);
	}
	 
}
