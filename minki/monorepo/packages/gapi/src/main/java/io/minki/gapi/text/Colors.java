package io.minki.gapi.text;

public enum Colors {
    ERROR(0xFF0000),
    WARNING(0xFFA500),
    SUCCESS(0x00FF00),
    PRIMARY(0x00fbff),
    SECONDARY(0xd200f7),
    TERTIARY(0xa1a1a1);

    private final int value;

    Colors(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
