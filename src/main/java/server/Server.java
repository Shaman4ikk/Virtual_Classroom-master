package server;

import entity.Message;
import entity.UserDTO;
import reprository.UserRepository;
import server.decoder.MessageDecoder;
import server.encoder.MessageEncoder;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ServerEndpoint(value = "/users",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class)
public class Server {

    private static final List<Session> sessions = new ArrayList<>();

    //Установка сессии
    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnMessage
    public void onMessage(Message message) throws InterruptedException {
        switch (message.getAction()) {
            //Проверка руки
            case "hand": {
                UserRepository.checkNull();
                List<UserDTO> list = UserRepository.getUsersList();
                for (UserDTO user : list) {
                    if (user.getName().equals(message.getName())) {
                        UserRepository.invertBool(user);
                        break;
                    }
                }
                message.setUserSet(UserRepository.getUsersList());
                break;
            }
            //Логин
            case "login": {
                message.setUserSet(UserRepository.getUsersList());
                break;
            }
            //Выход из классрума
            case "logout": {
                List<UserDTO> list = UserRepository.getUsersList().stream().filter(user -> !user.getName().equals(message.getName())).collect(Collectors.toList());
                UserRepository.logOut(message.getName());
                message.setUserSet(list);
                break;
            }
            default:
                break;
        }
        //Отправка данных клиенту
        sessions.forEach(s -> {
            try {
                s.getBasicRemote().sendObject(message);
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }
}