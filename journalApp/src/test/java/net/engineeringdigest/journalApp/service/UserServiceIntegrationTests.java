package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class UserServiceIntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setup() {
        userService.saveNewUser(UserEntity.builder()
                        .username("test")
                        .password("test")
                        .roles(Arrays.asList("USER"))
                        .build());
    }

    @Test
    public void testfindByUsernameANDdeleteByUsername() {
        Assertions.assertNotNull(userService.findByUsername("test"));
        Assertions.assertNull(userService.findByUsername("Ghanshaym"));
        Assertions.assertEquals(1, userService.deleteUserByUsername("test"));
    }

}
