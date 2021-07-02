package orm;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    @Column(name = "login")
    public String login;

    @Column(name = "password")
    public String password;

    @Column(name = "hand")
    public boolean handUp;


    public User(){}
    public User(int id, String name, String password, boolean handUp) {
        this.id = id;
        this.login = name;
        this.password = password;
        this.handUp = handUp;
    }

    public User(String name) {
        this.login = name;
    }

    public User(String name, boolean handStatus) {
        this.login = name;
        this.handUp = handStatus;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", handUp=" + handUp +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return login;
    }

    public void setName(String name) {
        this.login = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHandUp() {
        return handUp;
    }

    public void setHandUp(boolean handUp) {
        this.handUp = handUp;
    }
}
