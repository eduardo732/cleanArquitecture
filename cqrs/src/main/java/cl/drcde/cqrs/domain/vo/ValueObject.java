package cl.drcde.cqrs.domain.vo;

public interface ValueObject {
    public boolean equals(Object other);
    public int hashCode();
    public <T> T value();
}
