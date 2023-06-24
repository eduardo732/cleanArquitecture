package cl.drcde.cqrs.infrastructure.eventbus;

public abstract class BaseBus {
    protected Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (Exception e) {
            return null;
        }
    }
}
