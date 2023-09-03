package me.julie;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static me.julie.Colors.*;

public class ClientManager {
    private WebSocket ws;
    private Scanner scanner;
    private String username;

    public void run() throws IOException {
        WebSocketFactory factory = new WebSocketFactory();
        factory.setConnectionTimeout(8000);
        ws = factory.createSocket("ws://localhost:20202/chat");
        scanner = new Scanner(System.in);
        try {
            ws.connect();
            System.out.println();
            System.out.println();
            printlnReset(GREEN + "Connected to server!");
            printReset(BOLD + "Username -> ");
            username = scanner.nextLine();
            ws.sendText("username%" + username);
        } catch (WebSocketException e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
        }

        ws.addListener(new WebSocketAdapter() {
            // when server sends client something:
            @Override
            public void onTextMessage(WebSocket websocket, String message) {
                printlnReset(BOLD + BLUE + "client received: " + message);

                // server sends client a message that should be displayed in green
                if (message.startsWith("greenSystemMsg%")) {
                    printlnReset(GREEN + message.substring(15));
                }
                // server sends client a message that should be displayed in red
                if (message.startsWith("redSystemMsg%")) {
                    printlnReset(RED + message.substring(13));
                }

                // username does not exist yet/signing up
                if (message.equals("signup%")) {
                    printReset(BOLD + "Password -> ");
                    String password = scanner.nextLine();
                    printReset(BOLD + "Display Name -> ");
                    String displayName = scanner.nextLine();
                    ws.sendText("creatingAccount%" + username + "%" + password + "%" + displayName);
                }

                // username has account/log in prompt
                if (message.equals("login%")) {
                    printReset(BOLD + "Password -> ");
                    String password = scanner.nextLine();
                    ws.sendText("loginInfo%" + username + "%" + password);
                }

                // main menu
                if (message.equals("displayMenu%")) {
                    System.out.println("will display the main menu");
                    mainMenu();
                }
            }
        });


    }

    // displays the chat app menu
    private void mainMenu() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E - MMM dd, yyyy");
        String formattedDate = now.format(formatter);
        System.out.println();
        printlnReset(BOLD + hex("#bc94e0") + "== Main Menu ==");
        System.out.println(formattedDate);
        printReset(BOLD + hex("#853ec7") + "[1] ");
        printlnReset(BOLD + "Contacts");
        printReset(BOLD + hex("#853ec7") + "[2] ");
        printlnReset(BOLD + "Messages");
        printReset(BOLD + hex("#853ec7") + "[3] ");
        printlnReset(BOLD + "Settings");
        System.out.print("-> ");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> contactsMenu();
            case "2" -> messagesMenu();
            case "3" -> settingsMenu();
            default -> {
                printlnReset(RED + ITALICS + "Invalid input.");
                mainMenu();
            }
        }
    }

    // displays the contacts menu
    private void contactsMenu() {
        System.out.println("user pressed 1, displaying contacts");
    }

    // displays the messages menu
    private void messagesMenu() {
        System.out.println("user pressed 2, displaying messages");
    }

    // displays the settings menu
    private void settingsMenu() {
        System.out.println("user pressed 3, displaying settings");
    }
}
