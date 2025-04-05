package dev.xernas.ion.types;

public class Property {

    private final String name;
    private final String value;
    private String signature;

    public Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public Property(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }
}
