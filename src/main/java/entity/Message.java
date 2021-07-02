package entity;

import entity.UserDTO;

import java.util.List;

public class Message {

    private String name;
    private boolean handStatus;
    private String action;
    private List<UserDTO> userSet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHandStatus() {
        return handStatus;
    }

    public void setHandStatus(boolean handStatus) {
        this.handStatus = handStatus;
    }


    public List<UserDTO> getUserSet() {
        return userSet;
    }

    public void setUserSet(List<UserDTO> userSet) {
        this.userSet = userSet;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "Message{" +
                "name='" + name + '\'' +
                ", handStatus=" + handStatus +
                ", action='" + action + '\'' +
                ", userSet=" + userSet +
                '}';
    }
}
