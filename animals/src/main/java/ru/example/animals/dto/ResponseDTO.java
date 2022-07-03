package ru.example.animals.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;
import org.springframework.http.HttpStatus;
import ru.example.animals.exception.custom_exception.BaseError;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public final class ResponseDTO implements Serializable {
    @JsonIgnore
    @Transient
    private static final long serialVersionUID = 110L;

    private String info;
    private BaseError error;
    private Set<AnimalDTO> animals = new HashSet<>();
    private HttpStatus httpStatus;
}
