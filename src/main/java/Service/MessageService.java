package Service;

import java.util.List;

import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    private MessageDAO md; 

    public MessageService() {
        this.md = new MessageDAO();
    }
    
    
    public Message createNewPost(Message message){
       
        boolean messageEmpty = message.getMessage_text().isBlank();
        boolean messageLength = message.getMessage_text().length() > 255;
        boolean postedBy = md.checkAccountID(message);

        if(messageEmpty || messageLength && !postedBy){
            return null;
        } else{
            return md.createNewPost(message);    
        }   
        
      }

    public List<Message> getAllMessages() {
        return md.getAllMessages();
    }

    public Message getMessageByID(int messageID) { 
        return md.getMessageByID(messageID);
    }


    public int deleteMessagesByID(int messageID) {
       int deleted = md.deleteMessageByID(messageID);
       return deleted;
    }

    public Message updateMessageByID(int messageID, Message message) {

        boolean messageEmpty = message.getMessage_text().isBlank();
        boolean messageLength = message.getMessage_text().length() > 255;

        if(getMessageByID(messageID) == null || messageEmpty || messageLength) {
            return null;
        } else {
            
            md.updateMessageByID(messageID, message);
            return md.getMessageByID(messageID);
        }
    }

    public List<Message> getMessageByUser(int account_id) {
        return md.getMessageByUser(account_id);
    }
    
}
