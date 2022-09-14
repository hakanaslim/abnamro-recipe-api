package com.abnamro.recipe.filter;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class RequestHeaderStoreTests {
  final static String X_USER_ID = "TestUser";
  final static String X_USER_HEADER_NAME = "x-user-id";
  @Test
  void test_can_store_userID() {
    RequestHeaderStore store = new RequestHeaderStore();

    store.addHeader(X_USER_HEADER_NAME, X_USER_ID);

    assertEquals(X_USER_ID, store.getHeaders().get(X_USER_HEADER_NAME));
  }

  @Test
  void test_unload_store() {
    RequestHeaderStore store = new RequestHeaderStore();
    store.addHeader(X_USER_HEADER_NAME, X_USER_ID);
    store.unload();
    assertNull(store.getUserID());
  }

  @Test
  void test_can_store_different_userID_thread() throws InterruptedException {
    RequestHeaderStore store = new RequestHeaderStore();
    store.addHeader(X_USER_HEADER_NAME, X_USER_ID);

    ExecutorService executor = Executors.newFixedThreadPool(2);
    Future<Boolean> t1 = executor.submit(() -> checkToken(store, "userName-1"));
    Future<Boolean> t2 = executor.submit(() -> checkToken(store, "userName-2"));

    assertEquals(X_USER_ID, store.getHeaders().get(X_USER_HEADER_NAME));
    try {
      t1.get();
      t2.get();
    } catch (ExecutionException ex) {
      fail(ex.getCause().getMessage(), ex.getCause());
    }
  }

  private boolean checkToken(RequestHeaderStore store, String value) {
    store.addHeader(X_USER_HEADER_NAME, value);
    assertEquals(value, store.getHeaders().get(X_USER_HEADER_NAME));
    return true;
  }
}
