package cl.drcde.cqrs.domain.vo;

public final class Active implements ValueObject {
    private Boolean _value;

    public Active(Boolean _value) {
        this._value = _value;
    }

    @Override
    public Boolean value() {
        return this._value;
    }

    public void onOff() {
        this._value = !this.value();
    }
}
