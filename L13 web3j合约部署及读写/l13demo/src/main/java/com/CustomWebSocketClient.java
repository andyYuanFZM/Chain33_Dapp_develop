package com;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import org.web3j.protocol.websocket.WebSocketClient;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;
import java.text.SimpleDateFormat;

@Slf4j
public class CustomWebSocketClient extends WebSocketClient {
    public interface  ReconnectHandlerInterface{
        void onReconnect();
    }
    ArrayList<ReconnectHandlerInterface> handlers=new ArrayList<>();
    @Synchronized
    private void doReconnect(){
        if(this.isClosed()){
            try {
                this.reconnectBlocking();
                for (ReconnectHandlerInterface handler : handlers) {
                    handler.onReconnect();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Current thread needs to shutdown, Reconnect to websocket failed");
            }
        }
    }

    public CustomWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    public CustomWebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, httpHeaders);
    }

    @Override
    @Synchronized
    public void onClose(int code, String reason, boolean remote) {
    	System.out.print("进入关闭处理" + code + remote);
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String currentDateTime = dateFormat.format(now);
        System.out.println("Current date and time is " + currentDateTime);
        if(remote || code!=1000){
            this.doReconnect();
            //System.out.println("Reconnecting WebSocket connection to {}, because of disconnection reason: '{}'.",uri, reason);
        }else {
            super.onClose(code, reason, remote);
        }
    }

}