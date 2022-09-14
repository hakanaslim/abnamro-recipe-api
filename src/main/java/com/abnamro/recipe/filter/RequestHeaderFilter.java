package com.abnamro.recipe.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class RequestHeaderFilter implements Filter {

  private final RequestHeaderStore store;

  @Autowired
  public RequestHeaderFilter(RequestHeaderStore store) {
    this.store = store;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                       FilterChain filterChain) throws ServletException, IOException {
    String headerName = "x-user-id";
    String headerValue = extractHeader((HttpServletRequest) servletRequest, headerName);
    store.addHeader(headerName, headerValue);

    if (filterChain != null) {
      filterChain.doFilter(servletRequest, servletResponse);
    }
  }

  private String extractHeader(HttpServletRequest request, String headerName) {
    return request.getHeader(headerName);
  }
}
