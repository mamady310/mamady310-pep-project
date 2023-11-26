package DAO;

import Model.Message;

import Util.ConnectionUtil;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {

//check if the user exists
public boolean checkAccountID(Message message){
    try{ 
     Connection con = ConnectionUtil.getConnection();
     PreparedStatement ps = con.prepareStatement("SELECT * From account WHERE account_id =?;");
     ps.setInt(1, message.getPosted_by());
     ResultSet rs = ps.executeQuery();
     if (rs.next()) { 
         return true;
       } else {
         return false;
       }
 
     } catch (SQLException e) {
         e.printStackTrace();
         return false;
     }
 }

    public Message createNewPost(Message message){

         
        try {
            System.out.println("creating new post");
    
            Connection con = ConnectionUtil.getConnection();
            String sql = "INSERT INTO message (posted_by,message_text, time_posted_epoch) values(?,?,?);";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int newMessageID = (int) rs.getInt(1);
                return new Message(newMessageID, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
             
            }
          
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } 
    
        return null;
    }


    public List<Message> getAllMessages(){
        Connection con = ConnectionUtil.getConnection();
        List<Message> databaseMessages = new ArrayList<>();
        try {
          
            String sql = "SELECT * FROM message";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        
        
        
            while(rs.next()){
                Message messages = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                databaseMessages.add(messages);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return databaseMessages;
    }

    public Message getMessageByID(int messageID){
            Connection con = ConnectionUtil.getConnection();
        try {
    
            
            PreparedStatement ps = con.prepareStatement("SELECT * From message WHERE message_id = ?;");
            ps.setInt(1, messageID);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                //int messageNum = rs.getInt(1);
                int postedBY = rs.getInt("posted_by");
                String messageText = rs.getString("message_text");
                Long timePosted = rs.getLong("time_posted_epoch");
                 
                return new Message(messageID, postedBY, messageText, timePosted);
            }
            return null;

        }catch(SQLException e){
            System.out.println(e.getMessage());
        } 
        return null;
    }

    public int deleteMessageByID(int messageID){

        try {
            Connection con = ConnectionUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM message WHERE message_id = ?;");
            ps.setInt(1, messageID);
    
           int row = ps.executeUpdate();
            
            System.out.println(row); //1
            return row;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } 
        return 0;
    }

    public Message updateMessageByID(int messageID, Message message){

        try {
    
            Connection con = ConnectionUtil.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE message SET message_text = ? WHERE message_id = ?");
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, messageID);
            
            int row =  ps.executeUpdate();
            if(row > 0) {
                return new Message(messageID, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
            return null;
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } 
        return null;
    }


    public List<Message> getMessageByUser(int account_id){
        Connection con = ConnectionUtil.getConnection();
        List<Message> userMessages = new ArrayList<>();
        try {
        
        PreparedStatement ps = con.prepareStatement("SELECT message_id, posted_by, message_text, time_posted_epoch FROM message JOIN account ON message.posted_by = account.account_id WHERE account_id = ?");
        ps.setInt(1, account_id);
           
        
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Message messages = new Message(rs.getInt("message_id"), rs.getInt("posted_by"),
                        rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                userMessages.add(messages);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return userMessages;
    }
}
