package cl.drcde.cqrs.infrastructure.persistence.converter;

import cl.drcde.cqrs.domain.vo.Active;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
public final class ActiveAttributeConverter implements AttributeConverter<Active, Boolean> {
    @Override
    public Boolean convertToDatabaseColumn(Active active) {
        log.debug("ActiveAttributeConverter::convertToDatabaseColumn() attribute: {}", active);
        return active.value();
    }

    @Override
    public Active convertToEntityAttribute(Boolean aBoolean) {
        log.debug("ActiveAttributeConverter::convertToEntityAttribute() attribute: {}", aBoolean);
        return aBoolean == null ? null : new Active(aBoolean);
    }
}
