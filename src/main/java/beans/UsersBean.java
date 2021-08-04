package beans;

import entity.UserAccount;
import reprository.UserRepository;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.sql.Date;
import java.util.List;

@ManagedBean(name = "usersBean")
@SessionScoped
public class UsersBean {

    private UserAccount userAccount = new UserAccount();
    private boolean reg = false;
    private String date;

    public List<UserAccount> getListUsers() {
            return UserRepository.getUsersFromDB();
    }

    public void removeUser(int id){
        UserRepository.removeUserFromDB(id);
    }

    public String setUserAccount(int id){
        this.userAccount = UserRepository.getUser(id);
        return "regUser";
    }

    public UserAccount getUserAccount(){
        if (userAccount == null){
            return this.userAccount = new UserAccount();
        }
        try {
            date = this.userAccount.birthday.toString();
        } catch (Exception e){
            //eeption
        }
        return this.userAccount;
    }

    public String save(){
        userAccount.birthday = Date.valueOf(date);
        if(reg){
            UserRepository.addNewUser(userAccount);
        } else UserRepository.saveUser(userAccount);
        userAccount = new UserAccount();
        return "adminPage";
    }

    public boolean isReg() {
        return reg;
    }

    public void setReg(boolean reg) {
        this.reg = reg;
    }

    public String addUser(){
        this.reg = true;
        return "regUser";
    }

    public String back(){
        reg = false;
        return "adminPage";
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
