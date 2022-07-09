package ru.example.animals.exception.custom_exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class DuplicateAnimalNameException extends RuntimeException {
    private static final long serialVersionUID = -2526231598160599171L;

    public DuplicateAnimalNameException() {
        super("Duplicate new animal name");
    }
}