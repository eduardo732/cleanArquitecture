package cl.drcde.cqrs.domain.vo;

import cl.drcde.cqrs.domain.shared.exception.DomainException;
import lombok.NonNull;

import java.util.UUID;
import java.util.regex.Pattern;

public class UUIDv4 implements ValueObject{
    public static final int LENGTH = 36;

    private static final Pattern UUID_PATTERN = Pattern.compile("[0-9A-Za-z]{8}-[0-9A-Za-z]{4}-4[0-9A-Za-z]{3}-[89ABab][0-9A-Za-z]{3}-[0-9A-Za-z]{12}");
    private final @NonNull String _value;

    public UUIDv4(@NonNull String value) {
        if(value.trim().isEmpty()) throw new DomainException("UUID can't be null or empty");
        if(!UUID_PATTERN.matcher(value).matches()) throw new DomainException(String.format("The value %s isn't a correct UUID.", value));

        this._value = value;
    }
    public static UUIDv4 generate() { return new UUIDv4(UUID.randomUUID().toString()); }
    @Override
    public String value() {
        return this._value.toString();
    }
}
