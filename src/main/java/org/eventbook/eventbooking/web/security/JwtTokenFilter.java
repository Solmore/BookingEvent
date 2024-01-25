package org.eventbook.eventbooking.web.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;


@AllArgsConstructor
public class JwtTokenFilter extends GenericFilterBean {

    private  final JwtTokenProvider jwtTokenProvider;

    @Override
    @SneakyThrows
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain filterChain) {
        String bearerToken = ((HttpServletRequest) request)
                .getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken = bearerToken.substring(7);
        }
        try {
            if (bearerToken != null
                    && jwtTokenProvider.validateToken(bearerToken)) {
                Authentication authentication
                        = jwtTokenProvider.getAuthentication(bearerToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().
                            setAuthentication(authentication);
                }
            }

        } catch (Exception ignored) {

        }
        filterChain.doFilter(request, response);

    }
}
