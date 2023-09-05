package me.julie;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import me.julie.data.ContactsInfo;
import me.julie.data.JsonManager;
import me.julie.data.User;

import java.io.IOException;
import java.util.*;

public class ServerManager {
    private final JsonManager jsonManager = new JsonManager();

    public void startServer() throws IOException {
        var app = Javalin.create().start(20202);
        jsonManager.load();
        Map<WsContext, String> connections = new HashMap<>();
        app.ws("/chat", ws -> {
            ws.onMessage(ctx -> { // when a message is received by/sent to the server
                System.out.println("server received: " + ctx.message());

                // ===== start up / log in =====
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
                        ctx.send("redSystemMsg%[Username already exists or incorrect password]");
                        ctx.send("login%");
                    }
                }

                // ===== contacts =====
                // sends [#] contact's display name
                if (ctx.message().equals("allContactNames%")) {
                    User user = jsonManager.getLoginInfo().getLogins().get(connections.get(ctx));
                    int index = 1;
                    if (!user.getContacts().isEmpty()) {
                        for (ContactsInfo contact : user.getContacts()) {
                            ctx.send("contactName%" + index + "%" + contact.getDisplayName());
                            index++;
                        }
                    }
                    ctx.send("getContactMenuInput%" + index);
                }

                // get and send contact's info
                if (ctx.message().startsWith("getContactInfo%")) {
                    String[] info = ctx.message().split("%");
                    int index = Integer.parseInt(info[1]);
                    ContactsInfo contact = jsonManager.getLoginInfo().getLogins().get(connections.get(ctx))
                            .getContacts().get(index - 1);
                    ctx.send("contactInfo%" + contact.getUsername() + "%" + contact.getDisplayName());
                }

                // add contact
                if (ctx.message().startsWith("addContact%")) {
                    String[] info = ctx.message().split("%");
                    String contactUsername = info[1];
                    if (!jsonManager.getLoginInfo().getLogins().containsKey(contactUsername)) {
                        ctx.send("redSystemMsg%[User does not exist]");
                        ctx.send("displayContactsMenu%");
                    } else {
                        jsonManager.getLoginInfo().getLogins().get(connections.get(ctx)).getContacts()
                                .add(new ContactsInfo(contactUsername,
                                        jsonManager.getLoginInfo().getLogins().get(contactUsername).getDisplayName()));
                        jsonManager.save();
                        ctx.send("greenSystemMsg%[Contact added]");
                    }
                    ctx.send("displayContactsMenu%");
                }

                // remove contact & conversation log
                if (ctx.message().startsWith("removeContact%")) {
                    String[] info = ctx.message().split("%");
                    String username = info[1];
                    int indexOfName = jsonManager.getLoginInfo().getLogins().get(connections.get(ctx))
                            .getContactUsernames().indexOf(username);
                    jsonManager.getLoginInfo().getLogins().get(connections.get(ctx)).getContacts().remove(indexOfName);
                    for (List<String> groupChat : jsonManager.getChatId().getChatIds().keySet()) {
                        if (groupChat.contains(username) && groupChat.contains(connections.get(ctx))) {
                            jsonManager.getChatId().getChatIds().remove(groupChat);
                            int chatId = jsonManager.getChatId().getChatIds().get(groupChat);
                            jsonManager.getChatInfo().getChatLogs().remove(chatId);
                        }
                    }
                    jsonManager.save();
                    ctx.send("greenSystemMsg%[CONTACT REMOVED]");
                    ctx.send("displayContactsMenu%");
                }

                // change contact's display name
                if (ctx.message().startsWith("changeDisplayName%")) {
                    String[] info = ctx.message().split("%");
                    int indexOfContact = jsonManager.getLoginInfo().getLogins().get(connections.get(ctx))
                            .getContactUsernames().indexOf(info[1]);
                    jsonManager.getLoginInfo().getLogins().get(connections.get(ctx)).getContacts()
                            .get(indexOfContact).setDisplayName(info[2]);
                    jsonManager.save();
                    ctx.send("greenSystemMsg%[DISPLAY NAME CHANGED]");
                    ctx.send("displayContactsMenu%");
                }

                // ===== chats =====

            });
            ws.onClose(connections::remove);
        });
    }
}
