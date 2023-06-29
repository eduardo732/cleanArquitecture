package cl.drcde.cqrs.domain.vo;

import cl.drcde.cqrs.domain.shared.exception.DomainException;
import lombok.NonNull;

public final class Username implements ValueObject{
    public static final int LENGTH = 200;
    public final static int MIN_LENGTH = 1;
    public final static int MAX_LENGTH = 500;
    private final @NonNull String _value;

    public Username(@NonNull String value) {
        if(value.trim().isEmpty()) throw new DomainException("The attribute username can't be empty");
        if(this._validate(value)) throw new DomainException("The attribute username (%s) had a wrong format.");
        _value = value;
    }

    private boolean _validate(@NonNull String value) {
        int length = value.length();
        return length < MIN_LENGTH || length > MAX_LENGTH;
    }

    @Override
    public String  value() {
        return this._value;
    }
}
