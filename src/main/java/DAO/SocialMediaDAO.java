package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SocialMediaDAO {

    // Create a new account
    public Account createAccount(Account account) {
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, account.getUsername());
            stmt.setString(2, account.getPassword());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                account.setAccount_id(rs.getInt(1));
            }
            rs.close();
            stmt.close();
            return account;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Retrieve an account by username
    public Account getAccountByUsername(String username) {
        String sql = "SELECT * FROM account WHERE username = ?";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Account account = new Account();
                account.setAccount_id(rs.getInt("account_id"));
                account.setUsername(rs.getString("username"));
                account.setPassword(rs.getString("password"));
                rs.close();
                stmt.close();
                return account;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // Retrieve a message by ID
    public Message getMessageById(int messageId) {
        String sql = "SELECT * FROM message WHERE message_id = ?";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Message message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                rs.close();
                stmt.close();
                return message;
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    // Delete a message by ID
    public boolean deleteMessageById(int messageId) {
        String sql = "DELETE FROM message WHERE message_id = ?";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, messageId);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            return rowsAffected > 0; // Return true if a row was deleted.
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Retrieve all messages
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return messages;
    }

    // Retrieve messages by user ID
    public List<Message> getMessagesByUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM message WHERE posted_by = ?";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message message = new Message();
                message.setMessage_id(rs.getInt("message_id"));
                message.setPosted_by(rs.getInt("posted_by"));
                message.setMessage_text(rs.getString("message_text"));
                message.setTime_posted_epoch(rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return messages;
    }

    // Update message text
    public Message updateMessageText(int messageId, String newText) {
        String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newText);
            stmt.setInt(2, messageId);
            int rowsUpdated = stmt.executeUpdate();
            stmt.close();
            if (rowsUpdated > 0) {
                return getMessageById(messageId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    // Method to create a new message in the database.
    public Message createMessage(Message message) {
        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        Connection conn = null;
        try {
            conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1)); // Set the generated message_id
            }
            rs.close();
            stmt.close();
            return message; // Return the message with the generated ID.
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
