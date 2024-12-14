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

    public Map<String, Object> getData() {
        return data;
    }

    public static String mapToString(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            sb.append(key).append("=").append(value.toString()).append("\n");
        }
        return sb.toString();
    }

}
