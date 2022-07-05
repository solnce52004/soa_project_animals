package ru.example.auth.exception.mapper;

import java.util.Map;
import java.util.Set;

public interface MapperErrorFields<T extends Exception> {
    Map<String, Set<String>> getErrors(T ex);
}
