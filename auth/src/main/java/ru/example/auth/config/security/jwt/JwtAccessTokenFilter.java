package ru.example.auth.config.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.example.auth.dto.response.ResponseDTO;
import ru.example.auth.exception.custom_exception.BaseError;
import ru.example.auth.exception.custom_exception.JwtAuthException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class JwtAccessTokenFilter extends GenericFilterBean {
    private final JwtAccessTokenProvider jwtAccessTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //вытащили из запроса по тайному названию хэдера - токен
        final String token = jwtAccessTokenProvider.resolveToken(req);

        try {
            // проверили, что не протух
            if (token != null && jwtAccessTokenProvider.validateToken(token)) {

                final Authentication authentication = jwtAccessTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthException e) {
            //при перехвате ошибки валидации токена
            //чистим контекст и в респонс AbstractHandlerExceptionResolver пробрасываем ошибку с пойманным статусом
            SecurityContextHolder.clearContext();

            res.setContentType("*/*");
            res.setStatus(HttpStatus.FORBIDDEN.value());
            res.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            res.setHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name());

            BaseError error = new BaseError()
                    .setDetailMessage(e.getMessage())
                    .setHttpStatus(HttpStatus.FORBIDDEN.value())
                    .setHttpStatusName(HttpStatus.FORBIDDEN);

            final ResponseDTO responseDTO = new ResponseDTO()
                    .setError(error)
                    .setHttpStatus(HttpStatus.FORBIDDEN);

            ObjectMapper mapper = new ObjectMapper();
            final String json = mapper.writeValueAsString(responseDTO);
//            final String escapedJson = StringEscapeUtils.escapeJson(json);

            try (PrintWriter writer = res.getWriter()) {
                writer.write(json);
            }

            return;
        }

        chain.doFilter(req, res);
    }
}
