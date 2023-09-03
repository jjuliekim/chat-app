package me.julie;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import me.julie.data.JsonManager;
import me.julie.data.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ServerManager {
    private final JsonManager jsonManager = new JsonManager();

    public void startServer() throws IOException {
        var app = Javalin.create().start(20202);
        jsonManager.load();
        Map<WsContext, String> connections = new HashMap<>();
        app.ws("/chat", ws -> {
            ws.onMessage(ctx -> { // when a message is received by/sent to the server
                System.out.println("server received: " + ctx.message());
                // check if username has existing data
                if (ctx.message().startsWith("username%")) {
                    String[] info = ctx.message().split("%");
                    String username = info[1];
                    if (jsonManager.getLoginInfo().getLogins().containsKey(username)) {
                        ctx.send("login%");
                    } else {
                        ctx.send("signup%");
                    }
                }

                // create account: assign id, add to logins, save
                if (ctx.message().startsWith("creatingAccount%")) {
                    System.out.println("server is creating account");
                    String[] info = ctx.message().split("%");
                    Random random = new Random();
                    int id = random.nextInt(100, 1000);
                    System.out.println("user's id: " + id);
                    while (jsonManager.getUserId().getUserIds().containsKey(id)) {
                        id = random.nextInt(100, 1000);
                    }
                    System.out.println("server putting id into jsons");
                    jsonManager.getUserId().getUserIds().put(id, info[1]);
                    jsonManager.getLoginInfo().getLogins().put(info[1],
                            new User(info[1], info[2], info[3], id, new ArrayList<>()));
                    jsonManager.save();
                    connections.put(ctx, info[1]);
                    ctx.send("greenSystemMsg%Welcome " + info[3] + "!");
                    ctx.send("displayMenu%");
                }

                // log in: check password
                if (ctx.message().startsWith("loginInfo%")) {
                    String[] info = ctx.message().split("%");
                    String username = info[1];
                    String password = info[2];
                    User user = jsonManager.getLoginInfo().getLogins().get(username);
                    if (user.getPassword().equals(password)) {
                        connections.put(ctx, username);
                        ctx.send("greenSystemMsg%Welcome back " + user.getDisplayName() + "!");
                        ctx.send("displayMenu%");
                    } else {
                        ctx.send("redSystemMsg%Incorrect password.");
                        ctx.send("login%");
                    }
                }

            });
            ws.onClose(connections::remove);
        });
    }
}
