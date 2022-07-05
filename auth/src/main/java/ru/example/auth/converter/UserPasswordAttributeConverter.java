package ru.example.auth.converter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
@Component
public class UserPasswordAttributeConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String password) {
        return new BCryptPasswordEncoder(12).encode(password);
    }

    @Override
    public String convertToEntityAttribute(String password) {
        return password;
    }
}
