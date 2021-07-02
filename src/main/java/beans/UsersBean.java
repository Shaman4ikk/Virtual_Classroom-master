package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(name = "usersBean")
public class UsersBean {

    public String name;

    public String getName() {
        FacesContext context = FacesContext.getCurrentInstance();
        name = context.getExternalContext().getSessionMap().get("name").toString();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
