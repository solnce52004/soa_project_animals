package ru.example.animals.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestVerifyTokenDTO {
    private String accessTokenData;
}
