package reprository;

import org.springframework.security.core.userdetails.UserDetails;
import orm.User;
import entity.UserDTO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import security.UserDetailsImpl;
import util.HibernateUtil;

import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    static final Logger logger = LogManager.getLogger(UserRepository.class);

    public static List<UserDTO> arr;

    //Реализация паттерна Singleton для списка
    public static List<UserDTO> getUsersList() {
        if (arr == null) {
            arr = new ArrayList<>();
        }
        return arr;
    }

    public static boolean checkUser(Object name) {
        boolean result = false;
        for (UserDTO u : arr) {
            if (u.getName().equals(name)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public static boolean login(String login, String password) {
        Session session = HibernateUtil.getSession().openSession();
        try {
            password = hashMD5(password);
            User user = (User) session.createCriteria(User.class).add(Restrictions.eq("login", login)).add(Restrictions.eq("password", password)).uniqueResult();
            UserDTO userDTO = new UserDTO(user.login, user.handUp);
            addToListUser(userDTO);
            return true;
        } catch (HibernateException | NullPointerException e) {
            logger.debug("Exeption: " + e);
        } finally {
            session.close();
        }
        return false;
    }

    public static UserDetails login(String login) {
        Session session = HibernateUtil.getSession().openSession();
        try {
            User user = (User) session.createCriteria(User.class).add(Restrictions.eq("login", login)).uniqueResult();
            UserDTO userDTO = new UserDTO(user.login, user.handUp);
            UserDetails userDetails = new UserDetailsImpl(user.login, user.password);
            addToListUser(userDTO);
            return userDetails;
        } catch (HibernateException | NullPointerException e) {
            logger.debug("Exeption: " + e);
        } finally {
            session.close();
        }
        return null;
    }

    public static UserDTO checkUserDB(String name){
        Session session = HibernateUtil.getSession().openSession();
        try {
            User user = (User) session.createCriteria(User.class).add(Restrictions.eq("login", name)).uniqueResult();
            UserDTO userDTO = new UserDTO(user.login, user.handUp);
            return userDTO;
        } catch (HibernateException | NullPointerException e) {
            logger.debug("Exeption: " + e);
        } finally {
            session.close();
        }
        return null;
    }

    public static boolean register(String login, String password) {
        Session session = HibernateUtil.getSession().openSession();
        try {
            password = hashMD5(password);
            session.beginTransaction();
            User user = new User();
            user.setName(login);
            user.setPassword(password);
            user.setHandUp(false);
            session.save(user);
            session.getTransaction().commit();
            if (user.getName() != null && user.getPassword() != null) {
                UserDTO userDTO = new UserDTO(user.login, user.handUp);
                addToListUser(userDTO);
                return true;
            }
        } catch (HibernateException | NullPointerException e) {
            logger.debug("Exeption: " + e);
        } finally {
            session.close();
        }
        return false;
    }

    //Добавление пользователей в список
    public static void addToListUser(UserDTO user) {
        try {
            arr = getUsersList();
            if (!arr.contains(user)) {
                arr.add(user);
            }
        } catch (Exception e) {
            logger.debug("Exeption: " + e);
        }
    }

    public static String hashMD5(String password) {
        return DigestUtils.md5Hex(password);
    }

    public static void checkNull() {
        arr.removeIf(u -> u.getName() == null);
    }

    //Выход из классрума
    public static void logOut(String name) {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            for (UserDTO u : arr) {
                if (u.getName().equals(name)) {
                    arr.remove(u);
                    break;
                }
            }
            HibernateUtil.shutdown();
            context.getExternalContext().invalidateSession();
        } catch (Exception e) {
            logger.debug("Failed logOut: " + e);
        }
    }

    public static void connect() {
        Session session = HibernateUtil.getSession().openSession();
        session.beginTransaction();
        session.getTransaction().commit();
    }

    //Поднятие и опускание руки
    public static void invertBool(UserDTO user) {
        try {
            UserDTO user1;
            int index = 0;
            for (UserDTO u : arr) {
                if (u.getName().equals(user.getName())) {
                    index = arr.indexOf(u);
                    break;
                }
            }
            user1 = arr.get(index);
            user1.setHandUp(!user1.isHandUp());
            arr.set(index, user1);
        } catch (Exception e) {
            logger.debug("Failed handUp/handDown: " + e);
        }
    }
}
