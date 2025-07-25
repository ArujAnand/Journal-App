package net.engineeringdigest.journalApp.service;

import net.engineeringdigest.journalApp.entity.UserEntity;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void saveUser(UserEntity newUser) {
        userRepository.save(newUser);
    }

    public void saveNewUser(UserEntity newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(Arrays.asList("USER"));
        userRepository.save(newUser);
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public boolean findById(ObjectId id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public void deleteUserByUsername(String name) {
        userRepository.deleteByUsername(name);
    }
}
