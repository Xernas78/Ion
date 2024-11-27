package dev.xernas.packets;

import java.util.HashMap;
import java.util.Map;

public class PacketData {

    private final Map<String, Object> data = new HashMap<>();

    public <T> void put(String key, T value) {
        data.put(key, value);
    }

    public <T> T get(String key) {
        return (T) data.get(key);
    }

}
