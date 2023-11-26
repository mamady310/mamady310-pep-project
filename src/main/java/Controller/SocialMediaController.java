package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    ObjectMapper mapper = new ObjectMapper();
    private AccountService accountService;
    private MessageService messageService; 

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }


    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

       app.post("/register", this::registerNewAccount);
       app.post("/login", this::userLogin);
       app.post("/messages", this::createNewPost );
       app.get("/messages", this::getAllMessages);
       app.get("/messages/{message_id}", this::getMessageByID);
       app.delete("/messages/{message_id}", this::deleteMessagesByID);
       app.patch("/messages/{message_id}", this::updateMessageByID);
       app.get("/accounts/{account_id}/messages", this::getMessagesByUser);
       
       

        return app;
    }

    private void registerNewAccount(Context ctx) throws JsonProcessingException {
        Account account = mapper.readValue(ctx.body(), Account.class);

    
        if(accountService.credentialsCheck(account)){
            ctx.status(400);
        }else{
            Account registerUser = accountService.registerNewAccount(account);
            ctx.json(mapper.writeValueAsString(registerUser)).status(200);
           
        }
    }

    private void userLogin(Context ctx) throws JsonProcessingException {
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account login = accountService.userLogin(account);
        if(login == null){
            ctx.status(401);
        }else{
            ctx.json(mapper.writeValueAsString(login));
        }
    }

    private void createNewPost(Context ctx) throws JsonProcessingException {
       
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message newMessage = messageService.createNewPost(message);
        if(newMessage == null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(newMessage));
        }
    }

    private void getAllMessages(Context ctx) throws JsonProcessingException {
      if(messageService.getAllMessages() == null) {
        ctx.json("");
      } else {
        ctx.json(mapper.writeValueAsString(messageService.getAllMessages()));
      }
    }

    private void getMessageByID(Context ctx) throws JsonProcessingException {
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageRecord = messageService.getMessageByID(messageID);
         
        if(messageRecord != null) {
           ctx.json(mapper.writeValueAsString(messageRecord));
        } else {
           ctx.json("");
        }        
    }


     private void deleteMessagesByID(Context ctx) throws JsonProcessingException {
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageToDelete = messageService.getMessageByID(messageID);

        if(messageService.getMessageByID(messageID) == null) {
            ctx.status(200);
        } else {
            ctx.json(mapper.writeValueAsString(messageToDelete));
            messageService.deleteMessagesByID(messageID);
        }
     }

     private void updateMessageByID(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int messageID = Integer.parseInt(ctx.pathParam("message_id"));
        Message updatedMessage = messageService.updateMessageByID(messageID, message);
        
        if(updatedMessage != null) {
            ctx.json(mapper.writeValueAsString(updatedMessage)).status(200);
        } else {
            ctx.status(400);     
        }
     }
     
     private void getMessagesByUser(Context ctx) throws JsonProcessingException {
        int accountID = Integer.parseInt(ctx.pathParam("account_id"));
        if(messageService.getMessageByUser(accountID) != null) {
        ctx.json(mapper.writeValueAsString(messageService.getMessageByUser(accountID)));
        } else {
            ctx.json("");
        }

     }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }



}