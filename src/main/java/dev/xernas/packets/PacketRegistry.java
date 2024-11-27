package dev.xernas.packets;

import dev.xernas.particle.ParticleException;
import dev.xernas.enums.Bound;
import dev.xernas.enums.State;
import dev.xernas.packets.handshake.HandshakePacket;
import dev.xernas.packets.status.PingPongPacket;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

    private static final Map<State, Map<Bound[], Map<Integer, Class<? extends IPacket>>>> packets = new HashMap<>();

    static {
        register(State.HANDSHAKE, 0x00, HandshakePacket.class, Bound.INBOUND);
        register(State.STATUS, 0x01, PingPongPacket.class, Bound.INBOUND, Bound.OUTBOUND);
    }

    private static void register(State state, int id, Class<? extends IPacket> packet, Bound... bounds) {
        packets.computeIfAbsent(state,
                        k -> new HashMap<>()
                )
                .computeIfAbsent(bounds,
                        k -> new HashMap<>()
                )
                .put(id, packet);
    }

    public static IPacket get(State state, Bound bound, int id) throws ParticleException {
        Map<Integer, Class<? extends IPacket>> packetList = getPacketsOf(state, bound);
        if (packetList == null) return null;
        try {
            if (!packetList.containsKey(id)) return null;
            return packetList.get(id).getConstructor().newInstance();
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            throw new ParticleException("Failed to create packet instance", e);
        }
    }

    public static int getId(State state, Bound bound, Class<? extends IPacket> packet) {
        Map<Integer, Class<? extends IPacket>> packetList = getPacketsOf(state, bound);
        if (packetList == null) return -1;
        for (Map.Entry<Integer, Class<? extends IPacket>> entry : packetList.entrySet()) {
            if (entry.getValue().equals(packet)) {
                return entry.getKey();
            }
        }
        return -1;
    }

    public static boolean isPacket(State state, Bound bound, PacketData packetData, Class<? extends IPacket> packet) {
        Integer packetId = packetData.get("packetId");
        if (packetId == null) return false;
        return isPacket(state, bound, packetId, packet);
    }

    public static boolean isPacket(State state, Bound bound, int packetId, Class<? extends IPacket> packet) {
        Map<Integer, Class<? extends IPacket>> packetList = getPacketsOf(state, bound);
        if (packetList == null) return false;
        int id = getId(state, bound, packet);
        return id == packetId;
    }

    private static Map<Integer, Class<? extends IPacket>> getPacketsOf(State state, Bound bound) {
        Map<Bound[], Map<Integer, Class<? extends IPacket>>> statePackets = packets.get(state);
        if (statePackets == null) return null;
        Map<Integer, Class<? extends IPacket>> boundPackets = new HashMap<>();
        for (Map.Entry<Bound[], Map<Integer, Class<? extends IPacket>>> entry : statePackets.entrySet()) {
            for (Bound b : entry.getKey()) {
                if (b == bound) {
                    boundPackets.putAll(entry.getValue());
                }
            }
        }
        if (boundPackets.isEmpty()) return null;
        return boundPackets;
    }

}