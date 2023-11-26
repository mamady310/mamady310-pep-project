package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

public class AccountDAO{
 
   

public boolean checkIfUserExists(Account account){

    Connection con = ConnectionUtil.getConnection();
   try{ 
    
    PreparedStatement ps = con.prepareStatement("SELECT * From account WHERE username =?");
    ps.setString(1, account.getUsername());
    ResultSet rs = ps.executeQuery();
    if (rs.next()) { 
        //true if username already exists
        return true;
      } else {
        return false;
      }

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public Account registerNewAccount(Account account){

    Connection con = ConnectionUtil.getConnection();
    try {
        String sql = "INSERT INTO account (username, password) values(?, ?)";
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        ps.setString(1, account.getUsername()); 
        ps.setString(2, account.getPassword());
        ps.executeUpdate();
        ResultSet primarykey = ps.getGeneratedKeys();
        if(primarykey.next()){
            int userID = (int) primarykey.getInt(1);
            return new Account(userID, account.getUsername(), account.getPassword());
        }
    }catch(SQLException e){
        System.out.println(e.getMessage());
    } 
    return null;
}

public Account userLogin(Account account){
    
    Connection con = ConnectionUtil.getConnection();
    String userName = account.getUsername();
    String userPassword = account.getPassword();
    try{ 
     PreparedStatement ps = con.prepareStatement("SELECT * From account WHERE username =?");
     ps.setString(1, userName);
     ResultSet rs = ps.executeQuery();
     if (rs.next()) { 
            String user = rs.getString("username");
            String password =  rs.getString("password");
            int userID = rs.getInt("account_id");
            if(user.equals(userName) && password.equals(userPassword)) {
            return new Account(userID, user, password);
            }
       } 
              
     } catch (SQLException e) {
         e.printStackTrace();
      
     }
     return null;
 }


}