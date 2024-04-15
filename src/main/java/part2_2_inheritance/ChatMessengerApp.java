package part2_2_inheritance;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

abstract class BaseMessage {
    protected User sender;
    protected String content;

    public BaseMessage(User sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    // Getters and setters
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public abstract String render();
}

class TextMessage extends BaseMessage {
    public TextMessage(User sender, String content) {
        super(sender, content);
    }

    @Override
    public String render() {
        return sender.getNickname() + ": " + content;
    }
}

class User {
    private String nickname;
    private String phoneNumber;
    private String title;

    public User(String nickname, String phoneNumber, String title) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.title = title;
    }

    // Getters and setters
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Nickname: " + nickname + ", Phone Number: " + phoneNumber + ", Title: " + title;
    }
}

// Class representing a chat group
class Chat {
    private String chatName;
    private ArrayList<User> users;
    private ArrayList<BaseMessage> messages;

    public Chat(String chatName) {
        this.chatName = chatName;
        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    // Method to send a message in the chat
    public void addMessage(BaseMessage message) {
        messages.add(message);
    }

    // Method to display all messages in the chat
    public void displayMessages(ListView<String> messageListView) {
        messageListView.getItems().clear();
        for (BaseMessage message : messages) {
            messageListView.getItems().add(message.render());
        }
    }

    public ArrayList<FileMessage> getAllFileMessages() {
        ArrayList<FileMessage> fileMessages = new ArrayList<>();
        for (BaseMessage message : messages) {
            if (message instanceof FileMessage) {
                fileMessages.add((FileMessage) message);
            }
        }
        return fileMessages;
    }

    public ArrayList<ImageMessage> getAllImageMessages() {
        ArrayList<ImageMessage> imageMessages = new ArrayList<>();
        for (BaseMessage message : messages) {
            if (message instanceof ImageMessage) {
                imageMessages.add((ImageMessage) message);
            }
        }
        return imageMessages;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public ArrayList<BaseMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<BaseMessage> messages) {
        this.messages = messages;
    }
}

class FileMessage extends BaseMessage {
    private String fileName;

    public FileMessage(User sender, String content, String fileName) {
        super(sender, content);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String render() {
        return sender.getNickname() + " sent a file: " + fileName;
    }
}

class ImageMessage extends BaseMessage {
    private String imageUrl;

    public ImageMessage(User sender, String content, String imageUrl) {
        super(sender, content);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String render() {
        return sender.getNickname() + " sent an image: " + imageUrl;
    }
}

public class ChatMessengerApp extends Application {

    private Chat selectedChat;
    private ListView<Chat> chatListView;
    private ListView<String> userListView;
    private ListView<String> messageListView;
    private ComboBox<User> senderComboBox;
    private ComboBox<String> messageTypeComboBox;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField nicknameField = new TextField();
        nicknameField.setPromptText("Nickname");
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone Number");
        TextField titleField = new TextField();
        titleField.setPromptText("Title");

        Button addUserButton = new Button("Add User");
        addUserButton.setOnAction(event -> {
            String nickname = nicknameField.getText();
            String phoneNumber = phoneNumberField.getText();
            String title = titleField.getText();
            if (!nickname.isEmpty() && !phoneNumber.isEmpty() && !title.isEmpty()) {
                User user = new User(nickname, phoneNumber, title);
                selectedChat.addUser(user);
                updateUsersListView();
                nicknameField.clear();
                phoneNumberField.clear();
                titleField.clear();
                saveUsersToFile();
            }
        });
        senderComboBox = new ComboBox<>();
        senderComboBox.setPromptText("Select Sender");
        senderComboBox.setPrefWidth(150);

        messageTypeComboBox = new ComboBox<>();
        messageTypeComboBox.setPromptText("Select Message Type");
        messageTypeComboBox.setPrefWidth(150);
        messageTypeComboBox.setItems(FXCollections.observableArrayList("TextMessage", "ImageMessage", "FileMessage"));

        userListView = new ListView<>();
        userListView.setPrefHeight(100);

