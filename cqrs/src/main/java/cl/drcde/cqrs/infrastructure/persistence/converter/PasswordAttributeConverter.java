package cl.drcde.cqrs.infrastructure.persistence.converter;

import cl.drcde.cqrs.domain.vo.Password;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
public final class PasswordAttributeConverter implements AttributeConverter<Password, String> {
    @Override
    public String convertToDatabaseColumn(Password attribute) {
        log.debug("PasswordAttributeConverter::convertToDatabaseColumn() attribute: {}", attribute);
        return attribute.value();
    }

    @Override
    public Password convertToEntityAttribute(String dbData) {
        log.debug("PasswordAttributeConverter::convertToEntityAttribute() attribute: {}", dbData);
        return dbData == null ? null : new Password(dbData);
    }
}
