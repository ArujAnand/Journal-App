package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping
    public void createUser(@RequestBody UserEntity newUser) {
        userService.saveUser(newUser);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity user, @PathVariable String username) {
        UserEntity userInDB = userService.findByUsername(username);
        if (userInDB == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        userInDB.setUsername(user.getUsername());
        userInDB.setPassword(user.getPassword());
        userService.saveUser(userInDB);
        return new ResponseEntity<>(userInDB, HttpStatus.OK);
    }
}
