package beans;

import reprository.UserRepository;

import javax.faces.bean.ManagedBean;

@ManagedBean(name = "regBean")
public class RegBean {
    private String login;
    private String password;

    public String toLoginPage(){
        return "mainPage";
    }

    private String nextPage() {
        return "nextPage";
    }

    public String register(){
        if(UserRepository.register(login, password)){
            return nextPage();
        }
        return "mainPage";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
