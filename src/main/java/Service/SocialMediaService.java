package Service;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class SocialMediaService {
    private SocialMediaDAO dao;

    public SocialMediaService() {
        this.dao = new SocialMediaDAO();
    }

    // Register a new account
    public Account registerAccount(Account account) {
        // Validate account input
        if (account.getUsername() == null || account.getUsername().isBlank() || 
            account.getPassword() == null || account.getPassword().length() <= 4) {
            return null;
        }

        // Check if the username already exists
        if (dao.getAccountByUsername(account.getUsername()) != null) {
            return null; // Username already exists
        }

        return dao.createAccount(account);
    }

    // Login method
    public Account login(String username, String password) {
        Account account = dao.getAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account;
        }
        return null;
    }

    // Post a new message
    public Message postMessage(Message message) {
        // Validate message input
        if (message == null || message.getMessage_text() == null || 
            message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null;
        }

        return dao.createMessage(message);
    }

    // Retrieve a message by ID
    public Message retrieveMessage(int messageId) {
        return dao.getMessageById(messageId);
    }

    // Retrieve all messages
    public List<Message> retrieveAllMessages() {
        return dao.getAllMessages();
    }

    // Retrieve messages by a specific user
    public List<Message> retrieveMessagesByUser(int userId) {
        return dao.getMessagesByUserId(userId);
    }

    // Remove a message by its ID and the user ID for validation.
    public Message removeMessage(int messageId) {
        // Retrieve the message to check if it exists.
        Message message = dao.getMessageById(messageId);
        if (message == null) {
            // If the message does not exist, return null.
            return null;
        }
        boolean deleted = dao.deleteMessageById(messageId);
        return deleted ? message : null;
    }

    // Edit a message's text
    public Message editMessage(int messageId, String newText) {
        Message message = dao.getMessageById(messageId);
        if (message == null) {
            return null; // Message does not exist.
        }
        // Update message text in the database.
        return dao.updateMessageText(messageId, newText);
    }
}
