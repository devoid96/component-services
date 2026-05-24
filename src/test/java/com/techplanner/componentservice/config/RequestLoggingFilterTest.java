package com.techplanner.componentservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLoggingFilterTest {

    private final RequestLoggingFilter filter = new RequestLoggingFilter(new ObjectMapper());

    @Test
    @DisplayName("doFilter procesa solicitudes GET sin body")
    void shouldFilterGetRequest_whenCalled_passThrough() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/v1/component-service/cpus");
        request.setQueryString("page=0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = Mockito.mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(200);
        Mockito.verify(chain, Mockito.times(1)).doFilter(Mockito.any(), Mockito.eq(response));
    }

    @Test
    @DisplayName("doFilter procesa solicitudes con body")
    void shouldFilterPostRequest_whenBodyExists_passThrough() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/v1/component-service/cpus");
        request.setContentType("application/json");
        request.setContent("{\"brand\":\"AMD\",\"model\":\"Ryzen 7 7800X3D\"}".getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = (servletRequest, servletResponse) -> {
            servletRequest.getInputStream().readAllBytes();
            ((MockHttpServletResponse) servletResponse).setStatus(201);
        };

        filter.doFilter(request, response, chain);

        assertThat(response.getStatus()).isEqualTo(201);
    }
}