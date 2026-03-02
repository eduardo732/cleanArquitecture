package cl.drcde.cqrs.domain.vo;

import lombok.NonNull;

import java.util.Objects;
import java.util.UUID;

public final class UUIDv4 implements ValueObject {
    public static final int LENGTH = 36;
    private final @NonNull String _value;

    public UUIDv4(@NonNull String value) {
        try {
            // Validate that the string is a valid UUID
            UUID.fromString(value);
            this._value = value;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("The provided string is not a valid UUID format");
        }
    }

    /**
     * Generates a new random UUIDv4
     */
    public static UUIDv4 generate() {
        return new UUIDv4(UUID.randomUUID().toString());
    }

    @Override
    public String value() {
        return this._value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof UUIDv4)) return false;
        UUIDv4 that = (UUIDv4) other;
        return Objects.equals(_value, that._value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_value);
    }

    @Override
    public String toString() {
        return _value;
    }
}

