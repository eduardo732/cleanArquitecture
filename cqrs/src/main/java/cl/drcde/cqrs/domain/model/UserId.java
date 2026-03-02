package cl.drcde.cqrs.domain.model;

import cl.drcde.cqrs.domain.vo.UUIDv4;
import cl.drcde.cqrs.domain.vo.ValueObject;
import lombok.NonNull;

import java.util.Objects;
import java.util.UUID;

public final class UserId extends UUIDv4 {

    public UserId(@NonNull String value) {
        super(value);
    }

    /**
     * Generates a new random UserId
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID().toString());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof UserId that)) return false;
      return Objects.equals(this.value(), that.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value());
    }

    @Override
    public String toString() {
        return this.value();
    }
}

