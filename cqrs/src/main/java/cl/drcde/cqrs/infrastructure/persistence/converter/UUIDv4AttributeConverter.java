package cl.drcde.cqrs.infrastructure.persistence.converter;

import cl.drcde.cqrs.domain.vo.UUIDv4;
import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Slf4j
@Converter
public final class UUIDv4AttributeConverter implements AttributeConverter<UUIDv4, String> {

    @Override
    public String convertToDatabaseColumn(UUIDv4 attribute) {
        log.debug("UUIDv4AttributeConverter::convertToDatabaseColumn() attribute: {}", attribute);
        if (attribute == null) {
            return null;
        }
        return attribute.value();
    }

    @Override
    public UUIDv4 convertToEntityAttribute(String dbData) {
        log.debug("UUIDv4AttributeConverter::convertToEntityAttribute() dbData: {}", dbData);
        if (dbData == null) {
            return null;
        }
        return new UUIDv4(dbData);
    }
}

