package Service;

import DAO.AccountDAO;
import Model.Account;


public class AccountService {

   
   private AccountDAO sm; 

    public AccountService() {
        this.sm = new AccountDAO();
    }

   public boolean credentialsCheck(Account account){
      //check if username and pw meet requirements
      boolean usernameBlank = account.getUsername().isBlank();
      boolean passwordLength = account.getPassword().length() < 4; 
      boolean userExists = sm.checkIfUserExists(account);

      if( userExists || usernameBlank || passwordLength) {
         return true;
      }
     return false;
   }
   
   public boolean checkIfUserExists(Account account) {
    if( sm.checkIfUserExists(account)) {
       return true;
    } 
       return false;    
   }

   //Register a new user
 public Account registerNewAccount(Account account){

    return sm.registerNewAccount(account); 
}

  //login an existing user
  public Account userLogin(Account account){
    return sm.userLogin(account);
  }

    
}
