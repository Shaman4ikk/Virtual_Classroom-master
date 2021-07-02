package reprository;

import entity.UserDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UserRepositoryTest {


    @Before
    public void setUp() throws Exception {
        UserRepository.addToListUser(new UserDTO("test", true));
    }

    @Test
    public void testGetUsersList() {
        List<UserDTO> usersList = UserRepository.getUsersList();

        assertEquals("test", usersList.get(0).getName());
    }

    @Test
    public void testAddToListUser() {
        UserRepository.addToListUser(new UserDTO("test1", true));

        List<UserDTO> usersList = UserRepository.getUsersList();

        assertEquals("test1", usersList.get(1).getName());
    }

}