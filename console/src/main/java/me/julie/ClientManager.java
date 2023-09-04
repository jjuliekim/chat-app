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

                // contacts menu
                if (message.equals("displayContactsMenu%")) {
                    contactsMenu();
                }

                // list of contacts for contacts menu
                if (message.startsWith("contactName%")) {
                    String[] info = message.split("%");
                    String index = info[1];
                    String displayName = info[2];
                    printReset(BOLD + hex("#ebac1a") + "[" + index + "] ");
                    printlnReset(BOLD + displayName);
                }

                // rest of contacts menu
                if (message.startsWith("getContactMenuInput%")) {
                    String[] info = message.split("%");
                    int numOfContacts = Integer.parseInt(info[1]);
                    System.out.print("-> ");
                    String input = scanner.nextLine();
                    if (input.equalsIgnoreCase("back")) {
                        mainMenu();
                        return;
                    }
                    if (input.equalsIgnoreCase("new")) {
                        System.out.print("New contact username -> ");
                        String contactName = scanner.nextLine();
                        ws.sendText("addContact%" + contactName);
                        return;
                    }
                    try {
                        if (Integer.parseInt(input) <= numOfContacts) {
                            ws.sendText("getContactInfo%" + input);
                        }
                    } catch (NumberFormatException ignored) {
                        printlnReset(RED + ITALICS + "[INVALID OPTION]");
                        contactsMenu();
                    }
                }

                // individual contact's info/settings
                if (message.startsWith("contactInfo%")) {
                    String[] info = message.split("%");
                    String contactUsername = info[1];
                    String contactDisplayName = info[2];
                    System.out.println();
                    System.out.println("username: " + contactUsername);
                    System.out.println("display: " + contactDisplayName);
                    printlnReset(BOLD + hex("#f5d773") + info[1]);
                    printReset(BOLD + hex("#ebac1a") + "[1] ");
                    printlnReset(BOLD + "Change display name");
                    printReset(BOLD + hex("#ebac1a") + "[2] ");
                    printlnReset(BOLD + "Remove contact");
                    printReset(BOLD + hex("#ebac1a") + "[3] ");
                    printlnReset(BOLD + "Open conversation");
                    printReset(BOLD + hex("#ebac1a") + "[4] ");
                    printlnReset(BOLD + "Back");
                    System.out.print("-> ");

                    String input = scanner.nextLine();
                    switch (input) {
                        case "1" -> {
                            System.out.println("Current display name -> " + contactDisplayName);
                            System.out.print("New display name -> ");
                            String newName = scanner.nextLine();
                            ws.sendText("changeDisplayName%" + contactUsername + "%" + newName);
                        }
                        case "2" -> ws.sendText("removeContact%" + contactUsername);
                        case "3" -> {
                            System.out.println("opening conversation with " + contactDisplayName);
                        }
                        case "4" -> contactsMenu();
                    }
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
        System.out.println();
        printlnReset(BOLD + hex("#f5d773") + "== Contacts ==");
        printlnReset(BOLD + hex("#ebac1a") + "[BACK]  [NEW]");
        ws.sendText("allContactNames%");
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
