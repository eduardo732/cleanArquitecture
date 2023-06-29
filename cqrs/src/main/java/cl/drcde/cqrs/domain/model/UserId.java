package cl.drcde.cqrs.domain.model;

import cl.drcde.cqrs.domain.shared.model.EntityId;
import cl.drcde.cqrs.domain.vo.UUIDv4;
import lombok.NonNull;

public class UserId extends UUIDv4 implements EntityId {
    public UserId(@NonNull String value) {
        super(value);
    }
}
