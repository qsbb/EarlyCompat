package chenjunfu2.earlycompat.network;

import me.fallenbreath.fanetlib.api.event.FanetlibClientEvents;
import me.fallenbreath.fanetlib.api.event.FanetlibServerEvents;
import me.fallenbreath.fanetlib.api.packet.PacketCodec;
import me.fallenbreath.fanetlib.api.packet.PacketId;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.Packet;

import me.fallenbreath.fanetlib.api.packet.FanetlibPackets;

import java.util.function.Consumer;

public class EarlyCompatNetwork
{
	public static final PacketId<EarlyCompatPacket> PACKET_TYPE = PacketId.of("earlycompat", "network");

	public static class C2S
	{
	    public static final int HI = 0;
	}

	public static class S2C
	{
	    public static final int HI_ACK = 0;
	}

	// 创建 C2S 包
	@SuppressWarnings("unchecked")
	public static Packet<?> createC2S(int packetId, Consumer<NbtCompound> builder)
	{
	    NbtCompound nbt = new NbtCompound();
	    builder.accept(nbt);
	    return (Packet<?>) FanetlibPackets.createC2S(PACKET_TYPE, new EarlyCompatPacket(packetId, nbt));
	}

	// 创建 S2C 包
	@SuppressWarnings("unchecked")
	public static Packet<?> createS2C(int packetId, Consumer<NbtCompound> builder)
	{
	    NbtCompound nbt = new NbtCompound();
	    builder.accept(nbt);
	    return (Packet<?>) FanetlibPackets.createS2C(PACKET_TYPE, new EarlyCompatPacket(packetId, nbt));
	}
	
	public static void registerPackets()
	{
		FanetlibPackets.registerDual(
			EarlyCompatNetwork.PACKET_TYPE,
			PacketCodec.of(EarlyCompatPacket::write, EarlyCompatPacket::new),
			EarlyCompatC2ServerHandler::handle,//server
			EarlyCompatS2ClientHandler::handle//client
		);
	}
	
	public static void registerServerEvents()
	{
		FanetlibServerEvents.registerPlayerJoinListener(
			EarlyCompatC2ServerHandler::onPlayerJoin
		);
		FanetlibServerEvents.registerPlayerDisconnectListener(
			EarlyCompatC2ServerHandler::onPlayerDisconnect
		);
	}
	
	public static void registerClientEvents()
	{
		FanetlibClientEvents.registerGameJoinListener(
        	EarlyCompatS2ClientHandler::onGameJoin
        );
        FanetlibClientEvents.registerPlayerRespawnListener(
        	EarlyCompatS2ClientHandler::onPlayerRespawn
        );
		FanetlibClientEvents.registerDisconnectListener(
			EarlyCompatS2ClientHandler::onDisconnect
		);
	}
}
