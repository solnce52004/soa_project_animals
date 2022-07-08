package ru.example.auth.config.security.jwt;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import ru.example.auth.exception.custom_exception.JwtAuthException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@AllArgsConstructor
public class JwtAccessTokenFilter extends GenericFilterBean {
    private final JwtAccessTokenProvider jwtAccessTokenProvider;

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        //вытащили из запроса по тайному названию хэдера - токен
        final String token = jwtAccessTokenProvider.resolveToken((HttpServletRequest) request);

        try {
            // проверили, что не протух
            if (token != null && jwtAccessTokenProvider.validateToken(token)) {

                final Authentication authentication = jwtAccessTokenProvider.getAuthentication(token);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (JwtAuthException e) {
            //при перехвате ошибки валидации токета
            //чистим контекст и в респонс пропбрасываем ошибку с пойманным статусом
            SecurityContextHolder.clearContext();

            final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON.getType());
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            try (PrintWriter writer = httpServletResponse.getWriter()) {
                writer.write("{ \"error\": \"" + e.getMessage() + "\" }");
            }
            return;

//            httpServletResponse.sendError(e.getHttpStatus().value(), e.getMessage());
        }

        //применяем фильтр
        chain.doFilter(request, response);
    }
}