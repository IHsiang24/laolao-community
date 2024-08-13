package com.xiangkai.community.alpha.callback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class CustomizedTemplate {

    private final Client client = new Client();

    public <T> T execute(Callback<T> callback) {
        try {
            return callback.doWithClient(client);
        } catch (CallbackException e) {
            throw translateException(e);
        }
    }

    public Client getClient() {
        return client;
    }

    /**
     * 接受一个输入参数，返回一个结果
     */
    public <T, R> R execute(Function<T, R> function, T t) {
        return function.apply(t);
    }

    /**
     * 无参数，返回一个结果
     */
    public <T> T execute(Supplier<T> supplier) {
        return supplier.get();
    }

    /**
     * 接受一个输入参数，无返回
     */
    public <T> void execute(Consumer<T> consumer, T t) {
        consumer.accept(t);
    }

    private RuntimeException translateException(Exception exception) {
        return new RuntimeException(exception);
    }

    public <T, R> Function<T, R> lambdaWrapper(Function<T, R> function) {
        return i -> {
            try {
                function.apply(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };

    }


    public interface Callback<T> {
        T doWithClient(Client client) throws CallbackException;
    }

    public static class Client {

        private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

        public boolean doRequest() {
            LOGGER.info("Client::doRequest");
            return true;
        }
    }

    public static class CallbackException extends Exception {

        public CallbackException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        CustomizedTemplate template = new CustomizedTemplate();

        template.execute(new Callback<Boolean>() {
            @Override
            public Boolean doWithClient(Client client) throws CallbackException {
                return client.doRequest();
            }
        });
    }

}