        Label chatLabel = new Label("Chats:");
        chatListView = new ListView<>();
        chatListView.setPrefHeight(100);
        chatListView.setOnMouseClicked(event -> {
            selectedChat = chatListView.getSelectionModel().getSelectedItem();
            if (selectedChat != null) {
                updateUsersListView();
                selectedChat.displayMessages(messageListView);
            }
        });

        Label userLabel = new Label("Users:");
        HBox userBox = new HBox(10);
        userBox.getChildren().addAll(userLabel, userListView);

        messageListView = new ListView<>();
        messageListView.setPrefHeight(200);

        Button clearMessagesButton = new Button("Clear Messages");
        clearMessagesButton.setOnAction(event -> messageListView.getItems().clear());

        Button sendMessageButton = new Button("Send Message");
        TextField messageField = new TextField();
        messageField.setPromptText("Enter message...");
        sendMessageButton.setOnAction(event -> {
            String messageContent = messageField.getText();
            if (!messageContent.isEmpty() && messageTypeComboBox.getValue() != null) {
                User sender = senderComboBox.getValue(); // Use selected sender from ComboBox
                BaseMessage message = null;
                switch (messageTypeComboBox.getValue()) {
                    case "TextMessage":
                        message = new TextMessage(sender, messageContent);
                        break;
                    case "ImageMessage":
                        message = new TextMessage(sender, messageContent);
                        break;
                    case "FileMessage":
                        message = new TextMessage(sender, messageContent);

                }
                if (message != null) {
                    selectedChat.addMessage(message);
                    selectedChat.displayMessages(messageListView);
                    messageField.clear();
                }
            }
        });

        HBox messageBox = new HBox(10);
        messageBox.getChildren().addAll(messageField, sendMessageButton);

        Button clearUsersButton = new Button("Clear Users");
        clearUsersButton.setOnAction(event -> {
            if (selectedChat != null) {
                selectedChat.getUsers().clear();
                updateUsersListView();
                saveUsersToFile();
            }
        });

        root.getChildren().addAll(nicknameField, phoneNumberField, titleField, addUserButton,
                chatLabel, chatListView, userBox, userListView, clearMessagesButton, messageListView, messageBox, senderComboBox, messageTypeComboBox, clearUsersButton);

        primaryStage.setScene(new Scene(root, 400, 600));
        primaryStage.setTitle("Chat Messenger");
        primaryStage.show();

        Chat chat1 = new Chat("Chat 1");
        Chat chat2 = new Chat("Chat 2");
        User user1 = new User("Alice", "123456789", "Manager");
        User user2 = new User("Bob", "987654321", "Developer");
        chat1.addUser(user1);
        chat1.addUser(user2);
        chat2.addUser(user1);

        chatListView.setItems(FXCollections.observableArrayList(chat1, chat2));
        updateUsersListView();

        loadChatsFromFile();
        loadUsersFromFile();
    }

    private void updateUsersListView() {
        if (selectedChat != null) {
            ArrayList<String> userInfoList = new ArrayList<>();
            for (User user : selectedChat.getUsers()) {
                userInfoList.add(user.toString());
            }
            userListView.setItems(FXCollections.observableArrayList(userInfoList));
            senderComboBox.setItems(FXCollections.observableArrayList(selectedChat.getUsers()));
            senderComboBox.getSelectionModel().selectFirst(); // Select the first user by default
        }
    }

    private void saveUsersToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("users.dat"))) {
            oos.writeObject(selectedChat.getUsers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to read users from file
    private void loadUsersFromFile() {
        ArrayList<User> users = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("users.txt"))) {
            users = (ArrayList<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (users != null && !users.isEmpty()) {
            selectedChat.getUsers().addAll(users);
            updateUsersListView();
        }
    }

    private void saveChatsToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("chats.dat"))) {
            oos.writeObject(chatListView.getItems());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadChatsFromFile() {
        ArrayList<Chat> chats = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("chats.dat"))) {
            chats = (ArrayList<Chat>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (chats != null && !chats.isEmpty()) {
            chatListView.getItems().addAll(chats);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}


