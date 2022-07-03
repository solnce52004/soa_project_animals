package ru.example.animals.service.api.utilservice;

import org.springframework.beans.factory.annotation.Autowired;
import ru.example.animals.dto.AnimalDTO;
import ru.example.animals.dto.ResponseDTO;

public interface ApiService {
    String getHandlerName();

    ResponseDTO getResponse(AnimalDTO dto);

    @Autowired
    default void registerMySelf(Registrar registrar) {
        registrar.register(getHandlerName(), this);
    }
}
