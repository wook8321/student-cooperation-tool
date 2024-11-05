package com.stool.studentcooperationtools.websocket;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CustomSessionHandlerAdapter<T> implements StompFrameHandler {

    private final CompletableFuture<T> completableFuture;

    private final Class<T> tClass;

    public CustomSessionHandlerAdapter(final Class<T> tClass) {
        this.completableFuture = new CompletableFuture<T>();
        this.tClass = tClass;
    }

    @Override
    public Type getPayloadType(final StompHeaders headers) {
        return this.tClass;
    }

    @Override
    public void handleFrame(final StompHeaders headers, final Object payload) {
        completableFuture.complete((T)payload);
    }

    public T get(long time) throws ExecutionException, InterruptedException, TimeoutException {
        return this.completableFuture.get(time, TimeUnit.SECONDS);
    }
}
