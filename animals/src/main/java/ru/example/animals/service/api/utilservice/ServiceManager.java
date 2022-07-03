package ru.example.animals.service.api.utilservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceManager {
    private final Registrar registrar;

    @Autowired
    public ServiceManager(
            Registrar registrar,
            List<ApiService> apiServices
    ) {
        this.registrar = registrar;
//        LogHelper.logResponseSenderList(senders);
    }

    public ApiService getResponseService(String method) {
        return registrar.getResponseService(method);
    }
}
