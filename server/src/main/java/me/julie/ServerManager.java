package me.julie;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import me.julie.data.JsonManager;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerManager {
    private final JsonManager jsonManager = new JsonManager();

    public void startServer() throws IOException {
        var app = Javalin.create().start(20202);
        jsonManager.load();
        Map<WsContext, String> connections = new HashMap<>();
        app.ws("/chat", ws -> {
            ws.onMessage(ctx -> { // when a message is received by/sent to the server



            });
            ws.onClose(connections::remove);
        });
    }
}
