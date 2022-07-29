package nwpu.deviceonenet.onenet.websocket;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Junho
 * @date 2022/7/18 19:25
 */
@Component
public class WebSocketProducer {

    public void sendToWs(String msg){
        CopyOnWriteArraySet<WebSocketServer> webSocketSet =
                WebSocketServer.getWebSocketSet();
        webSocketSet.forEach(c -> {
            try {
                c.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
