package com.abnamro.recipe.filter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Scope("singleton")
public class RequestHeaderStore {

  private final ThreadLocal<Map<String, String>> headers = new ThreadLocal<>();
  public static final String X_USER_ID = "x-user-id";

  public void addHeader(String headerName, String value) {
    headers().put(headerName, value);
  }

  private Map<String, String> headers() {
    Map<String, String> rv = headers.get();
    if (rv == null) {
      rv = new HashMap<>();
      headers.set(rv);
    }
    return rv;
  }

  public Map<String, String> getHeaders() {
    return headers.get();
  }

  public void unload() {
    headers.remove();
  }

  public String getUserID() {
    if (getHeaders() == null) {
      return null;
    }

    return getHeaders().get(X_USER_ID);
  }
}
