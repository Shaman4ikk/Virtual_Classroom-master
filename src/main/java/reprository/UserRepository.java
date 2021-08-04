package reprository;

import entity.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
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
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public static void start(){
        Session session = HibernateUtil.getSession().openSession();
        try{
            if(session.createCriteria(User.class).setMaxResults(1).list().isEmpty()){
                session.beginTransaction();
                User user = new User();
                user.setName("admin");
                user.setPassword(hashMD5("12345"));
                user.setHandUp(false);
                user.setAccess("admin");
                session.save(user);
                session.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public static void saveUser(UserAccount userAccount){
        Session session = HibernateUtil.getSession().openSession();
        try {
            session.beginTransaction();
            User user = (User) session.createCriteria(User.class).add(Restrictions.eq("id", userAccount.id)).uniqueResult();
            user.setFirstName(userAccount.firstName);
            user.setLastName(userAccount.lastName);
            user.setBitrhday((Date) userAccount.birthday);
            session.update(user);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            logger.debug("Exeption: " + e);
        }
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
            List<String> grantsList = new ArrayList<>();
            grantsList.add(user.access);
            Set<GrantedAuthority> authoritiesSet = getGrantedAuthoritiesFromList(grantsList);
            UserDetails userDetails = new UserDetailsImpl(user.login, user.password, authoritiesSet);
            addToListUser(userDTO);
            return userDetails;
        } catch (HibernateException | NullPointerException e) {
            logger.debug("Exeption: " + e);
        } finally {
            session.close();
        }
        return null;
    }

    public static UserAccount getUser(int id){
        Session session = HibernateUtil.getSession().openSession();
        try {
            User user = (User) session.createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult();
            return new UserAccount(user.id, user.login, user.firstName, user.lastName, user.bitrhday, user.password);
        } catch (HibernateException | NullPointerException e) {
            logger.debug("Exeption: " + e);
        } finally {
            session.close();
        }
        return null;
    }

    public static List<UserAccount> getUsersFromDB(){
        Session session = HibernateUtil.getSession().openSession();
        List<User> users =  session.createCriteria(User.class).add(Restrictions.eq("access", "user")).list();
        List<UserAccount> userDTOS1 = new ArrayList<>();
        if(users != null){
            for(User u: users){
                UserAccount userDTO = new UserAccount(u.id, u.login, u.firstName, u.lastName, u.bitrhday);
                userDTOS1.add(userDTO);
            }
        }
        return userDTOS1;
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
            user.setAccess("user");
            session.save(user);
            session.getTransaction().commit();
            if (user.getName() != null && user.getPassword() != null) {
                return true;
            }
        } catch (HibernateException | NullPointerException e) {
            logger.debug("Exeption: " + e);
        } finally {
            session.close();
        }
        return false;
    }

    public static void addNewUser(UserAccount userAccount){
        Session session = HibernateUtil.getSession().openSession();
        try {
            session.beginTransaction();
            User user = new User();
            user.setName(userAccount.login);
            user.setPassword(hashMD5(userAccount.password));
            user.setFirstName(userAccount.firstName);
            user.setLastName(userAccount.lastName);
            if(userAccount.birthday != null){
                user.setBitrhday(userAccount.birthday);
            }
            user.setHandUp(false);
            user.setAccess("user");
            session.save(user);
            session.getTransaction().commit();
        } catch (HibernateException | NullPointerException e) {
            logger.debug("Exeption: " + e);
        } finally {
            session.close();
        }

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
        session.close();
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

    public static void removeUserFromDB(int id){
        System.out.println(id);
        Session session = HibernateUtil.getSession().openSession();
        try {
            session.beginTransaction();
            session.delete(session.createCriteria(User.class).add(Restrictions.eq("id", id)).uniqueResult());
            session.getTransaction().commit();
        } catch (HibernateException | NullPointerException e) {
            logger.debug("Exeption: " + e);
        } finally {
            session.close();
        }
    }

    static Set<GrantedAuthority> getGrantedAuthoritiesFromList(List<String> grantsList) {
        Set<GrantedAuthority> authoritiesSet = new HashSet<>();

        GrantedAuthority grAuth;
        for (String grant : grantsList) {
            grAuth = new GrantedAuthorityImpl(grant);
            authoritiesSet.add(grAuth);
        }
        return authoritiesSet;
    }

}
