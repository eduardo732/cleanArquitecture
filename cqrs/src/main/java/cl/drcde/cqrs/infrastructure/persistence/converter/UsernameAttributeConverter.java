package cl.drcde.cqrs.infrastructure.persistence.converter;

import cl.drcde.cqrs.domain.vo.Password;
import cl.drcde.cqrs.domain.vo.Username;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
public final class UsernameAttributeConverter implements AttributeConverter<Username, String> {
    @Override
    public String convertToDatabaseColumn(Username attribute) {
        log.debug("UsernameAttributeConverter::convertToDatabaseColumn() attribute: {}", attribute);
        return attribute.value();
    }

    @Override
    public Username convertToEntityAttribute(String dbData) {
        log.debug("UsernameAttributeConverter::convertToEntityAttribute() attribute: {}", dbData);
        return dbData == null ? null : new Username(dbData);
    }
}
