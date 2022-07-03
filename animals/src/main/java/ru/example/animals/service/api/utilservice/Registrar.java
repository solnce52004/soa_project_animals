package ru.example.animals.service.api.utilservice;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class Registrar {
    private final Map<String, ApiService> map = new HashMap<>();

    public void register(String method, ApiService apiService){
        map.put(method, apiService);
    }

    public ApiService getResponseService(String method){
        return getMap().get(method);
    }
}
