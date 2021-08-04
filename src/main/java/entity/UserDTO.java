package entity;

import java.util.Objects;

public class UserDTO {
    protected String login;
    protected String password;
    protected boolean handUp;

    public UserDTO(){}

    protected UserDTO(String login, String password, boolean handUp) {
        this.login = login;
        this.handUp = handUp;
        this.password = password;
    }

    public UserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /*public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }*/

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserDTO(String name, boolean handUp) {
        this.handUp = handUp;
        this.login = name;
    }

    public UserDTO(String name) {
        this.login = name;
    }

    public boolean isHandUp() {
        return handUp;
    }

    public void setHandUp(boolean handUp) {
        this.handUp = handUp;
    }

    public String getName() {
        return login;
    }

    public void setName(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", handUp=" + handUp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO)) return false;
        UserDTO user = (UserDTO) o;
        return getName().equals(user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
