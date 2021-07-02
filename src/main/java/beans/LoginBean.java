package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

@ManagedBean(name = "loginBean")
public class LoginBean {
    private String login;
    private String password;

    public static String nextPage() {
        return "nextPage";
    }

    public String toRegPage(){
        return "regPage";
    }

    public void login() throws ServletException, IOException {
        ExternalContext context = FacesContext.getCurrentInstance()
                .getExternalContext();
        RequestDispatcher dispatcher = ((ServletRequest) context.getRequest()).getRequestDispatcher("/j_spring_security_check");
        dispatcher.forward((ServletRequest) context.getRequest(),
                (ServletResponse) context.getResponse());
        FacesContext.getCurrentInstance().responseComplete();
        /*if(UserRepository.login(login, password)){
             return nextPage();
        }
        Вreturn "mainPage";*/
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

    public String getLogin(){
        return login;
    }

}
