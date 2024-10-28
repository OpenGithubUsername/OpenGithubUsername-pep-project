package Controller;

import Service.SocialMediaService;
import Model.Account;
import Model.Message;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class SocialMediaController {
    private SocialMediaService socialMediaService;

    public SocialMediaController() {
        this.socialMediaService = new SocialMediaService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Account endpoints
        app.post("/register", this::registerAccount);
        app.post("/login", this::login);

        // Message endpoints
        app.post("/messages", this::createMessage);
        app.get("/messages/{messageId}", this::getMessageById);
        app.get("/messages", this::getAllMessages);
        app.get("/accounts/{userId}/messages", this::getMessagesByUser);
        app.delete("/messages/{messageId}", this::deleteMessage);
        app.put("/messages/{messageId}", this::updateMessage);
        app.patch("/messages/{messageId}", this::updateMessage);

        return app;
    }

    private void registerAccount(Context context) {
        Account account = context.bodyAsClass(Account.class);
        Account registeredAccount = socialMediaService.registerAccount(account);
        if (registeredAccount != null) {
            context.status(200).json(registeredAccount);
        } else {
            context.status(400);
        }
    }

    private void login(Context context) {
        Account loginRequest = context.bodyAsClass(Account.class);
        Account account = socialMediaService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (account != null) {
            context.status(200).json(account);
        } else {
            context.status(401);
        }
    }

    private void createMessage(Context context) {
        Message message = context.bodyAsClass(Message.class);
        Message createdMessage = socialMediaService.postMessage(message);
        if (createdMessage != null) {
            context.status(200).json(createdMessage);
        } else {
            context.status(400);
        }
    }

    private void getMessageById(Context context) {
        try {
            String messageIdParam = context.pathParam("messageId");
            if (messageIdParam == null || messageIdParam.isEmpty()) {
                context.status(400);
                return;
            }
            int messageId = Integer.parseInt(context.pathParam("messageId"));
            Message message = socialMediaService.retrieveMessage(messageId);
            if (message != null) {
                context.status(200).json(message);
            } else {
                context.status(200);
            }
        } catch (NumberFormatException e) {
            // Handle cases where messageId is not a valid integer.
            context.status(400);
        } catch (Exception e) {
            // Catch any unexpected exceptions to prevent a 500 response.
            e.printStackTrace();
            context.status(500);
        }
    }

    private void getAllMessages(Context context) {
        List<Message> messages = socialMediaService.retrieveAllMessages();
        context.status(200).json(messages);
    }

    private void getMessagesByUser(Context context) {
        try{
            int userId = Integer.parseInt(context.pathParam("userId"));
            List<Message> messages = socialMediaService.retrieveMessagesByUser(userId);
            context.status(200).json(messages);
        }
        catch (NumberFormatException e) {
            // Handle invalid user ID format.
            context.status(400);
        } catch (Exception e) {
            // Catch any unexpected exceptions to prevent a 500 error.
            e.printStackTrace();
            context.status(500);
        }
    }

    private void deleteMessage(Context context) {
        try {
            String messageIdParam = context.pathParam("messageId");
            if (messageIdParam == null || messageIdParam.isEmpty()) {
                context.status(400);
                return;
            }
            int messageId = Integer.parseInt(messageIdParam);
            Message deletedMessage = socialMediaService.removeMessage(messageId);
            if (deletedMessage != null) {
                // If the message was found and deleted, return 200 with the deleted message.
                context.status(200).json(deletedMessage);
            } else {
                // If the message was not found, return 200 with an empty response.
                context.status(200).result("");
            }
        } catch (NumberFormatException e) {
            // Handle cases where messageId is not a valid integer.
            context.status(400);
        } catch (Exception e) {
            // Catch any unexpected exceptions to prevent a 500 error.
            e.printStackTrace();
            context.status(500);
        }
    }

    private void updateMessage(Context context) {
        try {
            int messageId;
            try {
                messageId = Integer.parseInt(context.pathParam("messageId"));
            } catch (NumberFormatException e) {
                context.status(400).result(""); // Empty response body on error
                return;
            }
            // Retrieve the input message text
            Message messageInput = context.bodyAsClass(Message.class);
            String newText = messageInput.getMessage_text();
            if (newText == null || newText.trim().isEmpty()) {
                context.status(400).result(""); // Ensure empty body for 400 error
                return;
            }
            if (newText.length() > 255) {
                context.status(400).result(""); // Empty response body on error
                return;
            }
            // Try to edit the message
            Message updatedMessage = socialMediaService.editMessage(messageId, newText);
            if (updatedMessage != null) {
                context.status(200).json(updatedMessage); // Return updated message on success
            } else {
                context.status(400).result(""); // Empty response body if messageId not found
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.status(500);
        }
    }
}
