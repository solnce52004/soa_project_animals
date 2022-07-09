package ru.example.animals.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.example.animals.enums.GenderType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter
@Component
@Slf4j
public class GenderTypeAttributeConverter implements AttributeConverter<GenderType, Character> {

    @Override
    public Character convertToDatabaseColumn(GenderType enumObject) {
        log.info("convertToDatabaseColumn {}", enumObject);
        if (enumObject == null) {
            return null;
        }
        return enumObject.getName();
    }

    @Override
    public GenderType convertToEntityAttribute(Character ch) {
        log.info("convertToEntityAttribute {}", ch);
        if (ch == null) {
            return GenderType.UNTITLED;
        }

        return Stream.of(GenderType.values())
                .filter(c -> c.getName().equals(ch))
                .findFirst()
                .orElse(GenderType.UNTITLED);
    }
}
