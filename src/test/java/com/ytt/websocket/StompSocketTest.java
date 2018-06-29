package com.ytt.websocket;


import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.sockjs.frame.Jackson2SockJsMessageCodec;

/**
 * @author yantaotao
 * @date 2018/6/27
 */
public class StompSocketTest {
    private static Logger logger = LoggerFactory.getLogger(StompSocketTest.class);
    private final static WebSocketHttpHeaders headers = new WebSocketHttpHeaders();

    @Test
    public void testStompSubscribe() throws ExecutionException, InterruptedException {
        StompSocketTest helloClient = new StompSocketTest();
        ListenableFuture<StompSession> f = helloClient.connect("http://172.21.17.13:9888/register?id=1");
        StompSession stompSession = f.get();
        logger.info("Subscribing to greeting topic using session " + stompSession);
        helloClient.subscribeGreetings("/user/queue/notice", stompSession);
        Thread.sleep(600000);
    }

    public ListenableFuture<StompSession> connect(String url) {
        Transport webSocketTransport = new WebSocketTransport(new StandardWebSocketClient());
        List<Transport> transports = Collections.singletonList(webSocketTransport);
        SockJsClient sockJsClient = new SockJsClient(transports);
        sockJsClient.setMessageCodec(new Jackson2SockJsMessageCodec());
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        return stompClient.connect(url, headers, new MyHandler());
        //return stompClient.connect(url, headers, new MyHandler(), "localhost", 9090);
    }

    public void subscribeGreetings(String url, StompSession stompSession) throws ExecutionException, InterruptedException {
        stompSession.subscribe(url, new StompFrameHandler() {
            public Type getPayloadType(StompHeaders stompHeaders) {
                return byte[].class;
            }
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                logger.info("Received greeting " + new String((byte[]) o));
            }
        });
    }

    private class MyHandler extends StompSessionHandlerAdapter {
        public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
            logger.info("Now connected");
        }

        @Override
        public void handleTransportError(StompSession session, Throwable exception) {
            exception.printStackTrace();
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
            super.handleFrame(headers, payload);
            logger.debug("=========================handleFrame");
        }
    }
}

